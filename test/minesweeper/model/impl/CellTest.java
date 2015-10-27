package minesweeper.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CellTest {
	
	Cell cell;

	@Before
	public void setUp() throws Exception {
		cell = new Cell(1,2);
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
		assertEquals(true, cell.isFlag());
		cell.setFlag(false);
		assertEquals(false, cell.isFlag());
	}
	
	@Test
	public void testContainsMine() {
		cell.setContainsMine(true);
		assertEquals(true, cell.isMine());
		cell.setContainsMine(false);
		assertEquals(false, cell.isMine());
	}
	
	@Test
	public void testIsOpened() {
		cell.setOpened(true);
		assertEquals(true, cell.isOpened());
		cell.setOpened(false);
		assertEquals(false, cell.isOpened());
	}

}
