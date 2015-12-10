package minesweeper.solverplugin.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.jacop.constraints.Linear;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.core.BooleanVar;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.InputOrderSelect;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;

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
		IGrid<ICell> grid = controller.getGrid();

		ImmutableSetMultimap<ICell, ICell> edgeMap = getEdgeMap(grid);

		System.out.println(edgeMap);

		ImmutableList<ICell> openCells = edgeMap.keySet().asList();
		ImmutableList<ICell> closedCells = ImmutableSet.copyOf(edgeMap.values()).asList();

		closedCells.stream().map(ICell::mkString).forEach(System.out::println);

		Store store = new Store();

		ArrayList<IntVar> varList = new ArrayList<>(closedCells.size());

		for (int i = 0; i < closedCells.size(); i++) {
			varList.add(new BooleanVar(store, Integer.toString(i)));
		}

		// --------

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

		IntVar[] varArray = varList.toArray(new IntVar[0]);

		Search<IntVar> label = new DepthFirstSearch<>();
		SelectChoicePoint<IntVar> select = new InputOrderSelect<>(store, varArray, new IndomainMin<IntVar>());

		boolean solutionFound = label.labeling(store, select);

		if (solutionFound) {
			LOGGER.info("Solution found:");
			Arrays.toString(varArray);
		} else {
			LOGGER.info("No solution found");
		}

		return false;
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
