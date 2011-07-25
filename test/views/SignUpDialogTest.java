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

		assertNotNull(window.getInputTextBox("firstNameTextField"));
		assertNotNull(window.getInputTextBox("lastNameTextField"));
		assertNotNull(window.getInputTextBox("userNameTextField"));
		assertNotNull(window.getPasswordField("passwordTextField"));
		assertNotNull(window.getPasswordField("confirmPasswordTextField"));
		assertNotNull(window.getInputTextBox("emailAddressTextField"));
		assertNotNull(window.getInputTextBox("contactNumberTextField"));
		assertNotNull(window.getInputTextBox("addressTextField"));

		assertEquals("", window.getInputTextBox("firstNameTextField").getText());
		assertEquals("", window.getInputTextBox("lastNameTextField").getText());
		assertEquals("", window.getInputTextBox("userNameTextField").getText());
		assertEquals("", window.getInputTextBox("emailAddressTextField")
				.getText());
		assertEquals("", window.getInputTextBox("contactNumberTextField")
				.getText());
		assertEquals("", window.getInputTextBox("addressTextField").getText());

		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("firstNameTextField")
						.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("lastNameTextField").getAwtComponent()
						.getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("userNameTextField").getAwtComponent()
						.getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getPasswordField("passwordTextField")
						.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getPasswordField("confirmPasswordTextField")
						.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("addressTextField").getAwtComponent()
						.getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("contactNumberTextField")
						.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray), window
						.getInputTextBox("emailAddressTextField")
						.getAwtComponent().getBorder());

		assertNotNull(window.getButton("Submit"));
		assertThat(window.getButton("Submit").isEnabled());
		assertThat(window.getButton("Submit").isVisible());
		assertNotNull(window.getButton("Cancel"));
		assertThat(window.getButton("Cancel").isEnabled());
		assertThat(window.getButton("Cancel").isVisible());

	}

}
