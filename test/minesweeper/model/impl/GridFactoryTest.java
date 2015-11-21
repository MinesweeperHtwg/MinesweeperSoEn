package minesweeper.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GridFactoryTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testRandomIllegalNumberOfMines() {
		new GridFactory(2, 2).random(4);
		thrown.expect(IllegalArgumentException.class);
		new GridFactory(2, 2).random(5);
	}

	@Test
	public void testRandomClearIllegalNumberOfMines() {
		new GridFactory(2, 2).randomClear(3, 0, 0);
		thrown.expect(IllegalArgumentException.class);
		new GridFactory(2, 2).randomClear(4, 0, 0);
	}

	@Test
	public void testSpecifiedIllegalNumberOfMines() {
		new GridFactory(2, 2).specified(new int[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } });
		thrown.expect(IllegalArgumentException.class);
		new GridFactory(2, 2).specified(new int[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 }, { 1, 2 } });
	}

	@Test
	public void testSpecifiedIllegalFormat() {
		thrown.expect(IllegalArgumentException.class);
		new GridFactory(2, 2).specified(new int[][] { { 0 } });
	}

	@Test
	public void testGetGridIllegalState() {
		thrown.expect(IllegalStateException.class);
		new GridFactory(2, 2).getGrid();
	}

	@Test
	public void testRandomGetGrid() {
		Grid grid = new GridFactory(5, 10).random(10).getGrid();

		assertEquals(5, grid.getHeight());
		assertEquals(10, grid.getWidth());

		long mines = grid.getCells().stream().filter(Cell::isMine).count();
		assertEquals(10, mines);
	}

	@Test
	public void testRandomClearGetGrid() {
		Grid grid = new GridFactory(5, 10).randomClear(49, 1, 2).getGrid();

		assertEquals(5, grid.getHeight());
		assertEquals(10, grid.getWidth());

		long mines = grid.getCells().stream().filter(Cell::isMine).count();
		assertEquals(49, mines);

		assertFalse(grid.getCell(1, 2).isMine());
	}

	@Test
	public void testSpecifiedGetGrid() {
		int[][] mineLocations = new int[][] { { 0, 0 }, { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };
		Grid grid = new GridFactory(5, 10).specified(mineLocations).getGrid();
		assertEquals(5, grid.getHeight());
		assertEquals(10, grid.getWidth());

		long mines = grid.getCells().stream().filter(Cell::isMine).count();
		assertEquals(5, mines);

		for (int i = 0; i < 5; i++) {
			assertTrue(grid.getCell(i, i).isMine());
		}
		assertEquals(2, grid.getCell(1, 0).getMines());
		assertEquals(1, grid.getCell(2, 0).getMines());
		assertEquals(0, grid.getCell(3, 0).getMines());
	}
}
