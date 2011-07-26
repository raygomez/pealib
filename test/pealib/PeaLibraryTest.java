package pealib;

import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "../models/user.xml", "../models/book.xml",
		"../models/reserves.xml", "../models/borrows.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class PeaLibraryTest extends UISpecTestCase {

	Window window;

	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
		User user = UserDAO.getUserById(1);
		PeaLibrary peaLibrary = new PeaLibrary();
		peaLibrary.setCurrentUser(user);
		peaLibrary.initialize();
		window = new Window(peaLibrary.getFrame());
		
	}

	@Test
	public void test() {
	}

}
