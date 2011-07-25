package views;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;

public class InOutBookSearchPanelTest extends UISpecTestCase {

	Panel panel;
	
	@Before
	public void setUp() throws Exception {
		panel = new Panel(new InOutBookSearchPanel());
	}

	@Test
	public void testInitialState() {
		TextBox searchBox = panel.getTextBox("Search");
		assertNotNull(searchBox);

		TextBox totalLabel = panel.getTextBox("totalLabel");
		assertEquals("", totalLabel.getText());
		assertNotNull(totalLabel);
		
		TextBox searchTextField = panel.getInputTextBox("searchTextField");
		assertNotNull(searchTextField);
		assertEquals("", searchTextField.getText());
		
		assertNotNull(panel.getTable());
		assertThat(panel.getTable().isEmpty());
		
		assertNotNull(panel.getButton("Submit"));
		assertThat(panel.getButton("Submit").isEnabled());
		assertThat(panel.getButton("Submit").isVisible());

		assertNotNull(panel.getButton("Clear"));
		assertThat(panel.getButton("Clear").isEnabled());
		assertThat(panel.getButton("Clear").isVisible());
		
	}
}
