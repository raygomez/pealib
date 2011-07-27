package pealib;

import models.User;
import models.UserDAO;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.ToggleButton;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Constants;

@DataSet({ "../models/user.xml", "../models/book.xml",
		"../models/reserves.xml", "../models/borrows.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class PeaLibraryUserTest extends UISpecTestCase {

	static Window window;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PeaLibrary peaLibrary = new PeaLibrary(Constants.TEST_CONFIG);
		User user = UserDAO.getUserById(1);
		peaLibrary.setCurrentUser(user);
		peaLibrary.initialize();
		window = new Window(peaLibrary.getFrame());
		
	}

	@Test
	public void testSidebar() {
		ToggleButton searchBooksButton = window.getToggleButton("Search Books");
		searchBooksButton.click();
		
		ToggleButton editProfileButton = window.getToggleButton("Edit Profile");
		editProfileButton.click();

		ToggleButton libraryCardButton = window.getToggleButton("View E-Library Card");
		libraryCardButton.click();
	}

}
