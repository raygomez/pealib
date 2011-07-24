package models;

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
	@ExpectedDataSet({ "expected/addReserves.xml" })
	public void testReserveBook() throws Exception {
		User user = UserDAO.getUserById(4);
		Book book = BookDAO.getBookById(2);
		TransactionDAO.reserveBook(book, user);
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
		TransactionDAO.borrowBook(bTransaction);
		TransactionDAO.denyBookRequest(bTransaction);
	}
}
