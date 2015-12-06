package minesweeper.aview.gui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import minesweeper.controller.IMinesweeperController;

class StatusPanel extends JPanel {
	private JLabel statusLabel;
	private IMinesweeperController controller;

	private static final long serialVersionUID = 1L;

	StatusPanel(final IMinesweeperController controller) {
		this.controller = controller;
		setBorder(BorderFactory.createLoweredBevelBorder());
		statusLabel = new JLabel();
		statusLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(statusLabel);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	void updateText() {
		statusLabel.setText(controller.getStatusLine());
	}

}
