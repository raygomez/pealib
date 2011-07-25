package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;

import net.miginfocom.swing.MigLayout;
import utilities.ErrorLabel;
import utilities.MyPasswordField;
import utilities.MyTextField;


public class SignUpDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private MyTextField firstNameTextField;
	private MyTextField lastNameTextField;
	private MyTextField userNameTextField;
	private MyPasswordField passwordTextField;
	private MyPasswordField confirmPasswordTextField;
	private MyTextField emailAddressTextField;
	private MyTextField contactNumberTextField;
	private MyTextField addressTextField;
	private JLabel lblErrorMessage;

	private JButton submitButton;
	private JButton cancelButton;
	
	public static final int EMPTY_FLAG = 0;
	public static final int FIRSTNAME_FLAG = 1 << 0; 
	public static final int LASTNAME_FLAG = 1 << 1; 
	public static final int USERNAME_FLAG = 1 << 2;
	public static final int PASSWORD_FLAG = 1 << 3;
	public static final int CONFIRM_PASSWORD_FLAG = 1 << 4;
	public static final int EMAIL_ADDRESS_FLAG = 1 << 5;
	public static final int CONTACT_NUMBER_FLAG = 1 << 6;
	public static final int ADDRESS_FLAG = 1 << 7;

	/**
	 * Create the dialog.
	 */
	public SignUpDialog() {
		setTitle("New User Account Application");
		setModal(true);
		setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width/3), (screenSize.height/4), 400, 375);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[10:n:10][110:n:110][225:n:225]", "[25:n:25][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20]"));
		
		JLabel lblPleaseFillin = new JLabel("Please Fill-in.");
		lblPleaseFillin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblPleaseFillin, "cell 0 0 2 1,growx,aligny center");

		lblErrorMessage = new ErrorLabel("");
		contentPanel.add(lblErrorMessage, "cell 2 0,alignx left,aligny center");

		JLabel lblFirstName = new JLabel("First Name:");
		contentPanel.add(lblFirstName, "cell 1 1,alignx left,aligny center");

		firstNameTextField = new MyTextField();
		firstNameTextField.setName("firstNameTextField");
		lblFirstName.setLabelFor(firstNameTextField);
		contentPanel.add(firstNameTextField, "cell 2 1,growx,aligny center");
		firstNameTextField.setColumns(10);

		JLabel lblLastName = new JLabel("Last Name:");
		contentPanel.add(lblLastName, "cell 1 2,alignx left,aligny center");

		lastNameTextField = new MyTextField();
		lastNameTextField.setName("lastNameTextField");
		lblLastName.setLabelFor(lastNameTextField);
		contentPanel.add(lastNameTextField, "cell 2 2,growx,aligny center");
		lastNameTextField.setColumns(10);

		JLabel lblUserName = new JLabel("User Name:");
		contentPanel.add(lblUserName, "cell 1 4,alignx left,aligny center");

		userNameTextField = new MyTextField();
		userNameTextField.setName("userNameTextField");
		lblUserName.setLabelFor(userNameTextField);
		contentPanel.add(userNameTextField, "cell 2 4,growx,aligny center");
		userNameTextField.setColumns(10);

		JLabel lblPassword = new JLabel("Password:");
		contentPanel.add(lblPassword, "cell 1 5,alignx left,aligny center");

		passwordTextField = new MyPasswordField();
		passwordTextField.setName("passwordTextField");
		lblPassword.setLabelFor(passwordTextField);
		contentPanel.add(passwordTextField, "cell 2 5,growx,aligny center");
		passwordTextField.setColumns(10);
		passwordTextField.setEchoChar('*');

		JLabel lblConfirmPassword = new JLabel("Confirm Password:");
		contentPanel.add(lblConfirmPassword, "cell 1 6,alignx left,aligny center");

		confirmPasswordTextField = new MyPasswordField();
		confirmPasswordTextField.setName("confirmPasswordTextField");
		lblConfirmPassword.setLabelFor(confirmPasswordTextField);
		contentPanel.add(confirmPasswordTextField, "cell 2 6,growx,aligny center");
		confirmPasswordTextField.setColumns(10);
		confirmPasswordTextField.setEchoChar('*');

		JLabel lblEmailAddress = new JLabel("E-mail Address:");
		contentPanel.add(lblEmailAddress, "cell 1 8,alignx left,aligny center");

		emailAddressTextField = new MyTextField();
		emailAddressTextField.setName("emailAddressTextField");
		lblEmailAddress.setLabelFor(emailAddressTextField);
		contentPanel.add(emailAddressTextField, "cell 2 8,growx,aligny center");
		emailAddressTextField.setColumns(10);

		JLabel lblContactNumber = new JLabel("Contact Number:");
		contentPanel.add(lblContactNumber, "cell 1 9,alignx left,aligny center");

		contactNumberTextField = new MyTextField();
		contactNumberTextField.setName("contactNumberTextField");
		contentPanel.add(contactNumberTextField, "cell 2 9,growx,aligny center");
		contactNumberTextField.setColumns(10);

		JLabel lblAddress = new JLabel("Address:");
		contentPanel.add(lblAddress, "cell 1 10,alignx left,aligny center");

		addressTextField = new MyTextField();
		addressTextField.setName("addressTextField");
		lblAddress.setLabelFor(addressTextField);
		contentPanel.add(addressTextField, "cell 2 10,growx,aligny center");
		addressTextField.setColumns(10);
	
		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new MigLayout("", "[80:n:80][100:n:100][10:n:10][100:n:100][80:n:80]", "[45:n:45]"));
		
		submitButton = new JButton("Submit", new ImageIcon("resources/images/signin.png"));
		submitButton.setActionCommand("Submit");
		buttonPane.add(submitButton, "cell 1 0,growx,aligny top");
		cancelButton = new JButton("Cancel", new ImageIcon("resources/images/logout32x32.png"));
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton, "cell 3 0,growx,aligny top");
	}
	
	/* Action Listeners */
	public void setActionListeners(ActionListener submit, ActionListener cancel, KeyListener enter, KeyListener username) {
		submitButton.addActionListener(submit);
		cancelButton.addActionListener(cancel);
		firstNameTextField.addKeyListener(enter);
		lastNameTextField.addKeyListener(enter);
		userNameTextField.addKeyListener(enter);
		passwordTextField.addKeyListener(enter);
		confirmPasswordTextField.addKeyListener(enter);
		emailAddressTextField.addKeyListener(enter);
		contactNumberTextField.addKeyListener(enter);
		addressTextField.addKeyListener(enter);
		userNameTextField.addKeyListener(username);
	}
	
	public void setFieldBorderColor(int maskedLabel) {
		if ((maskedLabel & FIRSTNAME_FLAG) == FIRSTNAME_FLAG) firstNameTextField.hasError(true);
		if ((maskedLabel & LASTNAME_FLAG) == LASTNAME_FLAG) lastNameTextField.hasError(true);
		if ((maskedLabel & USERNAME_FLAG) == USERNAME_FLAG) userNameTextField.hasError(true);
		if ((maskedLabel & PASSWORD_FLAG) == PASSWORD_FLAG) passwordTextField.hasError(true);
		if ((maskedLabel & CONFIRM_PASSWORD_FLAG) == CONFIRM_PASSWORD_FLAG) confirmPasswordTextField.hasError(true); 
		if ((maskedLabel & EMAIL_ADDRESS_FLAG) == EMAIL_ADDRESS_FLAG) emailAddressTextField.hasError(true);
		if ((maskedLabel & CONTACT_NUMBER_FLAG) == CONTACT_NUMBER_FLAG) contactNumberTextField.hasError(true);
		if ((maskedLabel & ADDRESS_FLAG) == ADDRESS_FLAG) addressTextField.hasError(true);
		
		if (maskedLabel == EMPTY_FLAG) {
			firstNameTextField.hasError(false);
			lastNameTextField.hasError(false);
			userNameTextField.hasError(false);
			passwordTextField.hasError(false);
			confirmPasswordTextField.hasError(false);
			emailAddressTextField.hasError(false);
			contactNumberTextField.hasError(false);
			addressTextField.hasError(false);
		}
	}
	
	/* Getters and Setters */
	public JLabel getLblErrorMessage() {
		return lblErrorMessage;
	}

	public void setLblErrorMessage(String errorMessage) {
		lblErrorMessage.setText(errorMessage);
	}

	public JTextField getTxtfldFirstName() {
		return firstNameTextField;
	}

	public JTextField getTxtfldLastName() {
		return lastNameTextField;
	}

	public JTextField getTxtfldUserName() {
		return userNameTextField;
	}

	public JPasswordField getTxtfldPassword() {
		return passwordTextField;
	}

	public JPasswordField getTxtfldConfirmPassword() {
		return confirmPasswordTextField;
	}

	public JTextField getTxtfldEmailAddress() {
		return emailAddressTextField;
	}

	public JTextField getTxtfldContactNumber() {
		return contactNumberTextField;
	}

	public JTextField getTxtfldAddress() {
		return addressTextField;
	}
}
