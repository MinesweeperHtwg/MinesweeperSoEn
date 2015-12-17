package minesweeper.solverplugin.workers;

import minesweeper.solverplugin.SolverPlugin;

public class CompleteSolverWorker extends AbstractSolverWorker {
	public CompleteSolverWorker(SolverPlugin solver) {
		super(solver);
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		return solver.solve();
	}
}
