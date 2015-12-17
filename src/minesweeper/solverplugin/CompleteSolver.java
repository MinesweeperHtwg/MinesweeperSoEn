package minesweeper.solverplugin;

import minesweeper.controller.IMinesweeperControllerSolvable;

public class CompleteSolver extends AbstractSolverWorker {
	public CompleteSolver(SolverPlugin solver, IMinesweeperControllerSolvable controller) {
		super(solver, controller);
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		return solver.solve(controller);
	}
}
