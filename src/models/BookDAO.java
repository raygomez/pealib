package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import utilities.MyConnection;
import utilities.PropertyLoader;

public class BookDAO {
	
	private Connection connection;
	
	public BookDAO() throws SQLException, ClassNotFoundException{
		Properties properties = new PropertyLoader("app.config").getProperties();
		connection = new MyConnection(properties).getConnection();
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public int addBook(Book book) throws SQLException{
		int intStat = 0;
		PreparedStatement insertBook = null;
		
		insertBook = connection.prepareStatement("INSERT INTO Books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES (?,?,?,?,?,?,?,?)");
		insertBook.setString(1, book.getIsbn());
		insertBook.setString(2, book.getTitle());
		insertBook.setString(3, book.getAuthor());
		insertBook.setString(4, book.getEdition());
		insertBook.setString(5, book.getPublisher());
		insertBook.setString(6, book.getDescription());
		insertBook.setInt(7, book.getYearPublish());
		insertBook.setInt(8, book.getCopies());
		
		intStat = insertBook.executeUpdate();
		
		return intStat;
	}
	
	public int editBook(Book book) throws SQLException{
		int intStat = 0;
		PreparedStatement updateBook = null;
		updateBook = connection.prepareStatement("UPDATE Books SET ISBN = ?, Title = ?, Author = ?, Edition = ?, Publisher = ?, Description = ?, YearPublish = ?, Copies = ? WHERE ID = ?");
		
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
		
		return intStat;
	}

	public int deleteBook(Book book) throws SQLException{
		int intStat = 0;
		PreparedStatement deleteBook = null;
		deleteBook = connection.prepareStatement("UPDATE Books SET Copies = 0 WHERE ID = ?");
		deleteBook.setInt(1, book.getBookId());
		intStat = deleteBook.executeUpdate();
		return intStat;
	}
	
	public ArrayList<Book> searchBook(String search) throws SQLException{
		ArrayList<Book> bookCollection = new ArrayList<Book>();
		PreparedStatement bookQuery = null;
		bookQuery = connection.prepareStatement("SELECT * FROM Book WHERE ISBN LIKE ? OR Title LIKE ? OR Author LIKE ? OR Edition LIKE ? OR Publisher LIKE ?");
		bookQuery.setString(1, "%"+search+"%");
		bookQuery.setString(2, "%"+search+"%");
		bookQuery.setString(3, "%"+search+"%");
		bookQuery.setString(4, "%"+search+"%");
		bookQuery.setString(5, "%"+search+"%");
		ResultSet rs = bookQuery.executeQuery();
		while(rs.next()){
			int bookID = Integer.parseInt(rs.getString("ID"));
			String isbn = rs.getString("ISBN");
			String title = rs.getString("Title");
			String edition = rs.getString("Edition");
			String author = rs.getString("Author");
			String publisher = rs.getString("Publisher");
			String description = rs.getString("Description");
			int yearPublish = Integer.parseInt(rs.getString("YearPublish"));
			int copies = Integer.parseInt(rs.getString("Copies"));
			Book addBook = new Book(bookID, isbn, title, edition, author, publisher, yearPublish, description, copies);
			bookCollection.add(addBook);
		}
		
		return bookCollection;
	}
	
}
