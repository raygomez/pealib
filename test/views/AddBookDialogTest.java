package views;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.TextBox;
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
		TextBox label;
		TextBox textField;
		Button button;

		String[] labelArray = new String[] { "Title:", "Author:",
				"Year Published:", "Publisher:", "ISBN:", "Description:",
				"Copies:" };

		for (String l : labelArray) {
			label = window.getTextBox(l);
			assertNotNull(label);
			assertThat(label.isVisible());
		}

		String[] textInputArray = new String[] { "titleTextField",
				"authorTextField", "yearPublishTextField",
				"publisherTextField", "isbnTextField", "descriptionTextArea" };

		for (String l : textInputArray) {
			textField = window.getTextBox(l);
			assertNotNull(textField);
			assertThat(textField.isVisible());
			assertEquals("", textField.getText());
		}

		String[] buttonArray = new String[] { "Add", "Cancel" };

		for (String s : buttonArray) {
			button = window.getButton(s);
			assertNotNull(button);
			assertThat(button.isEnabled());
			assertThat(button.isVisible());
		}

	}

}
