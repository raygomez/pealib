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
		{
			JLabel lblPleaseFillin = new JLabel("Please Fill-in.");
			lblPleaseFillin.setFont(new Font("Tahoma", Font.PLAIN, 14));
			contentPanel.add(lblPleaseFillin, "cell 0 0 2 1,growx,aligny center");
		}
		{
			lblErrorMessage = new ErrorLabel("");
			contentPanel.add(lblErrorMessage, "cell 2 0,alignx left,aligny center");
		}
		{
			lblFirstName = new JLabel("First Name:");
			contentPanel.add(lblFirstName, "cell 1 1,alignx left,aligny center");
		}
		{
			txtfldFirstName = new MyTextField();
			txtfldFirstName.setName("txtfldFirstName");
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
			txtfldLastName.setName("txtfldLastName");
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
			txtfldUserName.setName("txtfldUserName");
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
			txtfldPassword.setName("txtfldPassword");
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
			txtfldConfirmPassword.setName("txtfldConfirmPassword");
			lblConfirmPassword.setLabelFor(txtfldConfirmPassword);
			contentPanel.add(txtfldConfirmPassword, "cell 2 6,growx,aligny center");
			txtfldConfirmPassword.setColumns(10);
			txtfldConfirmPassword.setEchoChar('*');
		}
		{
			lblEmailAddress = new JLabel("E-mail Address:");
			contentPanel.add(lblEmailAddress, "cell 1 8,alignx left,aligny center");
		}
		{
			txtfldEmailAddress = new MyTextField();
			txtfldEmailAddress.setName("txtfldEmailAddress");
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
			txtfldContactNumber.setName("txtfldContactNumber");
			contentPanel.add(txtfldContactNumber, "cell 2 9,growx,aligny center");
			txtfldContactNumber.setColumns(10);
		}
		{
			lblAddress = new JLabel("Address:");
			contentPanel.add(lblAddress, "cell 1 10,alignx left,aligny center");
		}
		{
			txtfldAddress = new MyTextField();
			txtfldAddress.setName("txtfldAddress");
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
		if ((maskedLabel & FIRSTNAME_FLAG) == FIRSTNAME_FLAG) txtfldFirstName.hasError(true);
		if ((maskedLabel & LASTNAME_FLAG) == LASTNAME_FLAG) txtfldLastName.hasError(true);
		if ((maskedLabel & USERNAME_FLAG) == USERNAME_FLAG) txtfldUserName.hasError(true);
		if ((maskedLabel & PASSWORD_FLAG) == PASSWORD_FLAG) txtfldPassword.hasError(true);
		if ((maskedLabel & CONFIRM_PASSWORD_FLAG) == CONFIRM_PASSWORD_FLAG) txtfldConfirmPassword.hasError(true); 
		if ((maskedLabel & EMAIL_ADDRESS_FLAG) == EMAIL_ADDRESS_FLAG) txtfldEmailAddress.hasError(true);
		if ((maskedLabel & CONTACT_NUMBER_FLAG) == CONTACT_NUMBER_FLAG) txtfldContactNumber.hasError(true);
		if ((maskedLabel & ADDRESS_FLAG) == ADDRESS_FLAG) txtfldAddress.hasError(true);
		
		if (maskedLabel == EMPTY_FLAG) {
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
