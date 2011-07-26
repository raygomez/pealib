package utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConstantsTest {

	@Test
	public void testDEFAULT_ERROR() {
		assertEquals(0, Constants.DEFAULT_ERROR);
	}

	@Test
	public void testUSERNAME_ERROR() {
		assertEquals(1, Constants.USERNAME_ERROR);
	}

	@Test
	public void testPASSWORD_NOT_MATCH_ERROR() {
		assertEquals(2, Constants.PASSWORD_NOT_MATCH_ERROR);
	}

	@Test
	public void testINCORRECT_PASSWORD_ERROR() {
		assertEquals(3, Constants.INCORRECT_PASSWORD_ERROR);
	}

	@Test
	public void testPASSWORD_FORMAT_ERROR() {
		assertEquals(4, Constants.PASSWORD_FORMAT_ERROR);
	}

	@Test
	public void tesFIRST_NAME_FORMAT_ERRORt() {
		assertEquals(5, Constants.FIRST_NAME_FORMAT_ERROR);
	}

	@Test
	public void testLAST_NAME_FORMAT_ERROR() {
		assertEquals(6, Constants.LAST_NAME_FORMAT_ERROR);
	}

	@Test
	public void testCONTACT_NUMBER_FORMAT_ERROR() {
		assertEquals(7, Constants.CONTACT_NUMBER_FORMAT_ERROR);
	}

	@Test
	public void testEMAIL_FORMAT_ERROR() {
		assertEquals(8, Constants.EMAIL_FORMAT_ERROR);
	}

	@Test
	public void testUSERNAME_FORMAT() {
		assertEquals("[A-Za-z0-9_\\.]{4,20}", Constants.USERNAME_FORMAT);
	}

	@Test
	public void testPASSWORD_FORMAT() {
		assertEquals("[^\\s]{6,20}", Constants.PASSWORD_FORMAT);
	}

	@Test
	public void testNAME_FORMAT() {
		assertEquals("[A-Za-z\\s]{2,}", Constants.NAME_FORMAT);
	}

	@Test
	public void testCONTACT_NUMBER_FORMAT() {
		assertEquals("(\\d{7,11})?", Constants.CONTACT_NUMBER_FORMAT);
	}

	@Test
	public void testEMAIL_FORMAT() {
		assertEquals("([a-z]+[a-z0-9_\\.]+@\\w{2,}(\\.[a-z]{2,3})+)",
				Constants.EMAIL_FORMAT);
	}

	@Test
	public void testADDRESS_FORMAT() {
		assertEquals(".{2,100}", Constants.ADDRESS_FORMAT);
	}

	@Test
	public void testISBN_FORMAT_1() {
		assertEquals("[0-9]{13}", Constants.ISBN_FORMAT_1);
	}

	@Test
	public void testISBN_FORMAT_2() {
		assertEquals("[0-9]{10}", Constants.ISBN_FORMAT_2);
	}

	@Test
	public void testYEAR_PUBLISH_FORMAT() {
		assertEquals("[0-9]{4}", Constants.YEAR_PUBLISH_FORMAT);
	}

	@Test
	public void tesNAME_FORMAT_ERROR_MESSAGEt() {
		assertEquals(
				"Incorrect name format, please alphabetical characters only",
				Constants.NAME_FORMAT_ERROR_MESSAGE);
	}

	@Test
	public void testUSERNAME_FORMAT_ERROR_MESSAGE() {
		assertEquals(
				"Invalid username, please use alphanumeric characters, underscores and periods only.\n"
						+ "Usernames must be 4-20 characters long.",
				Constants.USERNAME_FORMAT_ERROR_MESSAGE);
	}

	@Test
	public void testPASSWORD_FORMAT_ERROR_MESSAGE() {
		assertEquals(
				"Invalid password. Passwords should be 6-20 characters long.",
				Constants.PASSWORD_FORMAT_ERROR_MESSAGE);
	}

	@Test
	public void testPASSWORD_NOT_MATCH_ERROR_MESSAGE() {
		assertEquals("Incorrect Password",
				Constants.PASSWORD_NOT_MATCH_ERROR_MESSAGE);
	}

	@Test
	public void testEMAIL_FORMAT_ERROR_MESSAGE() {
		assertEquals("Invalid email format.",
				Constants.EMAIL_FORMAT_ERROR_MESSAGE);
	}

	@Test
	public void testCONTACT_NUMBER_FORMAT_ERROR_MESSAGE() {
		assertEquals("Invalid contact number.",
				Constants.CONTACT_NUMBER_FORMAT_ERROR_MESSAGE);
	}

	@Test
	public void testAPP_CONFIG() {
		assertEquals("app.config", Constants.APP_CONFIG);
	}

	@Test
	public void testTEST_CONFIG() {
		assertEquals("test.config", Constants.TEST_CONFIG);
	}
}
