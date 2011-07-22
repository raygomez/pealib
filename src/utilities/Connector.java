package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Connector {

	static private String userId;
	static String password;
	static private String hostname;
	static private Connection con;
	static private String db;

	public Connector(String hostname, String db, String userId, String password) {
		setHostname(hostname);
		setDb(db);
		setUserId(userId);
		setPassword(password);
	}

	public Connector(String filename) {
		Properties properties = new PropertyLoader(filename).getProperties();
		setHostname(properties.getProperty("app.hostname"));
		setDb(properties.getProperty("app.db"));
		setUserId(properties.getProperty("app.username"));
		setPassword(properties.getProperty("app.password"));
	}

	public Connector() {
		Properties properties = new PropertyLoader(Constants.APP_CONFIG)
				.getProperties();
		setHostname(properties.getProperty("app.hostname"));
		setDb(properties.getProperty("app.db"));
		setUserId(properties.getProperty("app.username"));
		setPassword(properties.getProperty("app.password"));
	}

	public static String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		Connector.userId = userId;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		Connector.password = password;
	}

	/**
	 * @return the hostname
	 */
	public static String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname
	 *            the hostname to set
	 */
	public void setHostname(String hostname) {
		Connector.hostname = hostname;
	}

	/**
	 * @return the db
	 */
	public static String getDb() {
		return db;
	}

	/**
	 * @param db
	 *            the db to set
	 */
	public void setDb(String db) {
		Connector.db = db;
	}

	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://" + getHostname() + "/" + getDb();
		con = DriverManager.getConnection(url, getUserId(), getPassword());
		return con;
	}

	public static void close() throws Exception {
		getConnection().close();
	}
}
