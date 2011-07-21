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

	public int reserveBook(Book book, User user) throws SQLException{
		int intStat = 0;
		PreparedStatement makeReservation = null;
		
		makeReservation = connection.prepareStatement("INSERT INTO Reserves (UserID, BookID) VALUES (?,?)");
		makeReservation.setLong(1, user.getUserId());
		makeReservation.setLong(2, book.getBookId());
		
		intStat = makeReservation.executeUpdate();
		
		return intStat;
	}
	
	public int borrowBook(Book book, User user) throws SQLException{
		Calendar today = Calendar.getInstance();
		int intStat = 0;
		PreparedStatement borrowRequest = null;
		
		borrowRequest = connection.prepareStatement("INSERT INTO Borrows (UserID, BookID, DateRequested) VALUES (?,?,?)");
		borrowRequest.setLong(1, user.getUserId());
		borrowRequest.setLong(2, book.getBookId());
		borrowRequest.setDate(3, new Date(today.getTime().getTime()));
		
		intStat = borrowRequest.executeUpdate();
		
		return intStat;
	}
	
	public int acceptBookRequest(Borrow borrowedBook) throws SQLException{
		Calendar today = Calendar.getInstance();
		int intStat = 0;
		PreparedStatement acceptRequest = null;
		
		acceptRequest = connection.prepareStatement("UPDATE Borrows SET DateBorrowed = ? WHERE BorrowID = ?");
		acceptRequest.setDate(1, new Date(today.getTime().getTime()));
		acceptRequest.setLong(2, borrowedBook.getId());
		
		intStat = acceptRequest.executeUpdate();
		
		return intStat;
	}
	
	public int cancelReservation(Borrow reservedBook, User user) throws SQLException{
		int intStat = 0;
		PreparedStatement cancel = null;
		
		cancel = connection.prepareStatement("DELETE FROM Reserves WHERE UserID = ? AND BOokID = ?");
		cancel.setLong(1, user.getUserId());
		cancel.setLong(2, reservedBook.getBookId());
		
		intStat = cancel.executeUpdate();
		
		return intStat;
	}
}
