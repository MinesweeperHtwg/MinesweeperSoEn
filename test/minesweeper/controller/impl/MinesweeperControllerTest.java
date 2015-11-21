package minesweeper.controller.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import minesweeper.controller.impl.MinesweeperController.GameStatus;
import minesweeper.model.impl.GridFactory;

public class MinesweeperControllerTest {
	private MinesweeperController controller;

	@Before
	public void setUp() throws Exception {
		int[][] mineLocations = { { 0, 0 }, { 0, 1 }, { 2, 2 } };
		controller = new MinesweeperController(new GridFactory(3, 3).specified(mineLocations));
	}

	@Test
	public void testNewGame() {
		controller.newGame();
		assertEquals("New game started", controller.getStatusLine());
		controller.openCell(0, 0);
		controller.openCell(0, 0);
		assertEquals("Game over", controller.getStatusLine());
		controller.openAround(0, 0);
		assertEquals("Game over", controller.getStatusLine());
		controller.toggleFlag(0, 0);
		assertEquals("Game over", controller.getStatusLine());
		controller.newGame();
		assertEquals("New game started", controller.getStatusLine());
	}

	@Test
	public void testCommandLockGameOver() {
		controller.openCell(0, 0);
		controller.openCell(0, 0);
		assertEquals("Game over", controller.getStatusLine());
		controller.openAround(0, 0);
		assertEquals("Game over", controller.getStatusLine());
		controller.toggleFlag(0, 0);
		assertEquals("Game over", controller.getStatusLine());
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
		controller.openAround(0, 0);
		assertEquals("You've won!", controller.getStatusLine());
		controller.toggleFlag(0, 0);
		assertEquals("You've won!", controller.getStatusLine());
	}

	@Test
	public void testOpenCell() {
		controller.toggleFlag(1, 1);
		controller.openCell(1, 0);
		assertEquals("Opened (1, 0) = 2", controller.getStatusLine());
		controller.openCell(1, 0);
		assertEquals("Can't open (1, 0) = 2 because the cell has been opened already", controller.getStatusLine());
		controller.openCell(1, 1);
		assertEquals("Can't open (1, 1) = F because there is a flag", controller.getStatusLine());
		controller.openCell(0, 0);
		assertEquals("Game over. Mine opened at (0, 0) = M", controller.getStatusLine());
	}

	@Test
	public void testFloodOpen() {
		int[][] mineLocations = { { 0, 0 }, { 3, 3 } };
		controller = new MinesweeperController(new GridFactory(4, 4).specified(mineLocations));
		controller.openCell(3, 0);
		assertEquals("You've won!", controller.getStatusLine());
		assertEquals(" |1|0|0\n1|1|0|0\n0|0|1|1\n0|0|1| ", controller.getGridString());
	}

	@Test
	public void testOpenAround() {
		controller.openAround(1, 1);
		assertEquals("Can't open cells around (1, 1) =   because the cell is closed", controller.getStatusLine());
		controller.openCell(2, 1);
		controller.openAround(2, 1);
		assertEquals(
				"Can't open cells around (2, 1) = 1 because there is an incorrect number of flags around this cell",
				controller.getStatusLine());
		controller.toggleFlag(2, 2);
		controller.openAround(2, 1);
		assertEquals("Opened all cells around (2, 1) = 1", controller.getStatusLine());
		controller.toggleFlag(0, 1);
		controller.openAround(1, 2);
		assertEquals("You've won!", controller.getStatusLine());
	}

	@Test
	public void testOpenAroundLoose() {
		controller.openCell(1, 2);
		controller.toggleFlag(2, 2);
		controller.toggleFlag(0, 2);
		controller.openAround(1, 2);
		assertEquals("Game over. Mine opened at (0, 1) = M", controller.getStatusLine());
	}

	@Test
	public void testToggleFlag() {
		controller.toggleFlag(1, 1);
		assertEquals("Flag set at (1, 1) = F", controller.getStatusLine());
		controller.toggleFlag(1, 1);
		assertEquals("Flag removed at (1, 1) =  ", controller.getStatusLine());
		controller.openCell(1, 1);
		controller.toggleFlag(1, 1);
		assertEquals("Can't place flag at (1, 1) = 3 because the cell has been opened", controller.getStatusLine());
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
	public void testEnum() {
		assertEquals(GameStatus.RUNNING, GameStatus.valueOf("RUNNING"));
		assertEquals(5, GameStatus.values().length);
	}

}
