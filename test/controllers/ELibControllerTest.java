package controllers;

import models.User;
import models.UserDAO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Panel;
import org.uispec4j.TabGroup;
import org.uispec4j.Table;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "../models/user.xml", "../models/book.xml",
		"../models/reserves.xml", "../models/borrows.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ELibControllerTest extends UISpecTestCase {

	Panel panel;

	@Before
	public void setUp() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		User user = UserDAO.getUserById(1);
		panel = new Panel(new ELibController(user).getView());
	}

	@Test
	public void testInitialState() {
		TabGroup tabGroup = panel.getTabGroup();
		Table request = tabGroup.getSelectedTab().getTable();
		assertTrue(request.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Requested",
						"Cancel" }));
		assertEquals(1, request.getRowCount());
		assertThat(request.isEditable(new boolean[][] { { false, false, false,
				false, true } }));

		tabGroup.selectTab("Reservations");
		Table reservation = tabGroup.getSelectedTab().getTable();
		assertTrue(reservation.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Reserved",
						"Queue Number", "Cancel" }));
		assertEquals(2, reservation.getRowCount());
		assertThat(reservation.isEditable(new boolean[][] {
				{ false, false, false, false, false, true },
				{ false, false, false, false, false, true } }));

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
		assertThat(onLoan.isEditable(new boolean[][] {
				{ false, false, false, false, false },
				{ false, false, false, false, false },
				{ false, false, false, false, false } }));

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
		assertThat(history.isEditable(new boolean[][] {
				{ false, false, false, false, false },
				{ false, false, false, false, false } }));
	}

	@Test
	@ExpectedDataSet({ "../models/expected/denyBookRequestBorrows.xml" })
	public void testCancelRequest() {
		TabGroup tabGroup = panel.getTabGroup();

		tabGroup.selectTab("Requests");
		final Table requests = tabGroup.getSelectedTab().getTable();
		assertTrue(requests.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Requested",
						"Cancel" }));
		assertEquals(1, requests.getRowCount());

		WindowInterceptor.init(new Trigger() {

			@Override
			public void run() throws Exception {
				requests.click(0, 4);
			}
		}).process(new WindowHandler() {
			public Trigger process(Window dialog) {
				assertEquals("Confirm", dialog.getTitle());
				dialog.containsLabel(Constants.CONFIRM_CANCEL_REQUEST);
				return dialog.getButton("Yes").triggerClick();
			}
		}).run();
	}

	@Test
	@ExpectedDataSet({ "../models/expected/cancelReserves.xml" })
	public void testCancelReservation() {
		TabGroup tabGroup = panel.getTabGroup();

		tabGroup.selectTab("Reservations");
		final Table reservation = tabGroup.getSelectedTab().getTable();
		assertTrue(reservation.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Reserved",
						"Queue Number", "Cancel" }));
		assertEquals(2, reservation.getRowCount());

		WindowInterceptor.init(new Trigger() {

			@Override
			public void run() throws Exception {
				reservation.click(0, 5);
			}
		}).process(new WindowHandler() {
			public Trigger process(Window dialog) {
				assertEquals("Confirm", dialog.getTitle());
				dialog.containsLabel(Constants.CONFIRM_CANCEL_RESERVATION);
				return dialog.getButton("Yes").triggerClick();
			}
		}).run();
	}

	@Test
	@DataSet({ "../models/emptyBorrows.xml", "../models/emptyReserves.xml" })
	public void testEmptyState() {
		TabGroup tabGroup = panel.getTabGroup();
		Table request = tabGroup.getSelectedTab().getTable();
		assertTrue(request.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Requested",
						"Cancel" }));
		assertEquals(0, request.getRowCount());

		tabGroup.selectTab("Reservations");
		Table reservation = tabGroup.getSelectedTab().getTable();
		assertTrue(reservation.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Reserved",
						"Queue Number", "Cancel" }));
		assertEquals(0, reservation.getRowCount());

		tabGroup.selectTab("On Loan");
		Table onLoan = tabGroup.getSelectedTab().getTable();
		assertTrue(onLoan.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Borrowed",
						"Due Date" }));
		assertEquals(0, onLoan.getRowCount());

		tabGroup.selectTab("History");
		Table history = tabGroup.getSelectedTab().getTable();
		assertTrue(history.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Date Borrowed",
						"Date Returned" }));
		assertEquals(0, history.getRowCount());

		tabGroup.selectTab("Requests");

	}

}
