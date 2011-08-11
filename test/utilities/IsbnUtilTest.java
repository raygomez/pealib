package utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IsbnUtilTest {

	@Test
	public void testIsIsbnValid13Digit() {
		assertTrue(IsbnUtil.isIsbnValid("9780306406157"));
	}

	@Test
	public void testIsIsbnInvalid13Digit() {
		assertFalse(IsbnUtil.isIsbnValid("9780306406158"));
	}

	@Test
	public void testIsIsbnValid10Digit() {
		assertTrue(IsbnUtil.isIsbnValid("0306406152"));
	}

	@Test
	public void testIsIsbnValid10DigitWithLetters() {
		assertFalse(IsbnUtil.isIsbnValid("aaaaaaaaaa"));
	}

	
	@Test
	public void testIsIsbnValid10DigitWithX() {
		assertTrue(IsbnUtil.isIsbnValid("080442957X"));
	}

	@Test
	public void testIsIsbnInvalid10DigitWithX() {
		assertFalse(IsbnUtil.isIsbnValid("080442958X"));
	}

	@Test
	public void testIsIsbninvalid10Digit() {
		assertFalse(IsbnUtil.isIsbnValid("0306406153"));
	}
	
	@Test
	public void testIsIsbnValidBlank() {
		assertFalse(IsbnUtil.isIsbnValid(""));
	}
	
	@Test
	public void testIsIsbnValidLess10() {
		assertFalse(IsbnUtil.isIsbnValid("12345"));
	}
	
	@Test
	public void testIsIsbnValidBetween10and13() {
		assertFalse(IsbnUtil.isIsbnValid("12345678901"));
	}
	
	@Test
	public void testIsIsbnValidGreater13() {
		assertFalse(IsbnUtil.isIsbnValid("1234567890123456789"));
	}
	
	@Test
	public void testConvert(){
		assertEquals("0306406152", IsbnUtil.convert("9780306406157"));
	}
}
