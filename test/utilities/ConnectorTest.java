package utilities;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class ConnectorTest {

	@Test
	public void testGetConnectionWithConfig() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		Connection actual = Connector.getConnection();
		Connection expected = null;

		expected = DriverManager.getConnection("jdbc:mysql://localhost/test",
				"root", "");

		assertEquals(actual.getCatalog(), expected.getCatalog());
		assertEquals(actual.getClass(), expected.getClass());
		
		Connector.close();
	}

	@Test
	public void testGetConnection() throws Exception {
		Connector.init();
		Connection actual = Connector.getConnection();
		Connection expected = null;

		expected = DriverManager.getConnection("jdbc:mysql://10.127.160.190/pealib",
				"root", "1234");

		assertEquals(actual.getCatalog(), expected.getCatalog());
		assertEquals(actual.getClass(), expected.getClass());
		
		Connector.close();
	}
}
