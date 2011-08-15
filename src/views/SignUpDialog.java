package views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import utilities.ErrorLabel;
import utilities.MyPasswordField;
import utilities.MyTextArea;
import utilities.MyTextField;
import utilities.Strings;

public class SignUpDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private MyTextField firstNameTextField;
	private MyTextField lastNameTextField;
	private MyTextField userNameTextField;
	private MyPasswordField passwordTextField;
	private MyPasswordField confirmPasswordTextField;
	private MyTextArea emailAddressTextArea;
	private JScrollPane eMailScrollPane;
	private MyTextArea addressTextArea;
	private JScrollPane addressScrollPane;
	private MyTextField contactNumberTextField;
	private JLabel errorMessageLabel;
	private JLabel mandatoryFieldsLabel;

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
		setTitle(Strings.SIGN_UP_DIALOG_TITLE);
		setModal(true);
		setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width / 3), (screenSize.height / 4), 400, 450);
		getContentPane().setLayout(new MigLayout("", "[]", "[350:n:350][50:n:50]"));

		JPanel infoPane = new JPanel();
		getContentPane().add(infoPane, "cell 0 0,grow");
		infoPane.setLayout(new MigLayout("", "[10:n:10][130:n:130][220:n:220]", "[25:n:25][20:n:20][20:n:20][15:n:15][20:n:20][20:n:20][20:n:20][15:n:15][50:n:50][50:n:50][20:n:20][15:n:15]"));
		
		JLabel pleaseFillinLabel = new JLabel(Strings.SIGN_UP_INTRO_LABEL);
		pleaseFillinLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		infoPane.add(pleaseFillinLabel, "cell 0 0 2 1,growx,aligny center");

		errorMessageLabel = new ErrorLabel("");
		errorMessageLabel.setName("errorMessageLabel");
		infoPane.add(errorMessageLabel,
				"cell 2 0,alignx left,aligny center");

		JLabel firstNameLabel = new JLabel(Strings.FIRST_NAME_LABEL);
		infoPane.add(firstNameLabel, "cell 1 1,alignx left,aligny center");

		firstNameTextField = new MyTextField(30);
		firstNameTextField.setToolTipText(Strings.NAME_TOOLTIP);
		firstNameTextField.setName("firstNameTextField");
		firstNameLabel.setLabelFor(firstNameTextField);
		infoPane.add(firstNameTextField, "cell 2 1,growx,aligny center");
		firstNameTextField.setColumns(10);

		JLabel lastNameLabel = new JLabel(Strings.LAST_NAME_LABEL);
		infoPane.add(lastNameLabel, "cell 1 2,alignx left,aligny center");

		lastNameTextField = new MyTextField(30);
		lastNameTextField.setToolTipText(Strings.NAME_TOOLTIP);
		lastNameTextField.setName("lastNameTextField");
		lastNameLabel.setLabelFor(lastNameTextField);
		infoPane.add(lastNameTextField, "cell 2 2,growx,aligny center");
		lastNameTextField.setColumns(10);

		JLabel userNameLabel = new JLabel(Strings.USER_NAME_LABEL);
		infoPane.add(userNameLabel, "cell 1 4,alignx left,aligny center");

		userNameTextField = new MyTextField(20);
		userNameTextField.setToolTipText(Strings.USER_NAME_TOOLTIP);
		userNameTextField.setName("userNameTextField");
		userNameLabel.setLabelFor(userNameTextField);
		infoPane.add(userNameTextField, "cell 2 4,growx,aligny center");
		userNameTextField.setColumns(10);

		JLabel passwordLabel = new JLabel(Strings.PASSWORD_LABEL);
		infoPane.add(passwordLabel, "cell 1 5,alignx left,aligny center");

		passwordTextField = new MyPasswordField(20);
		passwordTextField.setToolTipText(Strings.PASSWORD_TOOLTIP);
		passwordTextField.setName("passwordTextField");
		passwordLabel.setLabelFor(passwordTextField);
		infoPane.add(passwordTextField, "cell 2 5,growx,aligny center");
		passwordTextField.setColumns(10);
		passwordTextField.setEchoChar('*');

		JLabel confirmPasswordLabel = new JLabel(Strings.CONFIRM_PASSWORD_LABEL);
		infoPane.add(confirmPasswordLabel,
				"cell 1 6,alignx left,aligny center");

		confirmPasswordTextField = new MyPasswordField(20);
		confirmPasswordTextField.setToolTipText(Strings.PASSWORD_TOOLTIP);
		confirmPasswordTextField.setName("confirmPasswordTextField");
		confirmPasswordLabel.setLabelFor(confirmPasswordTextField);
		infoPane.add(confirmPasswordTextField,
				"cell 2 6,growx,aligny center");
		confirmPasswordTextField.setColumns(10);
		confirmPasswordTextField.setEchoChar('*');
		
		JLabel emailAddressLabel = new JLabel(Strings.EMAIL_LABEL);
		infoPane.add(emailAddressLabel,
				"cell 1 8,alignx left,aligny center");

		emailAddressTextArea = new MyTextArea(254);
		emailAddressTextArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		emailAddressTextArea.setLineWrap(true);
		emailAddressTextArea.setToolTipText(Strings.EMAIL_TOOLTIP);
		emailAddressTextArea.setName("emailAddressTextField");
		emailAddressLabel.setLabelFor(emailAddressTextArea);
		emailAddressTextArea.setColumns(10);
		
		eMailScrollPane = new JScrollPane(emailAddressTextArea);
		infoPane.add(eMailScrollPane, "cell 2 8,grow");


		JLabel addressLabel = new JLabel(Strings.ADDRESS_LABEL);
		infoPane.add(addressLabel, "cell 1 9,alignx left,aligny center");

		addressTextArea = new MyTextArea(100);
		addressTextArea.setLineWrap(true);
		addressTextArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		addressTextArea.setToolTipText(Strings.ADDRESS_TOOLTIP);
		addressTextArea.setName("addressTextField");
		addressLabel.setLabelFor(addressTextArea);
		addressTextArea.setColumns(10);
	    
		addressScrollPane = new JScrollPane(addressTextArea);
		infoPane.add(addressScrollPane, "cell 2 9,grow");
		
		JLabel contactNumberLabel = new JLabel(Strings.CONTACT_NUMBER_LABEL);
		infoPane.add(contactNumberLabel,
				"cell 1 10,alignx left,aligny center");

		contactNumberTextField = new MyTextField(11);
		contactNumberTextField.setToolTipText(Strings.CONTACT_NUMBER_TOOLTIP);
		contactNumberTextField.setName("contactNumberTextField");
		infoPane.add(contactNumberTextField, "cell 2 10,growx,aligny center");
		contactNumberTextField.setColumns(10);
		
		mandatoryFieldsLabel = new JLabel(Strings.SIGN_UP_MANDATORY_LABEL);
		mandatoryFieldsLabel.setFont(new Font("Tahoma", Font.ITALIC, 10));
		infoPane.add(mandatoryFieldsLabel, "cell 1 11,growx,aligny top");

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, "cell 0 1, alignx center, growy");
		buttonPane.setLayout(new MigLayout("", "[][]", "[]"));

		submitButton = new JButton("Submit", new ImageIcon(
				"resources/images/signin.png"));
		submitButton.setActionCommand("Submit");
		buttonPane.add(submitButton, "cell 0 0, alignx left, aligny center");
		
		cancelButton = new JButton("Cancel", new ImageIcon(
				"resources/images/logout32x32.png"));
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton, "cell 1 0, alignx left, aligny center");
	}

	/* Action Listeners */
	public void setActionListeners(ActionListener submit,
			ActionListener cancel, KeyListener enter, KeyListener username) {
		submitButton.addActionListener(submit);
		cancelButton.addActionListener(cancel);
		firstNameTextField.addKeyListener(enter);
		lastNameTextField.addKeyListener(enter);
		userNameTextField.addKeyListener(enter);
		passwordTextField.addKeyListener(enter);
		confirmPasswordTextField.addKeyListener(enter);
		emailAddressTextArea.addKeyListener(enter);
		contactNumberTextField.addKeyListener(enter);
		addressTextArea.addKeyListener(enter);
		userNameTextField.addKeyListener(username);
	}

	public void setFieldBorderColor(int maskedLabel) {
		if ((maskedLabel & FIRSTNAME_FLAG) == FIRSTNAME_FLAG)
			firstNameTextField.hasError(true);
		if ((maskedLabel & LASTNAME_FLAG) == LASTNAME_FLAG)
			lastNameTextField.hasError(true);
		if ((maskedLabel & USERNAME_FLAG) == USERNAME_FLAG)
			userNameTextField.hasError(true);
		if ((maskedLabel & PASSWORD_FLAG) == PASSWORD_FLAG)
			passwordTextField.hasError(true);
		if ((maskedLabel & CONFIRM_PASSWORD_FLAG) == CONFIRM_PASSWORD_FLAG)
			confirmPasswordTextField.hasError(true);
		if ((maskedLabel & EMAIL_ADDRESS_FLAG) == EMAIL_ADDRESS_FLAG)
			emailAddressTextArea.hasError(true);
		if ((maskedLabel & CONTACT_NUMBER_FLAG) == CONTACT_NUMBER_FLAG)
			contactNumberTextField.hasError(true);
		if ((maskedLabel & ADDRESS_FLAG) == ADDRESS_FLAG)
			addressTextArea.hasError(true);

		if (maskedLabel == EMPTY_FLAG) {
			firstNameTextField.hasError(false);
			lastNameTextField.hasError(false);
			userNameTextField.hasError(false);
			passwordTextField.hasError(false);
			confirmPasswordTextField.hasError(false);
			emailAddressTextArea.hasError(false);
			contactNumberTextField.hasError(false);
			addressTextArea.hasError(false);
		}
	}

	/* Getters and Setters */
	public String getErrorMessage(){
		return errorMessageLabel.getText();
	}

	public void setErrorMessage(String errorMessage) {
		errorMessageLabel.setText(errorMessage);
	}

	public String getFirstName() {
		return firstNameTextField.getText().trim();
	}

	public String getLastName() {
		return lastNameTextField.getText().trim();
	}

	public String getUserName() {
		return userNameTextField.getText().trim();
	}

	public String getPassword() {
		return new String(passwordTextField.getPassword());
	}

	public void clearPassword() {
		passwordTextField.setText("");
	}

	public String getConfirmPassword() {
		return new String(confirmPasswordTextField.getPassword());
	}

	public void clearConfirmPassword() {
		confirmPasswordTextField.setText("");
	}

	public String getEmailAddress() {
		return emailAddressTextArea.getText().trim();
	}

	public String getContactNumber() {
		return contactNumberTextField.getText().trim();
	}

	public String getAddress() {
		return addressTextArea.getText().trim();
	}

	public JPanel getContentPanel() {
		return contentPanel;
	}
}
