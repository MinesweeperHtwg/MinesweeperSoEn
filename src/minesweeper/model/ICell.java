package minesweeper.model;

public interface ICell {

	enum State {
		OPENED, CLOSED, FLAG
	}

	int getCol();

	int getRow();

	int getMines();

	State getState();

	boolean isClosed();

	boolean isClosedWithoutFlag();

	boolean isOpened();

	boolean isFlag();

	boolean isMine();

	void setState(State state);

	String mkString();

	@Override
	String toString();

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);

}