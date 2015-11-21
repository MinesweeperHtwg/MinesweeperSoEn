package minesweeper.model;

import minesweeper.model.impl.Grid;

public interface IMineDistributeStrategy {
	void distributeMines(Grid grid);
}
