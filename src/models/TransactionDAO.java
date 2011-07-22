package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateMidnight;
import org.joda.time.Days;

public class TransactionDAO extends AbstractDAO {

	public TransactionDAO() throws Exception {
		super();
	}

	private Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void receiveBook(BorrowTransaction borrow) throws SQLException {

		String sql = "UPDATE Borrows SET ReturnDate = ? where ID = ?";
		Calendar today = Calendar.getInstance();

		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setDate(1, new Date(today.getTime().getTime()));
		ps.setInt(2, borrow.getId());
		ps.executeUpdate();

	}

	public int reserveBook(Book book, User user) throws SQLException {
		int intStat = 0;

		String sql = "INSERT INTO Reserves (UserID, BookID) VALUES (?,?)";

		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());

		intStat = ps.executeUpdate();

		return intStat;
	}

	public int borrowBook(Book book, User user) throws SQLException {
		Calendar today = Calendar.getInstance();
		int intStat = 0;

		String sql = "INSERT INTO Borrows (UserID, BookID, DateRequested) "
				+ "VALUES (?,?,?)";

		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());
		ps.setDate(3, new Date(today.getTime().getTime()));

		intStat = ps.executeUpdate();

		return intStat;
	}

	public int acceptBookRequest(BorrowTransaction borrowedBook) throws SQLException {
		Calendar today = Calendar.getInstance();
		int intStat = 0;

		String sql = "UPDATE Borrows SET DateBorrowed = ? WHERE BorrowID = ?";

		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setDate(1, new Date(today.getTime().getTime()));
		ps.setLong(2, borrowedBook.getId());

		intStat = ps.executeUpdate();

		return intStat;
	}

	public int cancelReservation(BorrowTransaction reservedBook, User user)
			throws SQLException {
		int intStat = 0;

		String sql = "DELETE FROM Reserves WHERE UserID = ? AND BOokID = ?";

		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, reservedBook.getBook().getBookId());

		intStat = ps.executeUpdate();

		return intStat;
	}

	public void denyBookRequest(BorrowTransaction borrow) throws SQLException {

		String sql = "Delete from Borrows where ID = ?";

		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setInt(1, borrow.getId());
		ps.executeUpdate();

	}

	public boolean isReservedByUser(Book book, User user) throws SQLException {
		int count = 0;

		String sql = "SELECT COUNT(*) FROM Reserves WHERE UserID = ? AND BookID = ?";

		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());
		ResultSet rs = ps.executeQuery();
		if (rs.first()) {
			count = rs.getInt(1);
		}

		return count != 0;
	}

	public BorrowTransaction getBorrowClass(Book book, User user) throws SQLException {

		String sql = "SELECT * FROM Reserves WHERE UserID = ? AND BookID = ?";
		BorrowTransaction borrow = null;

		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());
		ResultSet rs = ps.executeQuery();

		if (rs.first()) {
			borrow = new BorrowTransaction(rs.getInt("ID"), user, book,
					rs.getDate("DateRequested"), rs.getDate("DateBorrowed"),
					rs.getDate("DateReturned"));

		}

		return borrow;

	}

	public boolean isBorrowedByUser(Book book, User user) throws SQLException {
		int count = 0;

		String sql = "SELECT COUNT(*) FROM Borrows "
				+ "WHERE UserID = ? AND BOokID = ? AND DateReturned is NULL";

		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());
		ResultSet rs = ps.executeQuery();
		if (rs.first()) {
			count = rs.getInt(1);
		}

		return count != 0;
	}

	public int getAvailableCopies(Book book) throws SQLException {
		int count = 0;
		int available = 0;

		String sql = "SELECT COUNT(*) FROM Borrows "
				+ "WHERE BookID = ? AND DateReturned is NULL";

		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setLong(1, book.getBookId());
		ResultSet rs = ps.executeQuery();

		if (rs.first()) {
			count = rs.getInt(1);
		}

		available = book.getCopies() - count;

		return available;
	}

	public int getDaysOverdue(Book book, User user) throws SQLException {
		int days = 0;
		Date borrowedDate = null;
		Calendar today = Calendar.getInstance();

		String sql = "SELECT DateBorrowed FROM Borrows "
				+ "WHERE BookID = ? AND UserID = ? AND DateReturned is NULL";

		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setLong(1, book.getBookId());
		ps.setLong(2, user.getUserId());
		ResultSet rs = ps.executeQuery();

		if (rs.first()) {
			borrowedDate = rs.getDate(1);
		}

		Days d = Days.daysBetween(new DateMidnight(borrowedDate.getTime()),
				new DateMidnight(today.getTime().getTime()));
		days = d.getDays();
		return days;

	}

	public ArrayList<BorrowTransaction> searchOutgoingBook(String search)
			throws SQLException {
		ArrayList<BorrowTransaction> bookCollection = new ArrayList<BorrowTransaction>();
		String sql;
		PreparedStatement ps;

		if (search.equals("*")) {
			sql = "SELECT * FROM Books "
					+ "INNER JOIN Borrows ON Books.ID=Borrows.BookID "
					+ "JOIN Users ON Borrows.UserID=Users.ID WHERE "
					+ "DateBorrowed is NULL AND DateReturned is NULL ORDER "
					+ "BY Borrows.DateRequested";
			ps = getConnection().prepareStatement(sql);

		} else {
			sql = "SELECT * FROM Books "
					+ "INNER JOIN Borrows ON Books.ID=Borrows.BookID "
					+ "JOIN Users ON Borrows.UserID=Users.ID WHERE "
					+ "(DateBorrowed is NULL AND DateReturned is NULL) AND "
					+ "(CONCAT(Books.ISBN, Books.Title, Users.ID, "
					+ "Users.FirstName, Users.LastName) LIKE ?) ORDER BY "
					+ "Borrows.DateRequested";
			ps = getConnection().prepareStatement(sql);
			ps.setString(1, "%" + search + "%");
		}

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			User user = new User();
			user.setUserId(rs.getInt("Users.ID"));
			user.setFirstName(rs.getString("Users.FirstName"));
			user.setLastName(rs.getString("Users.LastName"));
			user.setType(rs.getString("Users.Type"));
			user.setUserName(rs.getString("Users.UserName"));
			user.setAddress(rs.getString("Users.Address"));
			user.setContactNo(rs.getString("Users.ContactNo"));
			user.setEmail(rs.getString("Users.Email"));

			BorrowTransaction borrowed = new BorrowTransaction(rs.getInt("Borrows.BorrowID"), user,
					new Book(rs.getInt("Books.ID"), rs.getString("Books.ISBN"),
							rs.getString("Books.Title"), rs
									.getString("Books.Edition"), rs
									.getString("Books.Author"), rs
									.getString("Books.Publisher"), rs
									.getInt("Books.YearPublish"), rs
									.getString("Books.Description"), rs
									.getInt("Books.Copies")),
					rs.getDate("DateRequested"), rs.getDate("DateBorrowed"),
					rs.getDate("DateReturned"));
			bookCollection.add(borrowed);
		}

		return bookCollection;
	}

	public ArrayList<BorrowTransaction> searchIncomingBook(String search)
			throws SQLException {
		ArrayList<BorrowTransaction> bookCollection = new ArrayList<BorrowTransaction>();
		String sql;
		PreparedStatement ps;

		if (search.equals("*")) {
			sql = "SELECT * FROM Books INNER JOIN Borrows ON "
					+ "Books.ID=Borrows.BookID JOIN Users ON "
					+ "Borrows.UserID=Users.ID WHERE DateBorrowed is not "
					+ "NULL AND DateReturned is NULL ORDER BY "
					+ "Borrows.DateRequested";
			ps = getConnection().prepareStatement(sql);

		} else {
			sql = "SELECT * FROM Books INNER JOIN Borrows ON "
					+ "Books.ID=Borrows.BookID JOIN Users ON "
					+ "Borrows.UserID=Users.ID WHERE (DateBorrowed is not "
					+ "NULL AND DateReturned is NULL) AND (CONCAT(Books.ISBN, "
					+ "Books.Title, Users.ID, Users.FirstName, Users.LastName) "
					+ "LIKE ?) ORDER BY Borrows.DateRequested";
			ps = getConnection().prepareStatement(sql);
			ps.setString(1, "%" + search + "%");
		}

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			User user = new User();
			user.setUserId(rs.getInt("Users.ID"));
			user.setFirstName(rs.getString("Users.FirstName"));
			user.setLastName(rs.getString("Users.LastName"));
			user.setType(rs.getString("Users.Type"));
			user.setUserName(rs.getString("Users.UserName"));
			user.setAddress(rs.getString("Users.Address"));
			user.setContactNo(rs.getString("Users.ContactNo"));
			user.setEmail(rs.getString("Users.Email"));

			BorrowTransaction borrowed = new BorrowTransaction(rs.getInt("Borrows.BorrowID"), user,
					new Book(rs.getInt("Books.ID"), rs.getString("Books.ISBN"),
							rs.getString("Books.Title"), rs
									.getString("Books.Edition"), rs
									.getString("Books.Author"), rs
									.getString("Books.Publisher"), rs
									.getInt("Books.YearPublish"), rs
									.getString("Books.Description"), rs
									.getInt("Books.Copies")),
					rs.getDate("DateRequested"), rs.getDate("DateBorrowed"),
					rs.getDate("DateReturned"));
			bookCollection.add(borrowed);
		}

		return bookCollection;
	}
}
