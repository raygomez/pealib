package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	private int userId;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String contactNo;
	private String type;
	
	public User(){
		
	}
	
	public User(int userId, String userName, String password, String firstName,
			String lastName, String email, String address, String contactNo, String type) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
		this.contactNo = contactNo;
		this.type = type;
	}
	
	public User(ResultSet rs) throws SQLException {
		userId = rs.getInt("Users.ID");
		firstName = rs.getString("Users.FirstName");
		lastName = rs.getString("Users.LastName");
		type = rs.getString("Users.Type");
		userName = rs.getString("Users.UserName");
		address = rs.getString("Users.Address");
		contactNo = rs.getString("Users.ContactNo");
		email = rs.getString("Users.Email");		
	}
	
	public User(int userId, String userName, String firstName,
			String lastName, String email, String address, String contactNo, String type) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
		this.contactNo = contactNo;
		this.type = type;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
