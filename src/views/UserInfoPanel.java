package views;

import java.awt.Insets;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import utilities.*;
import models.*;

public class UserInfoPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel tempLabel;

	private JButton changePasswordButton;
	private SaveButton saveButton;

	private MyTextField accountType;
	private MyTextField idNumber;
	private MyTextField username;
	private MyTextField firstName;
	private MyTextField lastName;
	private MyTextArea address;
	private MyTextField contactNumber;
	private MyTextField email;

	private ErrorLabel firstNameError;
	private ErrorLabel lastNameError;
	private ErrorLabel contactNumberError;
	private ErrorLabel emailError;
	private ErrorLabel addressError;

	public UserInfoPanel() {

		setLayout(new MigLayout("", "[]15[]", "[]10[]"));

		tempLabel = new JLabel("Account Type");
		accountType = new MyTextField(20);
		accountType.setEnabled(false);
		accountType.setName("accountType");

		add(tempLabel);
		add(accountType, "wrap");

		tempLabel = new JLabel("User ID");
		idNumber = new MyTextField(20);
		idNumber.setEnabled(false);
		idNumber.setName("idNumber");

		add(tempLabel);
		add(idNumber, "wrap");

		tempLabel = new JLabel("Username");
		username = new MyTextField(20);
		username.setEnabled(false);
		username.setName("username");

		add(tempLabel);
		add(username, "wrap");

		tempLabel = new JLabel("First Name");
		firstName = new MyTextField(20);
		firstName.setName("firstName");

		firstNameError = new ErrorLabel();
		firstNameError.setName("firstNameError");

		add(tempLabel);
		add(firstName);
		add(firstNameError, "wrap");

		tempLabel = new JLabel("Last Name");
		lastName = new MyTextField(20);
		lastName.setName("lastName");

		lastNameError = new ErrorLabel();
		lastNameError.setName("lastNameError");

		add(tempLabel);
		add(lastName);
		add(lastNameError, "wrap");

		tempLabel = new JLabel("Address");
		address = new MyTextArea("",20, 20);
		address.setMargin(new Insets(5,5,5,5));
		address.setLineWrap(true);
		address.setWrapStyleWord(true);		

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(address);
		
		addressError = new ErrorLabel();
		addressError.setName("addressError");
		
		add(tempLabel);
		add(scrollPane);
		add(addressError, "wrap");

		tempLabel = new JLabel("Contact Number");
		contactNumber = new MyTextField(20);
		contactNumber.setName("contactNumber");

		contactNumberError = new ErrorLabel();
		contactNumberError.setName("contactNumberError");

		add(tempLabel);
		add(contactNumber);
		add(contactNumberError, "wrap");

		tempLabel = new JLabel("Email");
		email = new MyTextField(20);
		email.setName("email");

		emailError = new ErrorLabel();
		emailError.setName("emailError");
				

		add(tempLabel);
		add(email);
		add(emailError, "wrap");

		saveButton = new SaveButton("Save Changes", new ImageIcon(
				"resources/images/save32x32.png"));
		add(saveButton);
		
		changePasswordButton = new JButton("Change Password", new ImageIcon(
		"resources/images/changepassword32x32.png"));
		add(changePasswordButton, "wrap");

		
		resetErrorMessages();
	}

	public void addChangePasswordListener(ActionListener changePassword) {
		changePasswordButton.addActionListener(changePassword);
	}

	public void addSaveListener(ActionListener save) {
		saveButton.addActionListener(save);
	}

	public void displayErrors(int[] errors) {
		for (int i : errors) {

			switch (i) {

			case Constants.FIRST_NAME_FORMAT_ERROR:
				firstNameError.setText(Constants.NAME_FORMAT_ERROR_MESSAGE);
				firstName.hasError(true);
				break;

			case Constants.LAST_NAME_FORMAT_ERROR:
				lastNameError.setText(Constants.NAME_FORMAT_ERROR_MESSAGE);
				lastName.hasError(true);
				break;

			case Constants.CONTACT_NUMBER_FORMAT_ERROR:
				contactNumberError.setText(Constants.CONTACT_NUMBER_FORMAT_ERROR_MESSAGE);
				contactNumber.hasError(true);
				break;

			case Constants.EMAIL_FORMAT_ERROR:
				emailError.setText(Constants.EMAIL_FORMAT_ERROR_MESSAGE);
				email.hasError(true);
				break;
			
			case Constants.ADDRESS_FORMAT_ERROR:
				addressError.setText(Constants.ADDRESS_ERROR_MESSAGE);
				address.hasError(true);
				break;
			}
		}
	}

	public void resetErrorMessages() {

		firstNameError.setText("");
		firstName.hasError(false);
		
		lastNameError.setText("");
		lastName.hasError(false);
		
		contactNumberError.setText("");
		contactNumber.hasError(false);
		
		emailError.setText("");
		email.hasError(false);
		
		addressError.setText("");
		address.hasError(false);
	}

	public String getAccountType() {
		return accountType.getText();
	}

	public String getIdNumber() {
		return idNumber.getText();
	}

	public String getUsername() {
		return username.getText();
	}

	public String getFirstName() {
		return firstName.getText();
	}

	public String getLastName() {
		return lastName.getText();
	}

	public String getAddress() {
		return address.getText();
	}

	public String getContactNumber() {
		return contactNumber.getText();
	}

	public String getEmail() {
		return email.getText();
	}

	public void setFields(User user) {
		this.accountType.setText(user.getType());
		this.idNumber.setText(user.getUserId() + "");
		this.username.setText(user.getUserName());
		this.firstName.setText(user.getFirstName());
		this.lastName.setText(user.getLastName());
		this.address.setText(user.getAddress());
		this.contactNumber.setText(user.getContactNo());
		this.email.setText(user.getEmail());
	}

	public void setFields(String accountType, String idNumber, String username,
			String firstName, String lastName, String address,
			String contactNumber, String email) {
		this.accountType.setText(accountType);
		this.idNumber.setText(idNumber);
		this.username.setText(username);
		this.firstName.setText(firstName);
		this.lastName.setText(lastName);
		this.address.setText(address);
		this.contactNumber.setText(contactNumber);
		this.email.setText(email);
	}

	public void clearFields() {
		this.accountType.setText("");
		this.idNumber.setText("");
		this.username.setText("");
		this.firstName.setText("");
		this.lastName.setText("");
		this.address.setText("");
		this.contactNumber.setText("");
		this.email.setText("");
	}

	public void setFirstNameEnabled(boolean enabled) {
		firstName.setEnabled(enabled);
	}

	public void setLastNameEnabled(boolean enabled) {
		lastName.setEnabled(enabled);
	}

	public void toggleButton(boolean value) {
		changePasswordButton.setEnabled(value);
		saveButton.setEnabled(value);
	}
	
	class SaveButton extends JButton{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ArrayList<ActionListener> actionListenerList;
		
		public SaveButton(){
			super();
			actionListenerList = new ArrayList<ActionListener>();
		}
		
		public SaveButton(String arg0, Icon arg1){
			super(arg0, arg1);
			actionListenerList = new ArrayList<ActionListener>();
		}
		
		public SaveButton(String arg0){
			super(arg0);
			actionListenerList = new ArrayList<ActionListener>();
		}
		
		@Override
		public void addActionListener(ActionListener l) {
			actionListenerList.add(l);
		}
		
		@Override
		protected void fireActionPerformed(ActionEvent event) {
			for(ActionListener l : actionListenerList){
				l.actionPerformed(event);
			}
		}
	}
}
