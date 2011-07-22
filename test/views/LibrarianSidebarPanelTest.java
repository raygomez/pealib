package views;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;

import views.LibrarianSidebarPanel;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class LibrarianSidebarPanelTest extends UISpecTestCase{

	private Panel panel;
	
	@Before
	public void setUp() throws Exception {
		panel = new Panel(new LibrarianSidebarPanel());
	}

	@Test
	public void testInitialState(){
		Button searchButton = panel.getButton("Search Books");
		assertNotNull(searchButton);
		assertThat(searchButton.isEnabled());
		assertThat(searchButton.isVisible());
		
		Button searchUsersButton = panel.getButton("Search Users");
		assertNotNull(searchUsersButton);
		assertThat(searchUsersButton.isEnabled());
		assertThat(searchUsersButton.isVisible());
		
		Button editProfileButton = panel.getButton("Edit Profile");
		assertNotNull(editProfileButton);
		assertThat(editProfileButton.isEnabled());
		assertThat(editProfileButton.isVisible());
		
		Button bookTransactionsButton = panel.getButton("Book Transactions");
		assertNotNull(bookTransactionsButton);
		assertThat(bookTransactionsButton.isEnabled());
		assertThat(bookTransactionsButton.isVisible());
		
		Button logoutButton = panel.getButton("Logout");
		assertNotNull(logoutButton);
		assertThat(logoutButton.isEnabled());
		assertThat(logoutButton.isVisible());
	}
}
