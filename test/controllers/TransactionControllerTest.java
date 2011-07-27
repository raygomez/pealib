package controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.TabGroup;
import org.uispec4j.Table;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "../models/user.xml", "../models/book.xml",
		"../models/reserves.xml", "../models/borrows.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class TransactionControllerTest extends UISpecTestCase {

	TabGroup tabGroup;

	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
		tabGroup = new TabGroup(new TransactionController().getTabbedPane());
	}

	@Test
	public void testInitialState() {
		tabGroup.selectTab("Incoming");
		Table incoming = tabGroup.getSelectedTab().getTable();
		assertTrue(incoming.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Username",
						"Date Borrowed" }));
		assertEquals(5, incoming.getRowCount());
		assertThat(incoming.contentEquals(new Object[][] {
				{ "1234567890121", "Harry Poter 2", "Niel",
						"nlazada (Niel Lazada)", "2011-06-15" },
				{ "1234567890121", "Harry Poter 2", "Niel",
						"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890123", "title1", "author1",
						"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890124", "title2", "author1",
						"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890121", "Harry Poter 2", "Niel",
						"kserrano (Karlo Serrano)", "2011-06-15" } }));

		tabGroup.selectTab("Outgoing");
		Table outgoing = tabGroup.getSelectedTab().getTable();
		assertTrue(outgoing.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Username",
						"Date Requested" }));
		assertEquals(4, outgoing.getRowCount());
		assertThat(outgoing.contentEquals(new Object[][] {
				{ "1234567890121", "Harry Poter 2", "Niel",
						"apantaleon (Annuary Pantaleon)", "2011-06-15" },
				{ "1234567890124", "title2", "author1", "ndizon (Niel Dizon)",
						"2011-06-15" },
				{ "1234567890125", "title3Pantaleon", "author2",
						"ndizon (Niel Dizon)", "2011-06-15" },
				{ "1234567890120", "Harry Poter 1", "Ewan ko",
						"jvillar (Jomel Pantaleon)", "2011-06-15" } }));

	}
}
