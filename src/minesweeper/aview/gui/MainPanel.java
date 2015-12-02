package minesweeper.aview.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import minesweeper.controller.IMinesweeperController;

public class MainPanel extends JPanel {

	private GridPanel gridPanel;
	private GameStatsPanel gameStatsPanel;

	public MainPanel(final IMinesweeperController controller) {
		gridPanel = new GridPanel(controller);
		add(gridPanel);

		gameStatsPanel = new GameStatsPanel(controller);
		add(gameStatsPanel);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	private static final long serialVersionUID = 1L;
}
