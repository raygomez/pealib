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

	public Book getBook() {
		return book;
	}


	public Date getDateReserved() {
		return dateReserved;
	}

}
