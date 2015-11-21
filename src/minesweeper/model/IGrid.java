package minesweeper.model;

import java.util.List;

public interface IGrid<T extends ICell> {

	T getCell(int row, int col);

	int getHeight();

	int getWidth();

	int getMines();

	long getSecondsSinceCreated();

	List<T> getCells();

	List<T> getAdjCells(int row, int col);

	@Override
	String toString();

}