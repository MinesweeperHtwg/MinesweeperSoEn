package minesweeper.aview.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		cellPanels = new CellPanel[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				CellPanel cellPanel = new CellPanel(controller, row, col);
				cellPanels[row][col] = cellPanel;
				c.gridy = row;
				c.gridx = col;
				add(cellPanel, c);
			}
		}
	}

	private static final long serialVersionUID = 1L;
}
