package minesweeper.controller;

public interface IMinesweeperController {

	void newGame();
	void exit();
	void openField(int col, int row);
}
