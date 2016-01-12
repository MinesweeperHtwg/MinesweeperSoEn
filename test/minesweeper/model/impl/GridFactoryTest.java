package minesweeper.model.impl;

import minesweeper.model.ICell;
import minesweeper.model.IGrid;
import minesweeper.model.IGridFactory;
import minesweeper.model.IGridFactory.Strategy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

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
	public void testSize() {
		IGrid<ICell> grid = new GridFactory().size(1, 2).mines(1).noMines().getGrid();
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
	public void testIllegalMines() {
		thrown.expect(IllegalArgumentException.class);
		new GridFactory().mines(-1);
	}

	@Test
	public void testRandomGetGrid() {
		GridFactory gFact = new GridFactory(5, 10);
		IGrid<ICell> grid = gFact.mines(10).random().getGrid();

		assertEquals(Strategy.RANDOM, gFact.getStrategy());

		assertEquals(5, grid.getHeight());
		assertEquals(10, grid.getWidth());

		long mines = grid.getCells().stream().filter(ICell::isMine).count();
		assertEquals(10, mines);
	}

	@Test
	public void testRandomClearGetGrid() {
		GridFactory gFact = new GridFactory(5, 10);
		IGrid<ICell> grid = gFact.mines(49).randomClear(1, 2).getGrid();

		assertEquals(Strategy.RANDOMCLEAR, gFact.getStrategy());

		assertEquals(5, grid.getHeight());
		assertEquals(10, grid.getWidth());

		long mines = grid.getCells().stream().filter(ICell::isMine).count();
		assertEquals(49, mines);

		assertFalse(grid.getCell(1, 2).isMine());
	}

	@Test
	public void testSpecifiedGetGrid() {
		int[][] mineLocations = new int[][] { { 0, 0 }, { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };

		GridFactory gFact = new GridFactory(5, 10);
		IGrid<ICell> grid = gFact.specified(mineLocations).getGrid();

		assertEquals(Strategy.SPECIFIED, gFact.getStrategy());

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

	@Test
	public void testNoMines() {
		GridFactory gFact = new GridFactory(5, 10);
		IGrid<ICell> grid = gFact.noMines().getGrid();

		assertEquals(Strategy.NOMINES, gFact.getStrategy());

		assertEquals(5, grid.getHeight());
		assertEquals(10, grid.getWidth());

		long mines = grid.getCells().stream().filter(ICell::isMine).count();
		assertEquals(0, mines);
	}

	@Test
	public void testInterfaceEnum() {
		assertEquals(IGridFactory.Strategy.RANDOM, IGridFactory.Strategy.valueOf("RANDOM"));
	}
}
