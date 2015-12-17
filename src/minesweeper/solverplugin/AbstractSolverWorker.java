package minesweeper.solverplugin;

import javax.swing.SwingWorker;

import minesweeper.controller.IMinesweeperControllerSolvable;

public abstract class AbstractSolverWorker extends SwingWorker<Boolean, Void> {

	protected final SolverPlugin solver;
	protected final IMinesweeperControllerSolvable controller;

	public AbstractSolverWorker(SolverPlugin solver, IMinesweeperControllerSolvable controller) {
		this.solver = solver;
		this.controller = controller;
	}
}
