package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyConnection {

	private String userId;
	private String password;
	private String hostname;
	private Connection con;
	private String db;

	public MyConnection(String hostname, String db, String userId,
			String password) {
		setHostname(hostname);
		setDb(db);
		setUserId(userId);
		setPassword(password);
	}

	public MyConnection(Properties properties) {
		setHostname(properties.getProperty("app.hostname"));
		setDb(properties.getProperty("app.db"));
		setUserId(properties.getProperty("app.username"));
		setPassword(properties.getProperty("app.password"));
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname
	 *            the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the db
	 */
	public String getDb() {
		return db;
	}

	/**
	 * @param db
	 *            the db to set
	 */
	public void setDb(String db) {
		this.db = db;
	}

	public Connection getConnection() throws SQLException,
			ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://" + getHostname() + "/" + getDb();
		con = DriverManager.getConnection(url, getUserId(), getPassword());
		return con;
	}
}
