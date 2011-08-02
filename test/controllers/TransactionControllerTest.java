package controllers;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.Key;
import org.uispec4j.TabGroup;
import org.uispec4j.Table;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.interception.WindowInterceptor;
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
		Connector.init(Constants.TEST_CONFIG);
		
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
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
	}
	
	@Test
	public void testOutgoingTabSwitching() {
		
		tabGroup.selectTab("Incoming");
		
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();
		
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				tabGroup.selectTab("Incoming");
			}
		}).processTransientWindow().run();
		
		/* Re-open Outgoing tab */
		TextBox searchBox = tabGroup.getSelectedTab().getInputTextBox();
		assertTrue(searchBox.getText().isEmpty());

		Button searchButton = tabGroup.getSelectedTab().getButton("Search");
		assertNotNull(searchButton);
		assertTrue(searchButton.isEnabled());
		
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();

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
				// TODO Auto-generated method stub
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();

		/* Re-open Incoming Tab */
		TextBox searchBox = tabGroup.getSelectedTab().getInputTextBox();
		assertTrue(searchBox.getText().isEmpty());

		Button searchButton = tabGroup.getSelectedTab().getButton("Search");
		assertNotNull(searchButton);
		assertTrue(searchButton.isEnabled());
		
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				tabGroup.selectTab("Incoming");
			}
		}).processTransientWindow().run();

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
	}
	
	@Test
	public void testGrant() {
		
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();
		
		Table outgoingItems = tabGroup.getSelectedTab().getTable();

		/* select first entry */
		outgoingItems.click(0, 0);

		Button grantButton = tabGroup.getSelectedTab().getButton("Grant");
		Button denyButton = tabGroup.getSelectedTab().getButton("Deny");
		
		assertTrue(grantButton.isEnabled());
		assertTrue(denyButton.isEnabled());
		
		WindowInterceptor.init(grantButton.triggerClick()).processTransientWindow().run();
				
		assertThat(outgoingItems.contentEquals( new Object[][] {
			{ "1234567890124", "title2", "author1", "ndizon (Niel Dizon)","2011-06-15" },
			{ "1234567890125", "title3Pantaleon", "author2", "ndizon (Niel Dizon)",	"2011-06-15" },
			{ "1234567890120", "Harry Poter 1", "Ewan ko", "jvillar (Jomel Pantaleon)", "2011-06-15" }
		} ));

		assertFalse(grantButton.isEnabled());
		assertFalse(denyButton.isEnabled());
	}
	
	@Test
	public void testDeny() {

		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();
		
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
		}).processTransientWindow().run();
		
		
		assertThat(outgoingItems.contentEquals( new Object[][] {
				{ "1234567890121", "Harry Poter 2", "Niel",	"apantaleon (Annuary Pantaleon)", "2011-06-15" },
				{ "1234567890125", "title3Pantaleon", "author2", "ndizon (Niel Dizon)",	"2011-06-15" },
				{ "1234567890120", "Harry Poter 1", "Ewan ko", "jvillar (Jomel Pantaleon)", "2011-06-15" }
		} ));

		assertFalse(grantButton.isEnabled());
		assertFalse(denyButton.isEnabled());
	}
	

	
	@Test
	public void testReturn() {
		tabGroup.selectTab("Incoming");
		Table incomingItems = tabGroup.getSelectedTab().getTable();
		
		/* select third entry */
		incomingItems.click(2, 0);
		
		Button returnButton = tabGroup.getSelectedTab().getButton("Return");
		assertTrue(returnButton.isEnabled());
		
		returnButton.click();
		
		assertThat(incomingItems.contentEquals( new Object[][] {
				{ "1234567890121", "Harry Poter 2", "Niel",	"nlazada (Niel Lazada)", "2011-06-15" },
				{ "1234567890121", "Harry Poter 2", "Niel",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890124", "title2", "author1",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890121", "Harry Poter 2", "Niel",	"kserrano (Karlo Serrano)", "2011-06-15" }
		} ));
		
		assertFalse(returnButton.isEnabled());
	}
	
	@Test
	@DataSet({ "../models/borrows.xml", "../models/transactionReserves.xml" })
	public void testReturnWithReservation() {
		tabGroup.selectTab("Incoming");
		Table incomingItems = tabGroup.getSelectedTab().getTable();
		
		/* select first entry */
		incomingItems.click(0, 0);
		
		Button returnButton = tabGroup.getSelectedTab().getButton("Return");
		assertTrue(returnButton.isEnabled());
		
		returnButton.click();
		
		assertThat(incomingItems.contentEquals( new Object[][] {
				{ "1234567890121", "Harry Poter 2", "Niel",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890123", "title1", "author1",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890124", "title2", "author1",	"jvillar (Jomel Pantaleon)", "2011-06-15" },
				{ "1234567890121", "Harry Poter 2", "Niel",	"kserrano (Karlo Serrano)", "2011-06-15" }
		} ));
		
		assertFalse(returnButton.isEnabled());
	}
	
	@Test
	public void testEmptyIncomingSearchThruButton () {
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
		
		
		Table incomingItems = tabGroup.getSelectedTab().getTable();
		assertEquals(0, incomingItems.getRowCount());
		assertThat(incomingItems.contentEquals( new Object[][] {} ));
	}
	
	@Ignore
	@Test
	public void testEmptyIncomingSearchThruText () throws Exception {
		tabGroup.selectTab("Incoming");
		final TextBox searchBox = tabGroup.getSelectedTab().getInputTextBox();
		

		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				searchBox.releaseKey(Key.X);
				searchBox.releaseKey(Key.ENTER);
			}
		}).processTransientWindow().run();
		

		
		Table incomingItems = tabGroup.getSelectedTab().getTable();
		assertEquals(0, incomingItems.getRowCount());
		assertThat(incomingItems.contentEquals( new Object[][] {} ));
	}

	@Ignore
	@Test
	public void testDenyWithReservation() {

		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				tabGroup.selectTab("Outgoing");
			}
		}).processTransientWindow().run();
		
		Table outgoingItems = tabGroup.getSelectedTab().getTable();

		/* select third entry */
		outgoingItems.click(2, 0);

		Button grantButton = tabGroup.getSelectedTab().getButton("Grant");
		Button denyButton = tabGroup.getSelectedTab().getButton("Deny");
		
		assertTrue(grantButton.isEnabled());
		assertTrue(denyButton.isEnabled());
		
		denyButton.click();
		
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
}
