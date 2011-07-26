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
import static org.unitils.reflectionassert.ReflectionAssert.*;
import utilities.Connector;
import utilities.Constants;

@DataSet({ "user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserDAOTest {

	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
	}

	@Test
	public void testGetUserInDatabase() throws Exception {
		User user = UserDAO.getUser("jvillar", "123456");
		User expected = UserDAO.getUserById(1);
		assertReflectionEquals(expected, user);
	}

	@Test
	public void testGetUserNotInDatabase() throws Exception {
		User user = UserDAO.getUser("jvillar", "12");
		assertNull(user);
	}

	@Test
	public void testGetUserById() throws Exception {
		User user = UserDAO.getUserById(1);
		assertEquals("Jomel", user.getFirstName());
		assertEquals("Pantaleon", user.getLastName());
		assertEquals("jvillar", user.getUserName());
		assertEquals("User", user.getType());
		assertEquals("USA", user.getAddress());
		assertEquals("1234567890", user.getContactNo());
		assertEquals("jomel.villar@gmail.com", user.getEmail());
	}
	
	@Test
	public void testGetUserByIdNotExisting() throws Exception {
		User user = UserDAO.getUserById(100);
		assertNull(user);
	}

	@Test
	public void testSearchUsersWithOneResult() throws Exception {
		ArrayList<User> users = UserDAO.searchActiveUsers("jvillar");
		assertEquals(1, users.size());
	}

	@Test
	public void testSearchUsersWithManyResults() throws Exception {
		ArrayList<User> users = UserDAO.searchActiveUsers("Niel");
		assertEquals(2, users.size());
	}

	@Test
	public void testSearchUsersWithLibrarian() throws Exception {
		ArrayList<User> users = UserDAO.searchActiveUsers("Pantaleon");
		assertEquals(1, users.size());
	}

	@Test
	public void testSearchUsersWithNoResults() throws Exception {
		ArrayList<User> users = UserDAO.searchActiveUsers("jvillar0");
		assertEquals(0, users.size());
	}

	@Test
	public void testSearchUsersWithDatafromOtherFields() throws Exception {
		ArrayList<User> users = UserDAO.searchActiveUsers("User");
		assertEquals(0, users.size());
		users = UserDAO.searchActiveUsers("nlazada@gmail.com");
		assertEquals(0, users.size());
		users = UserDAO.searchActiveUsers("1234567890");
		assertEquals(0, users.size());
		users = UserDAO.searchActiveUsers("USA");
		assertEquals(0, users.size());
		users = UserDAO.searchActiveUsers("6b86b273ff");
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
		User user = UserDAO.getUser("jvillar", "123456");
		user.setFirstName("Janine June");
		user.setLastName("Lim");
		user.setUserName("jlim");
		user.setType("Pending");
		user.setEmail("jlim@gmail.com");
		user.setPassword("1234567");

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
		boolean actual = UserDAO.checkPassword(1, "123456");
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
		assertNotNull(user);
		UserDAO.changePassword(user.getUserId(), "1234567");
	}
	
	@Test
	@DataSet({ "user_with_no_pending.xml" })
	public void testSearchAllPendingWithNoPending() throws Exception {
		ArrayList<User> users = UserDAO.searchAllPending("");
		assertEquals(0, users.size());
	}

	@Test
	public void testSearchAllPending() throws Exception {
		ArrayList<User> users = UserDAO.searchAllPending("");
		assertEquals(2, users.size());
	}

	@Test
	public void testSearchAllPendingWithKeyword() throws Exception {
		ArrayList<User> users = UserDAO.searchAllPending("Karlo");
		assertEquals(1, users.size());
	}

	@Test
	public void testSearchAllPendingWithKeywordwithNoResults() throws Exception {
		ArrayList<User> users = UserDAO.searchAllPending("Karlo0");
		assertEquals(0, users.size());
	}
	
	@Test
	@ExpectedDataSet({ "expected/denyPendingUser.xml" })
	public void testdenyPendingUser() throws Exception {
		User user = UserDAO.searchAllPending("").get(0);
		assertNotNull(user);
		UserDAO.denyPendingUser(user);
	}
}
