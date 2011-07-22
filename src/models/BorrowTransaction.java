package models;

import java.sql.Date;

public class BorrowTransaction {

	int id;
	User user;
	Book book;
	private Date dateRequested;
	Date dateBorrowed;
	Date dateReturned;

	public BorrowTransaction(int id, User user, Book book, Date dateRequested,
			Date dateBorrowed, Date dateReturned) {
		this.id = id;
		this.user = user;
		this.book = book;
		this.dateRequested = dateRequested;
		this.dateBorrowed = dateBorrowed;
		this.dateReturned = dateReturned;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Date getDateBorrowed() {
		return dateBorrowed;
	}

	public void setDateBorrowed(Date dateBorrowed) {
		this.dateBorrowed = dateBorrowed;
	}

	public Date getDateReturned() {
		return dateReturned;
	}

	public void setDateReturned(Date dateReturned) {
		this.dateReturned = dateReturned;
	}

	/**
	 * @return the dateRequested
	 */
	public Date getDateRequested() {
		return dateRequested;
	}

	/**
	 * @param dateRequested the dateRequested to set
	 */
	public void setDateRequested(Date dateRequested) {
		this.dateRequested = dateRequested;
	}

}
