package minesweeper.aview.gui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import minesweeper.controller.IMinesweeperController;

public class GameStatsPanel extends JPanel {
	private IMinesweeperController controller;
	private JLabel label;

	private static final long serialVersionUID = 1L;

	public GameStatsPanel(final IMinesweeperController controller) {
		this.controller = controller;
		label = new JLabel("Stats Placeholder");
		add(label);
		setBorder(BorderFactory.createLoweredBevelBorder());
	}

	public void updateGameStats() {
		label.setText(controller.getGameStats());
	}

}
