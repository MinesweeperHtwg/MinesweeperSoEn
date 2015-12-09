package minesweeper.aview.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;

import minesweeper.controller.IMinesweeperController;

public class MinesweeperMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	public MinesweeperMenuBar(final IMinesweeperController controller) {
		JMenu menu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;

		menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G);
		add(menu);

		menuItem = new JMenuItem("New");
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.newGame();
			}
		});
		menu.add(menuItem);

		menu.addSeparator();

		ButtonGroup group = new ButtonGroup();

		rbMenuItem = new JRadioButtonMenuItem("Easy");
		rbMenuItem.setMnemonic(KeyEvent.VK_1);
		rbMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.changeSettings(8, 8, 10);
			}
		});
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Intermediate");
		rbMenuItem.setMnemonic(KeyEvent.VK_2);
		rbMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.changeSettings(16, 16, 40);
			}
		});
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Hard");
		rbMenuItem.setMnemonic(KeyEvent.VK_3);
		rbMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.changeSettings(16, 30, 99);
			}
		});
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
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((MinesweeperFrame) SwingUtilities.getWindowAncestor(MinesweeperMenuBar.this)).repack();
			}
		});
		menu.add(menuItem);

		menu = new JMenu("Solve");
		menu.setMnemonic(KeyEvent.VK_D);
		add(menu);

		menuItem = new JMenuItem("Placeholder");
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		menu.add(menuItem);
	}
}
