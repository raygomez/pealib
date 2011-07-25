package views;

import org.junit.*;
import org.junit.runner.RunWith;
import org.uispec4j.*;
import org.unitils.*;

import views.UserSidebarPanel;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UserSidebarPanelTest extends UISpecTestCase {

	private Panel panel;

	@Before
	public void setUp() throws Exception {
		panel = new Panel(new UserSidebarPanel());
	}

	@Test
	public void testInitialState() {
		Button button;

		String[] buttonArray = new String[] { "Search Books", "Edit Profile",
				"View E-Library Card", "Logout" };

		for (String s : buttonArray) {
			button = panel.getButton(s);
			assertNotNull(button);
			assertThat(button.isEnabled());
			assertThat(button.isVisible());
		}
	}
}
