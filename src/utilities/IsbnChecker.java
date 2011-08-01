package utilities;

public class IsbnChecker {

	public static boolean isIsbnValid(String isbn) {
		boolean valid = true;
		int sum = 0;
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
}
