package minesweeper.aview.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import minesweeper.controller.IMinesweeperController;

public class GameStatsPanel extends JPanel {
	private IMinesweeperController controller;

	public GameStatsPanel(final IMinesweeperController controller) {
		this.controller = controller;
		add(new JLabel("Stats Placeholder"));
	}

	private static final long serialVersionUID = 1L;
}
