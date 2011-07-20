package model;

public class Book {
	private int bookId;
	private String isbn;
	private String title;
	private String author;
	private String publisher;
	private int yearPublish;
	private String description;
	private int copies;
	
	public Book(int bookId, String isbn, String title, String author,
			String publisher, int yearPublish, String description, int copies) {
		super();
		this.bookId = bookId;
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.yearPublish = yearPublish;
		this.description = description;
		this.copies = copies;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getYearPublish() {
		return yearPublish;
	}

	public void setYearPublish(int yearPublish) {
		this.yearPublish = yearPublish;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}
	
	
}
