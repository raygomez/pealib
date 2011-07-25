package utilities;

public class Constants {

	public static final int DEFAULT_ERROR = 0;
	public static final int USERNAME_ERROR = 1;
	public static final int PASSWORD_NOT_MATCH_ERROR = 2;
	public static final int INCORRECT_PASSWORD_ERROR = 3;
	public static final int PASSWORD_FORMAT_ERROR = 4;
	public static final int FIRST_NAME_FORMAT_ERROR = 5;
	public static final int LAST_NAME_FORMAT_ERROR = 6;
	public static final int CONTACT_NUMBER_FORMAT_ERROR = 7;
	public static final int EMAIL_FORMAT_ERROR = 8;

	public static final String USERNAME_FORMAT = "[A-Za-z0-9_\\.]{4,20}";
	public static final String PASSWORD_FORMAT = "[^\\s]{6,20}";
	public static final String NAME_FORMAT = "[A-Za-z\\s]{2,}";
	public static final String CONTACT_NUMBER_FORMAT = "(\\d{7,11})?";
	public static final String EMAIL_FORMAT = "([a-z]+[a-z0-9_\\.]+@\\w{2,}(\\.[a-z]{2,3})+)";
	public static final String ADDRESS_FORMAT = ".{2,100}";

	public static final String ISBN_FORMAT_1 = "[0-9]{13}";
	public static final String ISBN_FORMAT_2 = "[0-9]{10}";
	public static final String YEAR_PUBLISH_FORMAT = "[0-9]{4}";

	public static final String NAME_FORMAT_ERROR_MESSAGE = "Incorrect name format, please alphabetical characters only";
	public static final String USERNAME_FORMAT_ERROR_MESSAGE = "Invalid username, please use alphanumeric characters, underscores and periods only.\n"
			+ "Usernames must be 4-20 characters long.";
	public static final String PASSWORD_FORMAT_ERROR_MESSAGE = "Invalid password. Passwords should be 6-20 characters long.";
	public static final String PASSWORD_NOT_MATCH_ERROR_MESSAGE = "Incorrect Password";
	public static final String EMAIL_FORMAT_ERROR_MESSAGE = "Invalid email format.";
	public static final String CONTACT_NUMBER_FORMAT_ERROR_MESSAGE = "Invalid contact number.";

	public static final String APP_CONFIG = "app.config";
	public static final String TEST_CONFIG = "test.config";
}
