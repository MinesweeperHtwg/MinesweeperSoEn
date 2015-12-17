package minesweeper.solverplugin;

public interface SolverPlugin {

	String getSolverName();

	/**
	 * Solve a board as far as possible
	 *
	 * @return if the board was comletly solved
	 */
	boolean solve();

	/**
	 * Solve one step of a board
	 *
	 * @return if the solver is able to continue
	 */
	boolean solveOneStep();

	/**
	 * Set if the solver should guess if it is stuck
	 *
	 * @param guessing if the solver should guess
	 */
	void setGuessing(boolean guessing);
}
