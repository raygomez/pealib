package controllers;

import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Panel;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "../models/user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserControllerTest extends UISpecTestCase {

	Panel userInfoPanel;
	Panel userSearchPanel;
	
	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
		
		User user = UserDAO.getUserById(2);
		userInfoPanel = new Panel(new UserController(user).getUserInfoPanel());
		assertNotNull(userInfoPanel);
		userSearchPanel = new Panel(new UserController(user).getUserSearch());
		assertNotNull(userSearchPanel);
	}

	@Test
	public void testGetUserPanel() {

	}

}
