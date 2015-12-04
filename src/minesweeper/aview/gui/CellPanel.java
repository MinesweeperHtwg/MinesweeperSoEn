package minesweeper.aview.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import minesweeper.controller.IMinesweeperController;

public class CellPanel extends JPanel {
	private final int row;
	private final int col;

	private IMinesweeperController controller;
	private JLabel label;

	private static final long serialVersionUID = 1L;

	public CellPanel(IMinesweeperController controller, int row, int col) {
		this.row = row;
		this.col = col;
		this.controller = controller;

		label = new JLabel();
		add(label);

		// GridBagLayout for centering JLabel
		setLayout(new GridBagLayout());

		setMinimumSize(new Dimension(16, 16));
		setPreferredSize(new Dimension(16, 16));
		setBackground(new Color(192, 192, 192));

		updateCell();
	}

	public void updateCell() {
		String cellString = controller.getCellString(row, col);
		if (" ".equals(cellString)) {
			label.setText("");
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		} else if ("F".equals(cellString)) {
			label.setText("F");
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		} else {
			// TODO: mine/mineClicked icon, colored text/icons
			if ("0".equals(cellString)) {
				label.setText("");
			} else {
				label.setText(cellString);
			}
			setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.GRAY));
		}
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

}
