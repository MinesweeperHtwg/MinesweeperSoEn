package minesweeper;

import minesweeper.model.IGridFactory;
import minesweeper.model.impl.RandomGridFactory;
import minesweeper.model.impl.Grid;
import minesweeper.model.impl.GridFactory;

public class Minesweeper {
	public static void main(String[] args) {
		IGridFactory gFact = new GridFactory(10, 10, 20);
		Grid grid = gFact.getGrid();
		grid = new RandomGridFactory(grid).getGrid();

		System.out.println(grid.mkString());
	}
}
