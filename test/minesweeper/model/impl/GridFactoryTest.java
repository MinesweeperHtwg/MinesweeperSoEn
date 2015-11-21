package minesweeper.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import minesweeper.model.ICell;
import minesweeper.model.IGrid;

public class GridFactoryTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testRandomIllegalTooManyMines() {
		new GridFactory(2, 2).mines(4).random().getGrid();
		thrown.expect(IllegalArgumentException.class);
		new GridFactory(2, 2).mines(5).random().getGrid();
	}

	@Test
	public void testRandomClearIllegalTooManyMines() {
		new GridFactory(2, 2).mines(3).randomClear(0, 0).getGrid();
		thrown.expect(IllegalArgumentException.class);
		new GridFactory(2, 2).mines(4).randomClear(0, 0).getGrid();
	}

	@Test
	public void testSpecifiedIllegalTooManyMines() {
		new GridFactory(2, 2).specified(new int[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } }).getGrid();
		thrown.expect(IllegalArgumentException.class);
		new GridFactory(2, 2).specified(new int[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 }, { 1, 2 } }).getGrid();
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
	public void testSize() {
		IGrid<ICell> grid = new GridFactory().size(1, 2).mines(1).random().getGrid();
		assertEquals(1, grid.getHeight());
		assertEquals(2, grid.getWidth());
	}

	@Test
	public void testSizeIllegalHeight() {
		thrown.expect(IllegalArgumentException.class);
		new GridFactory().size(0, 1).mines(0).random().getGrid();
	}

	@Test
	public void testSizeIllegalWidth() {
		thrown.expect(IllegalArgumentException.class);
		new GridFactory().size(1, 0).mines(0).random().getGrid();
	}

	@Test
	public void testMinesIllegal() {
		thrown.expect(IllegalArgumentException.class);
		new GridFactory(1, 2).random().getGrid();
	}

	@Test
	public void testRandomGetGrid() {
		IGrid<ICell> grid = new GridFactory(5, 10).mines(10).random().getGrid();

		assertEquals(5, grid.getHeight());
		assertEquals(10, grid.getWidth());

		long mines = grid.getCells().stream().filter(ICell::isMine).count();
		assertEquals(10, mines);
	}

	@Test
	public void testRandomClearGetGrid() {
		IGrid<ICell> grid = new GridFactory(5, 10).mines(49).randomClear(1, 2).getGrid();

		assertEquals(5, grid.getHeight());
		assertEquals(10, grid.getWidth());

		long mines = grid.getCells().stream().filter(ICell::isMine).count();
		assertEquals(49, mines);

		assertFalse(grid.getCell(1, 2).isMine());
	}

	@Test
	public void testSpecifiedGetGrid() {
		int[][] mineLocations = new int[][] { { 0, 0 }, { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };
		IGrid<ICell> grid = new GridFactory(5, 10).specified(mineLocations).getGrid();
		assertEquals(5, grid.getHeight());
		assertEquals(10, grid.getWidth());

		long mines = grid.getCells().stream().filter(ICell::isMine).count();
		assertEquals(5, mines);

		for (int i = 0; i < 5; i++) {
			assertTrue(grid.getCell(i, i).isMine());
		}
		assertEquals(2, grid.getCell(1, 0).getMines());
		assertEquals(1, grid.getCell(2, 0).getMines());
		assertEquals(0, grid.getCell(3, 0).getMines());
	}
}
