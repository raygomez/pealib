package utilities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IsbnCheckerTest {

	@Test
	public void testIsIsbnValid13Digit() {
		assertTrue(IsbnChecker.isIsbnValid("9780306406157"));
	}

	@Test
	public void testIsIsbnInvalid13Digit() {
		assertFalse(IsbnChecker.isIsbnValid("9780306406158"));
	}

	@Test
	public void testIsIsbnValid10Digit() {
		assertTrue(IsbnChecker.isIsbnValid("0306406152"));
	}

	@Test
	public void testIsIsbnValid10DigitWithX() {
		assertTrue(IsbnChecker.isIsbnValid("080442957X"));
	}

	@Test
	public void testIsIsbnInvalid10DigitWithX() {
		assertFalse(IsbnChecker.isIsbnValid("080442958X"));
	}

	@Test
	public void testIsIsbninvalid10Digit() {
		assertFalse(IsbnChecker.isIsbnValid("0306406153"));
	}
}
