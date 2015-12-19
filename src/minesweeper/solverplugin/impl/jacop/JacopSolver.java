package minesweeper.solverplugin.impl.jacop;

import com.google.common.collect.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
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
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class JacopSolver implements SolverPlugin {

	private static final Logger logger = Logger.getLogger(JacopSolver.class);

	private final IMinesweeperControllerSolvable controller;

	// TODO: Implement guessing algorithm
	private boolean guessing;

	@Inject
	public JacopSolver(IMinesweeperControllerSolvable controller) {
		this.controller = controller;
	}

	@Override
	public String getSolverName() {
		return "JacopSolver";
	}

	@Override
	public boolean solve() {
		logger.info("Trying to solve complete board");
		while (solveOneStep()) {
			if (hasGameEnded(controller)) {
				return true;
			}
		}
		// TODO: Return real solve state
		return false;
	}

	@Override
	public boolean solveOneStep() {
		logger.info("\nSolving one step");

		ImmutableSetMultimap<ICell, ICell> completeEdgeMap = getEdgeMap(controller);
		ImmutableList<ICell> completeClosedCells = getClosedCells(completeEdgeMap);
		ImmutableList<ICell> completeOpenCells = getOpenCells(completeEdgeMap);

		List<ImmutableSetMultimap<ICell, ICell>> splitEdgeMaps = getSplitEdgeMaps(completeEdgeMap, completeClosedCells, completeOpenCells);
		List<ImmutableList<ICell>> splitClosedCells = splitEdgeMaps.stream().map(edgeMap -> getClosedCells(edgeMap)).collect(Collectors.toList());
		List<ImmutableList<ICell>> splitOpenCells = splitEdgeMaps.stream().map(edgeMap -> getOpenCells(edgeMap)).collect(Collectors.toList());

		logger.debug("\nNumber of independent systems: " + splitEdgeMaps.size());

		List<ICell> minesToFlag = new ArrayList<>();
		List<ICell> clearsToOpen = new ArrayList<>();

		for (int i = 0; i < splitEdgeMaps.size(); i++) {
			ImmutableSetMultimap<ICell, ICell> edgeMap = splitEdgeMaps.get(i);
			ImmutableList<ICell> closedCells = splitClosedCells.get(i);
			ImmutableList<ICell> openCells = splitOpenCells.get(i);

			double[] varProp = runJacop(edgeMap, closedCells, openCells);

			logger.debug("\nProbabilities:\n"
					+ Arrays.stream(varProp).mapToObj(d -> String.format("%,.3f", d)).collect(Collectors.joining(" ")));

			addConfidentCells(varProp, closedCells, minesToFlag, clearsToOpen);
		}

		return openAndFlagCells(controller, minesToFlag, clearsToOpen);

	}

	private static List<ImmutableSetMultimap<ICell, ICell>> getSplitEdgeMaps(ImmutableSetMultimap<ICell, ICell> edgeMap, ImmutableList<ICell> closedCells, ImmutableList<ICell> openCells) {
		UndirectedGraph<ICell, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

		closedCells.forEach(graph::addVertex);
		openCells.forEach(graph::addVertex);

		for (Map.Entry<ICell, Collection<ICell>> entry : edgeMap.asMap().entrySet()) {
			for (ICell cell : entry.getValue()) {
				graph.addEdge(entry.getKey(), cell);
			}
		}

		List<Set<ICell>> connectedSets = new ConnectivityInspector<>(graph).connectedSets();

		connectedSets.forEach(set -> set.removeIf(ICell::isClosed));

		List<ImmutableSetMultimap<ICell, ICell>> splitEdgeMaps = new ArrayList<>(connectedSets.size());
		for (Set<ICell> set : connectedSets) {
			ImmutableSetMultimap.Builder<ICell, ICell> builder = ImmutableSetMultimap.builder();
			set.forEach(c -> builder.putAll(c, edgeMap.get(c)));
			splitEdgeMaps.add(builder.build());
		}

		logger.debug(splitEdgeMaps);

		return splitEdgeMaps;
	}


	@Override
	public void setGuessing(boolean guessing) {
		this.guessing = guessing;
	}

	private static boolean hasGameEnded(IMinesweeperControllerSolvable controller) {
		String statusLine = controller.getStatusLine();
		return statusLine.contains("You've won!") || statusLine.contains("Game over");
	}

	private ImmutableList<ICell> getClosedCells(ImmutableSetMultimap<ICell, ICell> edgeMap) {
		return ImmutableSet.copyOf(edgeMap.values()).asList();
	}

	private ImmutableList<ICell> getOpenCells(ImmutableSetMultimap<ICell, ICell> edgeMap) {
		return edgeMap.keySet().asList();
	}

	private static ImmutableSetMultimap<ICell, ICell> getEdgeMap(IMinesweeperControllerSolvable controller) {
		IGrid<ICell> grid = controller.getGrid();

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

	/**
	 * Run jacop
	 *
	 * @return null if an error has occurred, otherwise the probabilities of closedCells
	 */
	private static double[] runJacop(ImmutableSetMultimap<ICell, ICell> edgeMap, ImmutableList<ICell> closedCells, ImmutableList<ICell> openCells) {
		Store store = new Store();

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

		Search<IntVar> label = new DepthFirstSearch<>();
		label.setPrintInfo(false);

		SimpleTimeOut timeOut = new SimpleTimeOut();
		label.setTimeOutListener(timeOut);
		label.setTimeOut(10);

		SelectChoicePoint<IntVar> select = new InputOrderSelect<>(store, varArray, new IndomainMin<>());

		SolutionListener<IntVar> solutionListener = label.getSolutionListener();
		solutionListener.searchAll(true);
		solutionListener.recordSolutions(true);

		if (!store.consistency()) {
			logger.error("Store inconsistent");
			return null;
		}

		// Perform search
		if (!label.labeling(store, select)) {
			logger.error("No solution found");
			return null;
		}

		if (timeOut.timeOutOccurred) {
			logger.error("Reached time limit at solution " + timeOut.solutionsNo);
			return null;
		}

		logger.debug("\nSearch Stats:\n" + label.toString());

		return getProbabilities(solutionListener);
	}

	private static double[] getProbabilities(SolutionListener<IntVar> solutionListener) {
		Domain[][] solutions = solutionListener.getSolutions();
		int varCount = solutionListener.getVariables().length;
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
	 * Finds confident cells and adds them to the lists.
	 */
	private static void addConfidentCells(double[] varProp, ImmutableList<ICell> closedCells, List<ICell> minesToFlag, List<ICell> clearsToOpen) {
		// Evaluate solution
		List<Integer> mineAtIndex = new ArrayList<>();
		List<Integer> clearAtIndex = new ArrayList<>();
		for (int i = 0; i < varProp.length; i++) {
			double prop = varProp[i];
			if (Double.compare(prop, 0.0) == 0) {
				clearAtIndex.add(i);
			}
			if (Double.compare(prop, 1.0) == 0) {
				mineAtIndex.add(i);
			}
		}

		mineAtIndex.stream()
		           .map(closedCells::get)
		           .filter(ICell::isClosedWithoutFlag)
		           .forEach(c -> minesToFlag.add(c));
		clearAtIndex.stream()
		            .map(closedCells::get)
		            .filter(ICell::isClosedWithoutFlag)
		            .forEach(c -> clearsToOpen.add(c));
	}

	/**
	 * @return if any cell was opened or closed
	 */
	private static boolean openAndFlagCells(IMinesweeperControllerSolvable controller, List<ICell> minesToFlag, List<ICell> clearsToOpen) {
		if (minesToFlag.isEmpty() && clearsToOpen.isEmpty()) {
			logger.info("\nFound no risk free cell to open or flag");
			return false;
		}

		minesToFlag.stream().forEach(cell -> controller.toggleFlag(cell.getRow(), cell.getCol()));
		clearsToOpen.stream().forEach(cell -> controller.openCell(cell.getRow(), cell.getCol()));

		logger.info("\nFound " + minesToFlag.size() + " mine(s) at:\n" + getCellCordsString(minesToFlag) + "\n Found "
				+ clearsToOpen.size() + " safe cell(s) at:\n" + getCellCordsString(clearsToOpen));
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
