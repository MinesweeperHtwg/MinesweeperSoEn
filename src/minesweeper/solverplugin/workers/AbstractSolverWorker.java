package minesweeper.solverplugin.workers;

import minesweeper.solverplugin.SolverPlugin;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public abstract class AbstractSolverWorker extends SwingWorker<Boolean, Void> {

	private static final Logger logger = Logger.getLogger(AbstractSolverWorker.class);

	protected final SolverPlugin solver;

	public AbstractSolverWorker(SolverPlugin solver) {
		this.solver = solver;
	}

	@Override
	protected void done() {
		try {
			get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Solver has thrown:", e);
		}
	}
}
