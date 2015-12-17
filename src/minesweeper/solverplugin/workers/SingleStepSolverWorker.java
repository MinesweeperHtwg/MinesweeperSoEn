package minesweeper.solverplugin.workers;

import minesweeper.solverplugin.SolverPlugin;

public class SingleStepSolverWorker extends AbstractSolverWorker {
	public SingleStepSolverWorker(SolverPlugin solver) {
		super(solver);
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		return solver.solveOneStep();
	}
}
