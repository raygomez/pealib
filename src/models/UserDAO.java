package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO extends AbstractDAO {

	private Connection connection;

	public UserDAO() throws SQLException, ClassNotFoundException {
		super();
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

	public User getUserById(int id) throws SQLException {

		User user = null;

		String sql = "SELECT * from Users where ID = ?";

		PreparedStatement ps = getConnection().prepareStatement(sql);
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

		return user;

	}

	public boolean isUsernameExisting(String username) throws SQLException {
		String query = "SELECT count(*) from Users where Username=?";
		PreparedStatement statement = getConnection().prepareStatement(query);
		statement.setString(1, username);
		ResultSet rs = statement.executeQuery();

		rs.next();
		return (rs.getInt(1) != 0);

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

		String sql = "SELECT * from Users "
				+ "where CONCAT(LastName,FirstName,UserName) like ? "
				+ "and Type != 'Librarian'";

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

	public void saveUser(User user) throws SQLException {
		String sql = "INSERT INTO Users "
				+ "(FirstName,LastName,UserName,Password,Type,"
				+ "Address, ContactNo, Email)"
				+ " values (?,?,?,SHA2(?,0),?,?,?,?);";

		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setString(1, user.getFirstName());
		ps.setString(2, user.getLastName());
		ps.setString(3, user.getUserName());
		ps.setString(4, user.getPassword());
		ps.setString(5, user.getType());
		ps.setString(6, user.getAddress());
		ps.setString(7, user.getContactNo());
		ps.setString(8, user.getEmail());

		ps.executeUpdate();
	}

	public void updateUser(User user) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "UPDATE Users "
			+ " set FirstName = ?, LastName = ?, UserName = ?, "
			+ "Address = ?, ContactNo = ?, Email = ? " +
					"WHERE ID = ?";

		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setString(1, user.getFirstName());
		ps.setString(2, user.getLastName());
		ps.setString(3, user.getUserName());
		ps.setString(4, user.getAddress());
		ps.setString(5, user.getContactNo());
		ps.setString(6, user.getEmail());
		ps.setString(7, user.getUserId()+"");
		
		ps.executeUpdate();
	}

}
