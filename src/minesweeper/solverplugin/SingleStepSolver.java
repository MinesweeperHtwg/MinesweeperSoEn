package minesweeper.solverplugin;

import minesweeper.controller.IMinesweeperControllerSolvable;

public class SingleStepSolver extends AbstractSolverWorker {
	public SingleStepSolver(SolverPlugin solver, IMinesweeperControllerSolvable controller) {
		super(solver, controller);
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		return solver.solveOneStep(controller);
	}
}
