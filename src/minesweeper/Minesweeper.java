package minesweeper;

import org.apache.log4j.PropertyConfigurator;

import minesweeper.aview.tui.TextUI;
import minesweeper.controller.impl.MinesweeperController;
import minesweeper.model.IGridFactory;
import minesweeper.model.impl.GridFactory;

public class Minesweeper {
	private Minesweeper() {
	}

	public static void main(String[] args) {
		// Set up logging through log4j
		PropertyConfigurator.configure("log4j.properties");

		IGridFactory gFact = new GridFactory(10, 20, 50);
		MinesweeperController controller = new MinesweeperController(gFact);
		TextUI tui = new TextUI(controller);
		
		controller.newGame();
		controller.openCell(0, 0);
		controller.openCell(1, 0);
		controller.openCell(0, 1);
		controller.openCell(1, 1);
		controller.openCell(1, 1);
	}
}
