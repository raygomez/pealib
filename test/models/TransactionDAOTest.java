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
	@ExpectedDataSet({"expected/receiveBorrows.xml"})
	public void testReceiveBook() throws Exception{
		User user = UserDAO.getUserById(1);
		BorrowTransaction bt = TransactionDAO.getOnLoanBooks(user).get(0);
		TransactionDAO.receiveBook(bt);
	}

	@Test
	@ExpectedDataSet({"expected/addReserves.xml"})
	public void testReserveBook() throws Exception{
		User user = UserDAO.getUserById(4);
		Book book = BookDAO.getBookById(2);
		TransactionDAO.reserveBook(book, user);
	}
}
