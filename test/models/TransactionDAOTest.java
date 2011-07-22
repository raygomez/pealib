package models;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.MyConnection;
import utilities.PropertyLoader;

@DataSet({"reserves.xml"})
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class TransactionDAOTest {

	TransactionDAO transactionDAO;

	@Before
	public void setUp() throws Exception {
		Connection con = new MyConnection(
				new PropertyLoader("test.config").getProperties())
				.getConnection();
		transactionDAO = new TransactionDAO();
		transactionDAO.setConnection(con);
	}

	@Test
	public void testreserved() {
		
		

	}

}
