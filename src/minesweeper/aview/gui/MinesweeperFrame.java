package minesweeper.aview.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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

	private StatusPanel statusPanel;
	private GridPanelWrapper gridPanelWrapper;
	private GridPanel gridPanel;
	private GameStatsPanel gameStatsPanel;

	static final Color BG = new Color(192, 192, 192);

	private static final long serialVersionUID = 1L;

	private MinesweeperMenuBar menubar;

	@Inject
	public MinesweeperFrame(final IMinesweeperController controller) {
		controller.addObserver(this);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			LOGGER.info("Can't change look and feel", e);
		}

		menubar = new MinesweeperMenuBar(controller);
		setJMenuBar(menubar);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(new CompoundBorder(BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createEmptyBorder(6, 6, 6, 6)));
		mainPanel.setBackground(BG);
		add(mainPanel);

		gameStatsPanel = new GameStatsPanel(controller);
		mainPanel.add(gameStatsPanel, BorderLayout.NORTH);

		gridPanelWrapper = new GridPanelWrapper(controller);
		gridPanel = gridPanelWrapper.getGridPanel();
		mainPanel.add(gridPanelWrapper, BorderLayout.CENTER);

		statusPanel = new StatusPanel(controller);
		mainPanel.add(statusPanel, BorderLayout.SOUTH);

		update(new NoCellChanged());

		setTitle("Minesweeper");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(true);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setMinSizeAndResize();
			}
		});
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
			gridPanelWrapper.dispatchEvent(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
			repack();
		} else if (e instanceof NoCellChanged) {
			// Nothing to do
		} else {
			throw new IllegalArgumentException("Illegal Event Type");
		}
	}

	private void setMinSizeAndResize() {
		setMinSize();
		setSize(getMinimumSize());
	}

	private void setMinSize() {
		Dimension min = getLayout().minimumLayoutSize(this);
		// Need to add 18 because minimum layout size is to small for whatever
		// reason
		min = new Dimension(min.width, min.height + 18);
		setMinimumSize(min);
	}

	private void repack() {
		if (getSize().equals(getMinimumSize())) {
			setMinSizeAndResize();
		} else {
			setMinSize();
		}
	}
}
