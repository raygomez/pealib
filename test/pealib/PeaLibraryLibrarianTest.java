package pealib;

import models.User;
import models.UserDAO;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.ToggleButton;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Constants;

@DataSet({ "../models/user.xml", "../models/book.xml",
		"../models/reserves.xml", "../models/borrows.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class PeaLibraryLibrarianTest extends UISpecTestCase {

	static Window window;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PeaLibrary peaLibrary = new PeaLibrary(Constants.TEST_CONFIG);
		User user = UserDAO.getUserById(2);
		peaLibrary.setCurrentUser(user);
		peaLibrary.initialize();
		window = new Window(peaLibrary.getMainFrame());

	}

	@Test
	public void testAuthenticate(){
		
		final PeaLibrary peaLib = new PeaLibrary(Constants.TEST_CONFIG);
		
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				peaLib.authenticate();
			}
		}).process(new WindowHandler(){

			@Override
			public Trigger process(Window login) throws Exception {
				login.getInputTextBox("username").setText("apantaleon");
				login.getPasswordField("password").setPassword("123456");
				
				return login.getButton("Log In").triggerClick();
			}
			
		}).run();
		
	}
	
	@Test
	public void testSidebar() {
		ToggleButton searchBooksButton = window.getToggleButton("Search Books");
		searchBooksButton.click();

		ToggleButton searchUsersButton = window.getToggleButton("Search Users");
		searchUsersButton.click();

		ToggleButton editProfileButton = window.getToggleButton("Edit Profile");
		editProfileButton.click();

		ToggleButton bookTransactionsButton = window
				.getToggleButton("Book Transactions");
		bookTransactionsButton.click();

	}
}
