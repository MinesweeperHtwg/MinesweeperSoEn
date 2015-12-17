package minesweeper.solverplugin.impl;

import com.google.inject.Injector;
import minesweeper.GridFactoryProviders;
import minesweeper.Minesweeper;
import minesweeper.controller.IMinesweeperControllerSolvable;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JacopSolverTest {

	private static JacopSolver jacopSolver;
	private static IMinesweeperControllerSolvable controller;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Set up logging through log4j
		PropertyConfigurator.configure("log4j.properties");

		Injector injector = Minesweeper.getInjector(GridFactoryProviders.testSolve);
		controller = injector.getInstance(IMinesweeperControllerSolvable.class);
		jacopSolver = injector.getInstance(JacopSolver.class);
	}

	@Before
	public void setUp() throws Exception {
		controller.newGame();
	}

	@Test
	public void testGetSolverName() throws Exception {
		assertEquals("JacopSolver", jacopSolver.getSolverName());
	}

	@Test
	public void testSolve() throws Exception {
		controller.openCell(0, 0);
		controller.openCell(0, 1);
		controller.openCell(1, 0);
		assertTrue(jacopSolver.solve());
		assertTrue(controller.getStatusLine().contains("You've won"));
		// TODO: when done implementing check status
		jacopSolver.solve();
	}

	@Test
	public void testSolveOneStep() throws Exception {
		controller.openCell(0, 0);
		controller.openCell(0, 1);
		controller.openCell(1, 0);
		assertTrue(jacopSolver.solveOneStep());
		assertTrue(controller.getGrid().getCell(1, 1).isFlag());

	}
}