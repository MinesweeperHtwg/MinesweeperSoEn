package minesweeper.model;

public interface IGridFactory {

	public enum Strategy {
		RANDOM, RANDOMCLEAR, SPECIFIED
	}

	/**
	 * Returns a generated grid
	 * 
	 * @return a generated grid
	 */
	IGrid<ICell> getGrid();

	Strategy getStrategy();

	IGridFactory specified(int[][] mineLocations);

	IGridFactory randomClear(int rowClear, int colClear);

	IGridFactory random();

	IGridFactory size(int height, int width);

	IGridFactory mines(int mines);

}