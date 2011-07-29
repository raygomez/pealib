package controllers;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.awt.Color;

import javax.swing.BorderFactory;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.Key;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "../models/user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AuthenticationControllerTest extends UISpecTestCase {

	Window window;

	@Before
	public void setUp() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		new AuthenticationController();
		window = new Window(AuthenticationController.getLogin());

	}

	@Test
	@ExpectedDataSet({ "../models/expected/saveUser.xml" })
	public void testSignUpSuccessful() throws Exception {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				window.getButton("Sign Up").click();
			}
		}).process(new WindowHandler() {
			public Trigger process(final Window dialog) {
				dialog.getInputTextBox("firstNameTextField").setText(
						"Janine June");
				dialog.getInputTextBox("lastNameTextField").setText("Lim");
				dialog.getInputTextBox("userNameTextField").setText("jlim");
				dialog.getPasswordField("passwordTextField").setPassword(
						"1234567");
				dialog.getPasswordField("confirmPasswordTextField")
						.setPassword("1234567");
				dialog.getInputTextBox("emailAddressTextField").setText(
						"jlim@gmail.com");
				dialog.getInputTextBox("contactNumberTextField").setText(
						"1234567890");
				dialog.getInputTextBox("addressTextField").setText("USA");
				return new Trigger() {
					@Override
					public void run() throws Exception {
						dialog.getInputTextBox("addressTextField").releaseKey(
								Key.ENTER);
					}

				};
			}
		}).processTransientWindow().process(new WindowHandler() {
			public Trigger process(Window dialog) {
				String actual = dialog.getTextBox("OptionPane.label").getText();
				String expected = "<html>Your account has been created."
						+ "<br>Please wait for the Librarian to activate "
						+ "your account.";
				assertTrue(actual.equals(expected));
				return dialog.getButton("OK").triggerClick();
			}
		}).run();
	}

	@Test
	public void testEmptyFields() {
		TextBox labelError = window.getTextBox("labelError");
		Button login = window.getButton("Log In");
		login.click();

		// assertReflectionEquals(
		// BorderFactory.createMatteBorder(1, 1, 2, 1,
		// Color.getHSBColor((float) 0.0, (float) 0.6, (float) 1)),
		// username.getAwtComponent().getBorder());
		// assertReflectionEquals(
		// BorderFactory.createMatteBorder(1, 1, 2, 1,
		// Color.getHSBColor((float) 0.0, (float) 0.6, (float) 1)),
		// password.getAwtComponent().getBorder());
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
	public void testWrongPassword() throws InterruptedException {
		TextBox username = window.getInputTextBox("username");
		PasswordField password = window.getPasswordField("password");
		TextBox labelError = window.getTextBox("labelError");
		username.setText("jvillar");
		password.setPassword("1234567");
		Button login = window.getButton("Log In");
		login.click();

		Thread.sleep(1000);

		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				username.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				username.getAwtComponent().getBorder());

		assertEquals("Username/Password Mismatch", labelError.getText());
	}

	@Test
	public void testPendingUser() throws InterruptedException {
		TextBox username = window.getInputTextBox("username");
		PasswordField password = window.getPasswordField("password");
		TextBox labelError = window.getTextBox("labelError");
		username.setText("rdgomez");
		password.setPassword("123456");
		Button login = window.getButton("Log In");
		login.click();

		Thread.sleep(1000);

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

	@Test
	public void testLoginSuccessful() throws Exception {
		TextBox username = window.getInputTextBox("username");
		PasswordField password = window.getPasswordField("password");
		TextBox labelError = window.getTextBox("labelError");
		username.setText("jvillar");
		password.setPassword("123456");
		final Button login = window.getButton("Log In");

		WindowInterceptor.init(new Trigger() {
			public void run() {
				login.click();
			}
		}).processTransientWindow().run();

		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				username.getAwtComponent().getBorder());
		assertReflectionEquals(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray),
				username.getAwtComponent().getBorder());
		assertEquals("", labelError.getText());
	}

	@Test
	public void testSignUpEmptyFields() throws Exception {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				window.getButton("Sign Up").click();
			}
		}).process(new WindowHandler("New User Account Application") {
			public Trigger process(Window dialog) {
				dialog.getInputTextBox("userNameTextField").releaseKey(
						Key.ENTER);
				String error = dialog.getTextBox("errorMessageLabel").getText();
				assertTrue(error.equals("Cannot leave mandatory fields empty."));
				return dialog.getButton("Cancel").triggerClick();
			}
		}).run();
	}

	@Test
	public void testSignUpNotUniqueUsername() throws Exception {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				window.getButton("Sign Up").click();
			}
		}).process(new WindowHandler("New User Account Application") {
			public Trigger process(Window dialog) {
				dialog.getInputTextBox("firstNameTextField").setText("1");
				dialog.getInputTextBox("lastNameTextField").setText("2");
				dialog.getPasswordField("passwordTextField").setPassword(
						"12345");
				dialog.getPasswordField("confirmPasswordTextField")
						.setPassword("12345");
				dialog.getInputTextBox("emailAddressTextField").setText(
						"####@gmail.com");
				dialog.getInputTextBox("contactNumberTextField").setText(
						"123456");
				dialog.getInputTextBox("addressTextField").setText("USA");

				dialog.getInputTextBox("userNameTextField").setText(
						"apantaleon");
				dialog.getButton("Submit").click();

				String error = dialog.getTextBox("errorMessageLabel").getText();
				assertTrue(error.equals("User name is already in use."));
				return dialog.getButton("Cancel").triggerClick();
			}
//		}).process(new WindowHandler() {
//			public Trigger process(Window dialog) {
//				String actual = dialog.getTextBox("OptionPane.label").getText();
//				String expected = "<html>An error was encountered while"
//						+ " creating your account.<br>"
//						+ "Please try again later.";
//				assertTrue(actual.equals(expected));
//				return dialog.getButton("OK").triggerClick();
//			}
		}).run();			
	}
	
	@Test
	public void testSignUpErrorWhileCheckingUsername() throws Exception {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				window.getButton("Sign Up").click();
			}
		}).process(new WindowHandler("New User Account Application") {
			public Trigger process(Window dialog) {
				dialog.getInputTextBox("firstNameTextField").setText("1");
				dialog.getInputTextBox("lastNameTextField").setText("2");
				dialog.getPasswordField("passwordTextField").setPassword(
						"12345");
				dialog.getPasswordField("confirmPasswordTextField")
						.setPassword("12345");
				dialog.getInputTextBox("emailAddressTextField").setText(
						"####@gmail.com");
				dialog.getInputTextBox("contactNumberTextField").setText(
						"123456");
				dialog.getInputTextBox("addressTextField").setText("USA");

				dialog.getInputTextBox("userNameTextField").setText(
						"apantaleon");
				/* invoke error in connection */
				Connector.init("build.xml");
				dialog.getButton("Submit").click();
				return dialog.getButton("Cancel").triggerClick();
			}
		}).process(new WindowHandler() {
			public Trigger process(Window dialog) {
				String actual = dialog.getTextBox("OptionPane.label").getText();
				String expected = "<html>An error was encountered while"
						+ " creating your account.<br>"
						+ "Please try again later.";
				assertTrue(actual.equals(expected));
				return dialog.getButton("OK").triggerClick();
			}
		}).run();
	}

	@Test
	public void testSignUpInvalidEmailCharacters() throws Exception {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				window.getButton("Sign Up").click();
			}
		}).process(new WindowHandler("New User Account Application") {
			public Trigger process(Window dialog) {
				dialog.getInputTextBox("firstNameTextField").setText("1");
				dialog.getInputTextBox("lastNameTextField").setText("2");
				dialog.getInputTextBox("userNameTextField").setText("abcd");
				dialog.getPasswordField("passwordTextField").setPassword(
						"12345");
				dialog.getPasswordField("confirmPasswordTextField")
						.setPassword("12345");
				dialog.getInputTextBox("contactNumberTextField").setText(
						"123456");
				dialog.getInputTextBox("addressTextField").setText("USA");

				dialog.getInputTextBox("emailAddressTextField").setText(
						"####@gmail.com");
				dialog.getButton("Submit").click();

				String error = dialog.getTextBox("errorMessageLabel").getText();
				assertTrue(error.equals("Invalid Input."));
				return dialog.getButton("Cancel").triggerClick();
			}
		}).run();
	}

	@Test
	public void testSignUpInvalidEmailLength() throws Exception {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				window.getButton("Sign Up").click();
			}
		}).process(new WindowHandler("New User Account Application") {
			public Trigger process(Window dialog) {
				dialog.getInputTextBox("firstNameTextField").setText("1");
				dialog.getInputTextBox("lastNameTextField").setText("2");
				dialog.getInputTextBox("userNameTextField").setText("abcd");
				dialog.getPasswordField("passwordTextField").setPassword(
						"12345");
				dialog.getPasswordField("confirmPasswordTextField")
						.setPassword("12345");
				dialog.getInputTextBox("contactNumberTextField").setText(
						"123456");
				dialog.getInputTextBox("addressTextField").setText("USA");

				dialog.getInputTextBox("emailAddressTextField").setText(
						"aaaaaaaaaaaaaaaaaaaaa@gmail.com");
				dialog.getButton("Submit").click();

				String error = dialog.getTextBox("errorMessageLabel").getText();
				assertTrue(error.equals("Invalid Input."));
				return dialog.getButton("Cancel").triggerClick();
			}
		}).run();
	}

	@Test
	public void testSignUpNotUniqueEmail() throws Exception {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				window.getButton("Sign Up").click();
			}
		}).process(new WindowHandler("New User Account Application") {
			public Trigger process(Window dialog) {
				dialog.getInputTextBox("firstNameTextField").setText("1");
				dialog.getInputTextBox("lastNameTextField").setText("2");
				dialog.getInputTextBox("userNameTextField").setText("abcd");
				dialog.getPasswordField("passwordTextField").setPassword(
						"12345");
				dialog.getPasswordField("confirmPasswordTextField")
						.setPassword("12345");
				dialog.getInputTextBox("contactNumberTextField").setText(
						"123456");
				dialog.getInputTextBox("addressTextField").setText("USA");

				dialog.getInputTextBox("emailAddressTextField").setText(
						"apantaleon@gmail.com");
				dialog.getInputTextBox("emailAddressTextField").releaseKey(
						Key.ENTER);

				String error = dialog.getTextBox("errorMessageLabel").getText();
				assertTrue(error.equals("E-mail address is already in use."));
				return dialog.getButton("Cancel").triggerClick();
			}
		}).run();
	}

	@Test
	public void testSignUpInvalidFields() throws Exception {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				window.getButton("Sign Up").click();
			}
		}).process(new WindowHandler("New User Account Application") {
			public Trigger process(Window dialog) {
				TextBox firstNameTextField = dialog
						.getInputTextBox("firstNameTextField");
				TextBox lastNameTextField = dialog
						.getInputTextBox("lastNameTextField");
				TextBox userNameTextField = dialog
						.getInputTextBox("userNameTextField");
				PasswordField passwordTextField = dialog
						.getPasswordField("passwordTextField");
				PasswordField confirmPasswordTextField = dialog
						.getPasswordField("confirmPasswordTextField");
				TextBox emailAddressTextField = dialog
						.getInputTextBox("emailAddressTextField");
				TextBox contactNumberTextField = dialog
						.getInputTextBox("contactNumberTextField");
				TextBox addressTextField = dialog
						.getInputTextBox("addressTextField");

				firstNameTextField.setText("1");
				lastNameTextField.setText("2");
				userNameTextField.setText("abcd");
				passwordTextField.setPassword("12345");
				confirmPasswordTextField.setPassword("12345");
				emailAddressTextField.setText("jlim@gmail.com");
				contactNumberTextField.setText("123456");
				addressTextField.setText("A");

				dialog.getButton("Submit").click();

				String error = dialog.getTextBox("errorMessageLabel").getText();
				assertTrue(error.equals("Invalid Input."));

				assertTrue(firstNameTextField.getText().isEmpty());
				assertTrue(lastNameTextField.getText().isEmpty());
				assertTrue(passwordTextField.passwordEquals(""));
				assertTrue(confirmPasswordTextField.passwordEquals(""));
				assertTrue(contactNumberTextField.getText().isEmpty());
				assertTrue(addressTextField.getText().isEmpty());
				return dialog.getButton("Cancel").triggerClick();
			}
		}).run();
	}

	@Test
	public void testSignUpPasswordMismatch() throws Exception {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				window.getButton("Sign Up").click();
			}
		}).process(new WindowHandler("New User Account Application") {
			public Trigger process(Window dialog) {
				dialog.getInputTextBox("firstNameTextField").setText(
						"Janine June");
				dialog.getInputTextBox("lastNameTextField").setText("Lim");
				dialog.getInputTextBox("userNameTextField").setText("jlim");
				dialog.getPasswordField("passwordTextField").setPassword(
						"1234567");
				dialog.getPasswordField("confirmPasswordTextField")
						.setPassword("123456789");
				dialog.getInputTextBox("emailAddressTextField").setText(
						"jlim@gmail.com");
				dialog.getInputTextBox("contactNumberTextField").setText(
						"1234567890");
				dialog.getInputTextBox("addressTextField").setText("USA");

				dialog.getButton("Submit").click();

				String error = dialog.getTextBox("errorMessageLabel").getText();
				assertTrue(error.equals("Mismatch in Confirm Password."));

				assertTrue(dialog.getPasswordField("passwordTextField")
						.passwordEquals(""));
				assertTrue(dialog.getPasswordField("confirmPasswordTextField")
						.passwordEquals(""));
				return dialog.getButton("Cancel").triggerClick();
			}
		}).run();
	}

	@Test
	public void testSignUpInvalidUsername() throws Exception {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				window.getButton("Sign Up").click();
			}
		}).process(new WindowHandler("New User Account Application") {
			public Trigger process(Window dialog) {
				dialog.getInputTextBox("firstNameTextField").setText("1");
				dialog.getInputTextBox("lastNameTextField").setText("2");
				dialog.getPasswordField("passwordTextField").setPassword(
						"12345");
				dialog.getPasswordField("confirmPasswordTextField")
						.setPassword("12345");
				dialog.getInputTextBox("emailAddressTextField").setText(
						"####@gmail.com");
				dialog.getInputTextBox("contactNumberTextField").setText(
						"123456");
				dialog.getInputTextBox("addressTextField").setText("USA");

				dialog.getInputTextBox("userNameTextField").setText("ab");
				dialog.getInputTextBox("userNameTextField").releaseKey(Key.C);
				dialog.getInputTextBox("userNameTextField").releaseKey(
						Key.ENTER);
				String error = dialog.getTextBox("errorMessageLabel").getText();
				assertTrue(error.equals("Invalid Input."));
				return dialog.getButton("Cancel").triggerClick();
			}
		}).run();
	}

}
