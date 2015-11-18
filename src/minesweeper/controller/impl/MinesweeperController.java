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

	//TODO: Add win
	private boolean gameOver = false;
	private int flags = 0;

	private Grid grid;
	private IGridFactory gFact;

	public MinesweeperController(IGridFactory gFact) {
		this.grid = gFact.getGrid();
		this.gFact = gFact;
	}

	@Override
	public void newGame() {
		grid = gFact.getGrid();
		gameOver = false;
		flags = 0;
		statusLine = "New game started";
		notifyObservers();
	}

	private boolean checkGameEnd() {
		if (gameOver) {
			statusLine = "Game over";
			notifyObservers();
			return true;
		}
		return false;
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
		if (cell.isMine()) {
			gameOver = true;
			statusLine = "Game over. Mine opened at " + cell.mkString();
			return;
		}
		if (cell.getMines() == 0) {
			floodOpen(cell);
		}
		statusLine = "Opened " + cell.mkString();
	}

	private void floodOpen(Cell cell) {
		List<Cell> adjCells = grid.getAdjCells(cell.getRow(), cell.getCol());
		for (Cell adjCell : adjCells) {
			if (adjCell.isClosed()) {
				adjCell.setState(State.OPENED);
				if (adjCell.getMines() == 0) {
					floodOpen(adjCell);
				}
			}
		}
	}

	//TODO: Fix bug: Wrong flags end game
	@Override
	public void openAround(int row, int col) {
		if (checkGameEnd()) {
			return;
		}
		Cell cell = grid.getCell(row, col);
		if (cell.isClosed()) {
			statusLine = "Can't open cells around " + cell.mkString() + " because the cell is closed";
		} else {
			// Get the number of flags around the requested cell
			List<Cell> adjCells = grid.getAdjCells(row, col);
			long flagCount = adjCells.stream().filter(Cell::isFlag).count();

			if (flagCount == cell.getMines()) {
				// If the number of flags matches the mine number, open all
				// closed Cells expect Cells with a flag on it
				adjCells.stream().filter(c -> c.getState() == State.CLOSED).forEach(c -> c.setState(State.OPENED));
				statusLine = "Opened all cells around " + cell.mkString();
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
