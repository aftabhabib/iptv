package fr.mipih.pastel.importRPPS;

import fr.mipih.pastel.iptv.util.Luhn;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class LuhnTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public LuhnTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(LuhnTest.class);
	}

	/**
	 * Test des digits pas bon :-)
	 */
	public void testBadDigits() {
		assertFalse(Luhn.Check("489787897"));
		assertFalse(Luhn.Check("573829491"));
		assertFalse(Luhn.Check("49927398717"));
		assertFalse(Luhn.Check("1234567812345678"));
		assertFalse(Luhn.Check("a"));
		assertFalse(Luhn.Check("10"));
	}

	/**
	 * Test des digits bon
	 */
	public void testDigitsOk() {
		assertFalse(Luhn.Check("1"));
		assertTrue(Luhn.Check("1234567812345670"));
		assertTrue(Luhn.Check("49927398716"));
		assertTrue(Luhn.Check("4897878973"));
		assertTrue(Luhn.Check("10003194932"));
		assertTrue(Luhn.Check("12345678903555"));
		assertTrue(Luhn.Check("012850003580200"));

		assertTrue(Luhn.Check("5292117612400828"));
		assertTrue(Luhn.Check("365497148374561"));
		assertTrue(Luhn.Check("6011213413155840"));
		assertTrue(Luhn.Check("4327503556777010"));
		assertTrue(Luhn.Check("4331574673930"));
		assertTrue(Luhn.Check("012850003580200"));
	}
}
