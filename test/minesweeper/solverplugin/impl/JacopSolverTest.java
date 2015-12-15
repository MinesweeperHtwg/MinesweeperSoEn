package minesweeper.solverplugin.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import minesweeper.MinesweeperModule;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JacopSolverTest {

	private JacopSolver jacopSolver;

	@Before
	public void setUp() throws Exception {
		jacopSolver = new JacopSolver();
		Injector injector = Guice.createInjector(new MinesweeperModule());
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