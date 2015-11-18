package minesweeper.model.impl;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class Cell {
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

    protected void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    protected void setMines(int mines) {
        checkArgument(mines >= 0, "Surrounding mines should be positive");
        this.mines = mines;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String mkString() {
        return "(" + row + ", " + col + ") = " + toString();
    }

    @Override
    public String toString() {
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
        return new HashCodeBuilder().append(col).append(row).append(state)
                .append(mines).append(isMine).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Cell rhs = (Cell) obj;
        return new EqualsBuilder().append(col, rhs.col).append(row, rhs.row)
                .append(state, rhs.state).append(mines, rhs.mines)
                .append(isMine, rhs.isMine).isEquals();
    }
}
