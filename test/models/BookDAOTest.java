package models;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;

import utilities.MyConnection;
import utilities.PropertyLoader;
import models.Book;
import models.BookDAO;

@DataSet({ "book.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class BookDAOTest {

	private BookDAO testBookDao;
	private Connection con;
	private Book testBook;

	@Before
	public void setUp() throws Exception {
		con = new MyConnection(
				new PropertyLoader("test.config").getProperties())
				.getConnection();
		testBook = new Book();
		testBookDao = new BookDAO();
		testBookDao.setConnection(con);
	}

	@Test
	public void testGetConnection() {
		assertEquals(con, testBookDao.getConnection());
	}

	@Test
	public void testIsIsbnExisting() throws SQLException {
		testBook.setIsbn("1234567890120");
		assertEquals(true, testBookDao.isIsbnExisting(testBook.getIsbn()));
	}

	@Test
	public void testIsIsbnNotExisting() throws SQLException {
		testBook.setIsbn("1234567890124");
		assertEquals(false, testBookDao.isIsbnExisting(testBook.getIsbn()));
	}

	@Test
	@ExpectedDataSet({ "expected/saveBook.xml" })
	public void testAddBook() throws SQLException {
		testBook.setIsbn("1234567890123");
		testBook.setTitle("Harry Poter 4");
		testBook.setAuthor("Ewan ko");
		testBook.setEdition("1st");
		testBook.setPublisher("HarryPublisher");
		testBook.setYearPublish(1901);
		testBook.setDescription("wizard book");
		testBook.setCopies(1);
		testBookDao.addBook(testBook);
	}

	@Test
	@ExpectedDataSet({ "expected/editedBook.xml" })
	public void testEditBook() throws SQLException {
		testBook.setBookId(3);
		testBook.setIsbn("1234567890122");
		testBook.setTitle("Harry Poter 31");
		testBook.setAuthor("Ewan ko1");
		testBook.setEdition("1st1");
		testBook.setPublisher("HarryPublisher1");
		testBook.setYearPublish(1902);
		testBook.setDescription("wizard book1");
		testBook.setCopies(2);
		assertEquals(1, testBookDao.editBook(testBook));
	}

	@Test
	@ExpectedDataSet({ "expected/deletedBook.xml" })
	public void testDeleteBook() throws SQLException {
		testBook.setBookId(3);
		assertEquals(1, testBookDao.deleteBook(testBook));
	}

	@Test
	public void testSearchBookAll() throws SQLException {
		ArrayList<Book> bookList = testBookDao.searchBook("*");
		assertEquals(1,bookList.get(0).getBookId());
		assertEquals(2,bookList.get(1).getBookId());
		assertEquals(3,bookList.get(2).getBookId());
	}
	
	@Test
	public void testSearchBookISBN() throws SQLException{
		ArrayList<Book> bookList = testBookDao.searchBook("1234567890120");
		assertEquals(1,bookList.get(0).getBookId());
	}
	
	@Test
	public void testSearchBookTitle() throws SQLException{
		ArrayList<Book> bookList = testBookDao.searchBook("Harry");
		assertEquals(1,bookList.get(0).getBookId());
		assertEquals(2,bookList.get(1).getBookId());
		assertEquals(3,bookList.get(2).getBookId());
	}
	
	@Test
	public void testSearchAuthor() throws SQLException{
		ArrayList<Book> bookList = testBookDao.searchBook("Ewan ko1");
		assertEquals(3,bookList.get(0).getBookId());
	}
	
	@Test
	public void testSearchEdition() throws SQLException{
		ArrayList<Book> bookList = testBookDao.searchBook("1st");
		assertEquals(1,bookList.get(0).getBookId());
		assertEquals(2,bookList.get(1).getBookId());
		assertEquals(3,bookList.get(2).getBookId());
	}

	@Test
	public void testSearchPublisher() throws SQLException{
		ArrayList<Book> bookList = testBookDao.searchBook("HarryPublisher1");
		assertEquals(2,bookList.get(0).getBookId());
	}
}
