package minesweeper.aview.gui;

import javax.swing.JPanel;

import minesweeper.controller.IMinesweeperController;

public class GridPanel extends JPanel {
	private IMinesweeperController controller;

	public GridPanel(final IMinesweeperController controller) {
		this.controller = controller;
	}

	private static final long serialVersionUID = 1L;
}
