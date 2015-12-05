package minesweeper.controller.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import minesweeper.controller.DimensionsChanged;
import minesweeper.controller.IMinesweeperController;
import minesweeper.controller.MultipleCellsChanged;
import minesweeper.controller.NoCellChanged;
import minesweeper.controller.SingleCellChanged;
import minesweeper.model.impl.GridFactory;
import minesweeper.util.observer.Event;

public class MinesweeperControllerTest {
	private IMinesweeperController controller;

	@Before
	public void setUp() throws Exception {
		PropertyConfigurator.configure("log4j.properties");
		int[][] mineLocations = { { 0, 0 }, { 0, 1 }, { 2, 2 } };
		controller = new ControllerWrapper(new GridFactory(3, 3).specified(mineLocations));
	}

	private void checkEventDimensions() {
		assertTrue(controller.getEvent() instanceof DimensionsChanged);
	}

	private void checkEventMultiple() {
		assertTrue(controller.getEvent() instanceof MultipleCellsChanged);
	}

	private void checkEventNoCell() {
		assertTrue(controller.getEvent() instanceof NoCellChanged);
	}

	private void checkEventSingleCell(int row, int col) {
		Event event = controller.getEvent();
		assertTrue(event instanceof SingleCellChanged);
		SingleCellChanged single = (SingleCellChanged) event;
		assertEquals(row, single.getRow());
		assertEquals(col, single.getCol());
	}

	@Test
	public void testHalfInitialized() {
		controller = new MinesweeperController(new GridFactory());
		controller.openCell(0, 0);
		controller.openAround(0, 0);
		controller.toggleFlag(0, 0);
		controller.newGame();
		controller.changeSettings(1, 2, 1);
	}

	@Test
	public void testChangeSettings() {
		controller.changeSettings(1, 2, 1);
		assertEquals(1, controller.getHeight());
		assertEquals(2, controller.getWidth());
		assertTrue(controller.getGameStats().contains("Unflagged mines left: 1"));
	}

	@Test
	public void testNewGame() {
		controller.newGame();
		assertEquals("New game started", controller.getStatusLine());
		checkEventMultiple();
	}

	@Test
	public void testCommandLockGameOver() {
		controller.openCell(0, 0);

		controller.openCell(0, 0);
		assertEquals("Game over", controller.getStatusLine());
		checkEventNoCell();

		controller.openAround(0, 0);
		assertEquals("Game over", controller.getStatusLine());
		checkEventNoCell();

		controller.toggleFlag(0, 0);
		assertEquals("Game over", controller.getStatusLine());
		checkEventNoCell();
	}

	@Test
	public void testCommandLockWin() {
		controller.toggleFlag(0, 0);
		controller.toggleFlag(0, 1);
		controller.toggleFlag(2, 2);
		controller.openCell(1, 1);
		controller.openAround(1, 1);

		controller.openCell(0, 0);
		assertEquals("You've won!", controller.getStatusLine());
		checkEventNoCell();

		controller.openAround(0, 0);
		assertEquals("You've won!", controller.getStatusLine());
		checkEventNoCell();

		controller.toggleFlag(0, 0);
		assertEquals("You've won!", controller.getStatusLine());
		checkEventNoCell();
	}

	@Test
	public void testOpenCell() {
		controller.toggleFlag(1, 1);
		controller.openCell(1, 0);
		assertEquals("Opened (1, 0) = 2", controller.getStatusLine());
		checkEventSingleCell(1, 0);

		controller.openCell(1, 0);
		assertEquals("Can't open (1, 0) = 2 because the cell has been opened already", controller.getStatusLine());
		checkEventNoCell();

		controller.openCell(1, 1);
		assertEquals("Can't open (1, 1) = F because there is a flag", controller.getStatusLine());
		checkEventNoCell();

		controller.openCell(0, 0);
		assertEquals("Game over. Mine opened at (0, 0) = M", controller.getStatusLine());
		checkEventSingleCell(0, 0);
	}

	@Test
	public void testFairOpen() {
		controller.changeSettings(10, 10, 99);
		controller.openCell(5, 5);
		assertEquals("You've won!", controller.getStatusLine());
	}

	@Test
	public void testFloodOpen() {
		int[][] mineLocations = { { 0, 0 }, { 3, 3 } };
		controller = new MinesweeperController(new GridFactory(4, 4).specified(mineLocations));
		controller.openCell(3, 0);
		assertEquals("You've won!", controller.getStatusLine());
		assertEquals(" |1|0|0\n1|1|0|0\n0|0|1|1\n0|0|1| ", controller.getGridString());
		checkEventMultiple();
	}

	@Test
	public void testOpenAround() {
		controller.openAround(1, 1);
		assertEquals("Can't open cells around (1, 1) =   because the cell is closed", controller.getStatusLine());
		checkEventNoCell();

		controller.openCell(2, 0);
		controller.openAround(2, 0);
		assertEquals("No cells to open around (2, 0) = 0", controller.getStatusLine());
		checkEventNoCell();

		controller.openCell(2, 1);
		controller.openAround(2, 1);
		assertEquals(
				"Can't open cells around (2, 1) = 1 because there is an incorrect number of flags around this cell",
				controller.getStatusLine());
		checkEventNoCell();

		controller.toggleFlag(2, 2);
		controller.openAround(2, 1);
		assertEquals("Opened all cells around (2, 1) = 1", controller.getStatusLine());
		checkEventMultiple();

		controller.toggleFlag(0, 1);
		controller.openAround(1, 2);
		assertEquals("You've won!", controller.getStatusLine());
		checkEventMultiple();
	}

	@Test
	public void testOpenAroundLoose() {
		controller.openCell(1, 2);
		controller.toggleFlag(2, 2);
		controller.toggleFlag(0, 2);
		controller.openAround(1, 2);
		assertEquals("Game over. Mine opened at (0, 1) = M", controller.getStatusLine());
		checkEventMultiple();
	}

	@Test
	public void testToggleFlag() {
		controller.toggleFlag(1, 1);
		assertEquals("Flag set at (1, 1) = F", controller.getStatusLine());
		checkEventSingleCell(1, 1);

		controller.toggleFlag(1, 1);
		assertEquals("Flag removed at (1, 1) =  ", controller.getStatusLine());
		checkEventSingleCell(1, 1);

		controller.openCell(1, 1);
		controller.toggleFlag(1, 1);
		assertEquals("Can't place flag at (1, 1) = 3 because the cell has been opened", controller.getStatusLine());
		checkEventNoCell();
	}

	@Test
	public void testGetGameStats() {
		assertTrue(controller.getGameStats().contains("Unflagged mines left: 3 Time: "));
	}

	@Test
	public void testGetGridString() {
		assertEquals(" | | \n | | \n | | ", controller.getGridString());
	}

	@Test
	public void testGetStatusLine() {
		assertEquals("Welcome to Minesweeper!", controller.getStatusLine());
	}

	@Test
	public void testGetCellString() {
		controller.openCell(1, 0);
		controller.toggleFlag(0, 1);
		controller.openCell(2, 2);
		assertEquals(" ", controller.getCellString(0, 0));
		assertEquals("2", controller.getCellString(1, 0));
		assertEquals("F", controller.getCellString(0, 1));
		assertEquals("M", controller.getCellString(2, 2));
	}

}
