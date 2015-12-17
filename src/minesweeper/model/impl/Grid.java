package minesweeper.model.impl;

import minesweeper.model.ICell;
import minesweeper.model.IGrid;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Grid<T extends ICell> implements IGrid<T> {
	private T[][] cells;
	private final int mines;
	private final int height;
	private final int width;
	private final Instant created;

	private static final int[][] adjCord = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0},
			{1, 1}};

	protected Grid(T[][] cells, int mines) {
		this.cells = cells;
		this.mines = mines;
		this.created = Instant.now();
		height = cells.length;
		width = cells[0].length;
	}

	@Override
	public T getCell(int row, int col) {
		if (!checkBounds(row, col)) {
			throw new IllegalArgumentException("Cell does not exist at this location");
		}
		return cells[row][col];
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getMines() {
		return mines;
	}

	@Override
	public long getSecondsSinceCreated() {
		return Duration.between(created, Instant.now()).getSeconds();
	}

	@Override
	public List<T> getCells() {
		List<T> cellList = new LinkedList<>();
		for (T[] rows : cells) {
			for (T cell : rows) {
				cellList.add(cell);
			}
		}
		return cellList;
	}

	@Override
	public List<T> getAdjCells(ICell cell) {
		return getAdjCells(cell.getRow(), cell.getCol());
	}

	@Override
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
