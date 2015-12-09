package minesweeper.aview.gui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import minesweeper.controller.IMinesweeperController;

class StatusPanel extends JPanel {
	private JTextArea statusLabel;
	private IMinesweeperController controller;

	private static final long serialVersionUID = 1L;

	StatusPanel(final IMinesweeperController controller) {
		this.controller = controller;
		setBorder(BorderFactory.createLoweredBevelBorder());
		statusLabel = new JTextArea(1, 15);
		statusLabel.setEditable(false);
		statusLabel.setHighlighter(null);
		statusLabel.setLineWrap(true);
		statusLabel.setWrapStyleWord(true);
		statusLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		add(statusLabel);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	}

	void updateText() {
		statusLabel.setText(controller.getStatusLine());
	}

}
