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
	private Grid grid;
	private IGridFactory gFact;

	public MinesweeperController(IGridFactory gFact) {
		this.grid = gFact.getGrid();
		this.gFact = gFact;
	}

	@Override
	public void newGame() {
		grid = gFact.getGrid();
		statusLine = "New game started";
		notifyObservers();
	}

	@Override
	public void openCell(int row, int col) {
		Cell cell = grid.getCell(row, col);
		if (cell.isFlag()) {
			statusLine = "The cell " + cell.mkString() + " can't be opened because it has a flag";
		} else if (cell.isOpened()) {
			statusLine = "The cell " + cell.mkString() + " can't be opened because it is already open";
		} else {
			cell.setState(State.OPENED);
			statusLine = "The cell " + cell.mkString() + " has been opened";
		}
		notifyObservers();
	}

	@Override
	public void openAround(int row, int col) {
		Cell cell = grid.getCell(row, col);
		if (cell.isClosed()) {
			statusLine = "Can't open cells around the cell " + cell.mkString() + " because it is closed";
		} else {
			// Get the number of flags around the requested cell
			List<Cell> adjCells = grid.getAdjCells(row, col);
			long flagCount = adjCells.stream().filter(Cell::isFlag).count();

			if (flagCount != cell.getMines()) {
				// If the number of flags matches the mine number, open all
				// closed Cells expect Cells with a flag on it
				adjCells.stream().filter(c -> c.getState() == State.CLOSED).forEach(c -> c.setState(State.OPENED));
				statusLine = "Opened all fields around the cell " + cell.mkString();
			} else {
				statusLine = "Can't open cells around the cell " + cell.mkString()
						+ " because there is an incorrect number of flags around this cell";
			}
		}
		notifyObservers();
	}

	@Override
	public void toggleFlag(int row, int col) {
		Cell cell = grid.getCell(row, col);
		if (cell.isOpened()) {
			statusLine = "The cell " + cell.mkString() + " can't have a flag because it is opened";
		} else if (cell.isFlag()) {
			cell.setState(State.CLOSED);
			statusLine = "The cell " + cell.mkString() + " now has a Flag";
		} else {
			cell.setState(State.FLAG);
			statusLine = "The cell " + cell.mkString() + " now has no Flag";
		}
		notifyObservers();
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
