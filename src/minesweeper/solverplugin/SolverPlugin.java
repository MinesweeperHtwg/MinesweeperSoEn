package minesweeper.solverplugin;

import minesweeper.controller.IMinesweeperControllerSolvable;

public interface SolverPlugin {

	String getSolverName();

	/**
	 * Solve a board as far as possible
	 * 
	 * @param controller the controller of the board to solve
	 * @return if the board was comletly solved
	 */
	boolean solve(IMinesweeperControllerSolvable controller);

	/**
	 * Solve one step of a board
	 * 
	 * @param controller the controller of the board to solve
	 * @return if the solver is able to continue
	 */
	boolean solveOneStep(IMinesweeperControllerSolvable controller);

	/**
	 * Set if the solver should guess if it is stuck
	 * @param guessing if the solver should guess
	 */
	void setGuessing(boolean guessing);
}
