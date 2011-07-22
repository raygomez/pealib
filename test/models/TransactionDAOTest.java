package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.PropertyLoader;

@DataSet({ "book.xml", "user.xml", "reserves.xml" })
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
	public void getBorrowClass() throws Exception {
		User user = new UserDAO().getUserById(1);
		Book book = new BookDAO().getBookById(1);

		assertNotNull(user);
		assertNotNull(book);
		// Borrow borrow = transactionDAO.getBorrowClass(book, user);
		//
		// assertNotNull(borrow);
		// assertEquals(1,borrow.getId());

	}

}
