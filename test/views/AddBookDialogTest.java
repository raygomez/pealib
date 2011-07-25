package views;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;

public class AddBookDialogTest extends UISpecTestCase {

	Window window;
	
	@Before
	public void setUp() throws Exception {
		window = new Window(new AddBookDialog());
	}

	@Test
	public void testInitialTest() {
		assertThat(window.isModal());
		
		assertNotNull(window.getTextBox("Title:"));
		assertNotNull(window.getTextBox("Author:"));
		assertNotNull(window.getTextBox("Year Published:"));
		assertNotNull(window.getTextBox("Publisher:"));
		assertNotNull(window.getTextBox("ISBN:"));
		assertNotNull(window.getTextBox("Description:"));
		assertNotNull(window.getTextBox("Copies:"));
		
		assertNotNull(window.getInputTextBox("titleTextField"));
		assertNotNull(window.getInputTextBox("authorTextField"));
		assertNotNull(window.getInputTextBox("yearPublishTextField"));
		assertNotNull(window.getInputTextBox("publisherTextField"));
		assertNotNull(window.getInputTextBox("isbnTextField"));
		assertNotNull(window.getInputTextBox("descriptionTextArea"));

		assertEquals("", window.getInputTextBox("titleTextField").getText());
		assertEquals("", window.getInputTextBox("authorTextField").getText());
		assertEquals("", window.getInputTextBox("yearPublishTextField").getText());
		assertEquals("", window.getInputTextBox("publisherTextField").getText());
		assertEquals("", window.getInputTextBox("isbnTextField").getText());
		assertEquals("", window.getInputTextBox("descriptionTextArea").getText());

		assertNotNull(window.getButton("Add"));
		assertThat(window.getButton("Add").isEnabled());
		assertThat(window.getButton("Add").isVisible());
		assertNotNull(window.getButton("Cancel"));
		assertThat(window.getButton("Cancel").isEnabled());
		assertThat(window.getButton("Cancel").isVisible());
	}

}
