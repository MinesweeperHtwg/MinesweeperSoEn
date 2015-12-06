package minesweeper;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import org.apache.log4j.PropertyConfigurator;

import com.google.inject.Guice;
import com.google.inject.Injector;

import minesweeper.aview.gui.MinesweeperFrame;
import minesweeper.aview.tui.TextUI;

public class Minesweeper {
	private Minesweeper() {
	}

	public static void main(String[] args) {
		// Set up logging through log4j
		PropertyConfigurator.configure("log4j.properties");

		Injector injector = Guice.createInjector(new MinesweeperModule());

		TextUI tui = injector.getInstance(TextUI.class);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MinesweeperFrame gui = injector.getInstance(MinesweeperFrame.class);
				gui.setVisible(true);
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
