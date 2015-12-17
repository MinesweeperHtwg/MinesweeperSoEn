package minesweeper.model;

public interface ICellMutable extends ICell {
	void setIsMine(boolean isMine);

	void setMines(int mines);
}
