package controllers;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.CheckBox;
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
	Panel userInfoPanel;
	Panel userSearchPanel;
	TabGroup tabGroup;

	@Before
	public void setUp() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		Emailer.setOn(false);

		User user = UserDAO.getUserById(2);
		userControl = new UserController(user);

		userInfoPanel = new Panel(userControl.getUserInfoPanel());
		assertNotNull(userInfoPanel);
		userSearchPanel = new Panel(userControl.getUserSearch());
		assertNotNull(userSearchPanel);
		tabGroup = userSearchPanel.getTabGroup();
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

		tabGroup.selectTab("Pending Applications");
	}

	@Test
	public void testInitialPendingTab() {
		tabGroup.selectTab("Pending Applications");
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

		tabGroup.selectTab("User Accounts");

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
		tabGroup.selectTab("Pending Applications");
		CheckBox selectAll = tabGroup.getSelectedTab().getCheckBox();
		selectAll.click();

		Table pendingTable = tabGroup.getSelectedTab().getTable();
		assertThat(pendingTable.columnEquals(2, new Object[] { true, true }));
	}

	@Test
	@ExpectedDataSet({ "../models/expected/acceptAllPendingUser.xml" })
	public void testAcceptAllPendingThroughSelectAll() {
		tabGroup.selectTab("Pending Applications");
		CheckBox selectAll = tabGroup.getSelectedTab().getCheckBox();
		selectAll.click();

		Button acceptButton = tabGroup.getSelectedTab().getButton("Accept");
		acceptButton.click();

		Table pendingTable = tabGroup.getSelectedTab().getTable();
		assertThat(pendingTable.rowCountEquals(0));
	}

	@Test
	@ExpectedDataSet({ "../models/expected/acceptAllPendingUser.xml" })
	public void testAcceptAllPendingThroughCheckBox() {
		tabGroup.selectTab("Pending Applications");
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
		tabGroup.selectTab("Pending Applications");
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
		tabGroup.selectTab("Pending Applications");
		CheckBox selectAll = tabGroup.getSelectedTab().getCheckBox();
		selectAll.click();

		Button acceptButton = tabGroup.getSelectedTab().getButton("Deny");
		acceptButton.click();

		Table pendingTable = tabGroup.getSelectedTab().getTable();
		assertThat(pendingTable.rowCountEquals(0));
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

	@ExpectedDataSet({ "../models/expected/changePassword.xml" })
	@Test
	public void testChangePassword() {
		Button changePasswordButton = userInfoPanel
				.getButton("Change Password");

		WindowInterceptor.init(changePasswordButton.triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window dialog) {
						dialog.getPasswordField("newpassword").setPassword("1234567");
						dialog.getPasswordField("repeatpassword").setPassword("1234567");
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
	public void testSelectAllPendingThroughCheckbox() {
		tabGroup.selectTab("Pending Applications");

		Table pendingTable = tabGroup.getSelectedTab().getTable();
		pendingTable.click(0, 2);
		pendingTable.click(1, 2);

		assertThat(pendingTable.columnEquals(2, new Object[] { true, true }));

		assertThat(tabGroup.getSelectedTab().getCheckBox().isSelected());
	}

	@Test
	public void testSelectAllThenUnselectAllPending() {
		tabGroup.selectTab("Pending Applications");

		CheckBox selectAll = tabGroup.getSelectedTab().getCheckBox();
		selectAll.click();
		selectAll.click();

		Table pendingTable = tabGroup.getSelectedTab().getTable();
		assertThat(pendingTable.columnEquals(2, new Object[] { false, false }));
	}
	
	@Test
	@ExpectedDataSet({ "../models/expected/acceptOnePendingUserEnd.xml" })
	public void testAcceptOnePendingEnd() {
		tabGroup.selectTab("Pending Applications");
		Table pendingTable = tabGroup.getSelectedTab().getTable();
		pendingTable.click(1, 2);

		Button acceptButton = tabGroup.getSelectedTab().getButton("Accept");
		acceptButton.click();

		assertThat(pendingTable.rowCountEquals(1));
	}
	
	@Test
	@ExpectedDataSet({ "../models/expected/acceptOnePendingUserStart.xml" })
	public void testAcceptOnePendingStart() {
		tabGroup.selectTab("Pending Applications");
		Table pendingTable = tabGroup.getSelectedTab().getTable();
		pendingTable.click(0, 2);

		Button acceptButton = tabGroup.getSelectedTab().getButton("Accept");
		acceptButton.click();

		assertThat(pendingTable.rowCountEquals(1));
	}
}
