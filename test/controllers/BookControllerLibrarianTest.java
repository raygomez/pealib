package controllers;

import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.Key;
import org.uispec4j.Panel;
import org.uispec4j.Table;
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
public class BookControllerLibrarianTest  extends UISpecTestCase {


	Panel librarianPanel;
	User librarian;
	BookController bookControllerLib;

	@Before
	public void setUp() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		librarian = UserDAO.getUserById(2);
		bookControllerLib = new BookController(librarian);
		librarianPanel = new Panel(
				bookControllerLib.getBookLayoutPanel());
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
						dialog.getInputTextBox("isbnTextField").setText(
						"8888888888");
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
						dialog.getInputTextBox("descriptionTextArea").setText(
								"0007222556");
						dialog.getButton("Add").click();
						dialog.getTextBox("ISBN already exist");
						return dialog.getButton("Cancel").triggerClick();
					}
				}).run();

	}
	
	@Test
	@Ignore
	@DataSet({ "../models/noCopybook.xml" })
	public void testValidSave() throws Exception{
		bookControllerLib = new BookController(librarian);
		librarianPanel = new Panel(
				bookControllerLib.getBookLayoutPanel());
		Panel bookSearch = new Panel(bookControllerLib.getBookSearch());
		Panel bookInfo = new Panel(bookControllerLib.getBookInfo());
		Table bookTable = bookSearch.getTable();
		bookTable.click(3,0);
		bookTable.click(3,0);
		System.out.println(bookControllerLib.getBookSearch().getTableBookList().getModel().getValueAt(4, 1) + " MUTYA" );
		bookInfo.getInputTextBox("titleTextField").setText(
		"test");
		bookInfo.getInputTextBox("publisherTextField").setText(
				"test");
		bookInfo.getInputTextBox("editionTextField").setText(
				"test");
		bookInfo.getInputTextBox("authorTextField").setText(
				"test");
		bookInfo.getInputTextBox("yearPublishTextField").setText(
				"1992");
		bookInfo.getInputTextBox("isbnTextField").setText(
				"0007222556");
		bookInfo.getInputTextBox("descriptionTextArea").setText(
				"12345678901");
		bookInfo.getButton("Save").click();
	}

	@Test
	@Ignore
	public void testValid2Save() throws Exception{
		librarianPanel = new Panel(
				bookControllerLib.getBookLayoutPanel());
		Panel bookSearch = new Panel(bookControllerLib.getBookSearch());
		Panel bookInfo = new Panel(bookControllerLib.getBookInfo());
		Table bookTable = bookSearch.getTable();
		bookTable.click(3,0);
		bookTable.click(3,0);
		bookInfo.getInputTextBox("isbnTextField").setText(
				"1209120976");
		bookInfo.getButton("Save").click();
	}

	@Test
	@Ignore
	public void testInvalidSave() throws Exception{
		librarian = UserDAO.getUserById(2);
		bookControllerLib = new BookController(librarian);
		librarianPanel = new Panel(
				bookControllerLib.getBookLayoutPanel());
		Panel bookSearch = new Panel(bookControllerLib.getBookSearch());
		Panel bookInfo = new Panel(bookControllerLib.getBookInfo());
		Table bookTable = bookSearch.getTable();
		bookTable.click(3,0);
		bookInfo.getInputTextBox("titleTextField").setText(
				"123456789012345678901234567890123456789012345678" +
				"901234567890123456789012345678901234567890123456" +
				"78901234567890");
				bookInfo.getInputTextBox("publisherTextField").setText(
						"123456789012345678901234567890123456789012345678" +
						"901234567890123456789012345678901234567890123456" +
						"78901234567890");
				bookInfo.getInputTextBox("editionTextField").setText(
						"123456789012345678901234567890123456789012345678" +
						"901234567890123456789012345678901234567890123456" +
						"78901234567890");
				bookInfo.getInputTextBox("authorTextField").setText(
						"123456789012345678901234567890123456789012345678" +
						"901234567890123456789012345678901234567890123456" +
						"78901234567890");
				bookInfo.getInputTextBox("yearPublishTextField").setText(
						"1234567");
				bookInfo.getInputTextBox("isbnTextField").setText(
						"8888888888");
				bookInfo.getInputTextBox("descriptionTextArea").setText(
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
						"901234567890123456789012345678901234567890123456" +
						"901234567890123456789012345678901234567890123456" +
						"901234567890123456789012345678901234567890123456" +
						"78901234567890");
				bookInfo.getButton("Save").click();
	}
	
	@Test
	@Ignore
	public void testEmptyYrSave() throws Exception{
		librarian = UserDAO.getUserById(2);
		bookControllerLib = new BookController(librarian);
		librarianPanel = new Panel(
				bookControllerLib.getBookLayoutPanel());
		Panel bookSearch = new Panel(bookControllerLib.getBookSearch());
		Panel bookInfo = new Panel(bookControllerLib.getBookInfo());
		Table bookTable = bookSearch.getTable();
		bookTable.click(3,0);
		bookInfo.getInputTextBox("yearPublishTextField").setText(
				"");
		bookInfo.getInputTextBox("isbnTextField").setText(
		"1234567890120");
		bookInfo.getButton("Save").click();
	}

	@Test
	@Ignore
	@DataSet({ "../models/noCopybook.xml" })
	public void testCopy0Save() throws Exception{
		librarian = UserDAO.getUserById(2);
		bookControllerLib = new BookController(librarian);
		librarianPanel = new Panel(
				bookControllerLib.getBookLayoutPanel());
		Panel bookSearch = new Panel(bookControllerLib.getBookSearch());
		Panel bookInfo = new Panel(bookControllerLib.getBookInfo());
		Table bookTable = bookSearch.getTable();
		bookTable.click(6,2);
		bookInfo.getButton("Save").click();
	}

	@Test
	@Ignore
	public void testYesDelete() throws Exception{
		librarian = UserDAO.getUserById(2);
		bookControllerLib = new BookController(librarian);
		librarianPanel = new Panel(
				bookControllerLib.getBookLayoutPanel());
		Panel bookSearch = new Panel(bookControllerLib.getBookSearch());
		Panel bookInfo = new Panel(bookControllerLib.getBookInfo());
		Table bookTable = bookSearch.getTable();
		bookTable.click(6,0);
		Button deleteBook = librarianPanel.getButton("Delete");
		bookInfo.getButton("Delete").click();
		WindowInterceptor.init(deleteBook.triggerClick())
		.process(new WindowHandler() {
			public Trigger process(Window dialog) {
				return dialog.getButton("Yes").triggerClick();
			}
		}).run();
	}
	
	@Test
	@Ignore
	public void testNoDelete() throws Exception{
		librarian = UserDAO.getUserById(2);
		bookControllerLib = new BookController(librarian);
		librarianPanel = new Panel(
				bookControllerLib.getBookLayoutPanel());
		Panel bookSearch = new Panel(bookControllerLib.getBookSearch());
		Panel bookInfo = new Panel(bookControllerLib.getBookInfo());
		Table bookTable = bookSearch.getTable();
		bookTable.click(6,0);
		Button deleteBook = librarianPanel.getButton("Delete");
		bookInfo.getButton("Delete").click();
		WindowInterceptor.init(deleteBook.triggerClick())
		.process(new WindowHandler() {
			public Trigger process(Window dialog) {
				return dialog.getButton("No").triggerClick();
			}
		}).run();
	}

	@Test
	@DataSet({ "../models/noCopybook.xml" })
	public void testNoCopyBookTable() throws Exception {
		librarianPanel = new Panel(
				new BookController(librarian).getBookLayoutPanel());
		Panel bookSearch = new Panel(bookControllerLib.getBookSearch());
		bookSearch.getButton("Search").click();
		bookSearch.getButton("Clear").click();
		bookSearch.getInputTextBox("textSearch").setText("zzzzzzzzzzzzzz");
		bookSearch.getButton("Search").click();
	}


}