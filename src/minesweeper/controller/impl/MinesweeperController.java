package minesweeper.controller.impl;

import java.util.List;

import minesweeper.controller.IMinesweeperController;
import minesweeper.model.ICell;
import minesweeper.model.ICell.State;
import minesweeper.model.IGrid;
import minesweeper.model.IGridFactory;
import minesweeper.model.IGridFactory.Strategy;
import minesweeper.util.observer.Observable;

public class MinesweeperController extends Observable implements IMinesweeperController {
	private String statusLine = "Welcome to Minesweeper!";

	public enum GameStatus {
		SETUPNEEDED, RUNNING, FIRSTCLICK, WIN, LOOSE
	}

	private GameStatus gameStatus;
	private int flags;
	private int openFields;

	private IGrid<ICell> grid;
	private IGridFactory gFact;

	public MinesweeperController(IGridFactory gFact) {
		this.gFact = gFact;
		try {
			reset();
		} catch (IllegalStateException e) {
			if (e.getMessage().equals("Mine placement not specified")) {
				// gFact isn't set up yet, caller must use changeSettings
				gameStatus = GameStatus.SETUPNEEDED;
			} else {
				throw e;
			}

		}
	}

	@Override
	public void changeSettings(int height, int width, int mines) {
		gFact.size(height, width).mines(mines).random();
		reset();
		notifyObservers();
	}

	@Override
	public void newGame() {
		if (gameStatus == GameStatus.SETUPNEEDED) {
			return;
		}
		reset();
		statusLine = "New game started";
		notifyObservers();
	}

	private void reset() {
		grid = gFact.getGrid();
		if (gFact.getStrategy() == Strategy.SPECIFIED) {
			gameStatus = GameStatus.RUNNING;
		} else {
			gameStatus = GameStatus.FIRSTCLICK;
		}
		flags = 0;
		openFields = grid.getHeight() * grid.getWidth();
	}

	private boolean checkStatus() {
		switch (gameStatus) {
		case RUNNING:
			return false;
		case FIRSTCLICK:
			return false;
		case LOOSE:
			statusLine = "Game over";
			notifyObservers();
			return true;
		case WIN:
			statusLine = "You've won!";
			notifyObservers();
			return true;
		case SETUPNEEDED:
			return true;
		default:
			throw new IllegalStateException("Enum added!");
		}
	}

	@Override
	public void openCell(int row, int col) {
		if (checkStatus()) {
			return;
		}

		if (gameStatus == GameStatus.FIRSTCLICK) {
			grid = gFact.randomClear(row, col).getGrid();
			gameStatus = GameStatus.RUNNING;
		}

		ICell cell = grid.getCell(row, col);
		if (cell.isFlag()) {
			statusLine = "Can't open " + cell.mkString() + " because there is a flag";
		} else if (cell.isOpened()) {
			statusLine = "Can't open " + cell.mkString() + " because the cell has been opened already";
		} else {
			executeOpenCell(cell);
		}
		notifyObservers();
	}

	private void executeOpenCell(ICell cell) {
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

	private void floodOpen(ICell cell) {
		List<ICell> adjCells = grid.getAdjCells(cell.getRow(), cell.getCol());
		for (ICell adjCell : adjCells) {
			if (adjCell.isClosedWithoutFlag()) {
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
		if (checkStatus()) {
			return;
		}
		ICell cell = grid.getCell(row, col);
		if (cell.isClosed()) {
			statusLine = "Can't open cells around " + cell.mkString() + " because the cell is closed";
		} else {
			List<ICell> adjCells = grid.getAdjCells(row, col);
			long flagCount = adjCells.stream().filter(ICell::isFlag).count();

			if (flagCount == cell.getMines()) {
				adjCells.stream().filter(c -> c.isClosedWithoutFlag()).forEach(c -> executeOpenCell(c));
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
		if (checkStatus()) {
			return;
		}
		ICell cell = grid.getCell(row, col);
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
