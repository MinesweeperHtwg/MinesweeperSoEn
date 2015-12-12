package minesweeper.solverplugin;

import javax.swing.SwingWorker;

import minesweeper.controller.IMinesweeperControllerSolveable;

public class SolverWorker extends SwingWorker<Boolean, Void> {

	private SolverPlugin solver;
	private IMinesweeperControllerSolveable controller;

	public SolverWorker(SolverPlugin solver, IMinesweeperControllerSolveable controller) {
		this.solver = solver;
		this.controller = controller;
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		return solver.solve(controller);
	}

}
