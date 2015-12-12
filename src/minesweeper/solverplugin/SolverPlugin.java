package minesweeper.solverplugin;

import minesweeper.controller.IMinesweeperControllerSolveable;

public interface SolverPlugin {

	String getSolverName();

	/**
	 * Solve a board as far as possible
	 * 
	 * @param controller
	 * @return if the board was comletly solved
	 */
	boolean solve(IMinesweeperControllerSolveable controller);

	/**
	 * Solve one step of a board
	 * 
	 * @param controller
	 * @return if the solver is able to continue
	 */
	boolean solveOneStep(IMinesweeperControllerSolveable controller);
}
