package minesweeper.solverplugin.impl;

import com.google.common.collect.*;
import minesweeper.controller.IMinesweeperControllerSolvable;
import minesweeper.model.ICell;
import minesweeper.model.IGrid;
import minesweeper.solverplugin.SolverPlugin;
import org.apache.log4j.Logger;
import org.jacop.constraints.Linear;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.core.BooleanVar;
import org.jacop.core.Domain;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JacopSolver implements SolverPlugin {

	private static final Logger LOGGER = Logger.getLogger(JacopSolver.class);

	private boolean guessing;

	private ImmutableSetMultimap<ICell, ICell> edgeMap;
	private ImmutableList<ICell> openCells;
	private ImmutableList<ICell> closedCells;

	private int varCount;

	// Jacop fields
	private Store store;
	private SolutionListener<IntVar> solutionListener;
	private Search<IntVar> label;

	private SelectChoicePoint<IntVar> select;
	private SimpleTimeOut timeOut;

	@Override
	public String getSolverName() {
		return "JacopSolver";
	}

	@Override
	public boolean solve(IMinesweeperControllerSolvable controller) {
		LOGGER.info("Trying to solve complete board");
		while (solveOneStep(controller)) {
			if (hasGameEnded(controller)) {
				return true;
			}
		}
		// TODO: Return real solve state
		return false;
	}

	@Override
	public boolean solveOneStep(IMinesweeperControllerSolvable controller) {
		LOGGER.info("\nSolving one step");

		buildCellCollections(controller);

		LOGGER.debug("\nClosedCells:\n" + getCellCordsString(closedCells));

		setupJacop();

		if (!store.consistency()) {
			LOGGER.info("Store incosistent");
			return false;
		}

		// Perform search
		if (!label.labeling(store, select)) {
			LOGGER.info("No solution found");
			return false;
		}
		if (timeOut.timeOutOccurred) {
			LOGGER.info("Reached time limit at solution " + timeOut.solutionsNo);
			return false;
		}

		LOGGER.debug("\nSearch Stats:\n" + label.toString());

		double[] varProp = getProbabilities();

		LOGGER.debug("\nProbabilities:\n"
				+ Arrays.stream(varProp).mapToObj(d -> String.format("%,.3f", d)).collect(Collectors.joining(" ")));

		return solveConfidentCells(controller, varProp);

	}

	@Override
	public void setGuessing(boolean guessing) {
		this.guessing = guessing;
	}

	private boolean hasGameEnded(IMinesweeperControllerSolvable controller) {
		String statusLine = controller.getStatusLine();
		return statusLine.contains("You've won!") || statusLine.contains("Game over");
	}

	private void buildCellCollections(IMinesweeperControllerSolvable controller) {
		IGrid<ICell> grid = controller.getGrid();

		edgeMap = getEdgeMap(grid);

		openCells = edgeMap.keySet().asList();
		closedCells = ImmutableSet.copyOf(edgeMap.values()).asList();

		varCount = closedCells.size();
	}

	private static ImmutableSetMultimap<ICell, ICell> getEdgeMap(IGrid<ICell> grid) {
		SetMultimap<ICell, ICell> edgeMap = LinkedHashMultimap.create();

		List<ICell> cells = grid.getCells();

		cells.stream()
		     .filter(ICell::isOpened)
		     .forEach(cell -> grid.getAdjCells(cell)
		                          .stream()
		                          .filter(ICell::isClosed)
		                          .forEach(adjCell -> edgeMap.put(cell, adjCell)));

		return ImmutableSetMultimap.copyOf(edgeMap);
	}

	private void setupJacop() {
		store = new Store();

		IntVar[] varArray = new IntVar[closedCells.size()];

		for (int i = 0; i < closedCells.size(); i++) {
			varArray[i] = new BooleanVar(store, Integer.toString(i));
		}

		for (ICell cell : openCells) {
			// initialized with 0
			int[] weightsArray = new int[closedCells.size()];

			edgeMap.get(cell).stream().map(closedCells::indexOf).forEach(i -> weightsArray[i] = 1);

			PrimitiveConstraint ctr = new Linear(store, varArray, weightsArray, "=", cell.getMines());
			store.impose(ctr);
		}

		label = new DepthFirstSearch<>();
		label.setPrintInfo(false);

		timeOut = new SimpleTimeOut();
		label.setTimeOutListener(timeOut);
		label.setTimeOut(10);

		select = new InputOrderSelect<>(store, varArray, new IndomainMin<>());

		solutionListener = label.getSolutionListener();
		solutionListener.searchAll(true);
		solutionListener.recordSolutions(true);
	}

	private double[] getProbabilities() {
		Domain[][] solutions = solutionListener.getSolutions();
		int solutionsCount = solutionListener.solutionsNo();

		double[] varProp = new double[varCount];
		for (int i = 0; i < varCount; i++) {
			for (int j = 0; j < solutionsCount; j++) {
				Domain d = solutions[j][i];
				if (!d.singleton() || !d.isNumeric()) {
					throw new IllegalStateException("Solution " + j + "at Domain " + i + " illegal");
				}
				varProp[i] += d.valueEnumeration().nextElement();
			}
			varProp[i] /= solutionsCount;
		}
		return varProp;
	}

	/**
	 * Finds safe cells and mines. Opens/Flags them. Doesn't take any risks.
	 *
	 * @return if something was found
	 */
	private boolean solveConfidentCells(IMinesweeperControllerSolvable controller, double[] varProp) {
		// Evaluate solution
		List<Integer> mineAtIndex = new ArrayList<>();
		List<Integer> clearAtIndex = new ArrayList<>();
		for (int i = 0; i < varCount; i++) {
			double prop = varProp[i];
			if (Double.compare(prop, 0.0) == 0) {
				clearAtIndex.add(i);
			}
			if (Double.compare(prop, 1.0) == 0) {
				mineAtIndex.add(i);
			}
		}

		List<ICell> mines = mineAtIndex.stream()
		                               .map(i -> closedCells.get(i)).filter(ICell::isClosedWithoutFlag)
		                               .collect(Collectors.toList());
		List<ICell> clears = clearAtIndex.stream()
		                                 .map(i -> closedCells.get(i)).filter(ICell::isClosedWithoutFlag)
		                                 .collect(Collectors.toList());

		if (mines.isEmpty() && clears.isEmpty()) {
			LOGGER.info("\nFound no risk free cell to open or flag");
			return false;
		}

		mines.stream().forEach(cell -> controller.toggleFlag(cell.getRow(), cell.getCol()));
		clears.stream().forEach(cell -> controller.openCell(cell.getRow(), cell.getCol()));

		LOGGER.info("\nFound " + mines.size() + " mine(s) at:\n" + getCellCordsString(mines) + "\n Found "
				+ clears.size() + " safe cell(s) at:\n" + getCellCordsString(clears));
		return true;
	}

	private static String getCellCordsString(List<ICell> cellList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cellList.size(); i++) {
			ICell cell = cellList.get(i);
			sb.append(i).append(":(").append(cell.getRow()).append(",").append(cell.getCol()).append(") ");
		}
		return sb.toString();
	}

}
