package minesweeper.model.impl;

import static org.junit.Assert.*;
import minesweeper.model.impl.Cell.State;

import org.junit.Before;
import org.junit.Test;

public class CellTest {

    Cell cell;

    @Before
    public void setUp() throws Exception {
        cell = new Cell(2, 1);
    }

    @Test
    public void testConstructor() {
        cell = new Cell(2, 1, State.OPENED, 2, true);
        assertEquals(1, cell.getCol());
        assertEquals(2, cell.getRow());
        assertEquals(State.OPENED, cell.getState());
        assertEquals(2, cell.getMines());
        assertEquals(true, cell.isMine());
    }

    @Test
    public void testEnum() {
        assertEquals(State.OPENED, State.valueOf("OPENED"));
    }

    @Test
    public void testCol() {
        assertEquals(1, cell.getCol());
    }

    @Test
    public void testRow() {
        assertEquals(2, cell.getRow());
    }

    @Test
    public void testGetMines() {
        cell.setMines(1);
        assertEquals(1, cell.getMines());
        cell.setMines(0);
        assertEquals(0, cell.getMines());
    }

    @Test
    public void testGetState() {
        cell.setState(State.CLOSED);
        assertEquals(State.CLOSED, cell.getState());
        cell.setState(State.FLAG);
        assertEquals(State.FLAG, cell.getState());
        cell.setState(State.OPENED);
        assertEquals(State.OPENED, cell.getState());
    }

    @Test
    public void testIsClosed() {
        cell.setState(State.OPENED);
        assertFalse(cell.isClosed());
        cell.setState(State.CLOSED);
        assertTrue(cell.isClosed());
        cell.setState(State.FLAG);
        assertTrue(cell.isClosed());
    }

    @Test
    public void testIsFlag() {
        cell.setState(State.OPENED);
        assertFalse(cell.isFlag());
        cell.setState(State.CLOSED);
        assertFalse(cell.isFlag());
        cell.setState(State.FLAG);
        assertTrue(cell.isFlag());
    }

    @Test
    public void testContainsMine() {
        cell.setIsMine(true);
        assertTrue(cell.isMine());
        cell.setIsMine(false);
        assertFalse(cell.isMine());
    }

    @Test
    public void testIsOpened() {
        cell.setState(State.OPENED);
        assertTrue(cell.isOpened());
        cell.setState(State.CLOSED);
        assertFalse(cell.isOpened());
        cell.setState(State.FLAG);
        assertFalse(cell.isOpened());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMinesIllegal() {
        cell.setMines(-1);
    }

    @Test
    public void testMkString() {
        cell.setState(State.CLOSED);
        assertEquals(" ", cell.mkString());
        cell.setState(State.FLAG);
        assertEquals("F", cell.mkString());
        cell.setState(State.OPENED);
        cell.setIsMine(true);
        assertEquals("M", cell.mkString());
        cell.setIsMine(false);
        cell.setMines(1);
        assertEquals("1", cell.mkString());
        cell.setMines(0);
        assertEquals("0", cell.mkString());
    }

    @Test
    public void testEquals() {
        Cell c1 = new Cell(2, 1);
        assertEquals(c1, c1);
        assertFalse(c1.equals(null));
        assertFalse(c1.equals(1));

        Cell c2 = new Cell(2, 2);
        assertFalse(c1.equals(c2));
        c2 = new Cell(1, 1);
        assertFalse(c1.equals(c2));
        c2 = new Cell(2, 1);
        c2.setState(State.FLAG);
        assertFalse(c1.equals(c2));
        c2.setState(State.CLOSED);
        c2.setIsMine(true);
        assertFalse(c1.equals(c2));
        c2.setIsMine(false);
        c2.setMines(1);
        assertFalse(c1.equals(c2));
        c2.setMines(0);
        assertTrue(c1.equals(c2));
    }
}
