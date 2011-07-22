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

	UserDAO userDAO;

	@Before
	public void setUp() throws Exception {
		new Connector("test.config");
		userDAO = new UserDAO();
	}

	@Test
	public void testGetUserInDatabase() throws Exception {
		User user = userDAO.getUser("jvillar", "1");
		assertNotNull(user);
	}

	@Test
	public void testGetUserNotInDatabase() throws Exception {
		User user = userDAO.getUser("jvillar", "12");
		assertNull(user);
	}

	@Test
	public void testSearchUsersWithOneResult() throws Exception {
		ArrayList<User> users = userDAO.searchUsers("jvillar");
		assertEquals(1, users.size());
	}

	@Test
	public void testSearchUsersWithManyResults() throws Exception {
		ArrayList<User> users = userDAO.searchUsers("Niel");
		assertEquals(2, users.size());
	}

	@Test
	public void testSearchUsersWithLibrarian() throws Exception {
		ArrayList<User> users = userDAO.searchUsers("Pantaleon");
		assertEquals(1, users.size());
	}

	@Test
	public void testSearchUsersWithNoResults() throws Exception {
		ArrayList<User> users = userDAO.searchUsers("jvillar0");
		assertEquals(0, users.size());
	}

	@Test
	public void testSearchUsersWithDatafromOtherFields() throws Exception {
		ArrayList<User> users = userDAO.searchUsers("User");
		assertEquals(0, users.size());
		users = userDAO.searchUsers("nlazada@gmail.com");
		assertEquals(0, users.size());
		users = userDAO.searchUsers("1234567890");
		assertEquals(0, users.size());
		users = userDAO.searchUsers("USA");
		assertEquals(0, users.size());
		users = userDAO.searchUsers("6b86b273ff");
		assertEquals(0, users.size());

	}

	@Test
	public void testIsUsernameExisting() throws Exception {
		boolean isExisting = userDAO.isUsernameExisting("jvillar");
		assertEquals(true, isExisting);
	}

	@Test
	public void testIsUsernameNotExisting() throws Exception {
		boolean notExisting = userDAO.isUsernameExisting("jvillar0");
		assertEquals(false, notExisting);
	}

	@Test
	@ExpectedDataSet({ "expected/saveUser.xml" })
	public void testSaveUser() throws Exception {
		User user = userDAO.getUser("jvillar", "1");
		user.setFirstName("Janine June");
		user.setLastName("Lim");
		user.setUserName("jlim");
		user.setEmail("jlim@gmail.com");
		user.setPassword("1");

		userDAO.saveUser(user);
	}

}
