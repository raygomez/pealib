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

	public User getUser() {
		return user;
	}

	public Book getBook() {
		return book;
	}

	public Date getDateBorrowed() {
		return dateBorrowed;
	}

	public Date getDateReturned() {
		return dateReturned;
	}

	/**
	 * @return the dateRequested
	 */
	public Date getDateRequested() {
		return dateRequested;
	}
}
