package views;
import org.junit.*;
import org.junit.runner.RunWith;
import org.uispec4j.*;
import org.unitils.*;

import views.UserSidebarPanel;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserSidebarPanelTest extends UISpecTestCase{
	
	private Panel panel;
	
	@Before
	public void setUp() throws Exception {
		panel = new Panel(new UserSidebarPanel());
	}
	
	@Test
	public void testInitialState(){
		Button searchButton = panel.getButton("Search Books");
		assertNotNull(searchButton);
		assertThat(searchButton.isEnabled());
		assertThat(searchButton.isVisible());
		
		Button editProfileButton = panel.getButton("Edit Profile");
		assertNotNull(editProfileButton);
		assertThat(editProfileButton.isEnabled());
		assertThat(editProfileButton.isVisible());
		
		Button transactionHistoryButton = panel.getButton("View E-Library Card");
		assertNotNull(transactionHistoryButton);
		assertThat(transactionHistoryButton.isEnabled());
		assertThat(transactionHistoryButton.isVisible());
		
		Button logoutButton = panel.getButton("Logout");
		assertNotNull(logoutButton);
		assertThat(logoutButton.isEnabled());
		assertThat(logoutButton.isVisible());
	}

	@Test
	public void testAddViewBooksListener() {
		
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
