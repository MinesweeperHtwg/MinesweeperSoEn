package minesweeper;

import com.google.inject.Provider;
import minesweeper.model.IGridFactory;
import minesweeper.model.impl.GridFactory;

public class GridFactoryProviders {

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
