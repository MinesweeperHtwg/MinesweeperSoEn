package minesweeper.solverplugin;

public class CompleteSolver extends AbstractSolverWorker {
	public CompleteSolver(SolverPlugin solver) {
		super(solver);
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		return solver.solve();
	}
}
