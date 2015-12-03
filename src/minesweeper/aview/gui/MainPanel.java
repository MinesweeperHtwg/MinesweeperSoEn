package minesweeper.aview.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import minesweeper.controller.IMinesweeperController;

public class MainPanel extends JPanel {

	private GridPanel gridPanel;
	private GameStatsPanel gameStatsPanel;

	public MainPanel(final IMinesweeperController controller) {
		setLayout(new BorderLayout());

		gameStatsPanel = new GameStatsPanel(controller);
		add(gameStatsPanel, BorderLayout.NORTH);

		gridPanel = new GridPanel(controller);
		add(gridPanel, BorderLayout.CENTER);
	}

	public GridPanel getGridPanel() {
		return gridPanel;
	}

	public GameStatsPanel getGameStatsPanel() {
		return gameStatsPanel;
	}

	private static final long serialVersionUID = 1L;
}
