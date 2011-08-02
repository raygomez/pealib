package controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.TabGroup;
import org.uispec4j.Table;
import org.uispec4j.TextBox;
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
public class TransactionControllerTest extends UISpecTestCase {
	TabGroup tabGroup;

	@Before
	public void setUp() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				tabGroup = new TabGroup(new TransactionController().getTabbedPane());
			}
		}).processTransientWindow().run();
	}

	@Test
	public void testInitialStateIncoming() {
		tabGroup.selectTab("Incoming");
		
		TextBox searchBox = tabGroup.getSelectedTab().getInputTextBox();
		assertTrue(searchBox.getText().isEmpty());

		Button searchButton = tabGroup.getSelectedTab().getButton("Search");
		assertNotNull(searchButton);
		assertTrue(searchButton.isEnabled());
		
		String expectedTotal = "Total Matches: 5";
		TextBox matchesLabel = tabGroup.getSelectedTab().getTextBox("totalLabel");
		assertTrue(expectedTotal.equals(matchesLabel.getText()));
		
		Table incoming = tabGroup.getSelectedTab().getTable();
		assertTrue(incoming.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Username",
						"Date Borrowed" }));
		assertEquals(5, incoming.getRowCount());
		assertThat(incoming.contentEquals( new Object[][] {
			{ "1234567890121", "Harry Poter 2", "Niel",	"nlazada (Niel Lazada)", "2011-06-15" },
			{ "1234567890121", "Harry Poter 2", "Niel",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
			{ "1234567890123", "title1", "author1",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
			{ "1234567890124", "title2", "author1",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
			{ "1234567890121", "Harry Poter 2", "Niel",	"kserrano (Karlo Serrano)", "2011-06-15" }
		} ));
		
		Button returnButton = tabGroup.getSelectedTab().getButton("Return");
		assertNotNull(returnButton);
		assertFalse(returnButton.isEnabled());
		
		String expectedOverdue = "";
		TextBox returnLabel = tabGroup.getSelectedTab().getTextBox("daysOverdueLabel");
		assertTrue(expectedOverdue.equals(returnLabel.getText()));
	}
	
	@Test
	public void testOutgoingTabSwitching() {
		tabGroup.selectTab("Incoming");
		
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();
		
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				tabGroup.selectTab("Incoming");
			}
		}).processTransientWindow().run();
		
		/* Re-open Outgoing tab */
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();

		TextBox searchBox = tabGroup.getSelectedTab().getInputTextBox();
		assertTrue(searchBox.getText().isEmpty());

		Button searchButton = tabGroup.getSelectedTab().getButton("Search");
		assertNotNull(searchButton);
		assertTrue(searchButton.isEnabled());
		
		String expectedTotal = "Total Matches: 4";
		TextBox matchesLabel = tabGroup.getSelectedTab().getTextBox("totalLabel");
		assertTrue(expectedTotal.equals(matchesLabel.getText()));
		
		Table outgoing = tabGroup.getSelectedTab().getTable();
		assertTrue(outgoing.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Username",
						"Date Requested" }));
		assertEquals(4, outgoing.getRowCount());
		assertThat(outgoing.contentEquals( new Object[][] {
			{ "1234567890121", "Harry Poter 2", "Niel",	"apantaleon (Annuary Pantaleon)", "2011-06-15" },
			{ "1234567890124", "title2", "author1", "ndizon (Niel Dizon)", "2011-06-15" },
			{ "1234567890125", "title3Pantaleon", "author2", "ndizon (Niel Dizon)",	"2011-06-15" },
			{ "1234567890120", "Harry Poter 1", "Ewan ko", "jvillar (Jomel Pantaleon)", "2011-06-15" }
		} ));

		Button grantButton = tabGroup.getSelectedTab().getButton("Grant");
		assertNotNull(grantButton);
		assertFalse(grantButton.isEnabled());
		
		Button denyButton = tabGroup.getSelectedTab().getButton("Deny");
		assertNotNull(denyButton);
		assertFalse(denyButton.isEnabled());
	}

	@Test
	public void testIncomingTabSwitching() {
		tabGroup.selectTab("Incoming");

		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();

		/* Re-open Incoming Tab */
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				tabGroup.selectTab("Incoming");
			}
		}).processTransientWindow().run();

		TextBox searchBox = tabGroup.getSelectedTab().getInputTextBox();
		assertTrue(searchBox.getText().isEmpty());

		Button searchButton = tabGroup.getSelectedTab().getButton("Search");
		assertNotNull(searchButton);
		assertTrue(searchButton.isEnabled());
		
		String expectedTotal = "Total Matches: 5";
		TextBox matchesLabel = tabGroup.getSelectedTab().getTextBox("totalLabel");
		assertTrue(expectedTotal.equals(matchesLabel.getText()));
		
		Table incoming = tabGroup.getSelectedTab().getTable();
		assertTrue(incoming.getHeader().contentEquals(
				new String[] { "ISBN", "Title", "Author", "Username",
						"Date Borrowed" }));
		assertEquals(5, incoming.getRowCount());
		assertThat(incoming.contentEquals( new Object[][] {
				{ "1234567890121", "Harry Poter 2", "Niel",	"nlazada (Niel Lazada)", "2011-06-15" },
				{ "1234567890121", "Harry Poter 2", "Niel",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890123", "title1", "author1",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890124", "title2", "author1",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890121", "Harry Poter 2", "Niel",	"kserrano (Karlo Serrano)", "2011-06-15" }
		} ));
		
		Button returnButton = tabGroup.getSelectedTab().getButton("Return");
		assertNotNull(returnButton);
		assertFalse(returnButton.isEnabled());
		
		String expectedOverdue = "";
		TextBox returnLabel = tabGroup.getSelectedTab().getTextBox("daysOverdueLabel");
		assertTrue(expectedOverdue.equals(returnLabel.getText()));
	}
	
	@Test
	public void testEmptyIncomingSearchThruButton() {
		tabGroup.selectTab("Incoming");
		TextBox searchBox = tabGroup.getSelectedTab().getInputTextBox();
		searchBox.setText("search");
		
		final Button searchButton = tabGroup.getSelectedTab().getButton("Search");
		
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				searchButton.click();
			}
		}).processTransientWindow().run();
		
		String expectedTotal = "Total Matches: 0";
		TextBox matchesLabel = tabGroup.getSelectedTab().getTextBox("totalLabel");
		assertTrue(expectedTotal.equals(matchesLabel.getText()));
		
		Table incomingItems = tabGroup.getSelectedTab().getTable();
		assertEquals(0, incomingItems.getRowCount());
		assertThat(incomingItems.contentEquals( new Object[][] {} ));
		
		String expectedOverdue = "";
		TextBox returnLabel = tabGroup.getSelectedTab().getTextBox("daysOverdueLabel");
		assertTrue(expectedOverdue.equals(returnLabel.getText()));
	}
	
	@Test
	@DataSet({ "../models/latestBorrows.xml" })
	public void testOverdue() {
		tabGroup.selectTab("Incoming");
		
		String expectedTotal = "Total Matches: 3";
		TextBox matchesLabel = tabGroup.getSelectedTab().getTextBox("totalLabel");
		assertTrue(expectedTotal.equals(matchesLabel.getText()));

		Table incomingItems = tabGroup.getSelectedTab().getTable();
		assertEquals(3, incomingItems.getRowCount());
		assertThat(incomingItems.contentEquals( new Object[][] {
				{ "1234567890125", "title3Pantaleon", "author2", "ndizon (Niel Dizon)",	"2011-07-18" },
				{ "1234567890121", "Harry Poter 2", "Niel",	"apantaleon (Annuary Pantaleon)", "2011-07-30" },
				{ "1234567890124", "title2", "author1", "ndizon (Niel Dizon)", "2011-08-02" }
		} ));

		String expectedHiddenOverdue = "";
		TextBox returnLabel = tabGroup.getSelectedTab().getTextBox("daysOverdueLabel");
		assertTrue(expectedHiddenOverdue.equals(returnLabel.getText()));
		
		incomingItems.click(1, 0);
		Button returnButton = tabGroup.getSelectedTab().getButton("Return");
		assertTrue(returnButton.isEnabled());
		String expectedNotOverdue = "Days Overdue: 0";
		returnLabel = tabGroup.getSelectedTab().getTextBox("daysOverdueLabel");
		assertTrue(expectedNotOverdue.equals(returnLabel.getText()));
		assertTrue(returnLabel.foregroundEquals("Black"));
		
		incomingItems.click(2, 0);
		assertTrue(returnButton.isEnabled());
		returnLabel = tabGroup.getSelectedTab().getTextBox("daysOverdueLabel");
		assertTrue(expectedNotOverdue.equals(returnLabel.getText()));
		assertTrue(returnLabel.foregroundEquals("Black"));
		
		incomingItems.click(0, 0);
		assertTrue(returnButton.isEnabled());
		returnLabel = tabGroup.getSelectedTab().getTextBox("daysOverdueLabel");
		String expectedOverdue = "Days Overdue: 1";
		System.out.println(returnLabel.getText());
		assertTrue(expectedOverdue.equals(returnLabel.getText()));
		assertTrue(returnLabel.foregroundEquals("Red"));
	}
	
	@Test
	@ExpectedDataSet({ "../models/expected/grantBorrows.xml" })
	public void testGrant() {
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();
		
		String expectedTotal = "Total Matches: 4";
		TextBox matchesLabel = tabGroup.getSelectedTab().getTextBox("totalLabel");
		assertTrue(expectedTotal.equals(matchesLabel.getText()));
		
		Table outgoingItems = tabGroup.getSelectedTab().getTable();
		/* select first entry */
		outgoingItems.click(0, 0);

		final Button grantButton = tabGroup.getSelectedTab().getButton("Grant");
		Button denyButton = tabGroup.getSelectedTab().getButton("Deny");
		
		assertTrue(grantButton.isEnabled());
		assertTrue(denyButton.isEnabled());
		
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				grantButton.click();
			}
		}).processTransientWindow().process(new WindowHandler("Borrow Request Granted") {
			public Trigger process(Window dialog) {
				String actualMessage = dialog.getTextBox("OptionPane.label").getText();
				String expectedMessage = "Successfully lent 1 book(s).";
				assertTrue(expectedMessage.equals(actualMessage));
				return dialog.getButton("OK").triggerClick();
			}
		})
		.run();
				
		assertThat(outgoingItems.contentEquals( new Object[][] {
			{ "1234567890124", "title2", "author1", "ndizon (Niel Dizon)","2011-06-15" },
			{ "1234567890125", "title3Pantaleon", "author2", "ndizon (Niel Dizon)",	"2011-06-15" },
			{ "1234567890120", "Harry Poter 1", "Ewan ko", "jvillar (Jomel Pantaleon)", "2011-06-15" }
		} ));

		assertFalse(grantButton.isEnabled());
		assertFalse(denyButton.isEnabled());
	}
	
	@Test
	@ExpectedDataSet({ "../models/expected/denyBorrows.xml" })
	public void testDeny() {
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();
		
		String expectedTotal = "Total Matches: 4";
		TextBox matchesLabel = tabGroup.getSelectedTab().getTextBox("totalLabel");
		assertTrue(expectedTotal.equals(matchesLabel.getText()));
		
		Table outgoingItems = tabGroup.getSelectedTab().getTable();
		/* select second entry */
		outgoingItems.click(1, 0);

		Button grantButton = tabGroup.getSelectedTab().getButton("Grant");
		final Button denyButton = tabGroup.getSelectedTab().getButton("Deny");
		
		assertTrue(grantButton.isEnabled());
		assertTrue(denyButton.isEnabled());
		
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				denyButton.click();
			}
		}).processTransientWindow().process(new WindowHandler("Borrow Request Denied") {
			public Trigger process(Window dialog) {
				String actualMessage = dialog.getTextBox("OptionPane.label").getText();
				String expectedMessage = "Refused to lend 1 book(s).";
				assertTrue(expectedMessage.equals(actualMessage));
				return dialog.getButton("OK").triggerClick();
			}
		}).run();
		
		assertThat(outgoingItems.contentEquals( new Object[][] {
				{ "1234567890121", "Harry Poter 2", "Niel",	"apantaleon (Annuary Pantaleon)", "2011-06-15" },
				{ "1234567890125", "title3Pantaleon", "author2", "ndizon (Niel Dizon)",	"2011-06-15" },
				{ "1234567890120", "Harry Poter 1", "Ewan ko", "jvillar (Jomel Pantaleon)", "2011-06-15" }
		} ));

		assertFalse(grantButton.isEnabled());
		assertFalse(denyButton.isEnabled());
	}
	
	@Test
	@ExpectedDataSet({ "../models/expected/denyBorrowsWithReservation.xml",
		"../models/expected/reservesNextUser.xml" })
	public void testDenyWithReservation() {
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();
		
		String expectedTotal = "Total Matches: 4";
		TextBox matchesLabel = tabGroup.getSelectedTab().getTextBox("totalLabel");
		assertTrue(expectedTotal.equals(matchesLabel.getText()));
		
		Table outgoingItems = tabGroup.getSelectedTab().getTable();
		/* select third entry */
		outgoingItems.click(2, 0);

		Button grantButton = tabGroup.getSelectedTab().getButton("Grant");
		final Button denyButton = tabGroup.getSelectedTab().getButton("Deny");
		
		assertTrue(grantButton.isEnabled());
		assertTrue(denyButton.isEnabled());
		
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				denyButton.click();
			}
		}).processTransientWindow().process(new WindowHandler("Borrow Request Denied") {
			public Trigger process(Window dialog) {
				String actualMessage = dialog.getTextBox("OptionPane.label").getText();
				String expectedMessage = "<html>Refused to lend "
				+ "1 book(s).<br>"
				+ "1 book(s) have pending reservations.<br>"
				+ "See Outgoing tab for details.";
				assertTrue(expectedMessage.equals(actualMessage));
				return dialog.getButton("OK").triggerClick();
			}
		})
		.run();
		
		assertTrue(expectedTotal.equals(matchesLabel.getText()));
		assertThat(outgoingItems.contentEquals( new Object[][] {
				{ "1234567890125", "title3Pantaleon", "author2", "jvillar (Jomel Pantaleon)",	"2011-08-02" },
				/* this should have been appended as the last entry. however test db allows null and is non-incrementing */
				
				{ "1234567890121", "Harry Poter 2", "Niel",	"apantaleon (Annuary Pantaleon)", "2011-06-15" },
				{ "1234567890124", "title2", "author1", "ndizon (Niel Dizon)", "2011-06-15" },
				{ "1234567890120", "Harry Poter 1", "Ewan ko", "jvillar (Jomel Pantaleon)", "2011-06-15" }
		} ));

		assertFalse(grantButton.isEnabled());
		assertFalse(denyButton.isEnabled());
	}
	
	@Test
	@ExpectedDataSet({ "../models/expected/returnBorrows.xml" })
	public void testReturn() {
		tabGroup.selectTab("Incoming");
		
		String expectedTotal = "Total Matches: 5";
		TextBox matchesLabel = tabGroup.getSelectedTab().getTextBox("totalLabel");
		assertTrue(expectedTotal.equals(matchesLabel.getText()));
		
		String initialExpectedOverdue = "";
		TextBox returnLabel = tabGroup.getSelectedTab().getTextBox("daysOverdueLabel");
		assertTrue(initialExpectedOverdue.equals(returnLabel.getText()));
		
		Table incomingItems = tabGroup.getSelectedTab().getTable();
		/* select third entry */
		incomingItems.click(2, 0);
		
		final Button returnButton = tabGroup.getSelectedTab().getButton("Return");
		assertTrue(returnButton.isEnabled());
		
		String expectedOverdue = "Days Overdue: 34";
		returnLabel = tabGroup.getSelectedTab().getTextBox("daysOverdueLabel");
		assertTrue(expectedOverdue.equals(returnLabel.getText()));
		assertTrue(returnLabel.foregroundEquals("Red"));
		
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				returnButton.click();
			}
		}).processTransientWindow().process(new WindowHandler("Borrowed Book Returned") {
			public Trigger process(Window dialog) {
				String actualMessage = dialog.getTextBox("OptionPane.label").getText();
				String expectedMessage = "Successfully returned 1 book(s).";
				assertTrue(expectedMessage.equals(actualMessage));
				return dialog.getButton("OK").triggerClick();
			}
		}).run();
		
		assertThat(incomingItems.contentEquals( new Object[][] {
				{ "1234567890121", "Harry Poter 2", "Niel",	"nlazada (Niel Lazada)", "2011-06-15" },
				{ "1234567890121", "Harry Poter 2", "Niel",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890124", "title2", "author1",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890121", "Harry Poter 2", "Niel",	"kserrano (Karlo Serrano)", "2011-06-15" }
		} ));
		
		String newExpectedOverdue = "";
		assertTrue(newExpectedOverdue.equals(returnLabel.getText()));

		assertFalse(returnButton.isEnabled());
	}
	
	@Test
	@DataSet({ "../models/borrows.xml", "../models/transactionReserves.xml" })
	@ExpectedDataSet({ "../models/expected/returnBorrowsWithReservation.xml",
		"../models/expected/transactionReservesNextUser.xml"})
	public void testReturnWithReservation() {
		tabGroup.selectTab("Incoming");
		
		String expectedTotal = "Total Matches: 5";
		TextBox matchesLabel = tabGroup.getSelectedTab().getTextBox("totalLabel");
		assertTrue(expectedTotal.equals(matchesLabel.getText()));
		
		String initialExpectedOverdue = "";
		TextBox returnLabel = tabGroup.getSelectedTab().getTextBox("daysOverdueLabel");
		assertTrue(initialExpectedOverdue.equals(returnLabel.getText()));
		
		Table incomingItems = tabGroup.getSelectedTab().getTable();
		/* select first entry */
		incomingItems.click(0, 0);
		
		final Button returnButton = tabGroup.getSelectedTab().getButton("Return");
		assertTrue(returnButton.isEnabled());
		
		String expectedOverdue = "Days Overdue: 34";
		returnLabel = tabGroup.getSelectedTab().getTextBox("daysOverdueLabel");
		assertTrue(expectedOverdue.equals(returnLabel.getText()));
		assertTrue(returnLabel.foregroundEquals("Red"));
		
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				returnButton.click();
			}
		}).processTransientWindow().process(new WindowHandler("Borrowed Book Returned") {
			public Trigger process(Window dialog) {
				String actualMessage = dialog.getTextBox("OptionPane.label").getText();
				String expectedMessage = "<html>Successfully returned 1 book(s).<br>"
				+ "1 book(s) have pending reservations.<br>"
				+ "See Outgoing tab for details.";
				assertTrue(expectedMessage.equals(actualMessage));
				return dialog.getButton("OK").triggerClick();
			}
		}).run();
		
		assertThat(incomingItems.contentEquals( new Object[][] {
				{ "1234567890121", "Harry Poter 2", "Niel",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890123", "title1", "author1",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890124", "title2", "author1",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890121", "Harry Poter 2", "Niel",	"kserrano (Karlo Serrano)", "2011-06-15" }
		} ));
		
		String newExpectedOverdue = "";
		assertTrue(newExpectedOverdue.equals(returnLabel.getText()));
		
		assertFalse(returnButton.isEnabled());
		
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();
		
		String outgoingExpectedTotal = "Total Matches: 5";
		TextBox outgoingMatchesLabel = tabGroup.getSelectedTab().getTextBox("totalLabel");
		assertTrue(outgoingExpectedTotal.equals(outgoingMatchesLabel.getText()));
		
		Table outgoingItems = tabGroup.getSelectedTab().getTable();
		assertEquals(5, outgoingItems.getRowCount());
		assertThat(outgoingItems.contentEquals( new Object[][] {
			{ "1234567890121", "Harry Poter 2", "Niel",	"ndizon (Niel Dizon)", "2011-08-02" },
			{ "1234567890121", "Harry Poter 2", "Niel",	"apantaleon (Annuary Pantaleon)", "2011-06-15" },
			{ "1234567890124", "title2", "author1", "ndizon (Niel Dizon)", "2011-06-15" },
			{ "1234567890125", "title3Pantaleon", "author2", "ndizon (Niel Dizon)",	"2011-06-15" },
			{ "1234567890120", "Harry Poter 1", "Ewan ko", "jvillar (Jomel Pantaleon)", "2011-06-15" }
		} ));
	}
}
