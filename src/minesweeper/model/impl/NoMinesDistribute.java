package minesweeper.model.impl;

import minesweeper.model.ICellMutable;
import minesweeper.model.IGrid;
import minesweeper.model.IMineDistributeStrategy;

public class NoMinesDistribute implements IMineDistributeStrategy {
	public NoMinesDistribute() {
	}

	@Override
	public void distributeMines(IGrid<ICellMutable> grid) {
	}
}
