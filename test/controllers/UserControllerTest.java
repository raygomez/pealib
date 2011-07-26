package controllers;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Panel;
import org.uispec4j.TabGroup;
import org.uispec4j.Table;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "../models/user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserControllerTest extends UISpecTestCase {

	Panel userInfoPanel;
	Panel userSearchPanel;
	
	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
		
		User user = UserDAO.getUserById(2);
		userInfoPanel = new Panel(new UserController(user).getUserInfoPanel());
		assertNotNull(userInfoPanel);
		userSearchPanel = new Panel(new UserController(user).getUserSearch());
		assertNotNull(userSearchPanel);
	}

	@Test
	public void testInitialUserSearchPanel() {
		TabGroup tabGroup = userSearchPanel.getTabGroup();
		assertNotNull(tabGroup);
		assertThat(tabGroup.isEnabled());
		assertThat(tabGroup.isVisible());

		assertThat(tabGroup.tabNamesEquals(new String[] { "User Accounts", "Pending Applications"}));
		
		assertThat(tabGroup.selectedTabEquals("User Accounts"));
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(userSearchPanel.getTable("tableUsers"), tabGroup
				.getSelectedTab().getTable());
		
		tabGroup.selectTab("Pending Applications");
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(userSearchPanel.getTable("tablePending"), tabGroup
				.getSelectedTab().getTable());
	}

}
