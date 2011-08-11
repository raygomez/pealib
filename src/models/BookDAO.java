package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import utilities.Connector;

public class BookDAO {

	public static boolean isIsbnExisting(String Isbn) throws Exception {
		boolean validate = false;

		String sql = "SELECT ISBN From Books WHERE Isbn LIKE ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, Isbn);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			validate = true;
		}
		Connector.close();
		return validate;
	}

	public static int addBook(Book book) throws Exception {
		int intStat = 0;

		String sql = "INSERT INTO Books (ISBN, Title, Author, "
				+ "Edition, Publisher, Description, YearPublish, "
				+ "Copies) VALUES (?,?,?,?,?,?,?,?)";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, book.getIsbn10());
		ps.setString(2, book.getTitle());
		ps.setString(3, book.getAuthor());
		ps.setString(4, book.getEdition());
		ps.setString(5, book.getPublisher());
		ps.setString(6, book.getDescription());
		ps.setInt(7, book.getYearPublish());
		ps.setInt(8, book.getCopies());

		intStat = ps.executeUpdate();

		Connector.close();

		return intStat;
	}

	public static int editBook(Book book) throws Exception {
		int intStat = 0;

		String sql = "UPDATE Books SET ISBN = ?, Title = ?, "
				+ "Author = ?, Edition = ?, Publisher = ?, "
				+ "Description = ?, YearPublish = ?, Copies = ? "
				+ "WHERE ID = ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);

		ps.setString(1, book.getIsbn10());
		ps.setString(2, book.getTitle());
		ps.setString(3, book.getAuthor());
		ps.setString(4, book.getEdition());
		ps.setString(5, book.getPublisher());
		ps.setString(6, book.getDescription());
		ps.setInt(7, book.getYearPublish());
		ps.setInt(8, book.getCopies());
		ps.setInt(9, book.getBookId());

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
			int bookID = rs.getInt("ID");
			String isbn = rs.getString("ISBN");
			String title = rs.getString("Title");
			String edition = rs.getString("Edition");
			String author = rs.getString("Author");
			String publisher = rs.getString("Publisher");
			String description = rs.getString("Description");
			int yearPublish = rs.getInt("YearPublish");
			int copies = rs.getInt("Copies");
			book = new Book(bookID, isbn, title, edition, author, publisher,
					yearPublish, description, copies);

		}
		Connector.close();

		return book;
	}

	public static ArrayList<Book> searchBook(String search) throws Exception {
		ArrayList<Book> bookCollection = new ArrayList<Book>();

		String sql = "SELECT * FROM Books WHERE ISBN LIKE ? OR Title LIKE ? OR Author LIKE ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, "%" + search + "%");
		ps.setString(2, "%" + search + "%");
		ps.setString(3, "%" + search + "%");

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int bookID = rs.getInt("ID");
			String isbn = rs.getString("ISBN");
			String title = rs.getString("Title");
			String edition = rs.getString("Edition");
			String author = rs.getString("Author");
			String publisher = rs.getString("Publisher");
			String description = rs.getString("Description");
			int yearPublish = rs.getInt("YearPublish");
			int copies = Integer.parseInt(rs.getString("Copies"));
			Book addBook = new Book(bookID, isbn, title, edition, author,
					publisher, yearPublish, description, copies);
			bookCollection.add(addBook);
		}

		Connector.close();

		return bookCollection;
	}

	public static ArrayList<Book> searchBookForUser(String search)
			throws Exception {
		ArrayList<Book> bookCollection = new ArrayList<Book>();

		String sql = "SELECT * FROM Books WHERE Copies > 0 and "
				+ "ISBN LIKE ? OR Title LIKE ? OR Author LIKE ?";

		PreparedStatement ps = Connector.getConnection().prepareStatement(sql);
		ps.setString(1, "%" + search + "%");
		ps.setString(2, "%" + search + "%");
		ps.setString(3, "%" + search + "%");

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int bookID = rs.getInt("ID");
			String isbn = rs.getString("ISBN");
			String title = rs.getString("Title");
			String edition = rs.getString("Edition");
			String author = rs.getString("Author");
			String publisher = rs.getString("Publisher");
			String description = rs.getString("Description");
			int yearPublish = rs.getInt("YearPublish");
			int copies = Integer.parseInt(rs.getString("Copies"));
			Book addBook = new Book(bookID, isbn, title, edition, author,
					publisher, yearPublish, description, copies);
			bookCollection.add(addBook);
		}

		Connector.close();

		return bookCollection;
	}
}
