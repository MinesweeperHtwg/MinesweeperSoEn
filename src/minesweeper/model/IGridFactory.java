package minesweeper.model;

import minesweeper.model.impl.Grid;

public interface IGridFactory {
    
    public abstract Grid getGrid();

}