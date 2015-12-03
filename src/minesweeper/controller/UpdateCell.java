package minesweeper.controller;

import minesweeper.util.observer.Event;

public class UpdateCell implements Event {
    private final int row;
    private final int col;
    
    public UpdateCell(int row, int col ) {
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
