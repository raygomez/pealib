package controllers;

import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
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
	User librarian;
	User user;

	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
		user = UserDAO.getUserById(1);
		userPanel = new Panel(new BookController(user).getBookLayoutPanel());
		librarian = UserDAO.getUserById(2);
		librarianPanel = new Panel(
				new BookController(librarian).getBookLayoutPanel());
	}

	@Test
	@DataSet({"../models/emptyBook.xml",
		"../models/emptyReserves.xml", "../models/emptyBorrows.xml" })
	public void testEmptyBookTable() throws Exception {
		userPanel = new Panel(
				new BookController(user).getBookLayoutPanel());
	}

	@Test
	public void testNotEmptyBookTable() throws Exception {
		userPanel = new Panel(
				new BookController(user).getBookLayoutPanel());
	}

	@Test
	public void testAddEmptyFields() throws Exception {
		Button addBook = librarianPanel.getButton("Add Book");

		WindowInterceptor.init(addBook.triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window dialog) {
						dialog.getButton("Add").click();
						dialog.getTextBox("Invalid Input");
						return dialog.getButton("Cancel").triggerClick();
					}
				}).run();

	}

	@Test
	public void testAddg100Char() throws Exception {
		Button addBook = librarianPanel.getButton("Add Book");

		WindowInterceptor.init(addBook.triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window dialog) {
						dialog.getInputTextBox("titleTextField").setText(
						"123456789012345678901234567890123456789012345678" +
						"901234567890123456789012345678901234567890123456" +
						"78901234567890");
						dialog.getInputTextBox("publisherTextField").setText(
								"123456789012345678901234567890123456789012345678" +
								"901234567890123456789012345678901234567890123456" +
								"78901234567890");
						dialog.getInputTextBox("editionTextField").setText(
								"123456789012345678901234567890123456789012345678" +
								"901234567890123456789012345678901234567890123456" +
								"78901234567890");
						dialog.getInputTextBox("authorTextField").setText(
								"123456789012345678901234567890123456789012345678" +
								"901234567890123456789012345678901234567890123456" +
								"78901234567890");
						dialog.getInputTextBox("yearPublishTextField").setText(
								"aaaa");
						dialog.getInputTextBox("isbnTextField").setText(
								"1234567890");
						dialog.getInputTextBox("descriptionTextArea").setText(
								"123456789012345678901234567890123456789012345678" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"901234567890123456789012345678901234567890123456" +
								"78901234567890");
						dialog.getButton("Add").click();
						dialog.getTextBox("Invalid Input");
						return dialog.getButton("Cancel").triggerClick();
					}
				}).run();

	}
	
	@Test
	public void testAddValid() throws Exception {
		Button addBook = librarianPanel.getButton("Add Book");

		WindowInterceptor.init(addBook.triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window dialog) {
						dialog.getInputTextBox("titleTextField").setText(
						"test");
						dialog.getInputTextBox("publisherTextField").setText(
								"test");
						dialog.getInputTextBox("editionTextField").setText(
								"test");
						dialog.getInputTextBox("authorTextField").setText(
								"test");
						dialog.getInputTextBox("yearPublishTextField").setText(
								"1992");
						dialog.getInputTextBox("isbnTextField").setText(
								"1209120976");
						dialog.getInputTextBox("descriptionTextArea").setText(
								"12345678901");
						dialog.getButton("Add").click();
						dialog.getTextBox("ISBN: 1209120976 was added");
						return dialog.getButton("Cancel").triggerClick();
					}
				}).run();

	}

	@Test
	@DataSet({ "../models/noCopybook.xml" })
	public void testExistingISBN() throws Exception {
		Button addBook = librarianPanel.getButton("Add Book");

		WindowInterceptor.init(addBook.triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window dialog) {
						dialog.getInputTextBox("titleTextField").setText(
						"test");
						dialog.getInputTextBox("publisherTextField").setText(
								"test");
						dialog.getInputTextBox("editionTextField").setText(
								"test");
						dialog.getInputTextBox("authorTextField").setText(
								"test");
						dialog.getInputTextBox("yearPublishTextField").setText(
								"1992");
						dialog.getInputTextBox("isbnTextField").setText(
								"8888888888");
						dialog.getInputTextBox("descriptionTextArea").setText(
								"12345678901");
						dialog.getButton("Add").click();
						dialog.getTextBox("ISBN already exist");
						return dialog.getButton("Cancel").triggerClick();
					}
				}).run();

	}

	@Test
	public void testSaveValid() throws Exception {
		
	}
}
