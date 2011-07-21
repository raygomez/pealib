package utilities;

public class Constants {

	public static final int USERNAME_ERROR = 0;
	public static final int PASSWORD_NOT_MATCH_ERROR = 1;
	public static final int PASSWORD_FORMAT_ERROR = 2;
	public static final int NAME_FORMAT_ERROR = 3;
	public static final int CONTACT_NUMBER_FORMAT_ERROR = 4;
	public static final int EMAIL_FORMAT_ERROR = 5;
	public static final String USERNAME_FORMAT = "[A-Za-z0-9_\\.]{4,20}";
	public static final String PASSWORD_FORMAT = "[^\\s]{6,20}";
	public static final String NAME_FORMAT = "[A-Za-z]{2,}";
	public static final String CONTACT_NUMBER_FORMAT = "(\\d{7,11})?";
	public static final String EMAIL_FORMAT = "([a-z]+[a-z0-9]+@\\w{2,}(\\.[a-z]{2,3})+)?";

	public static final String APP_CONFIG ="app.config";
	public static final String TEST_CONFIG ="test.config";

}

