package minesweeper.aview.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import minesweeper.controller.DimensionsChanged;
import minesweeper.controller.IMinesweeperController;
import minesweeper.controller.MultipleCellsChanged;
import minesweeper.controller.NoCellChanged;
import minesweeper.controller.SingleCellChanged;
import minesweeper.util.observer.Event;
import minesweeper.util.observer.IObserver;

public class MinesweeperFrame extends JFrame implements IObserver {
	private static final Logger LOGGER = Logger.getLogger(MinesweeperFrame.class);

	private final RepaintManager repaintMgr;

	private StatusPanel statusPanel;
	private GridPanel gridPanel;
	private GameStatsPanel gameStatsPanel;

	static final Color BG = new Color(192, 192, 192);

	private static final long serialVersionUID = 1L;

	@Inject
	public MinesweeperFrame(final IMinesweeperController controller) {
		controller.addObserver(this);

		repaintMgr = RepaintManager.currentManager(this);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			LOGGER.info("Can't change look and feel", e);
		}

		setJMenuBar(new MinesweeperMenuBar(controller));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(new CompoundBorder(BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createEmptyBorder(6, 6, 6, 6)));
		mainPanel.setBackground(BG);
		add(mainPanel);

		gameStatsPanel = new GameStatsPanel(controller);
		mainPanel.add(gameStatsPanel, BorderLayout.NORTH);

		gridPanel = new GridPanel(controller);
		mainPanel.add(gridPanel, BorderLayout.CENTER);

		statusPanel = new StatusPanel(controller);
		mainPanel.add(statusPanel, BorderLayout.SOUTH);

		update(new NoCellChanged());

		setTitle("Minesweeper");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(true);
		pack();
		setMinimumSize(getSize());
	}

	@Override
	public void update(Event e) {
		gameStatsPanel.updateGameStats();
		statusPanel.updateText();

		if (e instanceof MultipleCellsChanged) {
			gridPanel.updateAllCells();
		} else if (e instanceof SingleCellChanged) {
			SingleCellChanged updateCell = (SingleCellChanged) e;
			gridPanel.updateCell(updateCell.getRow(), updateCell.getCol());
		} else if (e instanceof DimensionsChanged) {
			gridPanel.rebuildCells();
			repaintMgr.addInvalidComponent(gridPanel);
		} else if (e instanceof NoCellChanged) {
			// Nothing to do
		} else {
			throw new IllegalArgumentException("Illegal Event Type");
		}

		repaintMgr.markCompletelyDirty(gridPanel);
	}
}
