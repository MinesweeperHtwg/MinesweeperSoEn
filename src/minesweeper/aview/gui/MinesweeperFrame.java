package minesweeper.aview.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import minesweeper.aview.tui.TextUI;
import minesweeper.controller.IMinesweeperController;
import minesweeper.util.observer.Event;
import minesweeper.util.observer.IObserver;

@SuppressWarnings("serial")
public class MinesweeperFrame extends JFrame implements IObserver {
	private static final Logger LOGGER = Logger.getLogger(TextUI.class);

	private IMinesweeperController controller;

	private Container pane;
	private StatusPanel statusPanel;
	private MainPanel mainPanel;

	public MinesweeperFrame(final IMinesweeperController controller) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			LOGGER.debug("Can't change look and feel");
		}

		this.controller = controller;
		controller.addObserver(this);

		pane = getContentPane();
		pane.setLayout(new BorderLayout());

		mainPanel = new MainPanel(controller);
		pane.add(mainPanel, BorderLayout.CENTER);

		statusPanel = new StatusPanel(controller);
		pane.add(statusPanel, BorderLayout.SOUTH);

		setTitle("Minesweeper");
		setResizable(true);
		setSize(300, 300);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void update(Event e) {

	}
}
