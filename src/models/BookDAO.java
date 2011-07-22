package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import utilities.Connector;

public class BookDAO{

	public static boolean isIsbnExisting(String Isbn) throws Exception {
		boolean validate = false;
		PreparedStatement searchIsbn = null;
		searchIsbn = Connector.getConnection().prepareStatement("SELECT ISBN From Books WHERE Isbn LIKE ?");
		searchIsbn.setString(1, Isbn);
		ResultSet rs = searchIsbn.executeQuery();
		if (rs.next()) {
			validate = true;
		}
		Connector.close();
		return validate;
	}

	public static int addBook(Book book) throws Exception {
		int intStat = 0;
		PreparedStatement insertBook = null;
		insertBook = Connector.getConnection()
				.prepareStatement("INSERT INTO Books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES (?,?,?,?,?,?,?,?)");
		insertBook.setString(1, book.getIsbn());
		insertBook.setString(2, book.getTitle());
		insertBook.setString(3, book.getAuthor());
		insertBook.setString(4, book.getEdition());
		insertBook.setString(5, book.getPublisher());
		insertBook.setString(6, book.getDescription());
		insertBook.setInt(7, book.getYearPublish());
		insertBook.setInt(8, book.getCopies());

		intStat = insertBook.executeUpdate();
		
		Connector.close();
		
		return intStat;
	}

	public static int editBook(Book book) throws Exception {
		int intStat = 0;
		PreparedStatement updateBook = null;
		updateBook = Connector.getConnection()
				.prepareStatement("UPDATE Books SET ISBN = ?, Title = ?, Author = ?, Edition = ?, Publisher = ?, Description = ?, YearPublish = ?, Copies = ? WHERE ID = ?");

		updateBook.setString(1, book.getIsbn());
		updateBook.setString(2, book.getTitle());
		updateBook.setString(3, book.getAuthor());
		updateBook.setString(4, book.getEdition());
		updateBook.setString(5, book.getPublisher());
		updateBook.setString(6, book.getDescription());
		updateBook.setInt(7, book.getYearPublish());
		updateBook.setInt(8, book.getCopies());
		updateBook.setInt(9, book.getBookId());

		intStat = updateBook.executeUpdate();
		
		Connector.close();
		
		return intStat;
	}

	public static int deleteBook(Book book) throws Exception {
		int intStat = 0;
		PreparedStatement deleteBook = null;
		deleteBook = Connector.getConnection()
				.prepareStatement("UPDATE Books SET Copies = 0 WHERE ID = ?");
		deleteBook.setInt(1, book.getBookId());
		intStat = deleteBook.executeUpdate();
		
		Connector.close();
		
		return intStat;
	}

	public static Book getBookById(int id) throws Exception {
		Book book = null;

		String sql = "SELECT * FROM Books where ID = ?";

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
		PreparedStatement bookQuery = null;

		if (search.equals("*")) {
			bookQuery = Connector.getConnection().prepareStatement("SELECT * FROM Books");
		} else {
			bookQuery = Connector.getConnection()
					.prepareStatement("SELECT * FROM Books WHERE CONCAT(ISBN, Title, Author, Publisher) LIKE ?");
			bookQuery.setString(1, "%" + search + "%");
		}

		ResultSet rs = bookQuery.executeQuery();
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
