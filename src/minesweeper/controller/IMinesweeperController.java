package minesweeper.controller;

public interface IMinesweeperController {
	void exit();
	void newGame();
	void openCell(int row, int col);
	void openAround(int row, int col);
	void toggleFlag(int row, int col);
	String getGridString();
	String getStatusLine();
}
