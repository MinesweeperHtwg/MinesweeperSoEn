package minesweeper.model.impl;

import java.util.Collections;
import java.util.List;

import minesweeper.model.ICellMutable;
import minesweeper.model.IMineDistributeStrategy;

public class RandomDistribute implements IMineDistributeStrategy {
	private int mines;

	public RandomDistribute(int mines) {
		this.mines = mines;
	}

	@Override
	public void distributeMines(Grid<ICellMutable> grid) {
		List<ICellMutable> cellList = grid.getCells();
		Collections.shuffle(cellList);
		for (int i = 0; i < mines; i++) {
			cellList.get(i).setIsMine(true);
		}
	}

}
