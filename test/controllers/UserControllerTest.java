package controllers;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.CheckBox;
import org.uispec4j.Key;
import org.uispec4j.Panel;
import org.uispec4j.TabGroup;
import org.uispec4j.Table;
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
import utilities.Emailer;

@DataSet({ "../models/user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserControllerTest extends UISpecTestCase {

	UserController userControl;
	UserController userControlOrdinary;
	Panel userInfoPanel;
	Panel userInfoPanelOrdinary;
	Panel userSearchPanel;
	TabGroup tabGroup;
	
	@Before
	public void setUp() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		Emailer.setOn(false);

		// librarian account
		User user = UserDAO.getUserById(2);
		userControl = new UserController(user);

		userInfoPanel = new Panel(userControl.getUserInfoPanel());
		assertNotNull(userInfoPanel);
		userSearchPanel = new Panel(userControl.getUserSearch());
		assertNotNull(userSearchPanel);
		tabGroup = userSearchPanel.getTabGroup();

		// user account
		User userOrdinary = UserDAO.getUserById(1);
		userControlOrdinary = new UserController(userOrdinary);
		userInfoPanelOrdinary = new Panel(
				userControlOrdinary.getUserInfoPanel());
		assertNotNull(userControlOrdinary);
	}

	@Test
	public void testInitialUserTab() {
		assertNotNull(tabGroup);
		assertThat(tabGroup.isEnabled());
		assertThat(tabGroup.isVisible());

		assertThat(tabGroup.tabNamesEquals(new String[] { "User Accounts",
				"Pending Applications" }));

		assertThat(tabGroup.selectedTabEquals("User Accounts"));
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(userSearchPanel.getTable("tableUsers"), tabGroup
				.getSelectedTab().getTable());

		Table usersTable = tabGroup.getSelectedTab().getTable();
		assertThat(usersTable.contentEquals(
				new String[] { "Username", "Name" }, new String[][] {
						{ "jvillar", "Jomel Pantaleon" },
						{ "nlazada", "Niel Lazada" },
						{ "ndizon", "Niel Dizon" } }));
		assertThat(usersTable.isEditable(new boolean[][] { { false, false },
				{ false, false }, { false, false } }));

		String temp;

		temp = userInfoPanel.getTextBox("accountType").getText();
		assertEquals("User", temp);
		assertFalse(userInfoPanel.getTextBox("accountType").isEditable());
		temp = userInfoPanel.getTextBox("idNumber").getText();
		assertEquals("1", temp);
		assertFalse(userInfoPanel.getTextBox("idNumber").isEditable());
		temp = userInfoPanel.getInputTextBox("username").getText();
		assertEquals("jvillar", temp);
		assertFalse(userInfoPanel.getTextBox("username").isEditable());

		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		
	}
	
	
	@Test
	public void testInitialPendingTab() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
				
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(userSearchPanel.getTable("tablePending"),
				tabGroup.getSelectedTab().getTable());

		assertTrue(userSearchPanel.getButton("Accept").isEnabled());
		assertTrue(userSearchPanel.getButton("Deny").isEnabled());
		assertTrue(userSearchPanel.getCheckBox().isEnabled());

		assertTrue(userSearchPanel.getButton("Search").isEnabled());
		assertTrue(userSearchPanel.getTextBox().isEnabled());

		Table pendingTable = tabGroup.getSelectedTab().getTable();
		assertThat(pendingTable.rowCountEquals(2));

		assertThat(pendingTable.contentEquals(new Object[][] {
				{ "rdgomez", "Ray Gomez"},
				{ "kserrano", "Karlo Serrano"} }));
		
		assertThat(pendingTable.selectionEquals(new boolean[][]{
				{true, true}, {false, false} }));

	}
	
	@Test
	public void testSwitchTabs(){
		tabGroup.selectTab("User Accounts");
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		assertThat(tabGroup.selectedTabEquals("Pending Applications"));
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(userSearchPanel.getTable("tablePending"), tabGroup
				.getSelectedTab().getTable());
		
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("User Accounts");
			}
		}).processTransientWindow().run();
		assertThat(tabGroup.selectedTabEquals("User Accounts"));
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(userSearchPanel.getTable("tableUsers"), tabGroup
				.getSelectedTab().getTable());
	}

	@Test
	public void testShowUserProfile() {
		tabGroup.selectTab("User Accounts");
		tabGroup.getSelectedTab().getTable().click(0, 0);
		assertSame(userInfoPanel.getAwtComponent(),
				userControl.getUserInfoPanel());
	}
	
	//auto-update of search that yeilds empty results
	@Test
	public void testSearchUserAutoUpdateNoUser() {
		tabGroup.selectTab("User Accounts");
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		
		final TextBox search = userSearchPanel.getInputTextBox();
	
		WindowInterceptor.init(new Trigger() {
			public void run() {
				search.pressKey(Key.Y).releaseKey(Key.Y);
			}
		}).processTransientWindow().run();	
		assertThat(tabGroup.getSelectedTab().getTable().rowCountEquals(0));
	}
	
	@Test
	public void testSearchUserEnter() {
		tabGroup.selectTab("User Accounts");
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		
		final TextBox search = userSearchPanel.getInputTextBox();
	
		search.setText("ndizon");
		WindowInterceptor.init(new Trigger() {
			public void run() {
				search.pressKey(Key.ENTER).releaseKey(Key.ENTER);
			}
		}).processTransientWindow().run();	
		assertThat(tabGroup.getSelectedTab().getTable().rowCountEquals(1));
	}
	
	@Test
	public void testSearchUserButton() {
		tabGroup.selectTab("User Accounts");
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		final Button button = userSearchPanel.getButton("Search");
		TextBox search = userSearchPanel.getInputTextBox();
	
		search.setText("ndizon");
		WindowInterceptor.init(new Trigger() {
			public void run() {
				button.click();
			}
		}).processTransientWindow().run();	
		assertThat(tabGroup.getSelectedTab().getTable().rowCountEquals(1));
	}

	@Test
	public void testSelectAllPendingThroughSelectAll() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		
		assertThat(tabGroup.selectedTabEquals("Pending Applications"));
		CheckBox selectAll = tabGroup.getSelectedTab().getCheckBox();
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		selectAll.click();

		Table pendingTable = tabGroup.getSelectedTab().getTable();
		
		assertFalse(pendingTable.selectionIsEmpty());
		assertThat(pendingTable.selectionEquals(new boolean[][]{
				{true, true}, {true,true} }));			
		assertEquals("kserrano", userInfoPanel.getInputTextBox("username").getText());
	}
	
	@Test
	@ExpectedDataSet({ "../models/expected/acceptAllPendingUser.xml" })
	public void testAcceptAllPendingThroughSelectAll() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		assertThat(tabGroup.selectedTabEquals("Pending Applications"));
		CheckBox selectAll = tabGroup.getSelectedTab().getCheckBox();
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		selectAll.click();

		Button acceptButton = tabGroup.getSelectedTab().getButton("Accept");
		acceptButton.click();

		Table pendingTable = tabGroup.getSelectedTab().getTable();
		assertThat(pendingTable.rowCountEquals(0));
	}

	@Test
	@ExpectedDataSet({ "../models/expected/acceptAllPendingUser.xml" })
	public void testAcceptAllPendingManually() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		Table pendingTable = tabGroup.getSelectedTab().getTable();		
		pendingTable.selectRowSpan(0, 1);
		Button acceptButton = tabGroup.getSelectedTab().getButton("Accept");
		acceptButton.click();

		assertThat(pendingTable.rowCountEquals(0));
	}
	
	@Test
	@ExpectedDataSet({ "../models/expected/denyAllPendingUser.xml" })
	public void testDenyAllPendingManually() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		Table pendingTable = tabGroup.getSelectedTab().getTable();
		pendingTable.selectRowSpan(0, 1);

		Button denyButton = tabGroup.getSelectedTab().getButton("Deny");
		denyButton.click();

		assertThat(pendingTable.rowCountEquals(0));
	}
	
	@Test
	@ExpectedDataSet({ "../models/expected/denyAllPendingUser.xml" })
	public void testDenyAllPendingThroughSelectAll() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		CheckBox selectAll = tabGroup.getSelectedTab().getCheckBox();
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		selectAll.click();

		Button denyButton = tabGroup.getSelectedTab().getButton("Deny");
		denyButton.click();

		Table pendingTable = tabGroup.getSelectedTab().getTable();
		assertThat(pendingTable.rowCountEquals(0));
	}
	
	@Test
	public void testSelectAllThenUnselectAllPending() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		CheckBox selectAll = tabGroup.getSelectedTab().getCheckBox();
		selectAll.click();
		
		Table pendingTable = tabGroup.getSelectedTab().getTable();
		
		assertThat(pendingTable.selectionEquals(new boolean[][]{
				{true, true}, {true,true} }));		
		assertEquals("kserrano", userInfoPanel.getInputTextBox("username").getText());
		assertThat(tabGroup.getSelectedTab().getCheckBox().isSelected());
		
		selectAll.click();
		pendingTable = tabGroup.getSelectedTab().getTable();
		assertThat(pendingTable.selectionEquals(new boolean[][]{
				{true, true}, {false, false} }));		
		assertEquals("rdgomez", userInfoPanel.getInputTextBox("username").getText());
		assertFalse(tabGroup.getSelectedTab().getCheckBox().isSelected());
	}
	
	@Test
	public void testSelectAllManuallyLastThenFirst() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());				
		Table pendingTable = tabGroup.getSelectedTab().getTable();
		
		assertThat(pendingTable.selectionEquals(new boolean[][]{
				{true, true}, {false,false} }));		
		assertEquals("rdgomez", userInfoPanel.getInputTextBox("username").getText());		
		assertFalse(tabGroup.getSelectedTab().getCheckBox().isSelected());
				
		pendingTable.selectRow(1);
		assertThat(pendingTable.selectionEquals(new boolean[][]{
				{false,false}, {true, true} }));		
		assertEquals("kserrano", userInfoPanel.getInputTextBox("username").getText());		
		assertFalse(tabGroup.getSelectedTab().getCheckBox().isSelected());

		pendingTable.selectRows(1,0);
		assertThat(pendingTable.selectionEquals(new boolean[][]{
				{true, true}, {true, true} }));		
		assertEquals("rdgomez", userInfoPanel.getInputTextBox("username").getText());
		assertThat(tabGroup.getSelectedTab().getCheckBox().isSelected());
	}

	@Test
	@ExpectedDataSet({ "../models/expected/acceptOnePendingUserEnd.xml" })
	public void testAcceptOnePendingEnd() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());		
		Table pendingTable = tabGroup.getSelectedTab().getTable();
		pendingTable.selectRow(1);

		Button acceptButton = tabGroup.getSelectedTab().getButton("Accept");
		acceptButton.click();

		assertThat(pendingTable.rowCountEquals(1));
	}
	
	@Test
	@ExpectedDataSet({ "../models/expected/acceptOnePendingUserStart.xml" })
	public void testAcceptOnePendingStart() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		Table pendingTable = tabGroup.getSelectedTab().getTable();
		pendingTable.selectRow(0);

		Button acceptButton = tabGroup.getSelectedTab().getButton("Accept");
		acceptButton.click();

		assertThat(pendingTable.rowCountEquals(1));
	}
	
	@ExpectedDataSet({ "../models/expected/changePassword.xml" })
	@Test
	public void testChangePasswordLibrarian() {
		Button changePasswordButton = userInfoPanel
				.getButton("Change Password");
		WindowInterceptor.init(changePasswordButton.triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window dialog) {
						dialog.getPasswordField("newpassword").setPassword(
								"1234567");
						dialog.getPasswordField("repeatpassword").setPassword(
								"1234567");
						return dialog.getButton("Change Password")
								.triggerClick();
					}

				}).process(new WindowHandler() {
					public Trigger process(Window dialog) {
						assertThat(dialog
								.containsLabel("Password successfully changed!"));
						return dialog.getButton("OK").triggerClick();
					}

				}).run();
	}

	@ExpectedDataSet({ "../models/expected/changePassword.xml" })
	@Test
	public void testChangePasswordUser() {
		Button changePasswordButton = userInfoPanelOrdinary
				.getButton("Change Password");
		WindowInterceptor.init(changePasswordButton.triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window dialog) {
						assertThat(dialog.getPasswordField("oldpassword")
								.isEnabled());
						dialog.getPasswordField("oldpassword").setPassword(
								"123456");
						dialog.getPasswordField("newpassword").setPassword(
								"1234567");
						dialog.getPasswordField("repeatpassword").setPassword(
								"1234567");
						return dialog.getButton("Change Password")
								.triggerClick();
					}

				}).process(new WindowHandler() {
					public Trigger process(Window dialog) {
						assertThat(dialog
								.containsLabel("Password successfully changed!"));
						return dialog.getButton("OK").triggerClick();
					}

				}).run();
	}

	@Test
	public void testChangePasswordIncorrectOld() {
		final Button changePasswordButton = userInfoPanelOrdinary
				.getButton("Change Password");
		WindowInterceptor.init(new Trigger() {

			@Override
			public void run() throws Exception {
				changePasswordButton.click();

			}
		}).process(new WindowHandler() {
			public Trigger process(Window dialog) {
				assertThat(dialog.getPasswordField("oldpassword").isEnabled());
				dialog.getPasswordField("oldpassword").setPassword("12345678");
				dialog.getPasswordField("newpassword").setPassword("1234567");
				dialog.getPasswordField("repeatpassword")
						.setPassword("1234567");
				dialog.getButton("Change Password").click();
				assertTrue(dialog.containsLabel("Incorrect password"));
				return dialog.getButton("Cancel").triggerClick();
			}

		}).run();
	}
	
	@Test
	public void testChangePasswordNotMatch() {
		final Button changePasswordButton = userInfoPanelOrdinary
				.getButton("Change Password");
		WindowInterceptor.init(new Trigger() {

			@Override
			public void run() throws Exception {
				changePasswordButton.click();

			}
		}).process(new WindowHandler() {
			public Trigger process(Window dialog) {
				assertThat(dialog.getPasswordField("oldpassword").isEnabled());
				dialog.getPasswordField("oldpassword").setPassword("123456");
				dialog.getPasswordField("newpassword").setPassword("1234567");
				dialog.getPasswordField("repeatpassword").setPassword("1234467");
				dialog.getButton("Change Password").click();
				assertTrue(dialog.containsLabel("New passwords do not match"));
				return dialog.getButton("Cancel").triggerClick();
			}

		}).run();
	}
	
	@Test
	public void testChangePasswordInvalidFormat() {
		final Button changePasswordButton = userInfoPanelOrdinary
				.getButton("Change Password");
		WindowInterceptor.init(new Trigger() {

			@Override
			public void run() throws Exception {
				changePasswordButton.click();

			}
		}).process(new WindowHandler() {
			public Trigger process(Window dialog) {
				assertThat(dialog.getPasswordField("oldpassword").isEnabled());
				dialog.getPasswordField("oldpassword").setPassword("123456");
				dialog.getPasswordField("newpassword").setPassword("1234");
				dialog.getPasswordField("repeatpassword").setPassword("1234");
				dialog.getButton("Change Password").click();
				assertTrue(dialog.containsLabel("Invalid password."));
				return dialog.getButton("Cancel").triggerClick();
			}

		}).run();
	}
	
	@Test
	public void testUpdateUserSuccessful() {
		Button saveChangesButton = userInfoPanel.getButton("Save Changes");

		WindowInterceptor.init(saveChangesButton.triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window dialog) {
						assertThat(dialog
								.containsLabel("Record successfully updated"));
						return dialog.getButton("OK").triggerClick();
					}
				}).run();

	}
	
	@Test
	public void testUpdateUserFailed() {
		Button saveChangesButton = userInfoPanel.getButton("Save Changes");
		assertThat(userInfoPanel.getTextBox("firstName").isEditable());
		assertThat(userInfoPanel.getTextBox("lastName").isEditable());
		assertThat(userInfoPanel.getTextBox("contactNumber").isEditable());
		assertThat(userInfoPanel.getTextBox("emailAdd").isEditable());
		assertThat(userInfoPanel.getInputTextBox("address").isEditable());		
		
		userInfoPanel.getTextBox("firstName").setText("");
		userInfoPanel.getTextBox("lastName").setText("");
		userInfoPanel.getTextBox("contactNumber").setText("1");
		userInfoPanel.getTextBox("emailAdd").setText("");
	 	userInfoPanel.getInputTextBox("address").setText("");
		saveChangesButton.click();
		
		assertEquals("Invalid Input", userInfoPanel.getTextBox("firstNameErrorLabel").getText());
		assertEquals("Invalid Input", userInfoPanel.getTextBox("lastNameErrorLabel").getText());
		assertEquals("Invalid Input", userInfoPanel.getTextBox("addressErrorLabel").getText());
		assertEquals(Constants.EMAIL_FORMAT_ERROR_MESSAGE, userInfoPanel.getTextBox("emailErrorLabel").getText());
		assertEquals(Constants.CONTACT_NUMBER_FORMAT_ERROR_MESSAGE, userInfoPanel.getTextBox("contactNumberErrorLabel").getText());
	}
	
	@Test
	public void testUpdateUserEmailExisting(){
		Button saveChangesButton = userInfoPanel.getButton("Save Changes");
		assertThat(userInfoPanel.getTextBox("emailAdd").isEditable());
		userInfoPanel.getTextBox("emailAdd").setText("rdgomez@gmail.com");
		saveChangesButton.click();
		assertEquals(Constants.EMAIL_EXIST_ERROR_MESSAGE, userInfoPanel.getTextBox("emailErrorLabel").getText());
	}
}
