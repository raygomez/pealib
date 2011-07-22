package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;

@DataSet({ "book.xml", "user.xml", "reserves.xml", "borrows.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class TransactionDAOTest {

	TransactionDAO transactionDAO;
	UserDAO userDAO;

	@Before
	public void setUp() throws Exception {
		new Connector("test.config");
		transactionDAO = new TransactionDAO();
		userDAO = new UserDAO();
	}

	@Test
	public void testGetBorrowTransaction() throws Exception {
		User user = UserDAO.getUserById(1);
		Book book = BookDAO.getBookById(1);

		assertNotNull(user);
		assertNotNull(book);
		BorrowTransaction borrow = TransactionDAO.getBorrowTransaction(book, user);
		assertNotNull(borrow);
		assertEquals(1, borrow.getId());
		assertEquals(book, borrow.getBook());
		assertEquals(user, borrow.getUser());
		assertEquals("2011-06-15", borrow.getDateRequested().toString());
		assertEquals("2011-06-15", borrow.getDateBorrowed().toString());
		assertEquals("2011-06-15", borrow.getDateReturned().toString());
	}
	
	@Test
	public void testGetBorrowTransactionIfNotExisting() throws Exception {
		User user = UserDAO.getUserById(1);
		Book book = BookDAO.getBookById(2);

		assertNotNull(user);
		assertNotNull(book);
		BorrowTransaction borrow = TransactionDAO.getBorrowTransaction(book, user);
		assertNull(borrow);
	}
}
