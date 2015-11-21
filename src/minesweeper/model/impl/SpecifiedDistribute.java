package minesweeper.model.impl;

import minesweeper.model.ICellMutable;
import minesweeper.model.IMineDistributeStrategy;

public class SpecifiedDistribute implements IMineDistributeStrategy {
	private int[][] mineLocations;

	public SpecifiedDistribute(int[][] mineLocations) {
		this.mineLocations = mineLocations;
	}

	@Override
	public void distributeMines(Grid<ICellMutable> grid) {
		for (int[] mineLocation : mineLocations) {
			grid.getCell(mineLocation[0], mineLocation[1]).setIsMine(true);
		}
	}

}
