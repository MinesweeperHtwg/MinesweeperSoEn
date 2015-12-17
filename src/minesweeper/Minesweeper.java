package minesweeper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import minesweeper.aview.gui.MinesweeperFrame;
import minesweeper.aview.tui.TextUI;
import minesweeper.controller.IMinesweeperController;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.util.Scanner;

public class Minesweeper {
	private static final Logger LOGGER = Logger.getLogger(Minesweeper.class);

	private Minesweeper() {
	}

	public static void main(String[] args) {
		// Set up logging through log4j
		PropertyConfigurator.configure("log4j.properties");

		GridFactoryModule gridFactoryModule = new GridFactoryModule();
		gridFactoryModule.setProvider(GridFactoryProviders.debugSolve);

		Injector injector = Guice.createInjector(new MinesweeperModule(), gridFactoryModule);

		IMinesweeperController controller = injector.getInstance(IMinesweeperController.class);
		controller.openCell(0, 0);
		controller.openCell(0, 3);

		TextUI tui = injector.getInstance(TextUI.class);

		try {
			SwingUtilities.invokeAndWait(() -> {
				MinesweeperFrame gui = injector.getInstance(MinesweeperFrame.class);
				gui.setVisible(true);
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

		// kill gui
		System.exit(0);
	}
}
