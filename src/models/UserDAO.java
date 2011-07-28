package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import utilities.Connector;

public class UserDAO {

	public static User getUserById(int id) throws Exception {
		User user = null;

		String sql = "SELECT * from Users where ID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setInt(1, id);
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

		Connector.close();
		return user;

	}

	public static User getUserByEmailOrUsername(String keyword)
			throws Exception {
		User user = null;

		String sql = "select * from Users where (Username = ? or Email = ?) and"
				+ " Type != 'Pending'";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, keyword);
		ps.setString(2, keyword);

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

		Connector.close();
		return user;

	}

	public static boolean isUsernameExisting(String username) throws Exception {

		boolean isExisting = true;

		String query = "SELECT count(*) from Users where Username=?";
		PreparedStatement ps = Connector.getConnection()
				.prepareStatement(query);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		rs.next();
		isExisting = rs.getInt(1) != 0;

		Connector.close();
		return isExisting;

	}

	public static boolean isEmailExisting(String email) throws Exception {

		boolean isExisting = true;

		String query = "SELECT count(*) from Users where Email=?";
		PreparedStatement ps = Connector.getConnection()
				.prepareStatement(query);
		ps.setString(1, email);
		ResultSet rs = ps.executeQuery();
		rs.next();
		isExisting = rs.getInt(1) != 0;

		Connector.close();
		return isExisting;

	}

	public static User getUser(String username, String password)
			throws Exception {
		User user = null;

		String sql = "SELECT * from Users where UserName=? and Password=SHA2(?,0)";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
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
		Connector.close();

		return user;
	}

	public static ArrayList<User> searchActiveUsers(String keyword)
			throws Exception {
		ArrayList<User> users = new ArrayList<User>();

		String sql = "SELECT * from Users "
				+ "where CONCAT(LastName,FirstName,UserName) like ? "
				+ "and Type = 'User'";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
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
		Connector.close();

		return users;
	}

	public static void saveUser(User user) throws Exception {

		String sql = "INSERT INTO Users "
				+ "(FirstName,LastName,UserName,Password,Type,"
				+ "Address, ContactNo, Email)"
				+ " values (?,?,?,SHA2(?,0),?,?,?,?);";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, user.getFirstName());
		ps.setString(2, user.getLastName());
		ps.setString(3, user.getUserName());
		ps.setString(4, user.getPassword());
		ps.setString(5, user.getType());
		ps.setString(6, user.getAddress());
		ps.setString(7, user.getContactNo());
		ps.setString(8, user.getEmail());
		ps.executeUpdate();

		Connector.close();
	}

	public static ArrayList<User> searchAllPending(String keyword)
			throws Exception {
		ArrayList<User> users = new ArrayList<User>();

		String sql = "SELECT * from Users where Type = 'Pending'"
				+ "and CONCAT(LastName,FirstName,UserName) like ? ";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
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
		Connector.close();

		return users;
	}

	public static void updateUser(User user) throws Exception {
		String sql = "UPDATE Users "
				+ " set FirstName = ?, LastName = ?, UserName = ?, "
				+ "Address = ?, ContactNo = ?, Email = ?, Type = ? "
				+ "WHERE ID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, user.getFirstName());
		ps.setString(2, user.getLastName());
		ps.setString(3, user.getUserName());
		ps.setString(4, user.getAddress());
		ps.setString(5, user.getContactNo());
		ps.setString(6, user.getEmail());
		ps.setString(7, user.getType());
		ps.setString(8, user.getUserId() + "");

		ps.executeUpdate();
		Connector.close();
	}

	public static boolean checkPassword(int userID, String oldPassword)
			throws Exception {
		boolean equals;

		String sql = "SELECT * FROM Users WHERE ID = ? AND Password = SHA2(?,0)";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setInt(1, userID);
		ps.setString(2, oldPassword);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			equals = true;
		} else
			equals = false;

		Connector.close();

		return equals;
	}

	public static void changePassword(int userID, String newPassword)
			throws Exception {

		String sql = "UPDATE Users SET Password = SHA2(?,0) WHERE ID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, newPassword);
		ps.setInt(2, userID);
		ps.executeUpdate();
		Connector.close();
	}

	public static void denyPendingUser(User user) throws Exception {

		String sql = "DELETE from Users WHERE ID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setInt(1, user.getUserId());
		ps.executeUpdate();
		Connector.close();
	}
}
