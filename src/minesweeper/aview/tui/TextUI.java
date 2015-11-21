package minesweeper.aview.tui;

import static java.lang.Integer.parseInt;

import org.apache.log4j.Logger;

import minesweeper.controller.impl.MinesweeperController;
import minesweeper.util.observer.Event;
import minesweeper.util.observer.IObserver;

public class TextUI implements IObserver {
	private static final String NEWLINE = System.getProperty("line.separator");

	private MinesweeperController controller;

	private static final Logger LOGGER = Logger.getLogger(TextUI.class);

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
			processCordLine(line);
			break;
		}
		return cont;
	}

	private void processCordLine(String line) {
		try {
			if (line.matches("(o|open) \\d+ \\d+")) {
				String[] args = line.split(" ");
				controller.openCell(parseInt(args[1]), parseInt(args[2]));
			} else if (line.matches("(a|around) \\d+ \\d+")) {
				String[] args = line.split(" ");
				controller.openAround(parseInt(args[1]), parseInt(args[2]));
			} else if (line.matches("(f|flag) \\d+ \\d+")) {
				String[] args = line.split(" ");
				controller.toggleFlag(parseInt(args[1]), parseInt(args[2]));
			} else if (line.matches("(s|set) \\d+ \\d+ \\d+")) {
				String[] args = line.split(" ");
				controller.changeSettings(parseInt(args[1]), parseInt(args[2]), parseInt(args[3]));
			} else {
				printCommands();
			}
		} catch (IllegalArgumentException e) {
			if (e.getMessage().equals("Cell does not exist at this location")) {
				printCommands();
			} else {
				throw e;
			}
		}
	}

	@Override
	public void update(Event e) {
		printTUI();
	}

	private void printTUI() {
		LOGGER.info(NEWLINE + controller.getGameStats() + NEWLINE + controller.getGridString() + NEWLINE
				+ controller.getStatusLine());
		printCommands();
	}

	private void printCommands() {
		LOGGER.info(NEWLINE
				+ "Commands: q|quit; n|new|newGame; s|set HEIGHT WIDTH MINES o|open ROW COL; a|around ROW COL; f|flag ROW COL");
	}
}
