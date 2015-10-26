package minesweeper.model.impl;

import minesweeper.model.ICell;

public class Cell implements ICell {
	private final int col;
	private final int row;
	private int mines;
	private boolean opened;
	private boolean containsMine;
	private boolean flag;

	public Cell(int col, int row) {
		this(col, row, 0, false, false, false);
	}

	public Cell(int col, int row, int mines, boolean opened,
			boolean containsMine, boolean flag) {
		this.col = col;
		this.row = row;
		this.mines = mines;
		this.opened = opened;
		this.containsMine = containsMine;
		this.flag = flag;
	}

	public boolean containsMine() {
		return containsMine;
	}

	public int getCol() {
		return col;
	}

	public int getMines() {
		return mines;
	}

	public int getRow() {
		return row;
	}

	public boolean isFlag() {
		return flag;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setContainsMine(boolean containsMine) {
		this.containsMine = containsMine;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setMines(int mines) {
		this.mines = mines;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public String mkString() {
		if (flag)
			return "F";
		if (!opened)
			return " ";
		if (containsMine) {
			return "M";
		}
		return String.valueOf(mines);
	}
}
