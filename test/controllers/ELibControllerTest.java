package controllers;

import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Panel;
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
public class ELibControllerTest extends UISpecTestCase {

	Panel panel;

	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
		User user = UserDAO.getUserById(1);
		panel = new Panel(new ELibController(user).getTabpane());
	}

	@Test
	public void testInitialState() {
		TabGroup tabGroup = panel.getTabGroup();
		Table request = tabGroup.getSelectedTab().getTable();
		assertTrue(request.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Requested",
						"Cancel" }));
		assertThat(request.isEmpty());

		tabGroup.selectTab("Reservations");
		Table reservation = tabGroup.getSelectedTab().getTable();
		assertTrue(reservation.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Reserved",
						"Queue Number", "Cancel" }));
		assertEquals(2, reservation.getRowCount());

		tabGroup.selectTab("On Loan");
		Table onLoan = tabGroup.getSelectedTab().getTable();
		assertTrue(onLoan.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Borrowed",
						"Due Date" }));
		assertEquals(3, onLoan.getRowCount());
		assertThat(onLoan.contentEquals(new Object[][] {
				{ "1234567890121", "Harry Poter 2", "Niel", "2011-06-15",
						"2011-06-29" },
				{ "1234567890123", "title1", "author1", "2011-06-15",
						"2011-06-29" },
				{ "1234567890124", "title2", "author1", "2011-06-15",
						"2011-06-29" } }));

		tabGroup.selectTab("History");
		Table history = tabGroup.getSelectedTab().getTable();
		assertTrue(history.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Borrowed",
						"Date Returned" }));
		assertEquals(2, history.getRowCount());
		assertThat(history.contentEquals(new Object[][] {
				{ "1234567890120", "Harry Poter 1", "Ewan ko", "2011-06-15",
						"2011-06-15" },
				{ "1234567890123", "title1", "author1", "2011-06-15",
						"2011-06-15" } }));

	}

}
