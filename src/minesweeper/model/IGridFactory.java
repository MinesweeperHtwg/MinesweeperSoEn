package minesweeper.model;

public interface IGridFactory {

	/**
	 * Returns a generated grid
	 * 
	 * @return a generated grid
	 */
	public abstract IGrid<ICell> getGrid();

	IGridFactory specified(int[][] mineLocations);

	IGridFactory randomClear(int mines, int rowClear, int colClear);

	IGridFactory random(int mines);

	IGridFactory size(int height, int width);

}