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
	private ErrorLabel firstNameErrorLabel;
	private ErrorLabel lastNameErrorLabel;
	private ErrorLabel contactNumberErrorLabel;
	private ErrorLabel addressErrorLabel;
	private ErrorLabel emailErrorLabel;

	public UserInfoPanel() {

		setLayout(new MigLayout("", "[]15[][]", "[]10[][][][][][][][][][][][][][]"));

		tempLabel = new JLabel("Account Type");
		accountType = new MyTextField(20);
		accountType.setEnabled(false);
		accountType.setName("accountType");

		add(tempLabel, "cell 0 0");
		add(accountType, "cell 1 0 2 1,growx");

		tempLabel = new JLabel("User ID");
		idNumber = new MyTextField(20);
		idNumber.setEnabled(false);
		idNumber.setName("idNumber");

		add(tempLabel, "cell 0 1");
		add(idNumber, "cell 1 1 2 1,growx");

		tempLabel = new JLabel("Username");
		username = new MyTextField(20);
		username.setEnabled(false);
		username.setName("username");

		add(tempLabel, "cell 0 2");
		add(username, "cell 1 2 2 1,growx");

		tempLabel = new JLabel("First Name");
		firstName = new MyTextField(20);
		firstName.setName("firstName");

		add(tempLabel, "cell 0 3");
		add(firstName, "cell 1 3 2 1,growx");

		tempLabel = new JLabel("Last Name");
		lastName = new MyTextField(20);
		lastName.setName("lastName");
		
		firstNameErrorLabel = new ErrorLabel(" ");
		firstNameErrorLabel.setName("firstNameErrorLabel");
		add(firstNameErrorLabel, "cell 0 4 3 1,alignx center,aligny center");

		add(tempLabel, "cell 0 5");
		add(lastName, "cell 1 5 2 1,growx");

		tempLabel = new JLabel("Address");
		address = new MyTextArea("",20, 20);
		address.setMargin(new Insets(5,5,5,5));
		address.setLineWrap(true);
		address.setWrapStyleWord(true);		
		address.setName("address");		

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(address);
		
		lastNameErrorLabel = new ErrorLabel(" ");
		lastNameErrorLabel.setName("lastNameErrorLabel");
		add(lastNameErrorLabel, "cell 0 6 3 1,alignx center,aligny center");
		
		add(tempLabel, "cell 0 7");
		add(scrollPane, "cell 1 7 2 1,growx");

		tempLabel = new JLabel("Contact Number");
		contactNumber = new MyTextField(20);
		contactNumber.setName("contactNumber");
		
		addressErrorLabel = new ErrorLabel(" ");
		addressErrorLabel.setName("addressErrorLabel");
		add(addressErrorLabel, "cell 0 8 3 1,alignx center,aligny center");

		add(tempLabel, "cell 0 9");
		add(contactNumber, "cell 1 9 2 1,growx");

		tempLabel = new JLabel("Email");
		email = new MyTextField(20);
		email.setName("emailAdd");
		
		contactNumberErrorLabel = new ErrorLabel(" ");
		contactNumberErrorLabel.setName("contactNumberErrorLabel");
		add(contactNumberErrorLabel, "cell 0 10 3 1,alignx center,aligny center");
				

		add(tempLabel, "cell 0 11");
		add(email, "cell 1 11 2 1,growx");

		saveButton = new SaveButton("Save Changes", new ImageIcon(
				"resources/images/save32x32.png"));
		add(saveButton, "cell 1 13 2 1,growx");
		
		emailErrorLabel = new ErrorLabel(" ");
		emailErrorLabel.setName("emailErrorLabel");
		add(emailErrorLabel, "cell 0 12 3 1,alignx center,aligny center");
		
		changePasswordButton = new JButton("Change Password", new ImageIcon(
		"resources/images/changepassword32x32.png"));
		add(changePasswordButton, "cell 0 13");

		
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
				firstNameErrorLabel.makeError("Invalid Input");
				firstName.hasError(true);
				break;

			case Constants.LAST_NAME_FORMAT_ERROR:
				lastNameErrorLabel.makeError("Invalid Input");
				lastName.hasError(true);
				break;

			case Constants.CONTACT_NUMBER_FORMAT_ERROR:
				contactNumberErrorLabel.makeError(Constants.CONTACT_NUMBER_FORMAT_ERROR_MESSAGE);
				contactNumber.hasError(true);
				break;

			case Constants.EMAIL_FORMAT_ERROR:
				emailErrorLabel.makeError(Constants.EMAIL_FORMAT_ERROR_MESSAGE);
				email.hasError(true);
				break;
			
			case Constants.EMAIL_EXIST_ERROR:
				emailErrorLabel.makeError(Constants.EMAIL_EXIST_ERROR_MESSAGE);
				email.hasError(true);
				break;
			
			case Constants.ADDRESS_FORMAT_ERROR:
				addressErrorLabel.makeError("Invalid Input");
				address.hasError(true);
				break;
			}
		}
	}

	public void resetErrorMessages() {
		firstName.hasError(false);
		lastName.hasError(false);
		contactNumber.hasError(false);
		email.hasError(false);
		address.hasError(false);
		firstNameErrorLabel.clear();
		lastNameErrorLabel.clear();
		contactNumberErrorLabel.clear();
		emailErrorLabel.clear();
		addressErrorLabel.clear();
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
/*
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
*/
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
	
	public void setEnableFields(boolean set){
		this.firstName.setEnabled(set);
		this.lastName.setEnabled(set);
		this.address.setEnabled(set);
		this.contactNumber.setEnabled(set);
		this.email.setEnabled(set);
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
