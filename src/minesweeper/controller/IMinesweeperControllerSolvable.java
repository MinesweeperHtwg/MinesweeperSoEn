package minesweeper.controller;

import minesweeper.model.ICell;
import minesweeper.model.IGrid;

public interface IMinesweeperControllerSolvable extends IMinesweeperController {

	IGrid<ICell> getGrid();

}
