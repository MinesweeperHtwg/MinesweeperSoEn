package minesweeper.model.impl;

import static org.junit.Assert.*;

import java.util.List;

import minesweeper.model.impl.Cell.State;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator;

public class GridTest {
    Grid grid;
    Cell[][] cells;

    @Before
    public void setUp() throws Exception {
        cells = new Cell[2][2];
        cells[0][0] = new Cell(0, 0, State.CLOSED, 0, false);
        cells[0][1] = new Cell(0, 1, State.OPENED,0,true);
        cells[1][0] = new Cell(1, 0, State.FLAG,0,false);
        cells[1][1] = new Cell(1, 1, State.OPENED,1,false);
        grid = new Grid(cells,1);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetCell() {
        assertEquals(new Cell(0, 0, State.CLOSED, 0, false), grid.getCell(0, 0));
    }

    @Test
    public void testCheckBounds() {
        thrown.expect(IllegalArgumentException.class);
        grid.getCell(0, 2);
    }
    
    @Test
    public void testGetHeight() {
    	assertEquals(2, grid.getHeight());
    }
    
    @Test
    public void testGetWidth() {
    	assertEquals(2, grid.getWidth());
    }
    
    @Test
    public void testGetMines() {
    	assertEquals(1, grid.getMines());
    }
    
    @Test
    public void testGetSecondsSinceCreated() {
    	long seconds = grid.getSecondsSinceCreated();
    	assertTrue(seconds >= 0 && seconds < 60);
    }

    @Test
    public void testGetCells() {
        List<Cell> cellList = grid.getCells();
        assertTrue(cellList.contains(cells[0][0]));
        assertTrue(cellList.contains(cells[0][1]));
        assertTrue(cellList.contains(cells[1][0]));
        assertTrue(cellList.contains(cells[1][1]));
        assertEquals(4, cellList.size());
    }
    
    @Test
    public void testGetAdjCells() {
    	List<Cell> cellList = grid.getAdjCells(0, 0);
        assertTrue(cellList.contains(cells[0][1]));
        assertTrue(cellList.contains(cells[1][0]));
        assertTrue(cellList.contains(cells[1][1]));
        assertEquals(3, cellList.size());
    }

    @Test
    public void testMkString() {
        assertEquals(" |M\nF|1", grid.toString());
    }

}
