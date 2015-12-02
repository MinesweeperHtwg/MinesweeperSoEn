package minesweeper.controller.impl;

import static minesweeper.controller.impl.MinesweeperController.GameStatus.FIRSTCLICK;
import static minesweeper.controller.impl.MinesweeperController.GameStatus.LOSE;
import static minesweeper.controller.impl.MinesweeperController.GameStatus.RUNNING;
import static minesweeper.controller.impl.MinesweeperController.GameStatus.SETUPNEEDED;
import static minesweeper.controller.impl.MinesweeperController.GameStatus.WIN;

import java.util.List;

import minesweeper.model.ICell;
import minesweeper.model.ICell.State;
import minesweeper.model.IGrid;
import minesweeper.model.IGridFactory;
import minesweeper.model.IGridFactory.Strategy;

public class MinesweeperController {
	private String statusLine = "Welcome to Minesweeper!";

	public enum GameStatus {
		SETUPNEEDED, RUNNING, FIRSTCLICK, WIN, LOSE
	}

	private GameStatus gameStatus;
	private int flags;
	private int openFields;

	private IGrid<ICell> grid;
	private IGridFactory gFact;

	protected MinesweeperController(IGridFactory gFact) {
		this.gFact = gFact;
		try {
			reset();
		} catch (IllegalStateException e) {
			if ("Mine placement not specified".equals(e.getMessage())) {
				// gFact isn't set up, caller must use changeSettings
				gameStatus = SETUPNEEDED;
			} else {
				throw e;
			}

		}
	}

	public void changeSettings(int height, int width, int mines) {
		gFact.size(height, width).mines(mines).random();
		reset();
		statusLine = "New Settings: height=" + height + " width=" + width + " mines=" + mines;
	}

	public void newGame() {
		if (gameStatus == SETUPNEEDED) {
			return;
		}
		reset();
		statusLine = "New game started";
	}

	private void reset() {
		grid = gFact.getGrid();
		if (gFact.getStrategy() == Strategy.SPECIFIED) {
			gameStatus = RUNNING;
		} else {
			gameStatus = FIRSTCLICK;
		}
		flags = 0;
		openFields = grid.getHeight() * grid.getWidth();
	}

	private boolean checkStatus() {
		if (gameStatus == RUNNING || gameStatus == FIRSTCLICK) {
			return false;
		}
		if (gameStatus == LOSE) {
			statusLine = "Game over";
			return true;
		}
		if (gameStatus == WIN) {
			statusLine = "You've won!";
			return true;
		}
		if (gameStatus == SETUPNEEDED) {
			return true;
		} else {
			throw new IllegalStateException("Enum added!");
		}
	}

	public void openCell(int row, int col) {
		if (checkStatus()) {
			return;
		}

		if (gameStatus == FIRSTCLICK) {
			grid = gFact.randomClear(row, col).getGrid();
			gameStatus = RUNNING;
		}

		ICell cell = grid.getCell(row, col);
		if (cell.isFlag()) {
			statusLine = "Can't open " + cell.mkString() + " because there is a flag";
		} else if (cell.isOpened()) {
			statusLine = "Can't open " + cell.mkString() + " because the cell has been opened already";
		} else {
			executeOpenCell(cell);
		}
	}

	private void executeOpenCell(ICell cell) {
		cell.setState(State.OPENED);
		openFields--;
		if (cell.isMine()) {
			gameStatus = LOSE;
			statusLine = "Game over. Mine opened at " + cell.mkString();
			return;
		}
		if (cell.getMines() == 0) {
			floodOpen(cell);
		}
		// check to ensure that an game over message does not get
		// overwritten when called from openAround.
		if (gameStatus == RUNNING) {
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
		if (gameStatus == RUNNING && openFields == grid.getMines()) {
			gameStatus = WIN;
			statusLine = "You've won!";
		}
	}

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
				if (gameStatus == RUNNING) {
					statusLine = "Opened all cells around " + cell.mkString();
				}
			} else {
				statusLine = "Can't open cells around " + cell.mkString()
						+ " because there is an incorrect number of flags around this cell";
			}
		}
	}

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
	}

	public String getGameStats() {
		return "Unflagged mines left: " + (grid.getMines() - flags) + " Time: " + grid.getSecondsSinceCreated() + "s";
	}

	public String getGridString() {
		return grid.toString();
	}

	public String getStatusLine() {
		return statusLine;
	}
}
