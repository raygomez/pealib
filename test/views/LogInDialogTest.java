package views;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;

public class LogInDialogTest extends UISpecTestCase {

	Window window;

	@Before
	public void setUp() {
		window = new Window(new LogInDialog());

	}

	@Test
	public void testInitialState() {
		Button login = window.getButton("Log in");
		assertNotNull(login);
		Button signup = window.getButton("Sign up");
		assertNotNull(signup);
		assertEquals("Username",window.getTextBox("lblUsername").getText());
		assertEquals("Password",window.getTextBox("lblPassword").getText());
		assertThat(window.titleEquals("Log In"));	
		
		TextBox username = window.getInputTextBox("username");
		assertNotNull(username);
		PasswordField password = window.getPasswordField("password");
		assertNotNull(password);
		
		assertEquals("", window.getTextBox("labelError").getText());
		
		assertTrue(window.isModal());
	}
	/*
	@Test
	public void testInvalidInput(){		
		Button button = window.getButton("Log In");
		button.click();
		assertEquals("Incomplete fields", window.getTextBox("labelError").getText());
	}
	*/

}
