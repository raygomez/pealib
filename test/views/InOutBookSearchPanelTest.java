package views;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;

import views.InOutBookSearchPanel;

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

		Button button;
		String[] buttonArray = new String[] { "Search"};

		for (String s : buttonArray) {
			button = panel.getButton(s);
			assertNotNull(button);
			assertThat(button.isEnabled());
			assertThat(button.isVisible());
		}

	}
}
