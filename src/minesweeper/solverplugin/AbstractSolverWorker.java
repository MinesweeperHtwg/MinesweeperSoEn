package minesweeper.solverplugin;

import minesweeper.controller.IMinesweeperControllerSolvable;

import javax.swing.*;

public abstract class AbstractSolverWorker extends SwingWorker<Boolean, Void> {

	protected final SolverPlugin solver;
	protected final IMinesweeperControllerSolvable controller;

	public AbstractSolverWorker(SolverPlugin solver, IMinesweeperControllerSolvable controller) {
		this.solver = solver;
		this.controller = controller;
	}
}
