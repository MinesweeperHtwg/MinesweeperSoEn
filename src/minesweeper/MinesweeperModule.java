package minesweeper;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import minesweeper.controller.IMinesweeperController;
import minesweeper.controller.impl.ControllerWrapper;
import minesweeper.model.IGridFactory;
import minesweeper.model.impl.GridFactory;

public class MinesweeperModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IMinesweeperController.class).to(ControllerWrapper.class);
	}

	@Provides
	IGridFactory getDebugEasyGridFactory() {
		return new GridFactory(10, 20).size(10, 20).mines(10);
	}
}
