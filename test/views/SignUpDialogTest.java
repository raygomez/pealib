package views;

import java.awt.Color;

import javax.swing.BorderFactory;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
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

		TextBox label;
		TextBox textField;
		Button button;
		PasswordField password;

		String[] labelArray = new String[] { "Please Fill-in.", "First Name:",
				"Last Name:", "User Name:", "Password:", "Confirm Password:",
				"E-mail Address:", "Contact Number:", "Address:" };

		for (String l : labelArray) {
			label = window.getTextBox(l);
			assertNotNull(label);
			assertThat(label.isVisible());
		}

		String[] textInputArray = new String[] { "firstNameTextField",
				"lastNameTextField", "userNameTextField",
				"emailAddressTextField", "contactNumberTextField",
				"addressTextField" };

		for (String l : textInputArray) {
			textField = window.getTextBox(l);
			assertNotNull(textField);
			assertThat(textField.isVisible());
			assertEquals("", textField.getText());
			assertReflectionEquals(
					BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
					textField.getAwtComponent().getBorder());
		}

		String[] buttonArray = new String[] { "Submit", "Cancel" };

		for (String s : buttonArray) {
			button = window.getButton(s);
			assertNotNull(button);
			assertThat(button.isEnabled());
			assertThat(button.isVisible());
		}

		String[] passwordFieldArray = new String[] { "passwordTextField",
				"confirmPasswordTextField" };

		for (String l : passwordFieldArray) {
			password = window.getPasswordField(l);
			assertNotNull(password);
			assertThat(password.isVisible());
			assertReflectionEquals(
					BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
					password.getAwtComponent().getBorder());
		}
	}
}
