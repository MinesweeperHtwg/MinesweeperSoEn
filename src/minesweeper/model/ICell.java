package minesweeper.model;

import minesweeper.model.impl.Cell.State;

public interface ICell {

	int getCol();

	int getRow();

	int getMines();

	State getState();

	boolean isClosed();

	boolean isFlag();

	boolean isMine();

	boolean isOpened();

	void setState(State state);

	String mkString();

	@Override
	String toString();

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);

}