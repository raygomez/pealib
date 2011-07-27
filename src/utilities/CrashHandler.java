package utilities;

import javax.swing.JOptionPane;

public class CrashHandler {

	public static void handle(Exception e) {
		JOptionPane.showMessageDialog(null, "Something really went wrong!\n"
				+ "We have to terminate this application.", "Connecton Error",
				JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
		e.printStackTrace();
	}
}
