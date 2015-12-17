package minesweeper;

import com.google.inject.Provider;
import minesweeper.model.IGridFactory;
import minesweeper.model.impl.GridFactory;

public class GridFactoryProviders {
	private GridFactoryProviders() {
		//no instance
	}

	public static final DebugEasyGridFactoryProvider debugEasy = new DebugEasyGridFactoryProvider();

	static class DebugEasyGridFactoryProvider implements Provider<IGridFactory> {
		@Override
		public IGridFactory get() {
			return new GridFactory(10, 20).mines(10);
		}
	}

	public static final DebugSolveGridFactoryProvider debugSolve = new DebugSolveGridFactoryProvider();

	static class DebugSolveGridFactoryProvider implements Provider<IGridFactory> {
		@Override
		public IGridFactory get() {
			return new GridFactory(2, 6).specified(new int[][]{{1, 0}, {1, 2}, {1, 3}, {1, 5}});
		}
	}

	public static final TestSolveGridFactoryProvider testSolve = new TestSolveGridFactoryProvider();

	static class TestSolveGridFactoryProvider implements Provider<IGridFactory> {
		@Override
		public IGridFactory get() {
			return new GridFactory(3, 6).specified(new int[][]{{1, 1}, {0, 5}, {2, 5}});
		}
	}
}
