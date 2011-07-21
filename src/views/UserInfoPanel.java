package views;

import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class UserInfoPanel extends JPanel {

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
		
		add(tempLabel);
		add(accountType,"wrap");
		
		tempLabel = new JLabel("User ID");
		idNumber = new JTextField(20);
		idNumber.setEnabled(false);
		
		add(tempLabel);
		add(idNumber, "wrap");
		
		tempLabel = new JLabel("Username");
		username = new JTextField(20);
		username.setEnabled(false);
		
		add(tempLabel);
		add(username, "wrap");
		
		changePasswordButton = new JButton("Change Password");
		add(changePasswordButton);
		
		tempLabel = new JLabel("First Name");
		firstName = new JTextField(20);
		firstNameError = new JLabel();
		firstNameError.setForeground(Color.RED);
		
		add(tempLabel);
		add(firstName);
		add(firstNameError, "wrap");
		
		tempLabel = new JLabel("Last Name");
		lastName = new JTextField(20);
		lastNameError = new JLabel();
		lastNameError.setForeground(Color.RED);
		
		add(tempLabel);
		add(lastName);
		add(lastNameError, "wrap");
		
		tempLabel = new JLabel("Address");
		address = new JTextArea(15, 20);
		
		add(tempLabel);
		add(address, "wrap");
		
		tempLabel = new JLabel("Contact Number");
		contactNumber = new JTextField(20);
		contactNumberError = new JLabel();
		contactNumberError.setForeground(Color.RED);
		
		add(tempLabel);
		add(contactNumber);
		add(contactNumberError, "wrap");
		
		tempLabel = new JLabel("Email");
		email = new JTextField();
		emailError = new JLabel();
		emailError.setForeground(Color.RED);
		
		add(tempLabel);
		add(email);
		add(emailError, "wrap");
		
		saveButton = new JButton("Save Changes");
		add(saveButton);
	}
	
	public void addChangePasswordListner(ActionListener changePassword){
		changePasswordButton.addActionListener(changePassword);
	}
	
	public void addSaveListener(ActionListener save){
		saveButton.addActionListener(save);
	}
	
	public void displayFirstNameError()	{
		
	}
}
