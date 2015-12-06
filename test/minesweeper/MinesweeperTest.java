package minesweeper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class MinesweeperTest {

	@Test
	public void testMain() {
		InputStream systemIn = new ByteArrayInputStream("o 5 5\nq".getBytes());
		System.setIn(systemIn);
		Minesweeper.main(null);
	}
}
