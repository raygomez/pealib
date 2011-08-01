package models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.joda.time.DateMidnight;
import org.joda.time.Days;

import utilities.Connector;

public class TransactionDAO {

	public static void returnBook(BorrowTransaction borrow) throws Exception {

		String sql = "UPDATE Borrows SET DateReturned = CURRENT_DATE() "
				+ "where BorrowID = ? and DateBorrowed is not NULL and "
				+ "DateRequested is not NULL";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setInt(1, borrow.getId());
		ps.executeUpdate();

		Connector.close();
	}

	public static int reserveBook(Book book, User user) throws Exception {

		String sql = "INSERT INTO Reserves (UserID, BookID) VALUES (?,?)";

		int intStat = 0;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());
		intStat = ps.executeUpdate();

		Connector.close();
		return intStat;
	}

	public static int requestBook(Book book, User user) throws Exception {

		String sql = "INSERT INTO Borrows (UserID, BookID, DateRequested) "
				+ "VALUES (?,?,CURRENT_DATE())";

		int intStat = 0;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());
		intStat = ps.executeUpdate();

		Connector.close();
		return intStat;
	}

	public static int borrowBook(BorrowTransaction borrowedBook)
			throws Exception {

		String sql = "UPDATE Borrows SET DateBorrowed = CURRENT_DATE() WHERE BorrowID = ?";

		int intStat = 0;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, borrowedBook.getId());
		intStat = ps.executeUpdate();

		Connector.close();
		return intStat;
	}

	public static BorrowTransaction getBorrowTransactionById(int id)
			throws Exception {

		String sql = "SELECT * FROM Borrows where BorrowID = ?";

		BorrowTransaction borTransaction = null;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setInt(1, id);

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			int BorrowID = rs.getInt("BorrowID");
			User user = UserDAO.getUserById(rs.getInt("UserID"));
			Book book = BookDAO.getBookById(rs.getInt("BookID"));
			Date dateRequested = rs.getDate("DateRequested");
			Date dateBorrowed = rs.getDate("DateBorrowed");
			Date dateReturned = rs.getDate("DateReturned");

			borTransaction = new BorrowTransaction(BorrowID, user, book,
					dateRequested, dateBorrowed, dateReturned);

		}

		Connector.close();
		return borTransaction;

	}

	public static ReserveTransaction getReserveTransaction(User user, Book book)
			throws Exception {

		String sql = "SELECT DatetimeReserved FROM Reserves where UserID = ? and BookID = ? ";

		ReserveTransaction rTransaction = null;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setInt(1, user.getUserId());
		ps.setInt(2, book.getBookId());

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			Timestamp datetimeReserved = rs.getTimestamp("DatetimeReserved");
			rTransaction = new ReserveTransaction(user, book, datetimeReserved);

		}

		Connector.close();
		return rTransaction;

	}

	public static int borrowBook(Book book, User user) throws Exception {

		String sql = "UPDATE Borrows SET DateBorrowed = CURRENT_DATE() "
				+ "WHERE UserID = ? and BookID = ? and "
				+ "DateBorrowed is NULL and DateReturned is NULL";

		int intStat = 0;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setInt(1, user.getUserId());
		ps.setInt(2, book.getBookId());
		intStat = ps.executeUpdate();

		Connector.close();
		return intStat;
	}

	public static int cancelReservation(ReserveTransaction rTransaction)
			throws Exception {

		String sql = "DELETE FROM Reserves WHERE UserID = ? AND BookID = ?";

		int intStat = 0;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, rTransaction.getUser().getUserId());
		ps.setLong(2, rTransaction.getBook().getBookId());
		intStat = ps.executeUpdate();

		Connector.close();
		return intStat;
	}

	public static void denyBookRequest(BorrowTransaction borrow)
			throws Exception {

		String sql = "Delete from Borrows where BorrowID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setInt(1, borrow.getId());
		ps.executeUpdate();

		Connector.close();
	}

	public static boolean isReservedByUser(Book book, User user)
			throws Exception {

		String sql = "SELECT COUNT(*) FROM Reserves WHERE UserID = ? AND BookID = ?";

		boolean isReserved = false;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());

		ResultSet rs = ps.executeQuery();
		rs.first();
		isReserved = rs.getInt(1) != 0;

		Connector.close();
		return isReserved;
	}

	public static ArrayList<ReserveTransaction> getReservedBooks(User user)
			throws Exception {

		String sql = "SELECT * FROM Reserves WHERE UserID = ?";

		ArrayList<ReserveTransaction> reserves = new ArrayList<ReserveTransaction>();
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Book book = BookDAO.getBookById(rs.getInt("BookID"));
			ReserveTransaction reserveTransaction = new ReserveTransaction(
					user, book, rs.getTimestamp("DatetimeReserved"));
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

		String sql = "SELECT * FROM Borrows WHERE UserID = ? "
				+ "and DateReturned is NULL and DateBorrowed is NOT NULL";

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

	public static ArrayList<BorrowTransaction> getRequestedBooks(User user)
			throws Exception {

		String sql = "SELECT * FROM Borrows WHERE UserID = ? and "
				+ "DateBorrowed is NULL and DateReturned is NULL";

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

	public static int getQueueInReservation(Book book, User user)
			throws Exception {

		String sql = "SELECT * FROM Reserves where BookID = ? ORDER BY DatetimeReserved";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, book.getBookId());
		ResultSet rs = ps.executeQuery();
		int ctr = 0;

		if (!isReservedByUser(book, user)) {
			return 0;
		}

		while (rs.next()) {
			ctr++;
			if (user.getUserId() == rs.getInt("UserID")) {
				break;
			}
		}

		Connector.close();
		return ctr;
	}

	public static boolean isBorrowedByUser(Book book, User user)
			throws Exception {

		String sql = "SELECT COUNT(*) FROM Borrows "
				+ "WHERE UserID = ? AND BOokID = ? AND DateReturned is NULL";

		int count = 0;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, user.getUserId());
		ps.setLong(2, book.getBookId());

		ResultSet rs = ps.executeQuery();
		rs.first();
		count = rs.getInt(1);

		Connector.close();
		return count != 0;
	}

	public static int getAvailableCopies(Book book) throws Exception {

		String sql = "SELECT COUNT(*) FROM Borrows "
				+ "WHERE BookID = ? AND DateReturned is NULL";

		int count = 0;
		int available = 0;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, book.getBookId());

		ResultSet rs = ps.executeQuery();
		rs.first();
		count = rs.getInt(1);
		available = book.getCopies() - count;

		Connector.close();
		return available;
	}

	public static int getDaysOverdue(Book book, User user) throws Exception {

		String sql = "SELECT DateBorrowed FROM Borrows "
				+ "WHERE BookID = ? AND UserID = ? AND DateReturned is NULL";

		int days = 0;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, book.getBookId());
		ps.setLong(2, user.getUserId());

		ResultSet rs = ps.executeQuery();
		if (rs.first()) {
			Date borrowedDate = rs.getDate(1);
			Days d = Days.daysBetween(
					new DateMidnight(borrowedDate.getTime()).plusWeeks(2),
					new DateMidnight());
			days = d.getDays();
		}

		Connector.close();
		return days;
	}

	public static int getDaysOverdue(BorrowTransaction transaction)
			throws Exception {

		String sql = "SELECT DateBorrowed FROM Borrows "
				+ "WHERE BookID = ? AND UserID = ? AND DateReturned is NULL";

		int days = 0;
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, transaction.getBook().getBookId());
		ps.setLong(2, transaction.getUser().getUserId());

		ResultSet rs = ps.executeQuery();
		if (rs.first()) {
			Date borrowedDate = rs.getDate(1);
			Days d = Days.daysBetween(
					new DateMidnight(borrowedDate.getTime()).plusWeeks(2),
					new DateMidnight());
			days = d.getDays();
		}

		Connector.close();
		return days;
	}

	public static ArrayList<BorrowTransaction> searchOutgoingBook(String search)
			throws Exception {

		String sql = "SELECT * FROM Books "
				+ "INNER JOIN Borrows ON Books.ID=Borrows.BookID "
				+ "JOIN Users ON Borrows.UserID=Users.ID WHERE "
				+ "(DateBorrowed is NULL AND DateReturned is NULL) AND "
				+ "(CONCAT(Books.ISBN, Books.Title, Books.Author, "
				+ "Users.UserName, Users.FirstName, Users.LastName) "
				+ "LIKE ?) ORDER BY Borrows.BorrowID";

		ArrayList<BorrowTransaction> bookCollection = new ArrayList<BorrowTransaction>();
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, "%" + search + "%");

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

			Book book = new Book(rs.getInt("Books.ID"),
					rs.getString("Books.ISBN"), rs.getString("Books.Title"),
					rs.getString("Books.Edition"),
					rs.getString("Books.Author"),
					rs.getString("Books.Publisher"),
					rs.getInt("Books.YearPublish"),
					rs.getString("Books.Description"),
					rs.getInt("Books.Copies"));

			BorrowTransaction borrowed = new BorrowTransaction(
					rs.getInt("Borrows.BorrowID"), user, book,
					rs.getDate("DateRequested"), rs.getDate("DateBorrowed"),
					rs.getDate("DateReturned"));
			bookCollection.add(borrowed);
		}

		Connector.close();
		return bookCollection;
	}

	public static ArrayList<BorrowTransaction> searchIncomingBook(String search)
			throws Exception {

		String sql = "SELECT * FROM Books INNER JOIN Borrows ON "
				+ "Books.ID=Borrows.BookID JOIN Users ON "
				+ "Borrows.UserID=Users.ID WHERE (DateBorrowed is not "
				+ "NULL AND DateReturned is NULL) AND "
				+ "(CONCAT(Books.ISBN, Books.Title, Books.Author, "
				+ "Users.UserName, Users.FirstName, Users.LastName) "
				+ "LIKE ?) ORDER BY Borrows.DateBorrowed, Borrows.BorrowID";

		ArrayList<BorrowTransaction> bookCollection = new ArrayList<BorrowTransaction>();
		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, "%" + search + "%");

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

			Book book = new Book(rs.getInt("Books.ID"),
					rs.getString("Books.ISBN"), rs.getString("Books.Title"),
					rs.getString("Books.Edition"),
					rs.getString("Books.Author"),
					rs.getString("Books.Publisher"),
					rs.getInt("Books.YearPublish"),
					rs.getString("Books.Description"),
					rs.getInt("Books.Copies"));

			BorrowTransaction borrowed = new BorrowTransaction(
					rs.getInt("Borrows.BorrowID"), user, book,
					rs.getDate("DateRequested"), rs.getDate("DateBorrowed"),
					rs.getDate("DateReturned"));
			bookCollection.add(borrowed);
		}

		Connector.close();
		return bookCollection;
	}

	public static boolean isBookReservedByOtherUsers(Book currentBook)
			throws Exception {

		String sql = "SELECT COUNT(*) FROM RESERVES WHERE BookID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, currentBook.getBookId());

		ResultSet rs = ps.executeQuery();
		rs.first();
		int countReservations = rs.getInt(1);

		Connector.close();
		return (countReservations != 0);
	}

	public static User getNextUser(Book currentBook) throws Exception {

		String sql = "SELECT * FROM RESERVES WHERE BookID = ? ORDER BY DatetimeReserved";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setLong(1, currentBook.getBookId());

		ResultSet rs = ps.executeQuery();
		Integer nextUserID = 0;
		if (rs.first()) {
			nextUserID = rs.getInt(1);
		}
		Connector.close();

		User user = UserDAO.getUserById(nextUserID);
		return user;
	}

	public static void passToNextUser(Book returnedBook) throws Exception {

		/* get the first user in queue */
		User nextUser = getNextUser(returnedBook);
		/* create borrow transaction */
		requestBook(returnedBook, nextUser);
		/* delete reservation transaction */
		ReserveTransaction nextUserReserveTransaction = getReserveTransaction(
				nextUser, returnedBook);
		cancelReservation(nextUserReserveTransaction);
	}
}
