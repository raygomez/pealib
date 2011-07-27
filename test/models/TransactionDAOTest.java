package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "book.xml", "user.xml", "reserves.xml", "borrows.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class TransactionDAOTest {

	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
	}

	@Test
	@ExpectedDataSet({ "expected/receiveBorrows.xml" })
	public void testReturnBook() throws Exception {
		User user = UserDAO.getUserById(1);
		BorrowTransaction bt = TransactionDAO.getOnLoanBooks(user).get(0);
		TransactionDAO.returnBook(bt);
	}

	@Test
	public void testReserveBook() throws Exception {
		User user = UserDAO.getUserById(4);
		Book book = BookDAO.getBookById(2);
		TransactionDAO.reserveBook(book, user);
		assertTrue(TransactionDAO.isReservedByUser(book, user));
	}

	@Test
	@ExpectedDataSet({ "expected/requestBorrows.xml" })
	public void testRequestBook() throws Exception {
		User user = UserDAO.getUserById(1);
		Book book = BookDAO.getBookById(6);
		TransactionDAO.requestBook(book, user);
	}

	@Test
	@ExpectedDataSet({ "expected/borrowBorrows.xml" })
	public void testBorrowBook() throws Exception {
		User user = UserDAO.getUserById(2);
		Book book = BookDAO.getBookById(2);
		TransactionDAO.borrowBook(book, user);
	}

	@Test
	@ExpectedDataSet({ "expected/borrowBorrows.xml" })
	public void testBorrowBookWithBorrowTransaction() throws Exception {
		BorrowTransaction bTransaction = TransactionDAO
				.getBorrowTransactionById(2);
		TransactionDAO.borrowBook(bTransaction);
	}

	@Test
	@ExpectedDataSet({ "expected/cancelReserves.xml" })
	public void testCancelReservation() throws Exception {
		User user = UserDAO.getUserById(1);
		Book book = BookDAO.getBookById(1);
		ReserveTransaction rtTransaction = TransactionDAO
				.getReserveTransaction(user, book);
		TransactionDAO.cancelReservation(rtTransaction);
	}

	@Test
	@ExpectedDataSet({ "expected/denyBookRequestBorrows.xml" })
	public void testDenyBookRequest() throws Exception {
		BorrowTransaction bTransaction = TransactionDAO
				.getBorrowTransactionById(2);
		TransactionDAO.denyBookRequest(bTransaction);
	}

	@Test
	public void testIsReserved() throws Exception {
		User user = UserDAO.getUserById(1);
		Book book = BookDAO.getBookById(1);
		assertTrue(TransactionDAO.isReservedByUser(book, user));
	}

	@Test
	public void testIsNotReserved() throws Exception {
		User user = UserDAO.getUserById(4);
		Book book = BookDAO.getBookById(1);
		assertFalse(TransactionDAO.isReservedByUser(book, user));
	}

	@Test
	public void testGetReservedBooksMany() throws Exception {
		User user = UserDAO.getUserById(1);
		Book book1 = BookDAO.getBookById(1);
		Book book2 = BookDAO.getBookById(2);
		Book book3 = BookDAO.getBookById(3);
		
		ArrayList<ReserveTransaction> transactions = TransactionDAO
				.getReservedBooks(user);
		ReserveTransaction r1 = TransactionDAO.getReserveTransaction(user, book1);
		ReserveTransaction r2 = TransactionDAO.getReserveTransaction(user, book2);
		ReserveTransaction r3 = TransactionDAO.getReserveTransaction(user, book3);
		assertReflectionEquals(r1, transactions.get(0));
		assertReflectionEquals(r2, transactions.get(1));
		assertReflectionEquals(r3, transactions.get(2));
		assertEquals(3, transactions.size());
	}

	@Test
	public void testGetReservedBooksOne() throws Exception {
		User user = UserDAO.getUserById(3);
		Book book = BookDAO.getBookById(3);
		ArrayList<ReserveTransaction> transactions = TransactionDAO
				.getReservedBooks(user);
		ReserveTransaction r1 = TransactionDAO.getReserveTransaction(user, book);
		assertReflectionEquals(r1, transactions.get(0));
		assertEquals(1, transactions.size());
	}

	@Test
	public void testGetReservedBooksNone() throws Exception {
		User user = UserDAO.getUserById(4);
		ArrayList<ReserveTransaction> transactions = TransactionDAO
				.getReservedBooks(user);
		assertEquals(0, transactions.size());
	}

	@Test
	public void testGetHistoryMany() throws Exception {
		User user = UserDAO.getUserById(1);
		ArrayList<BorrowTransaction> transactions = TransactionDAO
				.getHistory(user);
		assertEquals(2, transactions.size());
	}

	@Test
	public void testGetHistoryNone() throws Exception {
		User user = UserDAO.getUserById(3);
		ArrayList<BorrowTransaction> transactions = TransactionDAO
				.getHistory(user);
		assertEquals(0, transactions.size());
	}

	@Test
	public void testGetHistoryOne() throws Exception {
		User user = UserDAO.getUserById(2);
		ArrayList<BorrowTransaction> transactions = TransactionDAO
				.getHistory(user);
		assertEquals(1, transactions.size());
	}

	@Test
	public void testGetRequestedMany() throws Exception {
		User user = UserDAO.getUserById(4);
		ArrayList<BorrowTransaction> transactions = TransactionDAO
				.getRequestedBooks(user);
		assertEquals(2, transactions.size());
	}

	@Test
	public void testGetRequestedNone() throws Exception {
		User user = UserDAO.getUserById(3);
		ArrayList<BorrowTransaction> transactions = TransactionDAO
				.getRequestedBooks(user);
		assertEquals(0, transactions.size());
	}

	@Test
	public void testGetRequestedOne() throws Exception {
		User user = UserDAO.getUserById(2);
		ArrayList<BorrowTransaction> transactions = TransactionDAO
				.getRequestedBooks(user);
		assertEquals(1, transactions.size());
	}

	@Test
	public void testGetQueueInReservationFirst() throws Exception {
		User user = UserDAO.getUserById(1);
		Book book = BookDAO.getBookById(2);
		assertEquals(1, TransactionDAO.getQueueInReservation(book, user));

	}

	@Test
	public void testGetQueueInReservationNotFirst() throws Exception {
		User user = UserDAO.getUserById(3);
		Book book = BookDAO.getBookById(3);
		assertEquals(3, TransactionDAO.getQueueInReservation(book, user));
	}

	@Test
	public void testGetQueueInReservationNotReserved() throws Exception {
		User user = UserDAO.getUserById(4);
		Book book = BookDAO.getBookById(3);
		assertEquals(0, TransactionDAO.getQueueInReservation(book, user));
	}

	@Test
	public void testGetAvailableCopiesNoBorrows() throws Exception {
		Book book = BookDAO.getBookById(1);
		assertEquals(1, TransactionDAO.getAvailableCopies(book));
	}

	@Test
	public void testGetAvailableCopiesWithLoanAndRequests() throws Exception {
		Book book = BookDAO.getBookById(5);
		assertEquals(2, TransactionDAO.getAvailableCopies(book));
	}

	@Test
	public void testGetDaysOverdue() throws Exception {
		User user = UserDAO.getUserById(3);
		Book book = BookDAO.getBookById(2);
		assertEquals(42, TransactionDAO.getDaysOverdue(book, user));
	}

	@Test
	public void testGetDaysOverdueBorrowTransaction() throws Exception {
		BorrowTransaction transaction = TransactionDAO
				.getBorrowTransactionById(3);
		assertEquals(42, TransactionDAO.getDaysOverdue(transaction));
	}

	@Test
	public void testSearchOutgoingBookAll() throws Exception {
		ArrayList<BorrowTransaction> list = TransactionDAO
				.searchOutgoingBook("*");
		assertEquals(3, list.size());
	}

	@Test
	public void testSearchOutgoingBookNone() throws Exception {
		ArrayList<BorrowTransaction> list = TransactionDAO
				.searchOutgoingBook("gomez0");
		assertEquals(0, list.size());
	}

	@Test
	public void testSearchOutgoingBookMany() throws Exception {
		ArrayList<BorrowTransaction> list = TransactionDAO
				.searchOutgoingBook("title");
		assertEquals(2, list.size());
	}

	@Test
	public void testSearchOutgoingBookManyUserBook() throws Exception {
		ArrayList<BorrowTransaction> list = TransactionDAO
				.searchOutgoingBook("Pantaleon");
		assertEquals(2, list.size());
	}

	@Test
	public void testSearchIncomingBookAll() throws Exception {
		ArrayList<BorrowTransaction> list = TransactionDAO
				.searchIncomingBook("*");
		assertEquals(5, list.size());
	}

	@Test
	public void testSearchIncomingBookNone() throws Exception {
		ArrayList<BorrowTransaction> list = TransactionDAO
				.searchIncomingBook("gomez0");
		assertEquals(0, list.size());
	}

	@Test
	public void testSearchIncomingBookMany() throws Exception {
		ArrayList<BorrowTransaction> list = TransactionDAO
				.searchIncomingBook("Pantaleon");
		assertEquals(3, list.size());
	}

	@Test
	public void testSearchIncomingBookManyUserBook() throws Exception {
		ArrayList<BorrowTransaction> list = TransactionDAO
				.searchIncomingBook("Niel");
		assertEquals(3, list.size());
	}

	@Test
	public void testIsBorrowedByUser() throws Exception {
		User user = UserDAO.getUserById(3);
		Book book = BookDAO.getBookById(2);
		assertTrue(TransactionDAO.isBorrowedByUser(book, user));
	}

	@Test
	public void testIsNotBorrowedByUser() throws Exception {
		User user = UserDAO.getUserById(1);
		Book book = BookDAO.getBookById(1);
		assertFalse(TransactionDAO.isBorrowedByUser(book, user));
	}

	@Test
	public void testGetBorrowTransactionById() throws Exception {
		User user = UserDAO.getUserById(1);
		Book book = BookDAO.getBookById(1);
		BorrowTransaction transaction = TransactionDAO
				.getBorrowTransactionById(1);

		assertNotNull(transaction);
		assertEquals(1, transaction.getId());
		assertReflectionEquals(user, transaction.getUser());
		assertReflectionEquals(book, transaction.getBook());
		assertEquals("2011-06-15", transaction.getDateRequested().toString());
		assertEquals("2011-06-15", transaction.getDateBorrowed().toString());
		assertEquals("2011-06-15", transaction.getDateReturned().toString());
	}

	@Test
	public void testGetBorrowTransactionByIdNotExisting() throws Exception {
		BorrowTransaction transaction = TransactionDAO
				.getBorrowTransactionById(0);

		assertNull(transaction);
	}

	@Test
	public void testGetReserveTransaction() throws Exception {
		User user = UserDAO.getUserById(1);
		Book book = BookDAO.getBookById(1);
		ReserveTransaction transaction = TransactionDAO.getReserveTransaction(
				user, book);
		assertReflectionEquals(user, transaction.getUser());
		assertReflectionEquals(book, transaction.getBook());
		assertEquals("2011-07-27", transaction.getDateReserved().toString());
	}

	@Test
	public void testGetReserveTransactionNotExisting() throws Exception {
		User user = UserDAO.getUserById(2);
		Book book = BookDAO.getBookById(1);
		ReserveTransaction transaction = TransactionDAO.getReserveTransaction(
				user, book);
		assertNull(transaction);
	}
}
