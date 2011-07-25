package views;

import models.Book;
import models.BookDAO;
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
		assertNotNull(panelUser.getTextBox("Title:"));
		assertNotNull(panelUser.getTextBox("Author:"));
		assertNotNull(panelUser.getTextBox("Year Published:"));
		assertNotNull(panelUser.getTextBox("Publisher:"));
		assertNotNull(panelUser.getTextBox("ISBN:"));
		assertNotNull(panelUser.getTextBox("Description:"));
		assertNotNull(panelUser.getTextBox("Copies:"));

		assertNotNull(panelUser.getInputTextBox("titleTextField"));
		assertNotNull(panelUser.getInputTextBox("authorTextField"));
		assertNotNull(panelUser.getInputTextBox("yearPublishTextField"));
		assertNotNull(panelUser.getInputTextBox("publisherTextField"));
		assertNotNull(panelUser.getInputTextBox("isbnTextField"));
		assertNotNull(panelUser.getInputTextBox("descriptionTextArea"));
		
		assertFalse(panelUser.getInputTextBox("titleTextField").isEditable());
		assertFalse(panelUser.getInputTextBox("authorTextField").isEditable());
		assertFalse(panelUser.getInputTextBox("yearPublishTextField").isEditable());
		assertFalse(panelUser.getInputTextBox("publisherTextField").isEditable());
		assertFalse(panelUser.getInputTextBox("isbnTextField").isEditable());
		assertFalse(panelUser.getInputTextBox("descriptionTextArea").isEditable());

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
		
		assertNotNull(panelUser.getButton("Borrow"));
		assertThat(panelUser.getButton("Borrow").isEnabled());
		assertThat(panelUser.getButton("Borrow").isVisible());
		assertNotNull(panelUser.getButton("Reserve"));
		assertThat(panelUser.getButton("Reserve").isEnabled());
		assertThat(panelUser.getButton("Reserve").isVisible());
	}
	
	@Test
	public void testInitialStateLibrarian() {
		assertNotNull(panelLibrarian.getTextBox("Title:"));
		assertNotNull(panelLibrarian.getTextBox("Author:"));
		assertNotNull(panelLibrarian.getTextBox("Year Published:"));
		assertNotNull(panelLibrarian.getTextBox("Publisher:"));
		assertNotNull(panelLibrarian.getTextBox("ISBN:"));
		assertNotNull(panelLibrarian.getTextBox("Description:"));
		assertNotNull(panelLibrarian.getTextBox("Copies:"));

		assertNotNull(panelLibrarian.getInputTextBox("titleTextField"));
		assertNotNull(panelLibrarian.getInputTextBox("authorTextField"));
		assertNotNull(panelLibrarian.getInputTextBox("yearPublishTextField"));
		assertNotNull(panelLibrarian.getInputTextBox("publisherTextField"));
		assertNotNull(panelLibrarian.getInputTextBox("isbnTextField"));
		assertNotNull(panelLibrarian.getInputTextBox("descriptionTextArea"));

		assertThat(panelLibrarian.getInputTextBox("titleTextField").isEditable());
		assertThat(panelLibrarian.getInputTextBox("authorTextField").isEditable());
		assertThat(panelLibrarian.getInputTextBox("yearPublishTextField").isEditable());
		assertThat(panelLibrarian.getInputTextBox("publisherTextField").isEditable());
		assertThat(panelLibrarian.getInputTextBox("isbnTextField").isEditable());
		assertThat(panelLibrarian.getInputTextBox("descriptionTextArea").isEditable());

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
		
		assertNotNull(panelLibrarian.getButton("Save"));
		assertThat(panelLibrarian.getButton("Save").isEnabled());
		assertThat(panelLibrarian.getButton("Save").isVisible());
		assertNotNull(panelLibrarian.getButton("Delete"));
		assertThat(panelLibrarian.getButton("Delete").isEnabled());
		assertThat(panelLibrarian.getButton("Delete").isVisible());
		
		assertNotNull(panelLibrarian.getSpinner());
		assertThat(panelLibrarian.getSpinner().isEnabled());
		assertThat(panelLibrarian.getSpinner().isVisible());
		
	}
}
