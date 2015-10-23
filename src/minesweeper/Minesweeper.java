package minesweeper;

import minesweeper.model.IGridFactory;
import minesweeper.model.impl.DebugGridFactory;
import minesweeper.model.impl.Grid;

public class Minesweeper {
    public static void main(String[] args) {
        System.out.println("I'm alive!");
        
        IGridFactory gFact = new DebugGridFactory();
        
        Grid grid = gFact.getGrid();
        
        grid.getMines(0, 0);
    }
}
