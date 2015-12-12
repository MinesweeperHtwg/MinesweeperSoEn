package minesweeper;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.inject.Guice;
import com.google.inject.Injector;

import minesweeper.aview.gui.MinesweeperFrame;
import minesweeper.aview.tui.TextUI;
import minesweeper.controller.IMinesweeperController;

public class Minesweeper {
	private static final Logger LOGGER = Logger.getLogger(Minesweeper.class);

	private Minesweeper() {
	}

	public static void main(String[] args) {
		// Set up logging through log4j
		PropertyConfigurator.configure("log4j.properties");

		Injector injector = Guice.createInjector(new MinesweeperModule());

		IMinesweeperController controller = injector.getInstance(IMinesweeperController.class);
		controller.openCell(0, 0);
		controller.openCell(0, 2);
		controller.openCell(0, 4);
		controller.openCell(1, 1);

		TextUI tui = injector.getInstance(TextUI.class);

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					MinesweeperFrame gui = injector.getInstance(MinesweeperFrame.class);
					gui.setVisible(true);
				}
			});
		} catch (Exception e) {
			LOGGER.error("GUI initialization error", e);
		}

		boolean cont = true;
		Scanner scanner = new Scanner(System.in);
		while (cont) {
			cont = tui.processLine(scanner.nextLine());
		}
		scanner.close();
	}
}
