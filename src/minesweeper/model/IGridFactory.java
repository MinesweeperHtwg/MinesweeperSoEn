package minesweeper.model;

import minesweeper.model.impl.Grid;

public interface IGridFactory {
    
	/**
	 * Returns a generated grid
	 * @return a generated grid
	 */
    public abstract Grid getGrid();

}