package minesweeper.aview.tui;

import org.apache.log4j.Logger;

import minesweeper.controller.impl.MinesweeperController;
import minesweeper.util.observer.Event;
import minesweeper.util.observer.IObserver;

public class TextUI implements IObserver {
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private MinesweeperController controller;

	private Logger logger = Logger.getLogger(this.getClass());

	public TextUI(MinesweeperController controller) {
		this.controller = controller;
		controller.addObserver(this);
	}

	@Override
	public void update(Event e) {
		printTUI();
	}

	public void printTUI() {
		logger.info(NEWLINE + controller.getGridString());
		logger.info(NEWLINE + controller.getStatusLine());
	}
}
