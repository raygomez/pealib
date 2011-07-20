package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import utilities.PropertyLoader;
import utilities.MyConnection;

public class UserDAO {

	private Connection connection;

	public UserDAO() throws SQLException, ClassNotFoundException {
		Properties properties = new PropertyLoader("app.config")
				.getProperties();
		setConnection(new MyConnection(properties).getConnection());
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

	public User getUser(String username, String password) throws SQLException {

		User user = null;

		String sql = "SELECT * from Users where UserName=? and Password=SHA2(?,0)";

		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setString(1, username);
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			user = new User();
			user.setUserId(rs.getInt("ID"));
			user.setFirstName(rs.getString("FirstName"));
			user.setLastName(rs.getString("LastName"));
			user.setType(rs.getString("Type"));
			user.setUserName(rs.getString("UserName"));
			user.setAddress(rs.getString("Address"));
			user.setContactNo(rs.getString("ContactNo"));
			user.setEmail(rs.getString("Email"));
		}

		return user;
	}

	public ArrayList<User> searchUsers(String keyword) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();

		String sql = "SELECT * from Users where CONCAT(LastName,FirstName,UserName) like ?";

		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setString(1, "%" + keyword + "%");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			User user = new User();
			user.setUserId(rs.getInt("ID"));
			user.setFirstName(rs.getString("FirstName"));
			user.setLastName(rs.getString("LastName"));
			user.setType(rs.getString("Type"));
			user.setUserName(rs.getString("UserName"));
			user.setAddress(rs.getString("Address"));
			user.setContactNo(rs.getString("ContactNo"));
			user.setEmail(rs.getString("Email"));
			users.add(user);
		}

		return users;
	}
}
