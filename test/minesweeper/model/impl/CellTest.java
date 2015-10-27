package minesweeper.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CellTest {

	Cell cell;

	@Before
	public void setUp() throws Exception {
		cell = new Cell(1, 2);
	}

	@Test
	public void testCellChar() {
		assertTrue(new Cell(1, 2, 'M').isMine());
		assertFalse(new Cell(1, 2, ' ').isOpened());
		assertTrue(new Cell(1, 2, 'F').isFlag());
		for (int i = 0; i < 9; i++) {
			assertEquals(i,
					new Cell(1, 2, Character.forDigit(i, 10)).getMines());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCellCharIllegal() {
		new Cell(1,2,'9');
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
	public void testIsFlag() {
		cell.setFlag(true);
		assertTrue(cell.isFlag());
		cell.setFlag(false);
		assertFalse(cell.isFlag());
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
		cell.setOpened(true);
		assertTrue(cell.isOpened());
		cell.setOpened(false);
		assertFalse(cell.isOpened());
	}

	@Test
	public void testMkString() {
		assertEquals(" ", cell.mkString());
		cell.setIsMine(false);
		cell.setOpened(false);
		cell.setFlag(false);
		assertEquals(" ", cell.mkString());
		cell.setFlag(true);
		assertEquals("F", cell.mkString());
		cell.setFlag(false);
		cell.setOpened(true);
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
		Cell c1 = new Cell(1,2,'2');
		assertEquals(c1, c1);
		assertFalse(c1.equals(null));
		assertFalse(c1.equals(1));
		
		Cell c2 = new Cell(2,2,'2');
		assertFalse(c1.equals(c2));
		c2 = new Cell(1,1,'2');
		assertFalse(c1.equals(c2));
		c2 = new Cell(1,2,'1');
		assertFalse(c1.equals(c2));
		c2 = new Cell(1,2,'M');
		assertFalse(c1.equals(c2));
		c2 = new Cell(1,2,'F');
		assertFalse(c1.equals(c2));
		c2 = new Cell(1,2,' ');
		c2.setMines(2);
		assertFalse(c1.equals(c2));

		c2 = new Cell(1,2,'2');
		assertEquals(c1, c2);
	}
}
