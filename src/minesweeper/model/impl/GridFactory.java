package minesweeper.model.impl;

import java.util.Collections;
import java.util.List;

import minesweeper.model.IGridFactory;

public class GridFactory implements IGridFactory {
	private int height;
	private int width;
	private int mines;
	private int[][] mineLocations;

	public GridFactory(int height, int width, int mines) {
		if (height * width < mines) {
			throw new IllegalArgumentException("Cant construct a grid with more mines than cells");
		}

		this.height = height;
		this.width = width;
		this.mines = mines;
	}

	public GridFactory(int height, int width, int[][] mineLocations) {
		for (int[] mineLocation : mineLocations) {
			if (mineLocation.length != 2) {
				throw new IllegalArgumentException("Wrong mine location format");
			}
		}
		this.height = height;
		this.width = width;
		this.mineLocations = mineLocations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see minesweeper.model.impl.IGridFactory#getGrid()
	 */
	@Override
	public Grid getGrid() {
		Cell[][] cells = new Cell[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				cells[row][col] = new Cell(row, col);
			}
		}

		Grid grid = new Grid(cells);

		if (mineLocations == null) {
			distributeMinesRandomly(grid);
		} else {
			distributeMinesSpecified(grid);
		}
		updateMineNumbers(grid);

		return grid;
	}

	private void distributeMinesRandomly(Grid grid) {
		List<Cell> cellList = grid.getCells();
		Collections.shuffle(cellList);
		for (int i = 0; i < mines; i++) {
			cellList.get(i).setIsMine(true);
		}
	}

	private void distributeMinesSpecified(Grid grid) {
		for (int[] mineLocation : mineLocations) {
			grid.getCell(mineLocation[0], mineLocation[1]).setIsMine(true);
		}
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
