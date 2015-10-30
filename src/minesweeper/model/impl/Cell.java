package minesweeper.model.impl;

import minesweeper.model.ICell;

public class Cell implements ICell {
    public enum State {
        OPENED, CLOSED, FLAG
    }

    private final int col;
    private final int row;
    private State state;
    private int mines;

    private boolean isMine;

    public Cell(int col, int row) {
        this(col, row, State.CLOSED, 0, false);
    }

    public Cell(int col, int row, State state, int mines, boolean isMine) {
        this.col = col;
        this.row = row;
        setMines(mines);
        setState(state);
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getMines() {
        return mines;
    }

    public State getState() {
        return state;
    }

    public boolean isClosed() {
        if (state != State.OPENED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFlag() {
        if (state == State.FLAG) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isOpened() {
        if (state == State.OPENED) {
            return true;
        } else {
            return false;
        }
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public void setMines(int mines) {
        if (mines < 0)
            throw new IllegalArgumentException(
                    "Surrounding mines should not be negative.");
        this.mines = mines;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String mkString() {
        switch (state) {
        case CLOSED:
            return " ";
        case FLAG:
            return "F";
        default:
            if (isMine) {
                return "M";
            }
            return String.valueOf(mines);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cell other = (Cell) obj;
        if (col != other.col)
            return false;
        if (row != other.row)
            return false;
        if (state != other.state)
            return false;
        if (isMine != other.isMine)
            return false;
        if (mines != other.mines)
            return false;
        return true;
    }
}
