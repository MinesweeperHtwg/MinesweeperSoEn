package minesweeper;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.multibindings.Multibinder;

import minesweeper.controller.IMinesweeperController;
import minesweeper.controller.impl.ControllerWrapper;
import minesweeper.model.IGridFactory;
import minesweeper.model.impl.GridFactory;
import minesweeper.solverplugin.SolverPlugin;
import minesweeper.solverplugin.impl.JacopSolver;

public class MinesweeperModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IMinesweeperController.class).to(ControllerWrapper.class);

		bind(IGridFactory.class).toProvider(DebugSolveGridFactoryProvider.class);

		Multibinder<SolverPlugin> plugins = Multibinder.newSetBinder(binder(), SolverPlugin.class);
		plugins.addBinding().to(JacopSolver.class);
	}

	static class DebugEasyGridFactoryProvider implements Provider<IGridFactory> {
		@Override
		public IGridFactory get() {
			return new GridFactory(10, 20).mines(10);
		}
	}

	static class DebugSolveGridFactoryProvider implements Provider<IGridFactory> {
		@Override
		public IGridFactory get() {
			return new GridFactory(2, 6).specified(new int[][] { { 1, 0 }, { 1, 2 }, { 1, 3 }, { 1, 5 } });
		}
	}

}
