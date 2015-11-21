package minesweeper.controller.impl;

import java.util.List;

import minesweeper.controller.IMinesweeperController;
import minesweeper.model.IGridFactory;
import minesweeper.model.impl.Cell;
import minesweeper.model.impl.Cell.State;
import minesweeper.model.impl.Grid;
import minesweeper.util.observer.Observable;

public class MinesweeperController extends Observable implements IMinesweeperController {
	private String statusLine = "Welcome to Minesweeper!";

	public enum GameStatus {
		RUNNING, WIN, LOOSE
	}

	private GameStatus gameStatus = GameStatus.RUNNING;
	private int flags = 0;
	private int openFields;

	private Grid grid;
	private IGridFactory gFact;

	public MinesweeperController(IGridFactory gFact) {
		this.grid = gFact.getGrid();
		openFields = grid.getHeight() * grid.getWidth();
		this.gFact = gFact;
	}

	// TODO: Different game modes
	@Override
	public void newGame() {
		grid = gFact.getGrid();
		gameStatus = GameStatus.RUNNING;
		flags = 0;
		openFields = grid.getHeight() * grid.getWidth();
		statusLine = "New game started";
		notifyObservers();
	}

	private boolean checkGameEnd() {
		if (gameStatus == GameStatus.RUNNING) {
			return false;
		}
		if (gameStatus == GameStatus.LOOSE) {
			statusLine = "Game over";
			notifyObservers();
			return true;
		} else {
			statusLine = "You've won!";
			notifyObservers();
			return true;
		}
	}

	@Override
	public void openCell(int row, int col) {
		if (checkGameEnd()) {
			return;
		}
		Cell cell = grid.getCell(row, col);
		if (cell.isFlag()) {
			statusLine = "Can't open " + cell.mkString() + " because there is a flag";
		} else if (cell.isOpened()) {
			statusLine = "Can't open " + cell.mkString() + " because the cell has been opened already";
		} else {
			executeOpenCell(cell);
		}
		notifyObservers();
	}

	private void executeOpenCell(Cell cell) {
		cell.setState(State.OPENED);
		openFields--;
		if (cell.isMine()) {
			gameStatus = GameStatus.LOOSE;
			statusLine = "Game over. Mine opened at " + cell.mkString();
			return;
		}
		if (cell.getMines() == 0) {
			floodOpen(cell);
		}
		// check to ensure that an game over message does not get
		// overwritten when called from openAround.
		if (gameStatus == GameStatus.RUNNING) {
			statusLine = "Opened " + cell.mkString();
		}
		checkWin();
	}

	private void floodOpen(Cell cell) {
		List<Cell> adjCells = grid.getAdjCells(cell.getRow(), cell.getCol());
		for (Cell adjCell : adjCells) {
			if (adjCell.getState() == State.CLOSED) {
				adjCell.setState(State.OPENED);
				openFields--;
				if (adjCell.getMines() == 0) {
					floodOpen(adjCell);
				}
			}
		}
	}

	private void checkWin() {
		if (gameStatus == GameStatus.RUNNING && openFields == grid.getMines()) {
			gameStatus = GameStatus.WIN;
			statusLine = "You've won!";
		}
	}

	@Override
	public void openAround(int row, int col) {
		if (checkGameEnd()) {
			return;
		}
		Cell cell = grid.getCell(row, col);
		if (cell.isClosed()) {
			statusLine = "Can't open cells around " + cell.mkString() + " because the cell is closed";
		} else {
			List<Cell> adjCells = grid.getAdjCells(row, col);
			long flagCount = adjCells.stream().filter(Cell::isFlag).count();

			if (flagCount == cell.getMines()) {
				adjCells.stream().filter(c -> c.getState() == State.CLOSED).forEach(c -> executeOpenCell(c));
				// only print status if we haven't lost or won
				if (gameStatus == GameStatus.RUNNING) {
					statusLine = "Opened all cells around " + cell.mkString();
				}
			} else {
				statusLine = "Can't open cells around " + cell.mkString()
						+ " because there is an incorrect number of flags around this cell";
			}
		}
		notifyObservers();
	}

	@Override
	public void toggleFlag(int row, int col) {
		if (checkGameEnd()) {
			return;
		}
		Cell cell = grid.getCell(row, col);
		if (cell.isOpened()) {
			statusLine = "Can't place flag at " + cell.mkString() + " because the cell has been opened";
		} else if (cell.isFlag()) {
			cell.setState(State.CLOSED);
			statusLine = "Flag removed at " + cell.mkString();
			flags--;
		} else {
			cell.setState(State.FLAG);
			statusLine = "Flag set at " + cell.mkString();
			flags++;
		}
		notifyObservers();
	}

	public String getGameStats() {
		return "Unflagged mines left: " + (grid.getMines() - flags) + " Time: " + grid.getSecondsSinceCreated() + "s";
	}

	@Override
	public String getGridString() {
		return grid.toString();
	}

	@Override
	public String getStatusLine() {
		return statusLine;
	}
}
