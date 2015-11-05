package minesweeper.model.impl;

import java.util.List;

import minesweeper.model.IGridFactory;
import minesweeper.model.impl.Cell.State;

public class RandomGridFactory implements IGridFactory {
	private Grid grid;

	public RandomGridFactory(Grid grid) {
		this.grid = grid;
	}

	@Override
	public Grid getGrid() {
		List<Cell> cellList = grid.getCells();
		for (Cell cell : cellList) {
		    double r = Math.random();
			if (r < 0.75)
				cell.setState(State.OPENED);
			else if (r < 0.9)
				cell.setState(State.FLAG);
			else
			    cell.setState(State.CLOSED);

			cell.setMines((int) (Math.random() * 4));
		}
		return grid;
	}

}
