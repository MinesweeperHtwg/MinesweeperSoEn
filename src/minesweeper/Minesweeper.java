package minesweeper;

import minesweeper.model.IGridFactory;
import minesweeper.model.impl.Cell;
import minesweeper.model.impl.Grid;
import minesweeper.model.impl.GridFactory;
import minesweeper.model.impl.Cell.State;

public class Minesweeper {
	public static void main(String[] args) {
		IGridFactory gFact = new GridFactory(10, 20, 50);
		Grid grid = gFact.getGrid();
		for (Cell cell : grid.getCells()) {
			cell.setState(State.OPENED);
		}

		System.out.println(grid.mkString());
	}
}
