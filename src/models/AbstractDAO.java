package models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import utilities.Connector;
import utilities.PropertyLoader;

public abstract class AbstractDAO {

	private Connection connection;

	public AbstractDAO() throws Exception {
		super();
		Properties properties = new PropertyLoader("app.config")
				.getProperties();
		setConnection(Connector.getConnection());
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
