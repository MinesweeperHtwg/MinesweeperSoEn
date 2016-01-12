package minesweeper;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.multibindings.Multibinder;
import minesweeper.controller.IMinesweeperController;
import minesweeper.controller.IMinesweeperControllerSolvable;
import minesweeper.controller.impl.ControllerWrapper;
import minesweeper.model.IGridFactory;
import minesweeper.solverplugin.SolverPlugin;
import minesweeper.solverplugin.impl.jacop.JacopSolver;

public class MinesweeperModule extends AbstractModule {

	public static Injector getInjector(Provider<IGridFactory> provider) {
		GridFactoryModule gridFactoryModule = new GridFactoryModule();
		gridFactoryModule.setProvider(provider);
		return Guice.createInjector(new MinesweeperModule(), gridFactoryModule);
	}

	@Override
	protected void configure() {
		bind(IMinesweeperController.class).to(ControllerWrapper.class);
		bind(IMinesweeperControllerSolvable.class).to(ControllerWrapper.class);

		Multibinder<SolverPlugin> plugins = Multibinder.newSetBinder(binder(), SolverPlugin.class);
		plugins.addBinding().to(JacopSolver.class);
	}


}
