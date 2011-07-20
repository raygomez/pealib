package models;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;

import utilities.PropertyLoader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.MyConnection;
import models.User;
import models.UserDAO;

@DataSet({ "user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserDAOTest {

	UserDAO userDAO;
	
	@Before
	public void setUp() throws Exception {
			Connection con = new MyConnection(
					new PropertyLoader("test.config").getProperties())
					.getConnection();
			userDAO = new UserDAO();
			userDAO.setConnection(con);
	}

	
	@Test
	public void testGetUserInDatabase() throws SQLException {
		User user = userDAO.getUser("jvillar", "1");
		assertNotNull(user);
	}

	@Test
	public void testGetUserNotInDatabase() throws SQLException {
		User user = userDAO.getUser("jvillar", "12");
		assertNull(user);
	}
}
