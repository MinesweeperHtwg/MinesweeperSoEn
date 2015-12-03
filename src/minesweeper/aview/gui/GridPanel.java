package minesweeper.aview.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.RepaintManager;

import minesweeper.controller.IMinesweeperController;

public class GridPanel extends JPanel {
	private int height;
	private int width;
	private CellPanel[][] cellPanels;
	
	private final RepaintManager repaintMgr;

	private IMinesweeperController controller;
	
	private static final long serialVersionUID = 1L;

	public GridPanel(final IMinesweeperController controller) {
		this.controller = controller;
		rebuildCells();
        repaintMgr = RepaintManager.currentManager(this);
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
				cellPanels[row][col] = cellPanel;
				add(cellPanel);
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
