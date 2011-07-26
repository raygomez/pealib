package views;

import models.Book;
import models.BookDAO;
import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "../models/user.xml", "../models/book.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class BookInfoPanelTest extends UISpecTestCase {

	Panel panelUser;
	Panel panelLibrarian;
	Book book;
	User user;
	User librarian;

	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);

		book = BookDAO.getBookById(1);
		user = UserDAO.getUserById(1);
		assertEquals("User", user.getType());
		librarian = UserDAO.getUserById(2);
		assertEquals("Librarian", librarian.getType());
		panelUser = new Panel(new BookInfoPanel(book, user));
		panelLibrarian = new Panel(new BookInfoPanel(book, librarian));
	}

	@Test
	public void testInitialStateUser() {
		TextBox label;
		TextBox textField;
		Button button;
		
		String[] labelArray = new String[] { "Title:", "Author:",
				"Year Published:", "Publisher:", "ISBN:", "Description:",
				"Copies:" };

		for (String l : labelArray) {
			label = panelUser.getTextBox(l);
			assertNotNull(label);
			assertThat(label.isVisible());
		}

		String[] textInputArray = new String[] { "titleTextField",
				"authorTextField", "yearPublishTextField",
				"publisherTextField", "isbnTextField", "descriptionTextArea" };
						
		for (String l : textInputArray) {
			textField = panelUser.getTextBox(l);
			assertNotNull(textField);
			assertThat(textField.isVisible());
			assertFalse(textField.isEditable());
		}	

		assertEquals(book.getTitle(),
				panelUser.getInputTextBox("titleTextField").getText());
		assertEquals(book.getAuthor(),
				panelUser.getInputTextBox("authorTextField").getText());
		assertEquals("" + book.getYearPublish(),
				panelUser.getInputTextBox("yearPublishTextField").getText());
		assertEquals(book.getPublisher(),
				panelUser.getInputTextBox("publisherTextField").getText());
		assertEquals(book.getIsbn(), panelUser.getInputTextBox("isbnTextField")
				.getText());
		assertEquals(book.getDescription(),
				panelUser.getInputTextBox("descriptionTextArea").getText());
		
		String[] buttonArray = new String[] { "Borrow", "Reserve" };

		for (String s : buttonArray) {
			button = panelUser.getButton(s);
			assertNotNull(button);
			assertThat(button.isEnabled());
			assertThat(button.isVisible());
		}
	}
	
	@Test
	public void testInitialStateLibrarian() {
		TextBox label;
		TextBox textField;
		Button button;
		
		String[] labelArray = new String[] { "Title:", "Author:",
				"Year Published:", "Publisher:", "ISBN:", "Description:",
				"Copies:" };

		for (String l : labelArray) {
			label = panelLibrarian.getTextBox(l);
			assertNotNull(label);
			assertThat(label.isVisible());
		}
		
		String[] textInputArray = new String[] { "titleTextField",
				"authorTextField", "yearPublishTextField",
				"publisherTextField", "isbnTextField", "descriptionTextArea" };
						
		for (String l : textInputArray) {
			textField = panelLibrarian.getTextBox(l);
			assertNotNull(textField);
			assertThat(textField.isVisible());
			assertThat(textField.isEditable());
		}	

		assertEquals(book.getTitle(),
				panelLibrarian.getInputTextBox("titleTextField").getText());
		assertEquals(book.getAuthor(),
				panelLibrarian.getInputTextBox("authorTextField").getText());
		assertEquals("" + book.getYearPublish(),
				panelLibrarian.getInputTextBox("yearPublishTextField").getText());
		assertEquals(book.getPublisher(),
				panelLibrarian.getInputTextBox("publisherTextField").getText());
		assertEquals(book.getIsbn(), panelUser.getInputTextBox("isbnTextField")
				.getText());
		assertEquals(book.getDescription(),
				panelLibrarian.getInputTextBox("descriptionTextArea").getText());

		String[] buttonArray = new String[] { "Save", "Delete" };

		for (String s : buttonArray) {
			button = panelLibrarian.getButton(s);
			assertNotNull(button);
			assertThat(button.isEnabled());
			assertThat(button.isVisible());
		}
		
		assertNotNull(panelLibrarian.getSpinner());
		assertThat(panelLibrarian.getSpinner().isEnabled());
		assertThat(panelLibrarian.getSpinner().isVisible());
		
	}
}
