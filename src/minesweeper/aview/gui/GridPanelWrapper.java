package minesweeper.aview.gui;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import minesweeper.controller.IMinesweeperController;

public class GridPanelWrapper extends JPanel {
	private static final long serialVersionUID = 1L;

	private final GridPanel gridPanel;

	public GridPanelWrapper(final IMinesweeperController controller) {
		gridPanel = new GridPanel(controller, this);

		setBorder(new CompoundBorder(new EmptyBorder(6, 0, 6, 0), BorderFactory.createLoweredBevelBorder()));
		setBackground(MinesweeperFrame.BG);

		setLayout(new GridBagLayout());
		add(gridPanel);
	}

	public GridPanel getGridPanel() {
		return gridPanel;
	}

}
