package model.impl;

import model.AbstractCell;

public class Cell extends AbstractCell{
    private final int col;
    private final int row;
    
    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Cell(int col, int row) {
        this.col = col;
        this.row = row;
    }
    
    
}
