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

	public boolean processLine(String line) {
		boolean cont = true;
		switch (line) {
		case "q":
		case "quit":
			cont = false;
			break;
		case "n":
		case "new":
		case "newGame":
			controller.newGame();
			break;
		default:
			if (line.matches("(o|open) \\d+ \\d+")) {
				String[] args = line.split(" ");
				controller.openCell(Integer.valueOf(args[1]), Integer.valueOf(args[2]));
			} else if (line.matches("(a|around) \\d+ \\d+")) {
				String[] args = line.split(" ");
				controller.openAround(Integer.valueOf(args[1]), Integer.valueOf(args[2]));
			} else if (line.matches("(f|flag) \\d+ \\d+")) {
				String[] args = line.split(" ");
				controller.toggleFlag(Integer.valueOf(args[1]), Integer.valueOf(args[2]));
			} else {
				printCommands();
			}
			break;
		}
		return cont;
	}

	@Override
	public void update(Event e) {
		printTUI();
	}

	public void printTUI() {
		logger.info(NEWLINE + controller.getGridString());
		logger.info(NEWLINE + controller.getStatusLine());
		printCommands();
	}
	
	public void printCommands() {
		logger.info(NEWLINE + "Commands: q|quit; n|new|newGame; o|open ROW COL; a|around ROW COL; f|flag ROW COL");
	}
}
