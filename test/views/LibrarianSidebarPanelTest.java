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
public class LibrarianSidebarPanelTest extends UISpecTestCase {

	private Panel panel;

	@Before
	public void setUp() throws Exception {
		panel = new Panel(new LibrarianSidebarPanel());
	}

	@Test
	public void testInitialState() {
		Button button;

		String[] buttonArray = new String[] { "Search Books", "Search Users",
				"Edit Profile", "Book Transactions", "Logout" };

		for (String s : buttonArray) {
			button = panel.getButton(s);
			assertNotNull(button);
			assertThat(button.isEnabled());
			assertThat(button.isVisible());
		}

	}
}
