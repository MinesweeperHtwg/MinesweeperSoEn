package minesweeper;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import org.apache.log4j.PropertyConfigurator;

import minesweeper.aview.gui.MinesweeperFrame;
import minesweeper.aview.tui.TextUI;
import minesweeper.controller.IMinesweeperController;
import minesweeper.controller.impl.ControllerWrapper;
import minesweeper.model.IGridFactory;
import minesweeper.model.impl.GridFactory;

public class Minesweeper {
	private Minesweeper() {
	}

	public static void main(String[] args) {
		// Set up logging through log4j
		PropertyConfigurator.configure("log4j.properties");

		IGridFactory gFact = new GridFactory();
		gFact.size(10, 20).mines(10);
		IMinesweeperController controller = new ControllerWrapper(gFact);
		TextUI tui = new TextUI(controller);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MinesweeperFrame(controller);
			}
		});
		boolean cont = true;
		Scanner scanner = new Scanner(System.in);
		while (cont) {
			cont = tui.processLine(scanner.nextLine());
		}
		scanner.close();
	}
}
