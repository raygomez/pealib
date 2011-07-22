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

	public static boolean isUsernameExisting(String username) throws Exception {
		String query = "SELECT count(*) from Users where Username=?";
		PreparedStatement ps = Connector.getConnection()
				.prepareStatement(query);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		rs.next();

		Connector.close();

		return (rs.getInt(1) != 0);

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

	public static ArrayList<User> searchUsers(String keyword) throws Exception {
		ArrayList<User> users = new ArrayList<User>();

		String sql = "SELECT * from Users "
				+ "where CONCAT(LastName,FirstName,UserName) like ? "
				+ "and Type != 'Librarian'";

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

	public static ArrayList<User> getAllPending() throws Exception {
		ArrayList<User> users = new ArrayList<User>();

		String sql = "SELECT * from Users where Type != 'Pending'";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
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
		// TODO Auto-generated method stub
		String sql = "UPDATE Users "
				+ " set FirstName = ?, LastName = ?, UserName = ?, "
				+ "Address = ?, ContactNo = ?, Email = ? " + "WHERE ID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, user.getFirstName());
		ps.setString(2, user.getLastName());
		ps.setString(3, user.getUserName());
		ps.setString(4, user.getAddress());
		ps.setString(5, user.getContactNo());
		ps.setString(6, user.getEmail());
		ps.setString(7, user.getUserId() + "");

		Connector.close();
	}
}
