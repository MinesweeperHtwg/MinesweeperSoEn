package minesweeper.aview.gui;

import minesweeper.controller.IMinesweeperController;

import javax.swing.*;
import java.awt.*;

public class GameStatsPanel extends JPanel {
	private IMinesweeperController controller;
	private JLabel label;

	private static final long serialVersionUID = 1L;

	public GameStatsPanel(final IMinesweeperController controller) {
		this.controller = controller;
		label = new JLabel("Stats Placeholder");
		add(label);
		setBorder(BorderFactory.createLoweredBevelBorder());

		setLayout(new FlowLayout());
	}

	public void updateGameStats() {
		label.setText(controller.getGameStats());
	}

}
