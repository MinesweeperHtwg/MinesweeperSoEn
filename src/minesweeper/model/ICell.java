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

	/**
	 * Returns a long representation of this cell, e.g. "(1,0)=F"
	 *
	 * @return a long representation of this cell
	 */
	String mkString();

	/**
	 * Returns a short representation of this cell, e.g. "F" for a flag
	 *
	 * @return a short representation of this cell
	 */
	@Override
	String toString();

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);

}