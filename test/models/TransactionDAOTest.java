package models;

import org.junit.Before;
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

}
