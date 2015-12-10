package minesweeper.controller;

import minesweeper.model.ICell;
import minesweeper.model.IGrid;

public interface IMinesweeperControllerSolveable extends IMinesweeperController {

	IGrid<ICell> getGrid();

}
