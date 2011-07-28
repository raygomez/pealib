package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Connector {

	static private String userId_;
	static private String password_;
	static private String hostname_;
	static private Connection con_;
	static private String db_;

	public Connector(String filename) {
		Properties properties = new PropertyLoader(filename).getProperties();
		hostname_ = properties.getProperty("app.hostname");
		db_ = properties.getProperty("app.db");
		userId_ = properties.getProperty("app.username");
		password_ = properties.getProperty("app.password");
	}

	public Connector() {
		Properties properties = new PropertyLoader(Constants.APP_CONFIG)
				.getProperties();
		hostname_ = properties.getProperty("app.hostname");
		db_ = properties.getProperty("app.db");
		userId_ = properties.getProperty("app.username");
		password_ = properties.getProperty("app.password");
	}

	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://" + hostname_ + "/" + db_;
		con_ = DriverManager.getConnection(url, userId_, password_);
		return con_;
	}

	public static void close() throws Exception {
		con_.close();
	}
}
