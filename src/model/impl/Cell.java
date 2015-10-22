package model.impl;

import model.AbstractCell;

public class Cell extends AbstractCell{
    private final int column;
    private final int row;
    
    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Cell(int column, int row) {
        this.column = column;
        this.row = row;
    }
    
    
}
