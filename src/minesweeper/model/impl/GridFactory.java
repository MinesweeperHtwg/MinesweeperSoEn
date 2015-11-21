package minesweeper.model.impl;

import java.util.List;

import minesweeper.model.IGridFactory;
import minesweeper.model.IMineDistributeStrategy;

public class GridFactory implements IGridFactory {
	private final int height;
	private final int width;
	private int mines;
	private IMineDistributeStrategy distributor;

	public GridFactory(int height, int width) {
		this.height = height;
		this.width = width;
	}

	public GridFactory random(int mines) {
		checkParameters(height * width, mines);
		distributor = new RandomDistribute(mines);
		this.mines = mines;
		return this;
	}

	public GridFactory randomClear(int mines, int rowClear, int colClear) {
		checkParameters(height * width - 1, mines);
		distributor = new RandomClearDistribute(mines, rowClear, colClear);
		this.mines = mines;
		return this;
	}

	public GridFactory specified(int[][] mineLocations) {
		checkParameters(height * width, mineLocations.length);
		for (int[] mineLocation : mineLocations) {
			if (mineLocation.length != 2) {
				throw new IllegalArgumentException("Wrong mine location format");
			}
		}
		distributor = new SpecifiedDistribute(mineLocations);
		this.mines = mineLocations.length;
		return this;
	}

	private void checkParameters(int cells, int mines) {
		if (cells < mines) {
			throw new IllegalArgumentException("Cant construct a grid with more mines than cells");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see minesweeper.model.impl.IGridFactory#getGrid()
	 */
	@Override
	public Grid getGrid() {
		if (distributor == null) {
			throw new IllegalStateException("Must specify mine placement before calling getGrid()");
		}

		Grid grid = getInitalGrid();

		distributor.distributeMines(grid);

		updateMineNumbers(grid);

		return grid;
	}

	private Grid getInitalGrid() {
		Cell[][] cells = new Cell[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				cells[row][col] = new Cell(row, col);
			}
		}

		return new Grid(cells, mines);
	}

	private void updateMineNumbers(Grid grid) {
		List<Cell> cellList = grid.getCells();
		for (Cell cell : cellList) {
			List<Cell> adjCells = grid.getAdjCells(cell.getRow(), cell.getCol());
			int adjMines = 0;
			for (Cell adjCell : adjCells) {
				if (adjCell.isMine()) {
					adjMines++;
				}
			}
			cell.setMines(adjMines);
		}
	}
}
