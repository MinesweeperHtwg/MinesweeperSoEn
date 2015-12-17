package minesweeper.solverplugin.impl;

import com.google.inject.Injector;
import minesweeper.GridFactoryProviders;
import minesweeper.Minesweeper;
import minesweeper.controller.IMinesweeperController;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JacopSolverTest {

	private static JacopSolver jacopSolver;
	private static IMinesweeperController controller;



	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Set up logging through log4j
		PropertyConfigurator.configure("log4j.properties");

		Injector injector = Minesweeper.getInjector(GridFactoryProviders.debugSolve);
		controller = injector.getInstance(IMinesweeperController.class);
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

	}

	@Test
	public void testSolveOneStep() throws Exception {

	}
}