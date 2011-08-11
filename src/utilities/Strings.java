package utilities;

public class Strings {
	
	/* Log-in Strings */
	
	
	/* Sign-up Strings */
	
	
	/* Librarian -> Book Transactions Strings */
	public static final String GRANT_BOOK_TITLE = "Borrow Request Granted";
	public static final String GRANT_BOOK_MESSAGE = "<html>Successfully lent ";
	
	public static final String DENY_BOOK_TITLE = "Borrow Request Denied";
	public static final String DENY_BOOK_MESSAGE = "<html>Refused to lend ";
	
	public static final String RETURN_BOOK_TITLE = "Borrowed Book Returned"; 
	public static final String RETURN_BOOK_MESSAGE = "<html>Successfully returned ";
	
	public static final String BOOK_S = " book(s).<br>"; 
	public static final String PENDING_RESERVATIONS = " book(s) have " +
			"pending reservations.<br>See Outgoing tab for details.<br>";
	
	public static final String[] INCOMING_TABLE_HEADER = { "ISBN", "Title",
			"Author", "Username", "Date Borrowed", "Date Due" };
	public static final String[] OUTGOING_TABLE_HEADER = { "ISBN", "Title",
			"Author", "Username", "Date Requested" };

	public static final String TOTAL_MATCHES = "Total Matches: ";
	public static final String DAYS_OVERDUE = "Days Overdue: ";
}
