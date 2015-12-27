package minesweeper.aview.gui;

import javax.swing.*;
import java.awt.*;

public class CustomBoardDialogPanel extends JPanel {

	private final SpinnerModel heightSpinner;
	private final SpinnerModel widthSpinner;
	private final SpinnerModel mineSpinner;

	public CustomBoardDialogPanel() {
		setLayout(new GridLayout(3, 2));

		JSpinner spinner;

		add(new JLabel("Height"));

		heightSpinner = new SpinnerNumberModel(8, 2, 99, 1);
		spinner = new JSpinner(heightSpinner);
		add(spinner);

		add(new JLabel("Width"));

		widthSpinner = new SpinnerNumberModel(8, 2, 99, 1);
		spinner = new JSpinner(widthSpinner);
		add(spinner);

		add(new JLabel("Mines"));

		mineSpinner = new SpinnerNumberModel(1, 1, null, 1);
		spinner = new JSpinner(mineSpinner);
		add(spinner);
	}
}
