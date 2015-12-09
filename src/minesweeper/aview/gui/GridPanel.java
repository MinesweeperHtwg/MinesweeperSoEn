package minesweeper.aview.gui;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

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
		setBackground(MinesweeperFrame.BG);
	}

	private class CellListener extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			MouseEvent eConverted = SwingUtilities.convertMouseEvent(e.getComponent(), e, GridPanel.this);
			Object source = getComponentAt(eConverted.getPoint());
			if (!(source instanceof CellPanel)) {
				// mouse is outside window or not over a cell
				return;
			}
			CellPanel cellPanel = (CellPanel) source;
			int row = cellPanel.getRow();
			int col = cellPanel.getCol();
			if (SwingUtilities.isLeftMouseButton(eConverted)) {
				controller.openCell(row, col);
				return;
			}
			if (SwingUtilities.isMiddleMouseButton(eConverted)) {
				controller.openAround(row, col);
				return;
			}
			if (SwingUtilities.isRightMouseButton(eConverted)) {
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
				boolean rightEdge = width == col + 1;
				boolean bottomEdge = height == row + 1;

				CellPanel cellPanel = new CellPanel(controller, row, col, rightEdge, bottomEdge);
				cellPanel.addMouseListener(cellListener);
				cellPanels[row][col] = cellPanel;
				add(cellPanel);
				repaintMgr.markCompletelyClean(cellPanel);
			}
		}

		repaintMgr.markCompletelyDirty(this);
	}

	public void updateCell(int row, int col) {
		CellPanel cellPanel = cellPanels[row][col];
		cellPanel.updateCell();
	}

	public void updateAllCells() {
		for (CellPanel[] rows : cellPanels) {
			for (CellPanel cellPanel : rows) {
				cellPanel.updateCell();
				repaintMgr.markCompletelyClean(cellPanel);
			}
		}
		repaintMgr.markCompletelyDirty(this);
	}
}
