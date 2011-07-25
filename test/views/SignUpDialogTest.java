package views;

import java.awt.Color;

import javax.swing.BorderFactory;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;

import static org.unitils.reflectionassert.ReflectionAssert.*;

public class SignUpDialogTest extends UISpecTestCase {

	Window window;

	@Before
	public void setUp() throws Exception {
		window = new Window(new SignUpDialog());
	}

	@Test
	public void testInitialState() {
		assertThat(window.isModal());
		assertNotNull(window.getTextBox("Please Fill-in."));
		assertNotNull(window.getTextBox("First Name:"));
		assertNotNull(window.getTextBox("Last Name:"));
		assertNotNull(window.getTextBox("User Name:"));
		assertNotNull(window.getTextBox("Password:"));
		assertNotNull(window.getTextBox("Confirm Password:"));
		assertNotNull(window.getTextBox("E-mail Address:"));
		assertNotNull(window.getTextBox("Contact Number:"));
		assertNotNull(window.getTextBox("Address:"));

		assertNotNull(window.getInputTextBox("txtfldFirstName"));
		assertNotNull(window.getInputTextBox("txtfldLastName"));
		assertNotNull(window.getInputTextBox("txtfldUserName"));
		assertNotNull(window.getPasswordField("txtfldPassword"));
		assertNotNull(window.getPasswordField("txtfldConfirmPassword"));
		assertNotNull(window.getInputTextBox("txtfldEmailAddress"));
		assertNotNull(window.getInputTextBox("txtfldContactNumber"));
		assertNotNull(window.getInputTextBox("txtfldAddress"));

		assertEquals("", window.getInputTextBox("txtfldFirstName").getText());
		assertEquals("", window.getInputTextBox("txtfldLastName").getText());
		assertEquals("", window.getInputTextBox("txtfldUserName").getText());
		assertEquals("", window.getInputTextBox("txtfldEmailAddress").getText());
		assertEquals("", window.getInputTextBox("txtfldContactNumber")
				.getText());
		assertEquals("", window.getInputTextBox("txtfldAddress").getText());

		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("txtfldFirstName").getAwtComponent()
						.getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("txtfldLastName").getAwtComponent()
						.getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("txtfldUserName").getAwtComponent()
						.getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("txtfldEmailAddress")
						.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("txtfldContactNumber")
						.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("txtfldAddress").getAwtComponent()
						.getBorder());

		assertNotNull(window.getButton("Submit"));
		assertThat(window.getButton("Submit").isEnabled());
		assertThat(window.getButton("Submit").isVisible());
		assertNotNull(window.getButton("Cancel"));
		assertThat(window.getButton("Cancel").isEnabled());
		assertThat(window.getButton("Cancel").isVisible());

	}

}
