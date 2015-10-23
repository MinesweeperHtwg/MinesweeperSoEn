package minesweeper.model.impl;

import minesweeper.model.IGridFactory;

public class GridFactory implements IGridFactory {
    private int height;
    private int width;
    private int mines;
    
    public GridFactory(int height, int width, int mines) {
        this.height = height;
        this.width = width;
        this.mines = mines;
    }
    
    /* (non-Javadoc)
     * @see minesweeper.model.impl.IGridFactory#getGrid()
     */
    @Override
    public Grid getGrid() {
        Cell[][] cells = new Cell[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }

        for (Cell[] rows: cells) {
            for (Cell cell: rows) {
                cell.setFlag(false);
                cell.setOpened(false);
                cell.setContainsMine(false);
                cell.setMines(0);
            }
        }
        
        Grid grid = new Grid(cells);
        
        return grid;
    }
    
    
}
