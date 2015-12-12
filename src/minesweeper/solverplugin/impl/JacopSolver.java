package minesweeper.solverplugin.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

	@Override
	public String getSolverName() {
		return "JacopSolver";
	}

	@Override
	public boolean solve(IMinesweeperControllerSolveable controller) {
		LOGGER.info("Solving");

		IGrid<ICell> grid = controller.getGrid();

		ImmutableSetMultimap<ICell, ICell> edgeMap = getEdgeMap(grid);

		ImmutableList<ICell> openCells = edgeMap.keySet().asList();
		ImmutableList<ICell> closedCells = ImmutableSet.copyOf(edgeMap.values()).asList();

		LOGGER.info("\nClosedCells:\n" + getCellCordsString(closedCells));

		Store store = new Store();

		ArrayList<IntVar> varList = new ArrayList<>(closedCells.size());

		for (int i = 0; i < closedCells.size(); i++) {
			varList.add(new BooleanVar(store, Integer.toString(i)));
		}

		for (ICell cell : openCells) {
			ArrayList<Integer> weights = new ArrayList<>(Collections.nCopies(closedCells.size(), 0));
			edgeMap.get(cell).stream().map(closedCells::indexOf).forEach(i -> weights.set(i, 1));

			PrimitiveConstraint ctr = new Linear(store, varList, weights, "=", cell.getMines());
			store.impose(ctr);
		}

		if (!store.consistency()) {
			LOGGER.info("Store incosistent");
			return false;
		}

		IntVar[] varArray = varList.toArray(new IntVar[varList.size()]);

		Search<IntVar> label = new DepthFirstSearch<>();
		label.setPrintInfo(false);
		SelectChoicePoint<IntVar> select = new InputOrderSelect<>(store, varArray, new IndomainMin<IntVar>());

		SolutionListener<IntVar> solutionListener = label.getSolutionListener();
		solutionListener.searchAll(true);
		solutionListener.recordSolutions(true);

		// Perform search
		boolean solutionFound = label.labeling(store, select);

		if (!solutionFound) {
			LOGGER.info("No solution found");
			return false;
		}

		System.out.println(varArray[0].dom().value());

		label.printAllSolutions();

		Domain[][] solutions = solutionListener.getSolutions();
		int solutionsCount = solutionListener.solutionsNo();
		int varCount = varArray.length;

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

		System.out.println(Arrays.toString(varProp));

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

		List<ICell> mines = mineAtIndex.stream().map(i -> closedCells.get(i)).collect(Collectors.toList());
		List<ICell> clears = clearAtIndex.stream().map(i -> closedCells.get(i)).collect(Collectors.toList());

		mines.stream().filter(ICell::isClosedWithoutFlag)
				.forEach(cell -> controller.toggleFlag(cell.getRow(), cell.getCol()));
		clears.stream().filter(ICell::isClosedWithoutFlag)
				.forEach(cell -> controller.openCell(cell.getRow(), cell.getCol()));

		return false;
	}

	private static String getCellCordsString(List<ICell> cellList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cellList.size(); i++) {
			ICell cell = cellList.get(i);
			sb.append(i).append(": (").append(cell.getRow()).append(", ").append(cell.getCol()).append(")\n");
		}
		return sb.toString();
	}

	public static ImmutableSetMultimap<ICell, ICell> getEdgeMap(IGrid<ICell> grid) {
		SetMultimap<ICell, ICell> edgeMap = LinkedHashMultimap.create();

		List<ICell> cells = grid.getCells();

		cells.stream().filter(c -> c.isOpened()).forEach(cell -> {
			grid.getAdjCells(cell).stream().filter(adjCell -> adjCell.isClosed())
					.forEach(adjCell -> edgeMap.put(cell, adjCell));
		});

		return ImmutableSetMultimap.copyOf(edgeMap);
	}

}
