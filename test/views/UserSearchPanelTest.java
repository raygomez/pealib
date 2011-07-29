package views;

import static org.junit.Assert.*;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import javax.swing.JButton;
import javax.swing.JTable;

import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Panel;
import org.uispec4j.TabGroup;
import org.uispec4j.Table;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "../models/user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserSearchPanelTest extends UISpecTestCase {
	Panel panel;
	
	@Before
	public void setUp() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		panel = new Panel(new UserSearchPanel());
	}
	
	@Test
	public void testInitialState() {
		assertThat(panel.getButton().isEnabled());
		assertThat(panel.getButton().isVisible());
		assertThat(panel.getInputTextBox().isEditable());
		assertThat(panel.getInputTextBox().isEnabled());
		assertThat(panel.getInputTextBox().isVisible());
		assertEquals("",panel.getInputTextBox().getText());
		
		TabGroup tabGroup = panel.getTabGroup();
		assertNotNull(tabGroup);
		assertThat(tabGroup.isEnabled());
		assertThat(tabGroup.isVisible());

		assertThat(tabGroup.tabNamesEquals(new String[] { "User Accounts", "Pending Applications" }));
		assertThat(tabGroup.selectedTabEquals("User Accounts"));
	}
/*	
	@Test
	public void testGetFieldSearch() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSelectedTab() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUsersTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPendingTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCbAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetModelPending() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetModelUsers() {
		fail("Not yet implemented");
	}

	@Test
	public void testUserSearchPanel() {
		fail("Not yet implemented");
	}

	@Test
	public void testUsersPanel() {
		fail("Not yet implemented");
	}

	@Test
	public void testPendingAppPanel() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTableModel() {
		fail("Not yet implemented");
	}

	@Test
	public void testResetTabPane() {
		fail("Not yet implemented");
	}

	@Test
	public void testTogglePendingButtons() {
		fail("Not yet implemented");
	}

	@Test
	public void testToggleAllPendingComp() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddListeners() {
		fail("Not yet implemented");
	}
*/
}
