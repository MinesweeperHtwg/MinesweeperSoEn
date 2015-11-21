package minesweeper.model.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import minesweeper.model.ICell;
import minesweeper.model.ICellMutable;
import minesweeper.model.IGrid;
import minesweeper.model.IGridFactory;
import minesweeper.model.IMineDistributeStrategy;

public class GridFactory implements IGridFactory {
	private int height;
	private int width;
	private int mines = -1;
	private IMineDistributeStrategy distributor;

	public GridFactory() {
	}

	public GridFactory(int height, int width) {
		size(height, width);
	}

	@Override
	public IGridFactory size(int height, int width) {
		this.height = height;
		this.width = width;
		return this;
	}

	@Override
	public IGridFactory mines(int mines) {
		this.mines = mines;
		return this;
	}

	@Override
	public IGridFactory random() {
		checkArgument(mines >= 0, "Must specify number of mines >= 0");
		distributor = new RandomDistribute(mines);
		return this;
	}

	@Override
	public IGridFactory randomClear(int rowClear, int colClear) {
		checkArgument(mines >= 0, "Must specify number of mines >= 0");
		distributor = new RandomClearDistribute(mines, rowClear, colClear);
		return this;
	}

	@Override
	public IGridFactory specified(int[][] mineLocations) {
		for (int[] mineLocation : mineLocations) {
			if (mineLocation.length != 2) {
				throw new IllegalArgumentException("Wrong mine location format");
			}
		}
		distributor = new SpecifiedDistribute(mineLocations);
		this.mines = mineLocations.length;
		return this;
	}

	private void checkCellMineCount(int cells, int mines) {
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
	public IGrid<ICell> getGrid() {
		checkState(distributor != null, "Must specify mine placement before calling getGrid");
		checkArgument(height > 0 && width > 0, "Dimensions must be bigger than 0");
		if (distributor instanceof RandomClearDistribute) {
			checkCellMineCount(height * width - 1, mines);
		} else {
			checkCellMineCount(height * width, mines);
		}

		ICellMutable[][] cells = getEmptyCells();

		IGrid<ICellMutable> grid = new Grid<ICellMutable>(cells, mines);

		distributor.distributeMines(grid);

		updateMineNumbers(grid);

		return new Grid<ICell>(cells, mines);
	}

	private ICellMutable[][] getEmptyCells() {
		ICellMutable[][] cells = new ICellMutable[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				cells[row][col] = new Cell(row, col);
			}
		}

		return cells;
	}

	private void updateMineNumbers(IGrid<ICellMutable> grid) {
		List<ICellMutable> cellList = grid.getCells();
		for (ICellMutable cell : cellList) {
			List<ICellMutable> adjCells = grid.getAdjCells(cell.getRow(), cell.getCol());
			int adjMines = 0;
			for (ICellMutable adjCell : adjCells) {
				if (adjCell.isMine()) {
					adjMines++;
				}
			}
			cell.setMines(adjMines);
		}
	}
}
