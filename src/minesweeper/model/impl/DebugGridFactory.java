package minesweeper.model.impl;

import java.util.List;

import minesweeper.model.IGridFactory;

public class DebugGridFactory implements IGridFactory {
	private Grid grid;

	public DebugGridFactory(Grid grid) {
		this.grid = grid;
	}

	@Override
	public Grid getGrid() {
		List<Cell> cellList = grid.getList();
		for (Cell cell : cellList) {
			if (Math.random() < 0.75)
				cell.setOpened(true);
			if (Math.random() < 0.1)
				cell.setFlag(true);

			cell.setMines((int) (Math.random() * 4));
		}
		return grid;
	}

}
