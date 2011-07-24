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
import static org.unitils.reflectionassert.ReflectionAssert.*;

@DataSet({ "../models/user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class AuthenticationControllerTest extends UISpecTestCase {

	Window window;
	TextBox username;
	PasswordField password;
	TextBox labelError;

	@Before
	public void setUp() throws Exception {
		new AuthenticationController();
		window = new Window(AuthenticationController.getLogin());
		username = window.getInputTextBox("username");
		password = window.getPasswordField("password");
		labelError = window.getTextBox("labelError");

	}

	@Test
	public void testEmptyFields() {
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
}
