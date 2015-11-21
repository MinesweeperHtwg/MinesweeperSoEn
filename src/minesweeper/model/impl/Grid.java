package minesweeper.model.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import minesweeper.model.ICell;

public class Grid<T extends ICell> {
	private T[][] cells;
	private final int mines;
	private final int height;
	private final int width;
	private final Instant created;

	private static final int[][] adjCord = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
			{ 1, 1 } };

	protected Grid(T[][] cells, int mines) {
		this.cells = cells;
		this.mines = mines;
		this.created = Instant.now();
		height = cells.length;
		width = cells[0].length;
	}

	public T getCell(int row, int col) throws IllegalArgumentException {
		if (!checkBounds(row, col)) {
			throw new IllegalArgumentException("Cell does not exist at this location");
		}
		return cells[row][col];
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getMines() {
		return mines;
	}

	public long getSecondsSinceCreated() {
		return Duration.between(created, Instant.now()).getSeconds();
	}

	public List<T> getCells() {
		List<T> cellList = new LinkedList<>();
		for (T[] rows : cells) {
			for (T cell : rows) {
				cellList.add(cell);
			}
		}
		return cellList;
	}

	public List<T> getAdjCells(final int row, final int col) {
		List<T> result = new ArrayList<>(8);
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
		return Arrays.stream(cells).map(rows -> Arrays.stream(rows).map(T::toString).collect(Collectors.joining("|")))
				.collect(Collectors.joining("\n"));
	}

	private boolean checkBounds(int row, int col) {
		return col >= 0 && row >= 0 && col < width && row < height;
	}

}
