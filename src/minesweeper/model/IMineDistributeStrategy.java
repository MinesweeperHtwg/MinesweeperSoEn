package minesweeper.model;

public interface IMineDistributeStrategy {
	void distributeMines(IGrid<ICellMutable> grid);
}
