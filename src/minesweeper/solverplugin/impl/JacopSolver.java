package minesweeper.solverplugin.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.jacop.constraints.Linear;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.core.BooleanVar;
import org.jacop.core.Domain;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.InputOrderSelect;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SolutionListener;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;

import minesweeper.controller.IMinesweeperControllerSolveable;
import minesweeper.model.ICell;
import minesweeper.model.IGrid;
import minesweeper.solverplugin.SolverPlugin;

public class JacopSolver implements SolverPlugin {

	private static final Logger LOGGER = Logger.getLogger(JacopSolver.class);

	private ImmutableSetMultimap<ICell, ICell> edgeMap;
	private ImmutableList<ICell> openCells;
	private ImmutableList<ICell> closedCells;
	private int varCount;

	// Jacop fields

	private Store store;
	private SolutionListener<IntVar> solutionListener;
	private Search<IntVar> label;
	private SelectChoicePoint<IntVar> select;

	@Override
	public String getSolverName() {
		return "JacopSolver";
	}

	@Override
	public boolean solve(IMinesweeperControllerSolveable controller) {
		LOGGER.info("Solving");

		buildCellCollections(controller);

		LOGGER.info("\nClosedCells:\n" + getCellCordsString(closedCells));

		setupJacop();

		if (!store.consistency()) {
			LOGGER.info("Store incosistent");
			return false;
		}

		// Perform search
		boolean solutionFound = label.labeling(store, select);

		if (!solutionFound) {
			LOGGER.info("No solution found");
			return false;
		}

		label.printAllSolutions();

		double[] varProp = getProbabilities();

		System.out.println(Arrays.toString(varProp));

		solveConfidentCells(controller, varProp);

		return false;
	}

	private void buildCellCollections(IMinesweeperControllerSolveable controller) {
		IGrid<ICell> grid = controller.getGrid();

		edgeMap = getEdgeMap(grid);

		openCells = edgeMap.keySet().asList();
		closedCells = ImmutableSet.copyOf(edgeMap.values()).asList();

		varCount = closedCells.size();
	}

	private static ImmutableSetMultimap<ICell, ICell> getEdgeMap(IGrid<ICell> grid) {
		SetMultimap<ICell, ICell> edgeMap = LinkedHashMultimap.create();

		List<ICell> cells = grid.getCells();

		cells.stream().filter(c -> c.isOpened()).forEach(cell -> {
			grid.getAdjCells(cell).stream().filter(adjCell -> adjCell.isClosed())
					.forEach(adjCell -> edgeMap.put(cell, adjCell));
		});

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
		select = new InputOrderSelect<>(store, varArray, new IndomainMin<IntVar>());

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
	 * Findes safe cells and mines. Opens/Flags them. Doesn't take any risks.
	 * 
	 * @return if something was found
	 */
	private boolean solveConfidentCells(IMinesweeperControllerSolveable controller, double[] varProp) {
		// Evaluate solution
		ArrayList<Integer> mineAtIndex = new ArrayList<>();
		ArrayList<Integer> clearAtIndex = new ArrayList<>();
		for (int i = 0; i < varCount; i++) {
			double prop = varProp[i];
			if (prop == 0.0) {
				clearAtIndex.add(i);
			}
			if (prop == 1.0) {
				mineAtIndex.add(i);
			}
		}

		List<ICell> mines = mineAtIndex.stream().map(i -> closedCells.get(i)).filter(ICell::isClosedWithoutFlag)
				.collect(Collectors.toList());
		List<ICell> clears = clearAtIndex.stream().map(i -> closedCells.get(i)).filter(ICell::isClosedWithoutFlag)
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
