package minesweeper.model;

public interface IGridFactory {

	/**
	 * Returns a generated grid
	 * 
	 * @return a generated grid
	 */
	public abstract IGrid<ICell> getGrid();

}