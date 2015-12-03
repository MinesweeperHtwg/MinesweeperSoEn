package minesweeper.controller;

import minesweeper.util.observer.IObservable;

public interface IMinesweeperController extends IObservable {
	void changeSettings(int height, int width, int mines);

	void newGame();

	void openCell(int row, int col);

	void openAround(int row, int col);

	void toggleFlag(int row, int col);

	String getGameStats();

	String getGridString();

	String getStatusLine();

	String getCellString(int row, int col);

	int getHeight();

	int getWidth();
}
