package views;

import java.awt.Color;
import java.awt.event.*;

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
	private JButton saveButton;
	
	private JTextField accountType;
	private JTextField idNumber;
	private JTextField username;
	private JTextField firstName;
	private JTextField lastName;
	private JTextArea address;
	private JTextField contactNumber;
	private JTextField email;
	
	private JLabel firstNameError;
	private JLabel lastNameError;
	private JLabel contactNumberError;
	private JLabel emailError;
	
	public UserInfoPanel(){
		
		setLayout(new MigLayout("","[]15[]","[]10[]"));
		
		tempLabel = new JLabel("Account Type");
		accountType = new JTextField(20);
		accountType.setEnabled(false);
		accountType.setName("accountType");
		
		add(tempLabel);
		add(accountType,"wrap");
		
		tempLabel = new JLabel("User ID");
		idNumber = new JTextField(20);
		idNumber.setEnabled(false);
		idNumber.setName("idNumber");
		
		add(tempLabel);
		add(idNumber, "wrap");
		
		tempLabel = new JLabel("Username");
		username = new JTextField(20);
		username.setEnabled(false);
		username.setName("username");
		
		add(tempLabel);
		add(username, "wrap");
		
		changePasswordButton = new JButton("Change Password");
		add(changePasswordButton, "wrap");
		
		tempLabel = new JLabel("First Name");
		firstName = new JTextField(20);
		firstName.setName("firstName");
		
		firstNameError = new JLabel();
		firstNameError.setForeground(Color.RED);
		firstNameError.setName("firstNameError");
		
		add(tempLabel);
		add(firstName);
		add(firstNameError, "wrap");
		
		tempLabel = new JLabel("Last Name");
		lastName = new JTextField(20);
		lastName.setName("lastName");
		
		lastNameError = new JLabel();
		lastNameError.setForeground(Color.RED);
		lastNameError.setName("lastNameError");
		
		add(tempLabel);
		add(lastName);
		add(lastNameError, "wrap");
		
		tempLabel = new JLabel("Address");
		address = new JTextArea(15, 20);
		
		add(tempLabel);
		add(address, "wrap");
		
		tempLabel = new JLabel("Contact Number");
		contactNumber = new JTextField(20);
		contactNumber.setName("contactNumber");
		
		contactNumberError = new JLabel();
		contactNumberError.setForeground(Color.RED);
		contactNumberError.setName("contactNumberError");
		
		add(tempLabel);
		add(contactNumber);
		add(contactNumberError, "wrap");
		
		tempLabel = new JLabel("Email");
		email = new JTextField(20);
		email.setName("email");
		
		emailError = new JLabel();
		emailError.setForeground(Color.RED);
		emailError.setName("emailError");
		
		add(tempLabel);
		add(email);
		add(emailError, "wrap");
		
		saveButton = new JButton("Save Changes");
		add(saveButton);
	}
	
	public void addChangePasswordListener(ActionListener changePassword){
		changePasswordButton.addActionListener(changePassword);
	}
	
	public void addSaveListener(ActionListener save){
		saveButton.addActionListener(save);
	}
	
	public void displayErrors(int[] errors)	{
		for(int i = 0; i < errors.length; i++){
			
			switch(errors[i]){
				
				case Constants.FIRST_NAME_FORMAT_ERROR:
					firstNameError.setText(Constants.NAME_FORMAT_ERROR_MESSAGE);
					break;
				
				case Constants.LAST_NAME_FORMAT_ERROR:
					lastNameError.setText(Constants.NAME_FORMAT_ERROR_MESSAGE);
					break;
					
				case Constants.CONTACT_NUMBER_FORMAT_ERROR:
					contactNumber.setText(Constants.CONTACT_NUMBER_FORMAT_ERROR_MESSAGE);
					break;
				
				case Constants.EMAIL_FORMAT_ERROR:
					emailError.setText(Constants.EMAIL_FORMAT_ERROR_MESSAGE);
					break;
			}
		}
	}
	
	public void resetErrorMessages(){
		
		firstNameError.setText("");
		lastNameError.setText("");
		contactNumberError.setText("");
		emailError.setText("");
		
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
	
	public void setFields(User user){
		
	}
	
	public void setFields(String accountType, String idNumber, String username, 
			String firstName, String lastName, String address, String contactNumber,
			String email)
	{
		this.accountType.setText(accountType);
		this.idNumber.setText(idNumber);
		this.username.setText(username);
		this.firstName.setText(firstName);
		this.lastName.setText(lastName);
		this.address.setText(address);
		this.contactNumber.setText(contactNumber);
		this.email.setText(email);
	}
	
	public void setFirstNameEnabled(boolean enabled){
		firstName.setEnabled(enabled);
	}
	
	public void setLastNameEnabled(boolean enabled){
		lastName.setEnabled(enabled);
	}
}
