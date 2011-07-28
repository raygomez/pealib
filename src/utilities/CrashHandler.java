package utilities;

import javax.swing.JOptionPane;

public class CrashHandler {

	public static void handle(Exception e) {
		JOptionPane.showMessageDialog(null, "Something really went wrong!\n"
				+ "We have to terminate this application.", "Connecton Error",
				JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
		System.exit(-1);		
	}
}
