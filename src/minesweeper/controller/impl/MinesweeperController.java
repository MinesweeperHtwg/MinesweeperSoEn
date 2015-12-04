package minesweeper.controller.impl;

import java.util.List;
import java.util.stream.Collectors;

import minesweeper.controller.DimensionsChanged;
import minesweeper.controller.IMinesweeperController;
import minesweeper.controller.MultipleCellsChanged;
import minesweeper.controller.NoCellChanged;
import minesweeper.controller.SingleCellChanged;
import minesweeper.model.ICell;
import minesweeper.model.ICell.State;
import minesweeper.model.IGrid;
import minesweeper.model.IGridFactory;
import minesweeper.model.IGridFactory.Strategy;
import minesweeper.util.observer.Event;
import minesweeper.util.observer.Observable;

public class MinesweeperController extends Observable implements IMinesweeperController {

	private interface GameState {
		/**
		 * Checks the game status. If game has ended, sets statusLine, event and
		 * returns true. If game is still running, resets event and returns
		 * false.
		 * 
		 * @return if the game has ended.
		 */
		boolean checkStatus();
	}

	private class Running implements GameState {
		@Override
		public boolean checkStatus() {
			event = null;
			return false;
		}
	}

	private class FirstClick implements GameState {
		@Override
		public boolean checkStatus() {
			event = null;
			return false;
		}
	}

	private class Win implements GameState {
		@Override
		public boolean checkStatus() {
			statusLine = "You've won!";
			event = new NoCellChanged();
			return true;
		}
	}

	private class Lose implements GameState {
		@Override
		public boolean checkStatus() {
			statusLine = "Game over";
			event = new NoCellChanged();
			return true;
		}
	}

	private String statusLine = "Welcome to Minesweeper!";

	private GameState gameState;

	private Event event;

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
				// gFact isn't set up, use default settings
				this.gFact.size(10, 20).mines(10).random();
				reset();
			} else {
				throw e;
			}

		}
	}

	@Override
	public void changeSettings(int height, int width, int mines) {
		gFact.size(height, width).mines(mines).random();
		reset();
		statusLine = "New Settings: height=" + height + " width=" + width + " mines=" + mines;
		event = new DimensionsChanged();
	}

	@Override
	public void newGame() {
		reset();
		statusLine = "New game started";
		event = new MultipleCellsChanged();
	}

	private void reset() {
		grid = gFact.getGrid();
		if (gFact.getStrategy() == Strategy.SPECIFIED) {
			gameState = new Running();
		} else {
			gameState = new FirstClick();
		}
		flags = 0;
		openFields = grid.getHeight() * grid.getWidth();
	}

	@Override
	public void openCell(int row, int col) {
		if (gameState.checkStatus()) {
			return;
		}

		if (gameState instanceof FirstClick) {
			grid = gFact.randomClear(row, col).getGrid();
			gameState = new Running();
		}

		ICell cell = grid.getCell(row, col);
		if (cell.isFlag()) {
			statusLine = "Can't open " + cell.mkString() + " because there is a flag";
			event = new NoCellChanged();
		} else if (cell.isOpened()) {
			statusLine = "Can't open " + cell.mkString() + " because the cell has been opened already";
			event = new NoCellChanged();
		} else {
			event = new SingleCellChanged(row, col);
			executeOpenCell(cell);
		}
	}

	private void executeOpenCell(ICell cell) {
		cell.setState(State.OPENED);
		openFields--;
		if (cell.isMine()) {
			gameState = new Lose();
			statusLine = "Game over. Mine opened at " + cell.mkString();
			return;
		}
		if (cell.getMines() == 0) {
			floodOpen(cell);
			event = new MultipleCellsChanged();
		}
		// check to ensure that an game over message does not get
		// overwritten when called from openAround.
		if (gameState instanceof Running) {
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
		if (gameState instanceof Running && openFields == grid.getMines()) {
			gameState = new Win();
			statusLine = "You've won!";
		}
	}

	@Override
	public void openAround(int row, int col) {
		if (gameState.checkStatus()) {
			return;
		}
		ICell cell = grid.getCell(row, col);
		if (cell.isClosed()) {
			statusLine = "Can't open cells around " + cell.mkString() + " because the cell is closed";
			event = new NoCellChanged();
		} else {
			List<ICell> adjCells = grid.getAdjCells(row, col);
			long flagCount = adjCells.stream().filter(ICell::isFlag).count();

			if (flagCount == cell.getMines()) {
				List<ICell> cellsToOpen = adjCells.stream().filter(c -> c.isClosedWithoutFlag())
						.collect(Collectors.toList());
				if (cellsToOpen.size() == 0) {
					statusLine = "No cells to open around " + cell.mkString();
					event = new NoCellChanged();
				} else {
					cellsToOpen.forEach(c -> executeOpenCell(c));
					// only change statusLine if we haven't lost or won
					if (gameState instanceof Running) {
						statusLine = "Opened all cells around " + cell.mkString();
					}
					event = new MultipleCellsChanged();
				}
			} else {
				statusLine = "Can't open cells around " + cell.mkString()
						+ " because there is an incorrect number of flags around this cell";
				event = new NoCellChanged();
			}
		}
	}

	@Override
	public void toggleFlag(int row, int col) {
		if (gameState.checkStatus()) {
			return;
		}
		ICell cell = grid.getCell(row, col);
		if (cell.isOpened()) {
			statusLine = "Can't place flag at " + cell.mkString() + " because the cell has been opened";
			event = new NoCellChanged();
		} else if (cell.isFlag()) {
			cell.setState(State.CLOSED);
			statusLine = "Flag removed at " + cell.mkString();
			event = new SingleCellChanged(row, col);
			flags--;
		} else {
			cell.setState(State.FLAG);
			statusLine = "Flag set at " + cell.mkString();
			event = new SingleCellChanged(row, col);
			flags++;
		}
	}

	@Override
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

	@Override
	public String getCellString(int row, int col) {
		return grid.getCell(row, col).toString();
	}

	@Override
	public int getHeight() {
		return grid.getHeight();
	}

	@Override
	public int getWidth() {
		return grid.getWidth();
	}

	@Override
	public Event getEvent() {
		return event;
	}
}
