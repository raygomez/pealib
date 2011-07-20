package utilities;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import utilities.PropertyLoader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class MyConnectionTest {

	@Test
	public void testMyConnectionStringStringStringString() {
		MyConnection myConnection = new MyConnection("localhost", "test",
				"root", "");
		assertEquals(myConnection.getHostname(), "localhost");
		assertEquals(myConnection.getDb(), "test");
		assertEquals(myConnection.getUserId(), "root");
		assertEquals(myConnection.getPassword(), "");
	}

	@Test
	public void testMyConnectionProperties() {
		Properties properties = new PropertyLoader("test.config")
				.getProperties();

		MyConnection myConnection = new MyConnection(properties);
		assertEquals(myConnection.getHostname(), "localhost");
		assertEquals(myConnection.getDb(), "test");
		assertEquals(myConnection.getUserId(), "root");
		assertEquals(myConnection.getPassword(), "");
	}

	@Test(expected = SQLException.class)
	public void testGetConnectionIfNotExists() throws SQLException,
			ClassNotFoundException {

		MyConnection myConnection = new MyConnection("localhost", "mylib",
				"root", "1y");
		myConnection.getConnection();
	}

	@Test
	public void testGetConnection() throws SQLException, ClassNotFoundException {
		MyConnection myConnection = new MyConnection("localhost", "test",
				"root", "");
		Connection actual = myConnection.getConnection();
		Connection expected = null;

		expected = DriverManager.getConnection("jdbc:mysql://localhost/test",
				"root", "");

		assertEquals(actual.getCatalog(), expected.getCatalog());
		assertEquals(actual.getClass(), expected.getClass());
	}

}
