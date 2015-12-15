package minesweeper.solverplugin;

import javax.swing.SwingWorker;

import minesweeper.controller.IMinesweeperControllerSolvable;

public class SolverWorker extends SwingWorker<Boolean, Void> {

	private SolverPlugin solver;
	private IMinesweeperControllerSolvable controller;

	public SolverWorker(SolverPlugin solver, IMinesweeperControllerSolvable controller) {
		this.solver = solver;
		this.controller = controller;
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		return solver.solve(controller);
	}

}
