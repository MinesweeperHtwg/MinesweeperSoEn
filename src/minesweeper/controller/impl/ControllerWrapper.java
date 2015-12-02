package minesweeper.controller.impl;

import org.apache.log4j.Logger;

import minesweeper.controller.IMinesweeperController;
import minesweeper.model.IGridFactory;
import minesweeper.util.observer.Observable;

public class ControllerWrapper extends Observable implements IMinesweeperController {

	private MinesweeperController controller;

	private static final Logger LOGGER = Logger.getLogger(ControllerWrapper.class);

	public ControllerWrapper(IGridFactory gFact) {
		controller = new MinesweeperController(gFact);
	}

	private void pre() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		LOGGER.debug(stackTrace[2].getMethodName() + " was called.");
	}

	private void post() {
		notifyObservers();
	}

	@Override
	public void changeSettings(int height, int width, int mines) {
		pre();
		controller.changeSettings(height, width, mines);
		post();
	}

	@Override
	public void newGame() {
		pre();
		controller.newGame();
		post();
	}

	@Override
	public void openCell(int row, int col) {
		pre();
		controller.openCell(row, col);
		post();
	}

	@Override
	public void openAround(int row, int col) {
		pre();
		controller.openAround(row, col);
		post();
	}

	@Override
	public void toggleFlag(int row, int col) {
		pre();
		controller.toggleFlag(row, col);
		post();
	}

	@Override
	public String getGameStats() {
		return controller.getGameStats();
	}

	@Override
	public String getGridString() {
		return controller.getGridString();
	}

	@Override
	public String getStatusLine() {
		return controller.getStatusLine();
	}

}
