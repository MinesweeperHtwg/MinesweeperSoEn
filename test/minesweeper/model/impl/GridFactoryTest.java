package minesweeper.model.impl;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GridFactoryTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
	@Test
	public void testGridFactoryIllegal() {
		new GridFactory(10, 10, 100);
		thrown.expect(IllegalArgumentException.class);
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
