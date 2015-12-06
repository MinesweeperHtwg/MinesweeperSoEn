package minesweeper.aview.gui;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import minesweeper.controller.IMinesweeperController;

public class GridPanel extends JPanel {
	private IMinesweeperController controller;

	private int height;
	private int width;

	private CellPanel[][] cellPanels;

	private final MouseListener cellListener;

	private final RepaintManager repaintMgr;

	private static final long serialVersionUID = 1L;

	public GridPanel(final IMinesweeperController controller) {
		this.controller = controller;
		this.cellListener = new CellListener();
		repaintMgr = RepaintManager.currentManager(this);
		rebuildCells();
		setBorder(new CompoundBorder(new EmptyBorder(6, 0, 6, 0), BorderFactory.createLoweredBevelBorder()));
		setBackground(MinesweeperFrame.BG);
	}

	private class CellListener extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			e = SwingUtilities.convertMouseEvent(e.getComponent(), e, GridPanel.this);
			Object source = getComponentAt(e.getPoint());
			if (!(source instanceof CellPanel)) {
				// mouse is outside window or not over a cell
				return;
			}
			CellPanel cellPanel = (CellPanel) source;
			int row = cellPanel.getRow();
			int col = cellPanel.getCol();
			if (SwingUtilities.isLeftMouseButton(e)) {
				controller.openCell(row, col);
				return;
			}
			if (SwingUtilities.isMiddleMouseButton(e)) {
				controller.openAround(row, col);
				return;
			}
			if (SwingUtilities.isRightMouseButton(e)) {
				controller.toggleFlag(row, col);
				return;
			}
		}
	}

	public void rebuildCells() {
		removeAll();

		height = controller.getHeight();
		width = controller.getWidth();

		setLayout(new GridLayout(height, width));

		cellPanels = new CellPanel[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				CellPanel cellPanel = new CellPanel(controller, row, col);
				cellPanel.addMouseListener(cellListener);
				cellPanels[row][col] = cellPanel;
				add(cellPanel);
				repaintMgr.markCompletelyClean(cellPanel);
			}
		}
	}

	public void updateCell(int row, int col) {
		cellPanels[row][col].updateCell();
	}

	public void updateAllCells() {
		for (CellPanel[] rows : cellPanels) {
			for (CellPanel cellPanel : rows) {
				cellPanel.updateCell();
				repaintMgr.markCompletelyClean(cellPanel);
			}
		}
	}
}
