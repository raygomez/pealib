package controllers;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Panel;
import org.uispec4j.TabGroup;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.Constants;
import views.UserSearchPanel;

@DataSet({ "../models/user.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserControllerTest extends UISpecTestCase {

	UserController userControl;
	Panel userInfoPanel;
	Panel userSearchPanel;
	TabGroup tabGroup;
	
	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
		
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

		assertThat(tabGroup.tabNamesEquals(new String[] { "User Accounts", "Pending Applications"}));
		
		assertThat(tabGroup.selectedTabEquals("User Accounts"));
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(userSearchPanel.getTable("tableUsers"), tabGroup
				.getSelectedTab().getTable());
		
		String temp;
		
		temp = userInfoPanel.getTextBox("accountType").getText();
		assertEquals("User",temp);		
		temp = userInfoPanel.getTextBox("idNumber").getText();
		assertEquals("1",temp);		
		temp = userInfoPanel.getInputTextBox("username").getText();
		assertEquals("jvillar",temp);					
	}
	
	@Ignore
	@Test
	public void testInitialPendingTab(){
		tabGroup.selectTab("Pending Applications");
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(userSearchPanel.getTable("tablePending"), tabGroup
				.getSelectedTab().getTable());
		
		assertFalse(userSearchPanel.getButton("Accept").isEnabled());
		assertFalse(userSearchPanel.getButton("Deny").isEnabled());
		assertTrue(userSearchPanel.getCheckBox().isEnabled());
		
		assertTrue(userSearchPanel.getButton("Search").isEnabled());
		assertTrue(userSearchPanel.getTextBox().isEnabled());						
	}
	
	@Test
	public void testShowUserProfile(){
		tabGroup.selectTab("User Accounts");
		tabGroup.getSelectedTab().getTable().click(0, 0);
		assertReflectionEquals(userInfoPanel, userControl.getUserInfoPanel());
	}
	
	@Test
	public void test(){
		
	}

}
