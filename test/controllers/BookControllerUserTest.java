package controllers;

import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Key;
import org.uispec4j.Panel;
import org.uispec4j.Table;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "../models/user.xml", "../models/book.xml",
		"../models/reserves.xml", "../models/borrows.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class BookControllerUserTest extends UISpecTestCase {

	Panel userPanel;
	User user;
	BookController bookControllerUser;

	@Before
	public void setUp() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		user = UserDAO.getUserById(3);
		bookControllerUser = new BookController(user);
	}

	@Test
	@DataSet({"../models/emptyBook.xml",
		"../models/emptyReserves.xml", "../models/emptyBorrows.xml" })
	public void testEmptyBookTable() throws Exception {
		userPanel = new Panel(
				new BookController(user).getBookLayoutPanel());
	}

	@Test
	public void testReserves() throws Exception{
		Panel bookSearch = new Panel(bookControllerUser.getBookSearch());
		Panel bookInfo = new Panel(bookControllerUser.getBookInfo());
		Table bookTable = bookSearch.getTable();
		bookTable.click(0,0);
		bookInfo.getButton("Reserve").click();
		assertFalse(bookInfo.getButton("Reserve").isEnabled());
		assertFalse(bookInfo.getButton("Borrow").isEnabled());
	}

	@Test
	public void testBorrow() throws Exception{
		Panel bookSearch = new Panel(bookControllerUser.getBookSearch());
		Panel bookInfo = new Panel(bookControllerUser.getBookInfo());
		Table bookTable = bookSearch.getTable();
		bookTable.click(2,2);
		bookInfo.getButton("Borrow").click();
		assertEquals((String) bookTable.getJTable().getModel().getValueAt(2, 2), "<html><font color='red'>unavailable</font></html>");
		assertFalse(bookInfo.getButton("Reserve").isEnabled());
		assertFalse(bookInfo.getButton("Borrow").isEnabled());
	}

	@Test
	public void testNotEmptyBookTable() throws Exception {
		userPanel = new Panel(
				new BookController(user).getBookLayoutPanel());
		Panel bookSearch = new Panel(bookControllerUser.getBookSearch());
		bookSearch.getButton("Search").click();
		bookSearch.getButton("Clear").click();
		bookSearch.getInputTextBox("textSearch").typeKey(Key.A);
		bookSearch.getInputTextBox("textSearch").releaseKey(Key.ENTER);	
	}

}
