package minesweeper.solverplugin.workers;

import minesweeper.solverplugin.SolverPlugin;

import javax.swing.*;

public abstract class AbstractSolverWorker extends SwingWorker<Boolean, Void> {

	protected final SolverPlugin solver;

	public AbstractSolverWorker(SolverPlugin solver) {
		this.solver = solver;
	}
}
