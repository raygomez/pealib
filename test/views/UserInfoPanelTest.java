package views;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;

import utilities.Constants;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserInfoPanelTest extends UISpecTestCase {

	private Panel panel;
	private UserInfoPanel userInfoPanel;

	private String accountType;
	private String idNumber;
	private String username;
	private String firstName;
	private String lastName;
	private String address;
	private String contactNumber;
	private String email;

	@Before
	public void setUp() throws Exception {
		userInfoPanel = new UserInfoPanel();
		panel = new Panel(userInfoPanel);

		accountType = "User";
		idNumber = "19216811";
		username = "jajalim";
		firstName = "Jaja";
		lastName = "Lim";
		address = "NetworkLabs Bldg, UP Ayala Technohub, Diliman, Q.C.";
		contactNumber = "09171234567";
		email = "jaja.lim@yahoo.com";

		userInfoPanel.setFields(accountType, idNumber, username, firstName,
				lastName, address, contactNumber, email);
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

		String[] errorLabels = { "firstNameError", "lastNameError",
				"contactNumberError", "emailError" };

		for (String s : errorLabels) {
			label = panel.getTextBox(s);
			assertNotNull(label);
			assertEquals("", label.getText());
			assertThat(label.isVisible());
		}
	}

	@Test
	public void testDisplayErrors() {
		TextBox label;

		int[] errors = { Constants.FIRST_NAME_FORMAT_ERROR,
				Constants.LAST_NAME_FORMAT_ERROR, Constants.EMAIL_FORMAT_ERROR,
				Constants.CONTACT_NUMBER_FORMAT_ERROR };

		userInfoPanel.displayErrors(errors);

		String[] errorMessages = { Constants.EMAIL_FORMAT_ERROR_MESSAGE,
				Constants.CONTACT_NUMBER_FORMAT_ERROR_MESSAGE };

		for (String s : errorMessages) {
			label = panel.getTextBox(s);
			assertNotNull(label);
			assertThat(label.isVisible());
		}

		label = panel.getTextBox("firstNameError");
		assertNotNull(label);
		assertEquals(Constants.NAME_FORMAT_ERROR_MESSAGE, label.getText());
		assertThat(label.isVisible());

		label = panel.getTextBox("lastNameError");
		assertNotNull(label);
		assertEquals(Constants.NAME_FORMAT_ERROR_MESSAGE, label.getText());
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

		String[] errorLabels = { "firstNameError", "lastNameError",
				"contactNumberError", "emailError" };

		for (String s : errorLabels) {
			label = panel.getTextBox(s);
			assertNotNull(label);
			assertEquals("", label.getText());
			assertThat(label.isVisible());
		}

	}

	@Test
	public void testGetAccountType() {
		String s = userInfoPanel.getAccountType();
		assertEquals(accountType, s);
	}

	@Test
	public void testGetIdNumber() {
		String s = userInfoPanel.getIdNumber();
		assertEquals(idNumber, s);
	}

	@Test
	public void testGetUsername() {
		String s = userInfoPanel.getUsername();
		assertEquals(username, s);
	}

	@Test
	public void testGetFirstName() {
		String s = userInfoPanel.getFirstName();
		assertEquals(firstName, s);
	}

	@Test
	public void testGetLastName() {
		String s = userInfoPanel.getLastName();
		assertEquals(lastName, s);
	}

	@Test
	public void testGetAddress() {
		String s = userInfoPanel.getAddress();
		assertEquals(address, s);
	}

	@Test
	public void testGetContactNumber() {
		String s = userInfoPanel.getContactNumber();
		assertEquals(contactNumber, s);
	}

	@Test
	public void testGetEmail() {
		String s = userInfoPanel.getEmail();
		assertEquals(email, s);
	}

	@Test
	public void testSetFields() {
		UserInfoPanel uip = new UserInfoPanel();
		Panel p = new Panel(uip);

		uip.setFields(accountType, idNumber, username, firstName, lastName,
				address, contactNumber, email);

		String[] fieldEntries = { accountType, idNumber, username, firstName,
				lastName, address, contactNumber, email };

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
