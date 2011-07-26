package views;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.PasswordField;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;

public class ChangePasswordDialogTest extends UISpecTestCase {

	private Window window;
	PasswordField oldpassword;
	PasswordField newpassword;
	PasswordField repeatpassword;

	@Before
	public void setUp() throws Exception {
		window = new Window(new ChangePasswordDialog());

		oldpassword = window.getPasswordField("oldpassword");
		newpassword = window.getPasswordField("newpassword");
		repeatpassword = window.getPasswordField("repeatpassword");

	}

	@Test
	public void testInitialState() {
		assertThat(oldpassword.isEnabled());
		assertThat(newpassword.isEnabled());
		assertThat(repeatpassword.isEnabled());
		assertThat(window.getButton("Change Password").isEnabled());
		assertThat(window.getButton("Cancel").isEnabled());
	}

}
