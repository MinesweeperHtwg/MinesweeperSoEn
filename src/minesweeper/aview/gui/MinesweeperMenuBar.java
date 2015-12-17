package minesweeper.aview.gui;

import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Set;

import javax.swing.*;

import minesweeper.controller.IMinesweeperController;
import minesweeper.controller.IMinesweeperControllerSolvable;
import minesweeper.solverplugin.CompleteSolver;
import minesweeper.solverplugin.SingleStepSolver;
import minesweeper.solverplugin.SolverPlugin;
import minesweeper.solverplugin.AbstractSolverWorker;

public class MinesweeperMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	public MinesweeperMenuBar(final IMinesweeperController controller, final Set<SolverPlugin> plugins) {
		JMenu menu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;

		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G);
		add(menu);

		menuItem = new JMenuItem("New");
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.addActionListener(e -> controller.newGame());
		menu.add(menuItem);

		menu.addSeparator();

		ButtonGroup group = new ButtonGroup();

		rbMenuItem = new JRadioButtonMenuItem("Easy");
		rbMenuItem.setMnemonic(KeyEvent.VK_1);
		rbMenuItem.addActionListener(e -> controller.changeSettings(8, 8, 10));
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Intermediate");
		rbMenuItem.setMnemonic(KeyEvent.VK_2);
		rbMenuItem.addActionListener(e -> controller.changeSettings(16, 16, 40));
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Hard");
		rbMenuItem.setMnemonic(KeyEvent.VK_3);
		rbMenuItem.addActionListener(e -> controller.changeSettings(16, 30, 99));
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Custom...");
		rbMenuItem.setMnemonic(KeyEvent.VK_4);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		menu = new JMenu("Debug");
		menu.setMnemonic(KeyEvent.VK_D);
		add(menu);

		menuItem = new JMenuItem("Repack/Resize");
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.addActionListener(e -> ((MinesweeperFrame) SwingUtilities.getWindowAncestor(MinesweeperMenuBar.this)).repack());
		menu.add(menuItem);

		menu = new JMenu("Solve");
		menu.setMnemonic(KeyEvent.VK_D);
		add(menu);

		if (plugins.isEmpty() || !(controller instanceof IMinesweeperControllerSolvable)) {
			menu.setEnabled(false);
		} else {
			IMinesweeperControllerSolvable solvableController = (IMinesweeperControllerSolvable) controller;
			for (SolverPlugin plugin : plugins) {
				JMenu subMenu = new JMenu(plugin.getSolverName());
				menu.add(subMenu);

				JCheckBoxMenuItem subMenuCheckBox = new JCheckBoxMenuItem("Guessing");
				boolean defaultGuessing = false;
				subMenuCheckBox.setState(defaultGuessing);
				plugin.setGuessing(defaultGuessing);
				subMenuCheckBox.addItemListener(e -> plugin.setGuessing(e.getStateChange() == ItemEvent.SELECTED));
				subMenu.add(subMenuCheckBox);

				JMenuItem subMenuItem;

				subMenuItem = new JMenuItem("Complete solve");
				subMenuItem.addActionListener(e ->
						new CompleteSolver(plugin, solvableController).execute());
				subMenu.add(subMenuItem);

				subMenuItem = new JMenuItem("Single step solve");
				subMenuItem.addActionListener(e ->
						new SingleStepSolver(plugin, solvableController).execute());
				subMenu.add(subMenuItem);
			}
		}
	}
}
