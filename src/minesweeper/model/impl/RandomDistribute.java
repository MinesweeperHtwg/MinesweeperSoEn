package minesweeper.model.impl;

import minesweeper.model.ICellMutable;
import minesweeper.model.IGrid;
import minesweeper.model.IMineDistributeStrategy;

import java.util.Collections;
import java.util.List;

public class RandomDistribute implements IMineDistributeStrategy {
	private int mines;

	public RandomDistribute(int mines) {
		this.mines = mines;
	}

	@Override
	public void distributeMines(IGrid<ICellMutable> grid) {
		List<ICellMutable> cellList = grid.getCells();
		Collections.shuffle(cellList);
		for (int i = 0; i < mines; i++) {
			cellList.get(i).setIsMine(true);
		}
	}

}
