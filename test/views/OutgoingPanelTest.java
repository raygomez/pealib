package views;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.UISpecTestCase;

public class OutgoingPanelTest extends UISpecTestCase {

	Panel panel;

	@Before
	public void setUp() throws Exception {
		panel = new Panel(new OutgoingPanel());
	}

	@Test
	public void testInitialState() {
		
		String[] buttonArray = new String[] { "Grant", "Deny" };
		Button button;

		for (String s : buttonArray) {
			button = panel.getButton(s);
			assertNotNull(button);
			assertFalse(button.isEnabled());
			assertThat(button.isVisible());
		}

		Panel searchPanel = panel.getPanel("searchPanel");
		assertNotNull(searchPanel);
	}

}
