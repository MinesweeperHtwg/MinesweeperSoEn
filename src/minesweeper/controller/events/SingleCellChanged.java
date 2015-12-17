package minesweeper.controller.events;

import minesweeper.util.observer.Event;

public class SingleCellChanged implements Event {
	private final int row;
	private final int col;

	public SingleCellChanged(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
}
