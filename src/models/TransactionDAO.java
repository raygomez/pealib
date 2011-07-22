package models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateMidnight;
import org.joda.time.Days;

import utilities.Connector;

public class TransactionDAO {

	public static void receiveBook(BorrowTransaction borrow) throws Exception {

		String sql = "UPDATE Borrows SET DateReturned = ? "
				+ "where ID = ? and DateBorrowed is not NULL and "
				+ "DateRequested is not NULL";
		Calendar today = Calendar.getInstance();

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setDate(1, new Date(today.getTime().getTime()));
		ps.setInt(2, borrow.getId());
		ps.executeUpdate();
		Connector.close();

	}

	public static int reserveBook(Book book, User user) throws Exception {
		int intStat = 0;

		String sql = "INSERT INTO Reserves (UserID, BookID) VALUES (?,?)";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());

		intStat = ps.executeUpdate();
		Connector.close();
		return intStat;
	}

	public static int borrowBook(Book book, User user) throws Exception {
		Calendar today = Calendar.getInstance();
		int intStat = 0;

		String sql = "INSERT INTO Borrows (UserID, BookID, DateRequested) "
				+ "VALUES (?,?,?)";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());
		ps.setDate(3, new Date(today.getTime().getTime()));

		intStat = ps.executeUpdate();
		Connector.close();
		return intStat;
	}

	public static int acceptBookRequest(BorrowTransaction borrowedBook)
			throws Exception {
		Calendar today = Calendar.getInstance();
		int intStat = 0;

		String sql = "UPDATE Borrows SET DateBorrowed = ? WHERE BorrowID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setDate(1, new Date(today.getTime().getTime()));
		ps.setLong(2, borrowedBook.getId());

		intStat = ps.executeUpdate();
		Connector.close();
		return intStat;
	}

	public static int cancelReservation(BorrowTransaction reservedBook,
			User user) throws Exception {
		int intStat = 0;

		String sql = "DELETE FROM Reserves WHERE UserID = ? AND BOokID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, reservedBook.getBook().getBookId());

		intStat = ps.executeUpdate();
		Connector.close();
		return intStat;
	}

	public static void denyBookRequest(BorrowTransaction borrow)
			throws Exception {

		String sql = "Delete from Borrows where ID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setInt(1, borrow.getId());
		ps.executeUpdate();
		Connector.close();
	}

	public static boolean isReservedByUser(Book book, User user)
			throws Exception {
		int count = 0;

		String sql = "SELECT COUNT(*) FROM Reserves WHERE UserID = ? AND BookID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());
		ResultSet rs = ps.executeQuery();
		if (rs.first()) {
			count = rs.getInt(1);
		}
		Connector.close();
		return count != 0;
	}

	public static ArrayList<ReserveTransaction> getReservedBooks(User user)
			throws Exception {
		ArrayList<ReserveTransaction> reserves = new ArrayList<ReserveTransaction>();

		String sql = "SELECT * FROM Reserves WHERE UserID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Book book = BookDAO.getBookById(rs.getInt("BookID"));
			ReserveTransaction reserveTransaction = new ReserveTransaction(
					user, book, rs.getDate("DateReserved"));
			reserves.add(reserveTransaction);
		}
		Connector.close();
		return reserves;
	}

	public static ArrayList<BorrowTransaction> getHistory(User user)
			throws Exception {

		String sql = "SELECT * FROM Borrows WHERE UserID = ? and DateReturned is Not NULL";
		ArrayList<BorrowTransaction> borrows = new ArrayList<BorrowTransaction>();

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Book book = BookDAO.getBookById(rs.getInt("BookID"));
			BorrowTransaction borrow = new BorrowTransaction(
					rs.getInt("BorrowID"), user, book,
					rs.getDate("DateRequested"), rs.getDate("DateBorrowed"),
					rs.getDate("DateReturned"));

			borrows.add(borrow);
		}
		Connector.close();
		return borrows;

	}

	public static ArrayList<BorrowTransaction> getOnLoanBooks(User user)
			throws Exception {

		String sql = "SELECT * FROM Borrows WHERE UserID = ? and DateReturned is NULL";
		ArrayList<BorrowTransaction> borrows = new ArrayList<BorrowTransaction>();

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Book book = BookDAO.getBookById(rs.getInt("BookID"));
			BorrowTransaction borrow = new BorrowTransaction(
					rs.getInt("BorrowID"), user, book,
					rs.getDate("DateRequested"), rs.getDate("DateBorrowed"),
					rs.getDate("DateReturned"));

			borrows.add(borrow);
		}
		Connector.close();
		return borrows;

	}

	public static boolean isBorrowedByUser(Book book, User user)
			throws Exception {
		int count = 0;

		String sql = "SELECT COUNT(*) FROM Borrows "
				+ "WHERE UserID = ? AND BOokID = ? AND DateReturned is NULL";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());
		ResultSet rs = ps.executeQuery();
		if (rs.first()) {
			count = rs.getInt(1);
		}
		Connector.close();
		return count != 0;
	}

	public static int getAvailableCopies(Book book) throws Exception {
		int count = 0;
		int available = 0;

		String sql = "SELECT COUNT(*) FROM Borrows "
				+ "WHERE BookID = ? AND DateReturned is NULL";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, book.getBookId());
		ResultSet rs = ps.executeQuery();

		if (rs.first()) {
			count = rs.getInt(1);
		}

		available = book.getCopies() - count;
		Connector.close();
		return available;
	}

	public static int getDaysOverdue(Book book, User user) throws Exception {
		int days = 0;
		Date borrowedDate = null;
		Calendar today = Calendar.getInstance();

		String sql = "SELECT DateBorrowed FROM Borrows "
				+ "WHERE BookID = ? AND UserID = ? AND DateReturned is NULL";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, book.getBookId());
		ps.setLong(2, user.getUserId());
		ResultSet rs = ps.executeQuery();

		if (rs.first()) {
			borrowedDate = rs.getDate(1);
		}

		Days d = Days.daysBetween(new DateMidnight(borrowedDate.getTime()),
				new DateMidnight(today.getTime().getTime()));
		days = d.getDays();
		Connector.close();
		return days;

	}

	public static ArrayList<BorrowTransaction> searchOutgoingBook(String search)
			throws Exception {
		ArrayList<BorrowTransaction> bookCollection = new ArrayList<BorrowTransaction>();
		String sql;
		PreparedStatement ps;

		if (search.equals("*")) {
			sql = "SELECT * FROM Books "
					+ "INNER JOIN Borrows ON Books.ID=Borrows.BookID "
					+ "JOIN Users ON Borrows.UserID=Users.ID WHERE "
					+ "DateBorrowed is NULL AND DateReturned is NULL ORDER "
					+ "BY Borrows.DateRequested";
			ps = Connector.getConnection().prepareStatement(sql);

		} else {
			sql = "SELECT * FROM Books "
					+ "INNER JOIN Borrows ON Books.ID=Borrows.BookID "
					+ "JOIN Users ON Borrows.UserID=Users.ID WHERE "
					+ "(DateBorrowed is NULL AND DateReturned is NULL) AND "
					+ "(CONCAT(Books.ISBN, Books.Title, Users.ID, "
					+ "Users.FirstName, Users.LastName) LIKE ?) ORDER BY "
					+ "Borrows.DateRequested";
			ps = Connector.getConnection().prepareStatement(sql);
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

			BorrowTransaction borrowed = new BorrowTransaction(
					rs.getInt("Borrows.BorrowID"), user, new Book(
							rs.getInt("Books.ID"), rs.getString("Books.ISBN"),
							rs.getString("Books.Title"),
							rs.getString("Books.Edition"),
							rs.getString("Books.Author"),
							rs.getString("Books.Publisher"),
							rs.getInt("Books.YearPublish"),
							rs.getString("Books.Description"),
							rs.getInt("Books.Copies")),
					rs.getDate("DateRequested"), rs.getDate("DateBorrowed"),
					rs.getDate("DateReturned"));
			bookCollection.add(borrowed);
		}
		Connector.close();
		return bookCollection;
	}

	public static ArrayList<BorrowTransaction> searchIncomingBook(String search)
			throws Exception {
		ArrayList<BorrowTransaction> bookCollection = new ArrayList<BorrowTransaction>();
		String sql;
		PreparedStatement ps;

		if (search.equals("*")) {
			sql = "SELECT * FROM Books INNER JOIN Borrows ON "
					+ "Books.ID=Borrows.BookID JOIN Users ON "
					+ "Borrows.UserID=Users.ID WHERE DateBorrowed is not "
					+ "NULL AND DateReturned is NULL ORDER BY "
					+ "Borrows.DateRequested";
			ps = Connector.getConnection().prepareStatement(sql);

		} else {
			sql = "SELECT * FROM Books INNER JOIN Borrows ON "
					+ "Books.ID=Borrows.BookID JOIN Users ON "
					+ "Borrows.UserID=Users.ID WHERE (DateBorrowed is not "
					+ "NULL AND DateReturned is NULL) AND (CONCAT(Books.ISBN, "
					+ "Books.Title, Users.ID, Users.FirstName, Users.LastName) "
					+ "LIKE ?) ORDER BY Borrows.DateRequested";
			ps = Connector.getConnection().prepareStatement(sql);
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

			BorrowTransaction borrowed = new BorrowTransaction(
					rs.getInt("Borrows.BorrowID"), user, new Book(
							rs.getInt("Books.ID"), rs.getString("Books.ISBN"),
							rs.getString("Books.Title"),
							rs.getString("Books.Edition"),
							rs.getString("Books.Author"),
							rs.getString("Books.Publisher"),
							rs.getInt("Books.YearPublish"),
							rs.getString("Books.Description"),
							rs.getInt("Books.Copies")),
					rs.getDate("DateRequested"), rs.getDate("DateBorrowed"),
					rs.getDate("DateReturned"));
			bookCollection.add(borrowed);
		}
		Connector.close();
		return bookCollection;
	}
}
