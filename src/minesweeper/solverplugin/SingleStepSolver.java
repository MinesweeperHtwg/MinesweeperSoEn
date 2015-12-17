package minesweeper.solverplugin;

public class SingleStepSolver extends AbstractSolverWorker {
	public SingleStepSolver(SolverPlugin solver) {
		super(solver);
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		return solver.solveOneStep();
	}
}
