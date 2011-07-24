package controllers;

import java.awt.Color;

import javax.swing.BorderFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.Constants;
import static org.unitils.reflectionassert.ReflectionAssert.*;

@DataSet({ "../models/user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AuthenticationControllerTest extends UISpecTestCase {

	Window window;

	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
		new AuthenticationController();
		window = new Window(AuthenticationController.getLogin());

	}

	@Test
	public void testEmptyFields() {
		TextBox username = window.getInputTextBox("username");
		PasswordField password = window.getPasswordField("password");
		TextBox labelError = window.getTextBox("labelError");
		Button login = window.getButton("Log In");
		login.click();
		
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 2, 1,
						Color.getHSBColor((float) 0.0, (float) 0.6, (float) 1)),
				username.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 2, 1,
						Color.getHSBColor((float) 0.0, (float) 0.6, (float) 1)),
				password.getAwtComponent().getBorder());
		assertEquals("Incomplete fields", labelError.getText());
	}

	@Test
	public void testEmptyPassword() {
		TextBox username = window.getInputTextBox("username");
		PasswordField password = window.getPasswordField("password");
		TextBox labelError = window.getTextBox("labelError");
		username.setText("jvillar");
		Button login = window.getButton("Log In");
		login.click();

		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				username.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 2, 1,
						Color.getHSBColor((float) 0.0, (float) 0.6, (float) 1)),
				password.getAwtComponent().getBorder());
		assertEquals("Incomplete fields", labelError.getText());
	}

	@Test
	public void testEmptyUsername() {
		TextBox username = window.getInputTextBox("username");
		PasswordField password = window.getPasswordField("password");
		TextBox labelError = window.getTextBox("labelError");
		password.setPassword("123456");
		Button login = window.getButton("Log In");
		login.click();

		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 2, 1,
						Color.getHSBColor((float) 0.0, (float) 0.6, (float) 1)),
				username.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				password.getAwtComponent().getBorder());

		assertEquals("Incomplete fields", labelError.getText());
	}

	@Test
	public void testInvalidUsername() {
		TextBox username = window.getInputTextBox("username");
		PasswordField password = window.getPasswordField("password");
		TextBox labelError = window.getTextBox("labelError");
		username.setText("1");
		password.setPassword("123456");
		Button login = window.getButton("Log In");
		login.click();

		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 2, 1,
						Color.getHSBColor((float) 0.0, (float) 0.6, (float) 1)),
				username.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				password.getAwtComponent().getBorder());

		assertEquals("Invalid input", labelError.getText());
	}

	@Test
	public void testInvalidPassword() {
		TextBox username = window.getInputTextBox("username");
		PasswordField password = window.getPasswordField("password");
		TextBox labelError = window.getTextBox("labelError");
		username.setText("jvillar");
		password.setPassword("1");
		Button login = window.getButton("Log In");
		login.click();

		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				username.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 2, 1,
						Color.getHSBColor((float) 0.0, (float) 0.6, (float) 1)),
				password.getAwtComponent().getBorder());

		assertEquals("Invalid input", labelError.getText());
	}

	@Test
	public void testWrongPassword() {
		TextBox username = window.getInputTextBox("username");
		PasswordField password = window.getPasswordField("password");
		TextBox labelError = window.getTextBox("labelError");
		username.setText("jvillar");
		password.setPassword("1234567");
		Button login = window.getButton("Log In");
		login.click();

		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				username.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				username.getAwtComponent().getBorder());
		assertEquals("Username/Password Mismatch", labelError.getText());
	}

	@Test
	public void testPendingUser() {
		TextBox username = window.getInputTextBox("username");
		PasswordField password = window.getPasswordField("password");
		TextBox labelError = window.getTextBox("labelError");
		username.setText("rdgomez");
		password.setPassword("123456");
		Button login = window.getButton("Log In");
		login.click();

		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				username.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				username.getAwtComponent().getBorder());
		assertEquals("<html><center>Account still being processed."
				+ "<br/>Ask Librarian for further inquiries.</center></html>",
				labelError.getText());
	}

}
