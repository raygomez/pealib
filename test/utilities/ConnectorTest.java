package utilities;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ConnectorTest {

	@Test
	public void testMyConnectionStringStringStringString() {
		new Connector("localhost", "test", "root", "");
		assertEquals(Connector.getHostname(), "localhost");
		assertEquals(Connector.getDb(), "test");
		assertEquals(Connector.getUserId(), "root");
		assertEquals(Connector.getPassword(), "");
	}

	@Test(expected = SQLException.class)
	public void testGetConnectionIfNotExists() throws Exception {

		new Connector("localhost", "mylib", "root", "1y");
		Connector.getConnection();
	}

	@Test
	public void testGetConnection() throws Exception {
		new Connector("localhost", "test", "root", "");
		Connection actual = Connector.getConnection();
		Connection expected = null;

		expected = DriverManager.getConnection("jdbc:mysql://localhost/test",
				"root", "");

		assertEquals(actual.getCatalog(), expected.getCatalog());
		assertEquals(actual.getClass(), expected.getClass());
	}

}
