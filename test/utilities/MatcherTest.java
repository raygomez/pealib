package utilities;


import static org.junit.Assert.*;

import org.junit.*;


public class MatcherTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testUsernameMatcher(){
		String username = "jjdizon";
		assertTrue(username.matches(Constants.USERNAME_FORMAT));
	}
	
	@Test
	public void testUsernameMatcher2(){
		String username = "j.j_dizon03";
		assertTrue(username.matches(Constants.USERNAME_FORMAT));
	}
	
	@Test
	public void testUsernameMatcher3(){
		String username = "03j.j_dizon03";
		assertFalse(username.matches(Constants.USERNAME_FORMAT));
	}
	
	@Test
	public void testUsernameMatcher4(){
		String username = "j.j@d!zon03";
		assertFalse(username.matches(Constants.USERNAME_FORMAT));
	}
	
	@Test
	public void testUsernameMatcher5(){
		String username = "j03";
		assertFalse(username.matches(Constants.USERNAME_FORMAT));
	}
	
	@Test
	public void testUsernameMatcher6(){
		String username = "johnjosephreyesdizon03";
		assertFalse(username.matches(Constants.USERNAME_FORMAT));
	}
	
	@Test
	public void testUsernameMatcher7(){
		String username = "john_joseph.dizon003";
		assertTrue(username.matches(Constants.USERNAME_FORMAT));
	}
	
	@Test
	public void testPasswordMatcher(){
		String password = "abc456";
		assertTrue(password.matches(Constants.PASSWORD_FORMAT));
	}
	
	@Test
	public void testPasswordMatcher2(){
		String password = "abcdefghijklmnopq123";
		assertTrue(password.matches(Constants.PASSWORD_FORMAT));
	}
	
	@Test
	public void testPasswordMatcher3(){
		String password = "!@#$%^&*()klmnopq012";
		assertTrue(password.matches(Constants.PASSWORD_FORMAT));
	}
	
	@Test
	public void testPasswordMatcher4(){
		String password = "ab456";
		assertFalse(password.matches(Constants.PASSWORD_FORMAT));
	}
	
	@Test
	public void testPasswordMatcher5(){
		String password = "abc 456";
		assertFalse(password.matches(Constants.PASSWORD_FORMAT));
	}
	
	@Test
	public void testPasswordMatcher6(){
		String password = "abcdefghijklmnopqrst12345";
		assertFalse(password.matches(Constants.PASSWORD_FORMAT));
	}
	
	@Test
	public void testNameMatcher(){
		String name = "janine june";
		assertTrue(name.matches(Constants.NAME_FORMAT));
	}
	
	@Test
	public void testNameMatcher2(){
		String name = "janine-june";
		assertTrue(name.matches(Constants.NAME_FORMAT));
	}
	
	@Test
	public void testNameMatcher3(){
		String name = "Janine-June Lim";
		assertTrue(name.matches(Constants.NAME_FORMAT));
	}
	
	@Test
	public void testNameMatcher4(){
		String name = "Janine!@June";
		assertFalse(name.matches(Constants.NAME_FORMAT));
	}
	
	@Test
	public void testNameMatcher5(){
		String name = " Janine-June";
		assertFalse(name.matches(Constants.NAME_FORMAT));
	}
	
	@Test
	public void testNameMatcher6(){
		String name = "j";
		assertFalse(name.matches(Constants.NAME_FORMAT));
	}
	
	@Test
	public void testNameMatcher7(){
		String name = "José Protacio Rizal Mercado y Alonzo Realonda";
		assertFalse(name.matches(Constants.NAME_FORMAT));
	}
	
	@Test
	public void testContactNumberMatcher(){
		String number = "9384195";
		assertTrue(number.matches(Constants.CONTACT_NUMBER_FORMAT));
	}
	
	@Test
	public void testContactNumberMatcher2(){
		String number = "09178011454";
		assertTrue(number.matches(Constants.CONTACT_NUMBER_FORMAT));
	}
	
	@Test
	public void testContactNumberMatcher3(){
		String number = " 091780 1145 4";
		assertFalse(number.matches(Constants.CONTACT_NUMBER_FORMAT));
	}
	
	@Test
	public void testContactNumberMatcher4(){
		String number = "091780a11454";
		assertFalse(number.matches(Constants.CONTACT_NUMBER_FORMAT));
	}
	
	@Test
	public void testContactNumberMatcher5(){
		String number = "938419";
		assertFalse(number.matches(Constants.CONTACT_NUMBER_FORMAT));
	}
	
	@Test
	public void testContactNumberMatcher6(){
		String number = "091780114549";
		assertFalse(number.matches(Constants.CONTACT_NUMBER_FORMAT));
	}
	
	@Test
	public void testEmailMatcher(){
		String email = "username03@example.com";
		assertTrue(email.matches(Constants.EMAIL_FORMAT));
	}
	
	@Test
	public void testEmailMatcher2(){
		String email = "username03_firstname-lastname.password@somewhere.example.net.ph";
		assertTrue(email.matches(Constants.EMAIL_FORMAT));
	}
	
	@Test
	public void testEmailMatcher3(){
		String email = "username_fi@rst!name-lastname.password@somewhere.example.net.ph";
		assertFalse(email.matches(Constants.EMAIL_FORMAT));
	}
	
	@Test
	public void testEmailMatcher4(){
		String email = "3username@example.com";
		assertFalse(email.matches(Constants.EMAIL_FORMAT));
	}
	
	@Test
	public void testEmailMatcher5(){
		String email = "user name@example.com";
		assertFalse(email.matches(Constants.EMAIL_FORMAT));
	}
	
	@Test
	public void testEmailMatcher6(){
		String email = "-@-";
		assertFalse(email.matches(Constants.EMAIL_FORMAT));
	}
}
