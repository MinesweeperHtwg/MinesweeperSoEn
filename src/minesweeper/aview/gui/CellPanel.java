package minesweeper.aview.gui;

import minesweeper.controller.IMinesweeperController;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;

public class CellPanel extends JPanel {
	private final int row;
	private final int col;

	private final Border clickedBorder;

	private IMinesweeperController controller;
	private JLabel label;

	private static final long serialVersionUID = 1L;

	public CellPanel(IMinesweeperController controller, int row, int col, boolean rightEdge, boolean bottomEdge) {
		this.row = row;
		this.col = col;
		this.controller = controller;

		int rightLine = 0;
		int bottomLine = 0;

		if (rightEdge) {
			rightLine = 1;
		}
		if (bottomEdge) {
			bottomLine = 1;
		}

		clickedBorder = BorderFactory.createMatteBorder(1, 1, bottomLine, rightLine, Color.GRAY);

		label = new JLabel();
		add(label);

		// GridBagLayout for centering JLabel
		setLayout(new GridBagLayout());

		setPreferredSize(new Dimension(16, 16));
		setMinimumSize(new Dimension(16, 16));

		setBackground(MinesweeperFrame.BG);

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
			setBorder(clickedBorder);
		}
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

}
