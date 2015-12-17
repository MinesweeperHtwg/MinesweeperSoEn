package minesweeper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import minesweeper.aview.gui.MinesweeperFrame;
import minesweeper.aview.tui.TextUI;
import minesweeper.model.IGridFactory;
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

		Injector injector = getInjector(GridFactoryProviders.testSolve);

		TextUI tui = injector.getInstance(TextUI.class);
		tui.setPrintGrid(false);
		tui.setPrintCommands(false);

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

		// same instance because gui is a singleton
		MinesweeperFrame gui = injector.getInstance(MinesweeperFrame.class);
		gui.dispose();
	}

	public static Injector getInjector(Provider<IGridFactory> provider) {
		GridFactoryModule gridFactoryModule = new GridFactoryModule();
		gridFactoryModule.setProvider(provider);
		return Guice.createInjector(new MinesweeperModule(), gridFactoryModule);
	}
}
