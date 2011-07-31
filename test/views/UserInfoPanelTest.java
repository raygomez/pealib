package views;

import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "../models/user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserInfoPanelTest extends UISpecTestCase {

	private Panel panel;
	private UserInfoPanel userInfoPanel;
	private User user;

	@Before
	public void setUp() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		userInfoPanel = new UserInfoPanel();
		panel = new Panel(userInfoPanel);
		user = UserDAO.getUserById(2);
		userInfoPanel.setFields(user);
	}

	@Test
	public void testInitialState() {
		TextBox label;
		TextBox textField;
		Button button;

		Panel panel = new Panel(new UserInfoPanel());

		String[] buttonArray = { "Save Changes", "Change Password" };

		for (String s : buttonArray) {
			button = panel.getButton(s);
			assertNotNull(button);
			assertThat(button.isEnabled());
			assertThat(button.isVisible());
		}

		String[] labelArray = { "Account Type", "User ID", "Username",
				"First Name", "Last Name", "Address", "Contact Number", "Email" };

		for (String s : labelArray) {
			label = panel.getTextBox(s);
			assertNotNull(label);
			assertThat(label.isVisible());
		}

		String[] fieldNameArray = { "accountType", "idNumber", "username",
				"firstName", "lastName", "contactNumber", "email" };

		for (String s : fieldNameArray) {
			textField = panel.getInputTextBox(s);
			assertNotNull(textField);
			assertEquals("", textField.getText());
			assertThat(textField.isVisible());
		}

		String[] disabledFields = { "accountType", "idNumber", "username" };

		for (String s : disabledFields) {
			textField = panel.getInputTextBox(s);
			assertFalse(textField.isEnabled());
		}

		String[] enabledFields = { "firstName", "lastName", "contactNumber",
				"email" };
		for (String s : enabledFields) {
			textField = panel.getInputTextBox(s);
			assertThat(textField.isEnabled());
		}

		label = panel.getTextBox("lblError");
		assertNotNull(label);
		assertEquals(" ", label.getText());
		assertThat(label.isVisible());
	}

	@Test
	public void testDisplayErrors() {
		TextBox label;

		int[] errors = { Constants.FIRST_NAME_FORMAT_ERROR,
				Constants.LAST_NAME_FORMAT_ERROR, Constants.EMAIL_FORMAT_ERROR,
				Constants.CONTACT_NUMBER_FORMAT_ERROR };

		userInfoPanel.displayErrors(errors);

		label = panel.getTextBox("lblError");
		assertNotNull(label);
		assertThat(label.isVisible());

		label = panel.getTextBox("lblError");
		assertNotNull(label);
		assertEquals("Invalid Input", label.getText());
		assertThat(label.isVisible());
	}

	@Test
	public void testResetErrorMessages() {
		TextBox label;

		int[] errors = { Constants.FIRST_NAME_FORMAT_ERROR,
				Constants.LAST_NAME_FORMAT_ERROR, Constants.EMAIL_FORMAT_ERROR,
				Constants.CONTACT_NUMBER_FORMAT_ERROR };

		userInfoPanel.displayErrors(errors);
		userInfoPanel.resetErrorMessages();
		
		label = panel.getTextBox("lblError");
		assertNotNull(label);
		assertEquals(" ", label.getText());
		assertThat(label.isVisible());
	}

	@Test
	public void testGetAccountType() {
		String s = userInfoPanel.getAccountType();
		assertEquals(user.getType(), s);
	}

	@Test
	public void testGetIdNumber() {
		String s = userInfoPanel.getIdNumber();
		assertEquals(user.getUserId(), Integer.parseInt(s));
	}

	@Test
	public void testGetUsername() {
		String s = userInfoPanel.getUsername();
		assertEquals(user.getUserName(), s);
	}

	@Test
	public void testGetFirstName() {
		String s = userInfoPanel.getFirstName();
		assertEquals(user.getFirstName(), s);
	}

	@Test
	public void testGetLastName() {
		String s = userInfoPanel.getLastName();
		assertEquals(user.getLastName(), s);
	}

	@Test
	public void testGetAddress() {
		String s = userInfoPanel.getAddress();
		assertEquals(user.getAddress(), s);
	}

	@Test
	public void testGetContactNumber() {
		String s = userInfoPanel.getContactNumber();
		assertEquals(user.getContactNo(), s);
	}

	@Test
	public void testGetEmail() {
		String s = userInfoPanel.getEmail();
		assertEquals(user.getEmail(), s);
	}

	@Test
	public void testSetFields() {
		UserInfoPanel uip = new UserInfoPanel();
		Panel p = new Panel(uip);

		uip.setFields(user);
//		uip.setFields(user.getType(), user.getUserId()+"", user.getUserName(), user.getFirstName(), user.getLastName(),
//				user.getAddress(), user.getContactNo(), user.getEmail());

		String[] fieldEntries = { user.getType(), user.getUserId()+"", user.getUserName(), user.getFirstName(), user.getLastName(),
				user.getAddress(), user.getContactNo(), user.getEmail() };

		for (String s : fieldEntries) {
			TextBox tb = p.getInputTextBox(s);
			assertNotNull(tb);
		}
	}

	@Test
	public void testSetFirstNameEnabled() {
		userInfoPanel.setFirstNameEnabled(true);
		TextBox tb = panel.getInputTextBox("firstName");
		assertThat(tb.isEnabled());
	}

	@Test
	public void testSetFirstNameEnabled2() {
		userInfoPanel.setFirstNameEnabled(false);
		TextBox tb = panel.getInputTextBox("firstName");
		assertFalse(tb.isEnabled());
	}

	@Test
	public void testSetLastNameEnabled() {
		userInfoPanel.setLastNameEnabled(true);
		TextBox tb = panel.getInputTextBox("lastName");
		assertThat(tb.isEnabled());
	}

	@Test
	public void testSetLastNameEnabled2() {
		userInfoPanel.setLastNameEnabled(false);
		TextBox tb = panel.getInputTextBox("lastName");
		assertFalse(tb.isEnabled());
	}

}
