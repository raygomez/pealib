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
		Button grantButton = panel.getButton("Grant");
		assertNotNull(grantButton);
		assertFalse(grantButton.isEnabled());
		assertThat(grantButton.isVisible());
		
		Button denyButton = panel.getButton("Deny");
		assertNotNull(denyButton);
		assertFalse(denyButton.isEnabled());
		assertThat(denyButton.isVisible());

		Panel searchPanel = panel.getPanel("searchPanel");
		assertNotNull(searchPanel);
	}

}
