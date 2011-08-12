package utilities;

public class IsbnUtil {

	public static boolean isIsbnValid(String isbn) {
		boolean valid = true;
		int sum = 0;

		if (!isbn.matches(Constants.ISBN_FORMAT_1)
				&& !isbn.matches(Constants.ISBN_FORMAT_2)) {
			return false;
		}

		if (isbn.length() == 10) {
			for (int i = 0; i < 9; i++) {
				sum += (isbn.charAt(i) - '0') * (10 - i);
			}

			int check = 10;
			if (isbn.charAt(9) != 'X') {
				check = isbn.charAt(9) - '0';
			}

			valid = (sum + check) % 11 == 0;

		} else if (isbn.length() == 13) {
			for (int i = 0; i < 13; i += 2) {
				sum += (isbn.charAt(i) - '0');
			}
			for (int i = 1; i < 12; i += 2) {
				sum += (isbn.charAt(i) - '0') * 3;
			}
			valid = sum % 10 == 0;

		} else {
			valid = false;
		}

		return valid;
	}

	public static String convert(String isbn) {
		String newIsbn = "";

		if (isbn.length() == 10) {
			newIsbn = convert10to13(isbn);
		} else if (isbn.length() == 13) {
			newIsbn = convert13to10(isbn);
		}

		return newIsbn;
	}

	private static String convert10to13(String isbn10) {

		String isbn13 = "978" + isbn10.substring(0, 9);

		int checksum = 0;

		for (int i = 0; i < 12; i += 2) {
			checksum += (isbn13.charAt(i) - '0');
		}
		for (int i = 1; i < 12; i += 2) {
			checksum += (isbn13.charAt(i) - '0') * 3;
		}

		checksum = (10 - (checksum % 10)) % 10;
		isbn13 += checksum;

		return isbn13;
	}

	private static String convert13to10(String isbn13) {

		if (!isbn13.substring(0, 3).equals("978"))
			return null;

		String isbn10 = isbn13.substring(3, 12);
		int checksum = 0;
		int weight = 1;

		for (int i = 0; i < isbn10.length(); i++) {
			checksum += (isbn10.charAt(i) - '0') * weight;
			weight++;
		}
		checksum = checksum % 11;

		if (checksum == 10) {
			isbn10 += 'X';
		} else {
			isbn10 += checksum;
		}

		return isbn10;
	}
}
