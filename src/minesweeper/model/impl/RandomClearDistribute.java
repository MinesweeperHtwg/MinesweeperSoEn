package minesweeper.model.impl;

import java.util.Collections;
import java.util.List;

import minesweeper.model.ICellMutable;
import minesweeper.model.IMineDistributeStrategy;

public class RandomClearDistribute implements IMineDistributeStrategy {
	private int mines;
	private int rowClear;
	private int colClear;

	public RandomClearDistribute(int mines, int rowClear, int colClear) {
		this.mines = mines;
		this.rowClear = rowClear;
		this.colClear = colClear;
	}

	@Override
	public void distributeMines(Grid<ICellMutable> grid) {
		List<ICellMutable> cellList = grid.getCells();
		cellList.remove(new Cell(rowClear, colClear));
		Collections.shuffle(cellList);
		for (int i = 0; i < mines; i++) {
			cellList.get(i).setIsMine(true);
		}
	}

}
