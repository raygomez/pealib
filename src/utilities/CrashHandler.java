package utilities;

import javax.swing.JOptionPane;

public class CrashHandler {

	public static void handle() {
		JOptionPane.showMessageDialog(null, "Something really went wrong!\n"
				+ "We have to terminate this application.", "Connecton Error",
				JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
	}
}
