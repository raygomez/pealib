package controllers;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.CheckBox;
import org.uispec4j.Key;
import org.uispec4j.Panel;
import org.uispec4j.TabGroup;
import org.uispec4j.Table;
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
		temp = userInfoPanel.getTextBox("idNumber").getText();
		assertEquals("1", temp);
		temp = userInfoPanel.getInputTextBox("username").getText();
		assertEquals("jvillar", temp);

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

		assertFalse(userSearchPanel.getButton("Accept").isEnabled());
		assertFalse(userSearchPanel.getButton("Deny").isEnabled());
		assertTrue(userSearchPanel.getCheckBox().isEnabled());

		assertTrue(userSearchPanel.getButton("Search").isEnabled());
		assertTrue(userSearchPanel.getTextBox().isEnabled());

		Table pendingTable = tabGroup.getSelectedTab().getTable();
		assertThat(pendingTable.rowCountEquals(2));

		assertThat(pendingTable.contentEquals(new Object[][] {
				{ "rdgomez", "Ray Gomez", false },
				{ "kserrano", "Karlo Serrano", false } }));
		assertThat(pendingTable.isEditable(new boolean[][] {
				{ false, false, true }, { false, false, true } }));	

	}

	@Test
	public void testShowUserProfile() {
		tabGroup.selectTab("User Accounts");
		tabGroup.getSelectedTab().getTable().click(0, 0);
		assertSame(userInfoPanel.getAwtComponent(),
				userControl.getUserInfoPanel());
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
		assertThat(pendingTable.columnEquals(2, new Object[] { true, true }));
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
	public void testAcceptAllPendingThroughCheckBox() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		Table pendingTable = tabGroup.getSelectedTab().getTable();
		pendingTable.click(0, 2);
		pendingTable.click(1, 2);

		Button acceptButton = tabGroup.getSelectedTab().getButton("Accept");
		acceptButton.click();

		assertThat(pendingTable.rowCountEquals(0));
	}
	
	@Test
	@ExpectedDataSet({ "../models/expected/denyAllPendingUser.xml" })
	public void testDenyAllPendingThroughCheckBox() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		Table pendingTable = tabGroup.getSelectedTab().getTable();
		pendingTable.click(0, 2);
		pendingTable.click(1, 2);

		Button acceptButton = tabGroup.getSelectedTab().getButton("Deny");
		acceptButton.click();

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

		Button acceptButton = tabGroup.getSelectedTab().getButton("Deny");
		acceptButton.click();

		Table pendingTable = tabGroup.getSelectedTab().getTable();
		assertThat(pendingTable.rowCountEquals(0));
	}
	
	@Test
	public void testSelectAllPendingThroughCheckbox() {
		WindowInterceptor.init(new Trigger() {
			public void run() {
				tabGroup.selectTab("Pending Applications");
			}
		}).processTransientWindow().run();
		assertFalse(tabGroup.getSelectedTab().getTable().isEmpty());
		Table pendingTable = tabGroup.getSelectedTab().getTable();
		pendingTable.click(0, 2);
		pendingTable.click(1, 2);

		assertThat(pendingTable.columnEquals(2, new Object[] { true, true }));

		assertThat(tabGroup.getSelectedTab().getCheckBox().isSelected());
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
		assertThat(pendingTable.columnEquals(2, new Object[] { true, true }));
		assertThat(tabGroup.getSelectedTab().getCheckBox().isSelected());
		
		selectAll.click();
		pendingTable = tabGroup.getSelectedTab().getTable();
		assertThat(pendingTable.columnEquals(2, new Object[] { false, false }));
		assertFalse(tabGroup.getSelectedTab().getCheckBox().isSelected());
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
		pendingTable.click(1, 2);

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
		pendingTable.click(0, 2);

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
				assertTrue(dialog.containsLabel("Invalid password. Passwords should be 6-20 characters long."));
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
		assertEquals("Invalid Input", userInfoPanel.getTextBox("lblError").getText());
		
	}
}
