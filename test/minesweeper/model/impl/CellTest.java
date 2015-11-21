package minesweeper.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import minesweeper.model.ICell.State;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

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
	public void testIsClosedWithoutFlag() {
		cell.setState(State.OPENED);
		assertFalse(cell.isClosedWithoutFlag());
		cell.setState(State.CLOSED);
		assertTrue(cell.isClosedWithoutFlag());
		cell.setState(State.FLAG);
		assertFalse(cell.isClosedWithoutFlag());
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
		assertEquals("(2, 1) =  ", cell.mkString());
	}

	@Test
	public void testToString() {
		cell.setState(State.CLOSED);
		assertEquals(" ", cell.toString());
		cell.setState(State.FLAG);
		assertEquals("F", cell.toString());
		cell.setState(State.OPENED);
		cell.setIsMine(true);
		assertEquals("M", cell.toString());
		cell.setIsMine(false);
		cell.setMines(1);
		assertEquals("1", cell.toString());
		cell.setMines(0);
		assertEquals("0", cell.toString());
	}

	@Test
	public void testEqualsContract() {
		EqualsVerifier.forClass(Cell.class).suppress(Warning.NONFINAL_FIELDS).verify();
	}
}
