package minesweeper.aview.gui;

import javax.swing.JPanel;

import minesweeper.controller.IMinesweeperController;

public class GameStatsPanel extends JPanel {
	private IMinesweeperController controller;

	public GameStatsPanel(final IMinesweeperController controller) {
		this.controller = controller;
	}

	private static final long serialVersionUID = 1L;
}
