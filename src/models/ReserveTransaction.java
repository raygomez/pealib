package models;

import java.util.Date;

public class ReserveTransaction {
	User user;
	Book book;
	Date dateReserved;

	public ReserveTransaction(User user, Book book, Date dateReserved) {
		this.user = user;
		this.book = book;
		this.dateReserved = dateReserved;
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

	public Date getDateReserved() {
		return dateReserved;
	}

	public void setDateReserved(Date dateReserved) {
		this.dateReserved = dateReserved;
	}

}
