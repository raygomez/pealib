package views;

import java.awt.BorderLayout;
import java.awt.Color;
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
import utilities.MyPasswordField;
import utilities.MyTextField;


public class SignUpDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private MyTextField txtfldFirstName;
	private MyTextField txtfldLastName;
	private MyTextField txtfldUserName;
	private MyPasswordField txtfldPassword;
	private MyPasswordField txtfldConfirmPassword;
	private MyTextField txtfldEmailAddress;
	private MyTextField txtfldContactNumber;
	private MyTextField txtfldAddress;
	private JLabel lblErrorMessage;
	private JLabel lblFirstName;
	private JLabel lblLastName;
	private JLabel lblUserName;
	private JLabel lblPassword;
	private JLabel lblConfirmPassword;
	private JLabel lblEmailAddress;
	private JLabel lblContactNumber;
	private JLabel lblAddress;
	private JButton btnSubmit;
	private JButton btnCancel;
	
	public final int emptyFlag = 0;
	public final int firstNameFlag = 1 << 0; 
	public final int lastNameFlag = 1 << 1; 
	public final int userNameFlag = 1 << 2;
	public final int passwordFlag = 1 << 3;
	public final int confirmPasswordFlag = 1 << 4;
	public final int eMailAddressFlag = 1 << 5;
	public final int contactNumberFlag = 1 << 6;
	public final int addressFlag = 1 << 7;

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
		{
			JLabel lblPleaseFillin = new JLabel("Please Fill-in.");
			lblPleaseFillin.setFont(new Font("Tahoma", Font.PLAIN, 14));
			contentPanel.add(lblPleaseFillin, "cell 0 0 2 1,growx,aligny center");
		}
		{
			lblErrorMessage = new JLabel("");
			lblErrorMessage.setForeground(Color.red);
			contentPanel.add(lblErrorMessage, "cell 2 0,alignx left,aligny center");
		}
		{
			lblFirstName = new JLabel("First Name:");
			contentPanel.add(lblFirstName, "cell 1 1,alignx left,aligny center");
		}
		{
			txtfldFirstName = new MyTextField();
			lblFirstName.setLabelFor(txtfldFirstName);
			contentPanel.add(txtfldFirstName, "cell 2 1,growx,aligny center");
			txtfldFirstName.setColumns(10);
		}
		{
			lblLastName = new JLabel("Last Name:");
			contentPanel.add(lblLastName, "cell 1 2,alignx left,aligny center");
		}
		{
			txtfldLastName = new MyTextField();
			lblLastName.setLabelFor(txtfldLastName);
			contentPanel.add(txtfldLastName, "cell 2 2,growx,aligny center");
			txtfldLastName.setColumns(10);
		}
		{
			lblUserName = new JLabel("User Name:");
			contentPanel.add(lblUserName, "cell 1 4,alignx left,aligny center");
		}
		{
			txtfldUserName = new MyTextField();
			lblUserName.setLabelFor(txtfldUserName);
			contentPanel.add(txtfldUserName, "cell 2 4,growx,aligny center");
			txtfldUserName.setColumns(10);
		}
		{
			lblPassword = new JLabel("Password:");
			contentPanel.add(lblPassword, "cell 1 5,alignx left,aligny center");
		}
		{
			txtfldPassword = new MyPasswordField();
			lblPassword.setLabelFor(txtfldPassword);
			contentPanel.add(txtfldPassword, "cell 2 5,growx,aligny center");
			txtfldPassword.setColumns(10);
			txtfldPassword.setEchoChar('*');
		}
		{
			lblConfirmPassword = new JLabel("Confirm Password:");
			contentPanel.add(lblConfirmPassword, "cell 1 6,alignx left,aligny center");
		}
		{
			txtfldConfirmPassword = new MyPasswordField();
			lblConfirmPassword.setLabelFor(txtfldConfirmPassword);
			contentPanel.add(txtfldConfirmPassword, "cell 2 6,growx,aligny center");
			txtfldConfirmPassword.setColumns(10);
			txtfldConfirmPassword.setEchoChar('*');
		}
		{
			lblEmailAddress = new JLabel("e-mail Address:");
			contentPanel.add(lblEmailAddress, "cell 1 8,alignx left,aligny center");
		}
		{
			txtfldEmailAddress = new MyTextField();
			lblEmailAddress.setLabelFor(txtfldEmailAddress);
			contentPanel.add(txtfldEmailAddress, "cell 2 8,growx,aligny center");
			txtfldEmailAddress.setColumns(10);
		}
		{
			lblContactNumber = new JLabel("Contact Number:");
			contentPanel.add(lblContactNumber, "cell 1 9,alignx left,aligny center");
		}
		{
			txtfldContactNumber = new MyTextField();
			contentPanel.add(txtfldContactNumber, "cell 2 9,growx,aligny center");
			txtfldContactNumber.setColumns(10);
		}
		{
			lblAddress = new JLabel("Address:");
			contentPanel.add(lblAddress, "cell 1 10,alignx left,aligny center");
		}
		{
			txtfldAddress = new MyTextField();
			lblAddress.setLabelFor(txtfldAddress);
			contentPanel.add(txtfldAddress, "cell 2 10,growx,aligny center");
			txtfldAddress.setColumns(10);
		}
	
		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new MigLayout("", "[80:n:80][100:n:100][10:n:10][100:n:100][80:n:80]", "[45:n:45]"));
		{
			btnSubmit = new JButton("Submit", new ImageIcon("resources/images/signin.png"));
			btnSubmit.setActionCommand("Submit");
			buttonPane.add(btnSubmit, "cell 1 0,growx,aligny top");
		}
		{
			btnCancel = new JButton("Cancel", new ImageIcon("resources/images/logout32x32.png"));
			btnCancel.setActionCommand("Cancel");
			buttonPane.add(btnCancel, "cell 3 0,growx,aligny top");
		}
	}
	
	/* Action Listeners */
	public void setActionListeners(ActionListener submit, ActionListener cancel, KeyListener enter, KeyListener username) {
		btnSubmit.addActionListener(submit);
		btnCancel.addActionListener(cancel);
		txtfldFirstName.addKeyListener(enter);
		txtfldLastName.addKeyListener(enter);
		txtfldUserName.addKeyListener(enter);
		txtfldPassword.addKeyListener(enter);
		txtfldConfirmPassword.addKeyListener(enter);
		txtfldEmailAddress.addKeyListener(enter);
		txtfldContactNumber.addKeyListener(enter);
		txtfldAddress.addKeyListener(enter);
		txtfldUserName.addKeyListener(username);
	}
	
	public void setFieldBorderColor(int maskedLabel) {
		if ((maskedLabel & firstNameFlag) == firstNameFlag) txtfldFirstName.hasError(true);
		if ((maskedLabel & lastNameFlag) == lastNameFlag) txtfldLastName.hasError(true);
		if ((maskedLabel & userNameFlag) == userNameFlag) txtfldUserName.hasError(true);
		if ((maskedLabel & passwordFlag) == passwordFlag) txtfldPassword.hasError(true);
		if ((maskedLabel & confirmPasswordFlag) == confirmPasswordFlag) txtfldConfirmPassword.hasError(true); 
		if ((maskedLabel & eMailAddressFlag) == eMailAddressFlag) txtfldEmailAddress.hasError(true);
		if ((maskedLabel & contactNumberFlag) == contactNumberFlag) txtfldContactNumber.hasError(true);
		if ((maskedLabel & addressFlag) == addressFlag) txtfldAddress.hasError(true);
		
		if (maskedLabel == emptyFlag) {
			txtfldFirstName.hasError(false);
			txtfldLastName.hasError(false);
			txtfldUserName.hasError(false);
			txtfldPassword.hasError(false);
			txtfldConfirmPassword.hasError(false);
			txtfldEmailAddress.hasError(false);
			txtfldContactNumber.hasError(false);
			txtfldAddress.hasError(false);
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
		return txtfldFirstName;
	}

	public JTextField getTxtfldLastName() {
		return txtfldLastName;
	}

	public JTextField getTxtfldUserName() {
		return txtfldUserName;
	}

	public JPasswordField getTxtfldPassword() {
		return txtfldPassword;
	}

	public JPasswordField getTxtfldConfirmPassword() {
		return txtfldConfirmPassword;
	}

	public JTextField getTxtfldEmailAddress() {
		return txtfldEmailAddress;
	}

	public JTextField getTxtfldContactNumber() {
		return txtfldContactNumber;
	}

	public JTextField getTxtfldAddress() {
		return txtfldAddress;
	}
}
