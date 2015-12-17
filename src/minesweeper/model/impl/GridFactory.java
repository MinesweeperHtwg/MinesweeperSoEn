package minesweeper.model.impl;

import minesweeper.model.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class GridFactory implements IGridFactory {
	// Only one instance of NoMinesDistribute is needed
	private static final IMineDistributeStrategy NOMINESDISTRIBUTE = new NoMinesDistribute();

	private int height;
	private int width;
	private int mines;

	private IMineDistributeStrategy distributor;
	private Strategy strategy;

	/**
	 * Generates new GridFactory, initialized with a gridsize of 1,1 and
	 * noMines()
	 */
	public GridFactory() {
		this(1, 1);
	}

	/**
	 * Generates new GridFactory, initialized with a specified gridsize and
	 * noMines()
	 *
	 * @param height the height of the generated grid
	 * @param width  the with of the generated grid
	 */
	public GridFactory(int height, int width) {
		size(height, width);
		noMines();
		mines = 0;
	}

	@Override
	public IGridFactory size(int height, int width) {
		checkArgument(height > 0 && width > 0, "Dimensions must be bigger than 0");
		this.height = height;
		this.width = width;
		return this;
	}

	@Override
	public IGridFactory mines(int mines) {
		checkArgument(mines >= 0, "Mines must be positive");
		this.mines = mines;
		return this;
	}

	@Override
	public IGridFactory random() {
		distributor = new RandomDistribute(mines);
		strategy = Strategy.RANDOM;
		return this;
	}

	@Override
	public IGridFactory randomClear(int rowClear, int colClear) {
		distributor = new RandomClearDistribute(mines, rowClear, colClear);
		strategy = Strategy.RANDOMCLEAR;
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
		strategy = Strategy.SPECIFIED;
		this.mines = mineLocations.length;
		return this;
	}

	@Override
	public IGridFactory noMines() {
		distributor = NOMINESDISTRIBUTE;
		strategy = Strategy.NOMINES;
		return this;
	}

	@Override
	public IGrid<ICell> getGrid() {
		checkReadyForReturn();

		ICellMutable[][] cells = getEmptyCells();

		IGrid<ICellMutable> grid = new Grid<ICellMutable>(cells, mines);

		distributor.distributeMines(grid);

		updateMineNumbers(grid);

		return new Grid<ICell>(cells, mines);
	}

	private void checkReadyForReturn() {
		if (distributor instanceof RandomClearDistribute) {
			checkCellMineCount(height * width - 1, mines);
		} else {
			checkCellMineCount(height * width, mines);
		}
	}

	private void checkCellMineCount(int cells, int mines) {
		if (cells < mines) {
			throw new IllegalArgumentException("Cant construct a grid with more mines than cells");
		}
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

	@Override
	public Strategy getStrategy() {
		return strategy;
	}
}
