package testPackage;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import time.TimeCheck;


public class TestTimeCheck {

	@Test
	public void generareString_dupa() {

		String s = TimeCheck.generareString("2018-12-25");

		assertEquals(s, "2018-12-25 | Da          | Dupa         | 145");

	}

	@Test
	public void generareString_inainte() {

		String s = TimeCheck.generareString("2018-01-26");

		assertEquals(s, "2018-01-26 | Da          | Inainte      | 187");

	}
	
	@Test
	public void generareString_egal() {

		String s = TimeCheck.generareString("2018-08-01");

		assertEquals(s, "2018-08-01 | Da          | Egal         | 0");

	}
	
	@Test
	public void generareString_eronat() {

		String s = TimeCheck.generareString("asd");

		assertEquals(s, "asd        | Nu          |               ");

	}

}
