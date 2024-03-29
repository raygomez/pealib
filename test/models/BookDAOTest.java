package models;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "book.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class BookDAOTest {

	private Book testBook;

	@Before
	public void setUp() throws Exception {
		Connector.init(Constants.TEST_CONFIG);
		testBook = new Book();
	}

	@Test
	public void testGetBookById() throws Exception{
		Book book = BookDAO.getBookById(1);
		assertEquals("Harry Poter 1",book.getTitle());
	}
	
	@Test
	public void testGetBookByIdNotExisting() throws Exception{
		Book book = BookDAO.getBookById(100);
		assertNull(book);
	}
	
	@Test
	public void testIsIsbnExisting() throws Exception {
		testBook.setIsbn10("1234567890120");
		assertEquals(true, BookDAO.isIsbnExisting(testBook.getIsbn10()));
	}

	@Test
	public void testIsIsbnNotExisting() throws Exception {
		testBook.setIsbn10("0123456789012");
		assertEquals(false, BookDAO.isIsbnExisting(testBook.getIsbn10()));
	}

	@Test
	@ExpectedDataSet({ "expected/saveBook.xml" })
	public void testAddBook() throws Exception {
		testBook.setIsbn10("1234567890127");
		testBook.setTitle("Harry Poter 4");
		testBook.setAuthor("Ewan ko");
		testBook.setEdition("1st");
		testBook.setPublisher("HarryPublisher");
		testBook.setYearPublish(1901);
		testBook.setDescription("wizard book");
		testBook.setCopies(3);
		BookDAO.addBook(testBook);
	}

	@Test
	@ExpectedDataSet({ "expected/editedBook.xml" })
	public void testEditBook() throws Exception {
		testBook.setBookId(3);
		testBook.setIsbn10("1234567890122");
		testBook.setTitle("Harry Poter 31");
		testBook.setAuthor("Ewan ko1");
		testBook.setEdition("1st1");
		testBook.setPublisher("HarryPublisher1");
		testBook.setYearPublish(1902);
		testBook.setDescription("wizard book1");
		testBook.setCopies(2);
		assertEquals(1, BookDAO.editBook(testBook));
	}

	@Test
	@ExpectedDataSet({ "expected/deletedBook.xml" })
	public void testDeleteBook() throws Exception {
		testBook.setBookId(3);
		assertEquals(1, BookDAO.deleteBook(testBook));
	}

	@Test
	public void testSearchBookAll() throws Exception {
		ArrayList<Book> bookList = BookDAO.searchBook("");
		assertEquals(1,bookList.get(0).getBookId());
		assertEquals(2,bookList.get(1).getBookId());
		assertEquals(3,bookList.get(2).getBookId());
	}
	
	@Test
	public void testSearchBookISBN() throws Exception{
		ArrayList<Book> bookList = BookDAO.searchBook("1234567890120");
		assertEquals(1,bookList.get(0).getBookId());
	}
	
	@Test
	public void testSearchBookTitle() throws Exception{
		ArrayList<Book> bookList = BookDAO.searchBook("Harry");
		assertEquals(1,bookList.get(0).getBookId());
		assertEquals(2,bookList.get(1).getBookId());
		assertEquals(3,bookList.get(2).getBookId());
	}
	
	@Test
	public void testSearchAuthor() throws Exception{
		ArrayList<Book> bookList = BookDAO.searchBook("Ewan ko1");
		assertEquals(3,bookList.get(0).getBookId());
	}

	@Test
	public void testSearchPublisher() throws Exception{
		ArrayList<Book> bookList = BookDAO.searchBook("HarryPublisher1");
		assertEquals(2,bookList.get(0).getBookId());
	}
	
	@Test
	public void testSearchForUserAll() throws Exception{
		ArrayList<Book> bookList = BookDAO.searchBook("");
		for(int i = 0; i < bookList.size(); i++){
			assertNotSame(0, bookList.get(i).getCopies());
		}
	}
	
	@Test
	public void testSearchBookForUserISBN() throws Exception{
		ArrayList<Book> bookList = BookDAO.searchBookForUser("1234567890120");
		for(int i = 0; i < bookList.size(); i++){
			assertNotSame(0, bookList.get(i).getCopies());
		}
	}
	
	@Test
	public void testSearchBookForUserTitle() throws Exception{
		ArrayList<Book> bookList = BookDAO.searchBookForUser("Harry");
		for(int i = 0; i < bookList.size(); i++){
			assertNotSame(0, bookList.get(i).getCopies());
		}
	}
	
	@Test
	public void testSearchBookForUserAuthor() throws Exception{
		ArrayList<Book> bookList = BookDAO.searchBookForUser("Ewan ko1");
		assertNotSame(0, bookList.get(0).getCopies());
	}

	@Test
	public void testSearchForUserPublisher() throws Exception{
		ArrayList<Book> bookList = BookDAO.searchBookForUser("HarryPublisher1");
		assertNotSame(0, bookList.get(0).getCopies());
	}
	
	
}
