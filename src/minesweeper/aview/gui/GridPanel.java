package minesweeper.aview.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import minesweeper.controller.IMinesweeperController;

public class GridPanel extends JPanel {
	private int height;
	private int width;
	private CellPanel[][] cellPanels;

	private IMinesweeperController controller;

	public GridPanel(final IMinesweeperController controller) {
		this.controller = controller;
		reset();
	}

	public void reset() {
		height = controller.getHeight();
		width = controller.getWidth();

		setLayout(new GridLayout(height, width));

		cellPanels = new CellPanel[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				CellPanel cellPanel = new CellPanel(controller, row, col);
				cellPanels[row][col] = cellPanel;
				add(cellPanel);
			}
		}
	}

	private static final long serialVersionUID = 1L;
}
