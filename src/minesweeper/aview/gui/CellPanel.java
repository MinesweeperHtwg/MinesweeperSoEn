package minesweeper.aview.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;

import minesweeper.controller.IMinesweeperController;

public class CellPanel extends JPanel {
	private final int row;
	private final int col;

	private IMinesweeperController controller;
	private JLabel label;

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
		if (cellString.equals(" ")) {
			label.setText("");
			setBorder(new BevelBorder(BevelBorder.RAISED));
		} else {
			// TODO: mine/mineClicked icon, colored text/icons
			if (cellString.equals("0")) {
				label.setText("");
			} else {
				label.setText(cellString);
			}
			setBorder(new MatteBorder(1, 1, 0, 0, Color.GRAY));
		}

	}

	private static final long serialVersionUID = 1L;
}
