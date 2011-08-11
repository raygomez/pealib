package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import utilities.Connector;
import utilities.IsbnUtil;

public class BookDAO {

	public static boolean isIsbnExisting(String isbn) throws Exception {
		boolean validate = false;

		String otherISBN = IsbnUtil.convert(isbn);
		String sql = "";
		PreparedStatement ps = null;

		if (otherISBN == null) {
			// otherISBN is ISBN10 and is not existing
			sql = "SELECT COUNT(*) From Books WHERE Isbn13 = ?";
			ps = Connector.getConnection().prepareStatement(sql);
			ps.setString(1, isbn);
		} else {

			sql = "SELECT COUNT(*) From Books WHERE Isbn13 = ? and Isbn10 = ?";
			ps = Connector.getConnection().prepareStatement(sql);

			if (isbn.length() == 13) {
				ps.setString(1, isbn);
				ps.setString(2, otherISBN);
			} else {
				// argument is ISBN10
				ps.setString(1, otherISBN);
				ps.setString(2, isbn);
			}
		}

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			validate = true;
		}
		Connector.close();
		return validate;
	}

	public static int addBook(Book book) throws Exception {
		int intStat = 0;

		String sql = "INSERT INTO Books (ISBN10, ISBN13, Title, Author, "
				+ "Edition, Publisher, Description, YearPublish, "
				+ "Copies) VALUES (?,?,?,?,?,?,?,?)";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, book.getIsbn10());
		ps.setString(2, book.getIsbn13());
		ps.setString(3, book.getTitle());
		ps.setString(4, book.getAuthor());
		ps.setString(5, book.getEdition());
		ps.setString(6, book.getPublisher());
		ps.setString(7, book.getDescription());
		ps.setInt(8, book.getYearPublish());
		ps.setInt(9, book.getCopies());

		intStat = ps.executeUpdate();

		Connector.close();

		return intStat;
	}

	public static int editBook(Book book) throws Exception {
		int intStat = 0;

		String sql = "UPDATE Books SET ISBN10 = ?, ISBN13 = ?, "
				+ "Title = ?, Author = ?, Edition = ?, Publisher = ?, "
				+ "Description = ?, YearPublish = ?, Copies = ? "
				+ "WHERE ID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);

		ps.setString(1, book.getIsbn10());
		ps.setString(2, book.getIsbn13());
		ps.setString(3, book.getTitle());
		ps.setString(4, book.getAuthor());
		ps.setString(5, book.getEdition());
		ps.setString(6, book.getPublisher());
		ps.setString(7, book.getDescription());
		ps.setInt(8, book.getYearPublish());
		ps.setInt(9, book.getCopies());
		ps.setInt(10, book.getBookId());

		intStat = ps.executeUpdate();

		Connector.close();

		return intStat;
	}

	public static int deleteBook(Book book) throws Exception {
		int intStat = 0;

		String sql = "UPDATE Books SET Copies = 0 WHERE ID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setInt(1, book.getBookId());
		intStat = ps.executeUpdate();

		Connector.close();

		return intStat;
	}

	public static Book getBookById(int id) throws Exception {
		Book book = null;

		String sql = "SELECT * FROM Books WHERE ID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setInt(1, id);

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			book = new Book(rs);
		}
		
		Connector.close();

		return book;
	}

	public static ArrayList<Book> searchBook(String search) throws Exception {
		ArrayList<Book> bookCollection = new ArrayList<Book>();

		String sql = "SELECT * FROM Books WHERE ISBN10 LIKE ? OR "
				+ "ISBN13 LIKE ? OR Title LIKE ? OR Author LIKE ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, "%" + search + "%");
		ps.setString(2, "%" + search + "%");
		ps.setString(3, "%" + search + "%");
		ps.setString(4, "%" + search + "%");

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Book addBook = new Book(rs);
			bookCollection.add(addBook);
		}

		Connector.close();

		return bookCollection;
	}

	public static ArrayList<Book> searchBookForUser(String search)
			throws Exception {
		ArrayList<Book> bookCollection = new ArrayList<Book>();

		String sql = "SELECT * FROM Books WHERE Copies > 0 and "
				+ "ISBN10 LIKE ? OR ISBN13 LIKE ? OR Title LIKE ? OR "
				+ "Author LIKE ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, "%" + search + "%");
		ps.setString(2, "%" + search + "%");
		ps.setString(3, "%" + search + "%");
		ps.setString(4, "%" + search + "%");

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Book addBook = new Book(rs);
			bookCollection.add(addBook);
		}

		Connector.close();

		return bookCollection;
	}
}
