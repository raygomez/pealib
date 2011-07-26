package views;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.TabGroup;
import org.uispec4j.UISpecTestCase;

public class InOutTabbedPaneTest extends UISpecTestCase {

	TabGroup tabGroup;

	@Before
	public void setUp() throws Exception {
		tabGroup = new TabGroup(new InOutTabbedPane());
	}

	@Test
	public void test() {
		assertNotNull(tabGroup);
		assertThat(tabGroup.isEnabled());
		assertThat(tabGroup.isVisible());
		assertThat(tabGroup.tabNamesEquals(new String[] { "Incoming",
				"Outgoing" }));
	}

}
