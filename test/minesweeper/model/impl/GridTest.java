package minesweeper.model.impl;

import static org.junit.Assert.*;

import java.util.List;

import minesweeper.model.impl.Cell.State;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GridTest {
    Grid grid;
    Cell[][] cells;

    @Before
    public void setUp() throws Exception {
        cells = new Cell[2][2];
        cells[0][0] = new Cell(0, 0, State.CLOSED, 0, false);
        cells[1][0] = new Cell(1, 0, State.OPENED,0,true);
        cells[0][1] = new Cell(0, 1, State.FLAG,0,false);
        cells[1][1] = new Cell(1, 1, State.OPENED,1,false);
        grid = new Grid(cells);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGridNull() {
        thrown.expect(IllegalArgumentException.class);
        new Grid(null);
    }

    @Test
    public void testGridIllegalArrayEmpty() {
        thrown.expect(IllegalArgumentException.class);
        new Grid(new Cell[0][]);
    }

    @Test
    public void testGridIllegalArrayTwoDimensionsNull() {
        thrown.expect(IllegalArgumentException.class);
        Cell[][] cells = new Cell[10][10];
        cells[0] = null;
        new Grid(cells);
    }

    @Test
    public void testGridIllegalArrayTwoDimensionsLengthZero() {
        thrown.expect(IllegalArgumentException.class);
        new Grid(new Cell[10][0]);
    }

    @Test
    public void testGetCell() {
        assertEquals(new Cell(0, 0, State.CLOSED, 0, false), grid.getCell(0, 0));
    }

    @Test
    public void testGetMines() {
        assertEquals(1, grid.getMines(1, 1));
    }

    @Test
    public void testIsFlag() {
        assertEquals(true, grid.isFlag(0, 1));
    }

    @Test
    public void testIsMine() {
        assertEquals(true, grid.isMine(1, 0));
    }

    @Test
    public void testIsOpened() {
        assertEquals(true, grid.isOpened(1, 1));
    }

    @Test
    public void testCheckBounds() {
        thrown.expect(IllegalArgumentException.class);
        grid.getCell(0, 2);
    }

    @Test
    public void testGetList() {
        List<Cell> cellList = grid.getList();
        assertTrue(cellList.contains(cells[0][0]));
        assertTrue(cellList.contains(cells[0][1]));
        assertTrue(cellList.contains(cells[1][0]));
        assertTrue(cellList.contains(cells[1][1]));
        assertEquals(4, cellList.size());
    }

    @Test
    public void testMkString() {
        assertEquals(" |F\nM|1\n", grid.mkString());
    }

}
