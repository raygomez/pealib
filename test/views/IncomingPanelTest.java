package views;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;

public class IncomingPanelTest extends UISpecTestCase {

	Panel panel;

	@Before
	public void setUp() throws Exception {
		panel = new Panel(new IncomingPanel());
	}

	@Test
	public void testInitialState() {
		Button returnButton = panel.getButton("Return");
		assertNotNull(returnButton);
		assertFalse(returnButton.isEnabled());
		assertThat(returnButton.isVisible());

		TextBox daysOverdueLabel = panel.getTextBox("daysOverdueLabel");
		assertNotNull(daysOverdueLabel);
		assertEquals("", daysOverdueLabel.getText());

		Panel searchPanel = panel.getPanel("searchPanel");
		assertNotNull(searchPanel);
	}

}
