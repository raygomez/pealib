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

import controllers.UserController;

import utilities.Connector;
import utilities.Constants;



@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserSearchPanelTest extends UISpecTestCase {
	
	UserSearchPanel userSearch;
	UserController control;
	Panel panel;
	TabGroup tabGroup;
	
	@Before
	public void setUp() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		userSearch = new UserSearchPanel();
		panel = new Panel(userSearch);
		tabGroup = panel.getTabGroup();
	}
	
	@Test
	public void testInitialState() {
		assertThat(panel.getButton().isEnabled());
		assertThat(panel.getButton().isVisible());
		assertThat(panel.getInputTextBox().isEditable());
		assertThat(panel.getInputTextBox().isEnabled());
		assertThat(panel.getInputTextBox().isVisible());
		assertEquals("",panel.getInputTextBox().getText());
	
		assertNotNull(tabGroup);
		assertThat(tabGroup.isEnabled());
		assertThat(tabGroup.isVisible());

		assertThat(tabGroup.tabNamesEquals(new String[] { "User Accounts", "Pending Applications" }));
		assertThat(tabGroup.selectedTabEquals("User Accounts"));
		
		tabGroup.selectTab("Pending Applications");		
		assertThat(tabGroup.selectedTabEquals("Pending Applications"));
	}
}
