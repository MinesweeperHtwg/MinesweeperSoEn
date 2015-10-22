package model.impl;

public class Grid {
    private Cell[][] cells;
    
    public Grid(int height, int width) {
        cells = new Cell[height][width];
        
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }
}
