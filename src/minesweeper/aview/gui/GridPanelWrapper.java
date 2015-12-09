package minesweeper.aview.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import minesweeper.controller.IMinesweeperController;

public class GridPanelWrapper extends JPanel {
	private static final long serialVersionUID = 1L;

	private final GridPanel gridPanel;

	public GridPanelWrapper(final IMinesweeperController controller) {
		gridPanel = new GridPanel(controller);

		setBorder(new CompoundBorder(new EmptyBorder(6, 0, 6, 0), BorderFactory.createLoweredBevelBorder()));
		setBackground(MinesweeperFrame.BG);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeProportinal(gridPanel);
			}
		});

		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		add(gridPanel);
	}

	private void resizeProportinal(JPanel panel) {
		Dimension innerPref = panel.getLayout().minimumLayoutSize(panel);
		double innerWidth = innerPref.getWidth();
		double innerHeight = innerPref.getHeight();

		double outerWidth = getSize().getWidth() - 4;
		double outerHeight = getSize().getHeight() - 16;

		double scale = Math.min(outerWidth / innerWidth, outerHeight / innerHeight);

		Dimension size = new Dimension((int) (innerWidth * scale), (int) (innerHeight * scale));

		panel.setPreferredSize(size);

		revalidate();
		repaint();
	}

	public GridPanel getGridPanel() {
		return gridPanel;
	}

}
