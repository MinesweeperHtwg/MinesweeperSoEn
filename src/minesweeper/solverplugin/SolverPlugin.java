package minesweeper.solverplugin;

import minesweeper.controller.IMinesweeperControllerSolveable;

public interface SolverPlugin {

	String getSolverName();

	boolean solve(IMinesweeperControllerSolveable controller);

}
