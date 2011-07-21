package models;

import java.sql.Date;

public class Borrow {

	int id;
	int userId;
	int bookId;
	private Date dateRequested;
	Date dateBorrowed;
	Date dateReturned;

	public Borrow(int id, int userId, int bookId, Date dateRequested,
			Date dateBorrowed, Date dateReturned) {
		this.id = id;
		this.userId = userId;
		this.bookId = bookId;
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
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
