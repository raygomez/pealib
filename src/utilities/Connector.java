package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

public class Connector {

	static private String userId_;
	static private String password_;
	static private String hostname_;
	static private Connection con_;
	static private String db_;

	public static void init(String filename) {
		Properties properties = new PropertyLoader(filename).getProperties();
		hostname_ = properties.getProperty("app.hostname");
		db_ = properties.getProperty("app.db");
		userId_ = properties.getProperty("app.username");
		password_ = properties.getProperty("app.password");
	}
	
	public static void init() {
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

	public static Date getCurrentDate() throws Exception {
		String sql = "SELECT CURRENT_DATE()";
		Date currentDate = null;

		PreparedStatement ps = getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			currentDate = rs.getDate(1);
		}

		close();
		return currentDate;
	}

	public static boolean testConnection(Properties databaseProperties){
		
		hostname_ = databaseProperties.getProperty("app.hostname");
		db_ = databaseProperties.getProperty("app.db");
		userId_ = databaseProperties.getProperty("app.username");
		password_ = databaseProperties.getProperty("app.password");
		
		String sql = "show tables";
		
		try{
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.executeQuery();
			close();
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public static Timestamp getTimestamp() throws Exception {
		String sql = "SELECT NOW()";
		Timestamp timestamp = null;

		PreparedStatement ps = getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			timestamp = rs.getTimestamp(1);
		}

		close();
		return timestamp;
	}
}
