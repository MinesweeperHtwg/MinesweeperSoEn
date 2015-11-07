package minesweeper.model.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Grid {
	private Cell[][] cells;
	private final int height;
	private final int width;

	private static final int[][] adjCord = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
			{ 1, 1 } };

	protected Grid(Cell[][] cells) {
		if (cells == null || cells.length < 1 || cells[0] == null || cells[0].length < 1) {
			throw new IllegalArgumentException("Cells input array illegal");
		}
		this.cells = cells;
		height = cells.length;
		width = cells[0].length;
	}

	public Cell getCell(int row, int col) throws IllegalArgumentException {
		if (!checkBounds(row, col)) {
			throw new IllegalArgumentException("Index out of bounds");
		}
		return cells[row][col];
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getMines(int row, int col) {
		return getCell(row, col).getMines();
	}

	public boolean isFlag(int row, int col) {
		return getCell(row, col).isFlag();
	}

	public boolean isMine(int row, int col) {
		return getCell(row, col).isMine();
	}

	public boolean isOpened(int row, int col) {
		return getCell(row, col).isOpened();
	}

	public List<Cell> getCells() {
		List<Cell> cellList = new LinkedList<>();
		for (Cell[] rows : cells) {
			for (Cell cell : rows) {
				cellList.add(cell);
			}
		}
		return cellList;
	}

	public List<Cell> getAdjCells(final int row, final int col) {
		List<Cell> result = new ArrayList<>(8);
		for (int[] cord : adjCord) {
			int rowGet = cord[0] + row;
			int colGet = cord[1] + col;
			if (checkBounds(rowGet, colGet)) {
				result.add(cells[rowGet][colGet]);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return Arrays.stream(cells)
				.map(rows -> Arrays.stream(rows).map(Cell::toString).collect(Collectors.joining("|")))
				.collect(Collectors.joining("\n"));
	}

	private boolean checkBounds(int row, int col) {
		return col >= 0 && row >= 0 && col < width && row < height;
	}

}
