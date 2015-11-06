package minesweeper.model.impl;

import javax.annotation.Generated;

public class Cell {
    public enum State {
        OPENED, CLOSED, FLAG
    }

    private final int col;
    private final int row;
    private State state;
    private int mines;

    private boolean isMine;

    public Cell(int row, int col) {
        this(row, col, State.CLOSED, 0, false);
    }

    public Cell(int row, int col, State state, int mines, boolean isMine) {
        this.col = col;
        this.row = row;
        setMines(mines);
        setState(state);
        setIsMine(isMine);
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
        return state != State.OPENED;
    }

    public boolean isFlag() {
        return state == State.FLAG;
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isOpened() {
        return state == State.OPENED;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + col;
        result = prime * result + (isMine ? 1231 : 1237);
        result = prime * result + mines;
        result = prime * result + row;
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
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
        if (isMine != other.isMine)
            return false;
        if (mines != other.mines)
            return false;
        if (row != other.row)
            return false;
        if (state != other.state)
            return false;
        return true;
    }
}
