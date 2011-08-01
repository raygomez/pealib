package views;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Panel;
import org.uispec4j.TabGroup;
import org.uispec4j.UISpecTestCase;

public class ELibTabbedPanelTest extends UISpecTestCase {

	Panel panel;

	@Before
	public void setUp() throws Exception {
		panel = new Panel(new ELibTabbedPanel());
	}

	@Test
	public void testInitialState() {
		TabGroup tabGroup = panel.getTabGroup();
		assertNotNull(tabGroup);
		assertThat(tabGroup.isEnabled());
		assertThat(tabGroup.isVisible());

		assertThat(tabGroup.tabNamesEquals(new String[] { "Requests",
				"Reservations", "On Loan", "History" }));
		
		assertThat(tabGroup.selectedTabEquals("Requests"));
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(panel.getTable("requestTable"), tabGroup
				.getSelectedTab().getTable());

		tabGroup.selectTab("Reservations");
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(panel.getTable("reserveTable"), tabGroup
				.getSelectedTab().getTable());

		tabGroup.selectTab("On Loan");
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(panel.getTable("onloanTable"), tabGroup
				.getSelectedTab().getTable());

		tabGroup.selectTab("History");
		assertNotNull(tabGroup.getSelectedTab().getTable());
		assertReflectionEquals(panel.getTable("historyTable"), tabGroup
				.getSelectedTab().getTable());

	}
}
