package models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import utilities.MyConnection;
import utilities.PropertyLoader;

public class TransactionDAO {

	private Connection connection;
	
	public TransactionDAO() throws SQLException, ClassNotFoundException{
		Properties properties = new PropertyLoader("app.config").getProperties();
		connection = new MyConnection(properties).getConnection();
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
}
