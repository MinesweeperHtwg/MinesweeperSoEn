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

		Multibinder<SolverPlugin> plugins = Multibinder.newSetBinder(binder(), SolverPlugin.class);
		plugins.addBinding().to(JacopSolver.class);
	}


}
