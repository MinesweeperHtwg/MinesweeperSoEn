package minesweeper.aview.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.RepaintManager;
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

        gridPanel = new GridPanel(controller);
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

    @Override
    public void update(Event e) {
        gameStatsPanel.updateGameStats();
        gridPanel.updateAllCells();
        statusPanel.updateText();

        repaintMgr.markCompletelyDirty(gridPanel);
    }
}
