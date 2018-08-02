package testPackage;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import project.java.time.TimeCheckMain;


public class TestTimeCheck {

	@Test
	public void generareString_dupa() {

		String s = TimeCheckMain.generareString("2018-12-25");

		assertEquals(s, "2018-12-25 | Da          | Dupa         | 145");

	}

	@Test
	public void generareString_inainte() {

		String s = TimeCheckMain.generareString("2018-01-26");

		assertEquals(s, "2018-01-26 | Da          | Inainte      | 187");

	}
	
	@Test
	public void generareString_egal() {

		String s = TimeCheckMain.generareString("2018-08-01");

		assertEquals(s, "2018-08-01 | Da          | Egal         | 0");

	}
	
	@Test
	public void generareString_eronat() {

		String s = TimeCheckMain.generareString("asd");

		assertEquals(s, "asd        | Nu          |               ");

	}

}
