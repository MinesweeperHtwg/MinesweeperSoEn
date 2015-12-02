package minesweeper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class MinesweeperTest {

	@Test
	public void testMain() {
		InputStream systemIn = new ByteArrayInputStream("q".getBytes());
		System.setIn(systemIn);
		Minesweeper.main();
	}
}
