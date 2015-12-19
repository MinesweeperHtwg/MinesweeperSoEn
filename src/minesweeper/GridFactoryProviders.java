package minesweeper;

import com.google.inject.Provider;
import minesweeper.model.IGridFactory;
import minesweeper.model.impl.GridFactory;

public class GridFactoryProviders {
	public static final DebugEasy debugEasy = new DebugEasy();
	public static final DebugSolve debugSolve = new DebugSolve();
	public static final TestSolve testSolve = new TestSolve();

	private GridFactoryProviders() {
		//no instance
	}

	static class DebugEasy implements Provider<IGridFactory> {
		@Override
		public IGridFactory get() {
			return new GridFactory(10, 20).mines(10);
		}
	}

	static class DebugSolve implements Provider<IGridFactory> {
		@Override
		public IGridFactory get() {
			return new GridFactory(2, 6).specified(new int[][]{{1, 0}, {1, 2}, {1, 3}, {1, 5}});
		}
	}

	static class TestSolve implements Provider<IGridFactory> {
		@Override
		public IGridFactory get() {
			return new GridFactory(3, 6).specified(new int[][]{{1, 1}, {0, 5}, {2, 5}});
		}
	}
}
