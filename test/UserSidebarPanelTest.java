import static org.junit.Assert.*;

import javax.swing.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.unitils.UnitilsJUnit4TestClassRunner;

import views.UserSidebarPanel;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserSidebarPanelTest {
	
	private Panel panel;
	
	@Before
	public void setUp() throws Exception {
		panel = new Panel(new UserSidebarPanel());
	}
	
	@Test
	public void initialState(){
		Button searchButton = panel.getButton();
	}

	@Test
	public void testAddSearchBookListener() {
		
	}

	@Test
	public void testAddEditProfileListener() {
		
	}

	@Test
	public void testAddShowTransactionHistoryListener() {
		
	}

	@Test
	public void testAddLogoutListener() {
		
	}

}
