package minesweeper.aview.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import minesweeper.controller.IMinesweeperController;

public class GameStatsPanel extends JPanel {
	private IMinesweeperController controller;
    private JLabel label;

	public GameStatsPanel(final IMinesweeperController controller) {
		this.controller = controller;
		label = new JLabel("Stats Placeholder");
		add(label);
	}
	
	public void updateGameStats() {
	    label.setText(controller.getGameStats());
	}

	private static final long serialVersionUID = 1L;
}
