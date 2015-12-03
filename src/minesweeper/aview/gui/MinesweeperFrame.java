package minesweeper.aview.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import minesweeper.controller.IMinesweeperController;
import minesweeper.controller.impl.UpdateAllEvent;
import minesweeper.util.observer.Event;
import minesweeper.util.observer.IObserver;

@SuppressWarnings("serial")
public class MinesweeperFrame extends JFrame implements IObserver {
    private static final Logger LOGGER = Logger
            .getLogger(MinesweeperFrame.class);

    private IMinesweeperController controller;

    private final RepaintManager repaintMgr;

    private Container pane;
    private StatusPanel statusPanel;
    private GridPanel gridPanel;
    private GameStatsPanel gameStatsPanel;

    public MinesweeperFrame(final IMinesweeperController controller) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.info("Can't change look and feel: " + e);
        }

        this.controller = controller;
        controller.addObserver(this);

        pane = getContentPane();
        pane.setLayout(new BorderLayout());

        gameStatsPanel = new GameStatsPanel(controller);
        pane.add(gameStatsPanel, BorderLayout.NORTH);

        gridPanel = new GridPanel(controller, new CellListener());
        add(gridPanel, BorderLayout.CENTER);

        statusPanel = new StatusPanel(controller);
        pane.add(statusPanel, BorderLayout.SOUTH);

        repaintMgr = RepaintManager.currentManager(this);

        update(new UpdateAllEvent());

        setTitle("Minesweeper");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        pack();
        setVisible(true);
    }

    private class CellListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Object source = e.getSource();
            if (!(source instanceof CellPanel)) {
                throw new IllegalArgumentException("Unsupported Event");
            }
            CellPanel cellPanel = (CellPanel) source;
            int row = cellPanel.getRow();
            int col = cellPanel.getCol();
            if (SwingUtilities.isLeftMouseButton(e)) {
                controller.openCell(row, col);
                return;
            }
            if (SwingUtilities.isMiddleMouseButton(e)) {
                controller.openAround(row, col);
                return;
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                controller.toggleFlag(row, col);
                return;
            }
        }
    }

    @Override
    public void update(Event e) {
        gameStatsPanel.updateGameStats();
        gridPanel.updateAllCells();
        statusPanel.updateText();

        repaintMgr.markCompletelyDirty(gridPanel);
    }
}
