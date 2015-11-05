package minesweeper.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GridFactoryTest {
	@Test(expected = IllegalArgumentException.class)
	public void testGridFactoryIllegal() {
		new GridFactory(10, 10, 101);
	}

	@Test
	public void testGetGrid() {
		GridFactory gFact = new GridFactory(5, 10, 10);
		Grid grid = gFact.getGrid();
		
		assertEquals(5, grid.getHeight());
		assertEquals(10, grid.getWidth());

		int mines = 0;
		for (Cell cell : grid.getCells()) {
			if (cell.isMine()) {
				mines++;
			}
		}
		assertEquals(10, mines);
	}
}
