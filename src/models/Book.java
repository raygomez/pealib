package models;

public class Book {
	private int bookId;
	private String isbn10;
	private String isbn13;
	private String title;
	private String edition;
	private String author;
	private String publisher;
	private int yearPublish;
	private String description;
	private int copies;
	
	public Book(){
		
	}
	
	public Book(int bookId, String isbn, String title, String edition, String author,
			String publisher, int yearPublish, String description, int copies) {
		super();
		this.bookId = bookId;
		this.isbn10 = isbn;
		this.title = title;
		this.edition = edition;
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

	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn) {
		this.isbn10 = isbn;
	}

	/**
	 * @return the isbn13
	 */
	public String getIsbn13() {
		return isbn13;
	}

	/**
	 * @param isbn13 the isbn13 to set
	 */
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
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
