package minesweeper.model.impl;

import java.util.Collections;
import java.util.List;

import minesweeper.model.IGridFactory;

public class GridFactory implements IGridFactory {
	private int height;
	private int width;
	private int mines;

	public GridFactory(int height, int width, int mines) {
		if (height * width < mines) {
			throw new IllegalArgumentException(
					"Cant construct a grid with more mines than cells!");
		}

		this.height = height;
		this.width = width;
		this.mines = mines;
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

		// Distribute mines on the grid
		List<Cell> cellList = grid.getList();
		Collections.shuffle(cellList);
		for (int i = 0; i < mines; i++) {
			cellList.get(i).setIsMine(true);
		}

		return grid;
	}

}
