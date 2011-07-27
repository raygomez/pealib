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

@DataSet({ "../models/user.xml", "../models/book.xml",
		"../models/reserves.xml", "../models/borrows.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class BookControllerTest extends UISpecTestCase {

	Panel userPanel;
	Panel librarianPanel;

	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
		User user = UserDAO.getUserById(1);
		userPanel = new Panel(new BookController(user).getBookLayoutPanel());
		User librarian = UserDAO.getUserById(1);
		librarianPanel = new Panel(
				new BookController(librarian).getBookLayoutPanel());
	}

	@Test
	public void test() {
	}

}
