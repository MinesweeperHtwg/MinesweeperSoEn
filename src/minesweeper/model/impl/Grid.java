package minesweeper.model.impl;

public class Grid {
    private Cell[][] cells;
    private int height = 0;
    private int width = 0;
    
    protected Grid(Cell[][] cells) {
        setCells(cells);
    }
    
    protected void setCells(Cell[][] cells) {
        this.cells = cells;
        height = cells.length;
        width = cells[0].length;
    }
    
    public Cell getCell(int height, int width) {
        return cells[height][width];
    }
    
    public int getMines(int height, int width) {
        return getCell(height, width).getMines();
    }
    

    
}
