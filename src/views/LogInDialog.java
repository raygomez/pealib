package views;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class LogInDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel labelUsername = new JLabel("Username");
	private JLabel labelPassword = new JLabel("Password");
	private JTextField fieldUsername = new JTextField(30);
	private JTextField fieldPassword = new JTextField(30);
	private JLabel labelError = new JLabel("");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LogInDialog dialog = new LogInDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LogInDialog() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setBounds((screenSize.width/3), (screenSize.height/3), 450, 300);	
		getContentPane().add(contentPanel);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new MigLayout("", "60px[]", "50px[]"));
		
	    contentPanel.add(labelUsername, "");
		
		
		
	}

}
