package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Properties;

import utilities.MyConnection;
import utilities.PropertyLoader;

public class TransactionDAO {

	public final static int BOOK_UNAVAILABLE = 0;
	public final static int SUCCESSFUL = 1;
	public final static int ALREADY_IN = 2;
	private Connection connection;

	public TransactionDAO() throws SQLException, ClassNotFoundException {
		Properties properties = new PropertyLoader("app.config")
				.getProperties();
		connection = new MyConnection(properties).getConnection();
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void receiveBook(Borrow borrow) throws SQLException {

		String sql = "UPDATE Borrows SET ReturnDate = ? where ID = ?";
		Calendar today = Calendar.getInstance();

		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setDate(1, new Date(today.getTime().getTime()));
		ps.setInt(2, borrow.getId());
		ps.executeUpdate();

	}

}
