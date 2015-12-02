package minesweeper.aview.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import minesweeper.controller.IMinesweeperController;

class StatusPanel extends JPanel {
	private JLabel statusLabel;
	private IMinesweeperController controller;

	StatusPanel(final IMinesweeperController controller) {
		setBorder(LineBorder.createBlackLineBorder());
		statusLabel = new JLabel();
		statusLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(statusLabel);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	void setText(String s) {
		statusLabel.setText(controller.getStatusLine());
	}

	private static final long serialVersionUID = 1L;
}
