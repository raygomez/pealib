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
	
	/* User Search Strings */
	
	//for JOptionPane
	public static final String ACCEPT_USERS_MESSAGE_START = "Successfully accepted ";
	public static final String DENY_USERS_MESSAGE_START = "Denied ";
	public static final String USERS_MESSAGE_END = " users.";
	public static final String EMAIL_SEND_COUNT_MESSAGE_START = "\nYou were able to send ";
	public static final String EMAIL_SEND_COUNT_MESSAGE_END = " email notifications to the users.";
	
	//for table columns
	public static final String[] USERS_TABLE_COLUMNS = new String[] { "Username", "Name" };
	
	//for saving/editing profile
	public static final String SAVE_MESSAGE = "Record successfully updated!";
	
	//for reset password
	public static final String PASSWORD_RESET_MESSAGE = "The new password was successfully mailed to the user.";
	public static final String INTERNET_CONNECTION_FAIL_MESSAGE = "Internet Connection Error:\n" +
															"Please check if you have a internet connection.";
	public static final String RESET_PASSWORD_TITLE = "Reset Password";
	public static final String PASSWORD_CHANGE_MESSAGE = "Your password was successfully changed!";
	
}
