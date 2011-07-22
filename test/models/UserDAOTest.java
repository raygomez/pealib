package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;

import utilities.Connector;

@DataSet({ "user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserDAOTest {

	@Before
	public void setUp() throws Exception {
		new Connector("test.config");

	}

	@Test
	public void testGetUserInDatabase() throws Exception {
		User user = UserDAO.getUser("jvillar", "1");
		assertNotNull(user);
	}

	@Test
	public void testGetUserNotInDatabase() throws Exception {
		User user = UserDAO.getUser("jvillar", "12");
		assertNull(user);
	}

	@Test
	public void testSearchUsersWithOneResult() throws Exception {
		ArrayList<User> users = UserDAO.searchUsers("jvillar");
		assertEquals(1, users.size());
	}

	@Test
	public void testSearchUsersWithManyResults() throws Exception {
		ArrayList<User> users = UserDAO.searchUsers("Niel");
		assertEquals(2, users.size());
	}

	@Test
	public void testSearchUsersWithLibrarian() throws Exception {
		ArrayList<User> users = UserDAO.searchUsers("Pantaleon");
		assertEquals(1, users.size());
	}

	@Test
	public void testSearchUsersWithNoResults() throws Exception {
		ArrayList<User> users = UserDAO.searchUsers("jvillar0");
		assertEquals(0, users.size());
	}

	@Test
	public void testSearchUsersWithDatafromOtherFields() throws Exception {
		ArrayList<User> users = UserDAO.searchUsers("User");
		assertEquals(0, users.size());
		users = UserDAO.searchUsers("nlazada@gmail.com");
		assertEquals(0, users.size());
		users = UserDAO.searchUsers("1234567890");
		assertEquals(0, users.size());
		users = UserDAO.searchUsers("USA");
		assertEquals(0, users.size());
		users = UserDAO.searchUsers("6b86b273ff");
		assertEquals(0, users.size());

	}

	@Test
	public void testIsUsernameExisting() throws Exception {
		boolean isExisting = UserDAO.isUsernameExisting("jvillar");
		assertEquals(true, isExisting);
	}

	@Test
	public void testIsUsernameNotExisting() throws Exception {
		boolean notExisting = UserDAO.isUsernameExisting("jvillar0");
		assertEquals(false, notExisting);
	}

	@Test
	@ExpectedDataSet({ "expected/saveUser.xml" })
	public void testSaveUser() throws Exception {
		User user = UserDAO.getUser("jvillar", "1");
		user.setFirstName("Janine June");
		user.setLastName("Lim");
		user.setUserName("jlim");
		user.setEmail("jlim@gmail.com");
		user.setPassword("1");

		UserDAO.saveUser(user);
	}
	
	@Test
	@ExpectedDataSet({ "expected/updateUser.xml" })
	public void testUpdateUser() throws Exception {
		User user = UserDAO.getUserById(4);
		user.setFirstName("Joseph");
		user.setLastName("Dizon");
		user.setEmail("jjrdizon@gmail.com");
		user.setAddress("Fairview, Q.C.");
		user.setContactNo("09178011454");

		UserDAO.updateUser(user);
	}
	
	@Test
	public void testCheckPassword() throws Exception {
		boolean actual = UserDAO.checkPassword(1, "1");
		assertEquals(true, actual);
	}
	
	@Test
	public void testCheckPassword2() throws Exception {
		boolean actual = UserDAO.checkPassword(1, "2");
		assertEquals(false, actual);
	}

	@Test
	@ExpectedDataSet({ "expected/changePassword.xml" })
	public void testChangePassword() throws Exception {
		User user = UserDAO.getUserById(4);
		UserDAO.changePassword(user.getUserId(), "5");
	}
}
