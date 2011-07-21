package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

public class SignUp extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtfldFirstName;
	private JTextField txtfldLastName;
	private JTextField txtfldUserName;
	private JPasswordField txtfldPassword;
	private JPasswordField txtfldConfirmPassword;
	private JTextField txtfldEmailAddress;
	private JTextField txtfldContactNumber;
	private JTextField txtfldAddress;
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
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SignUp dialog = new SignUp();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SignUp() {
		setTitle("New User Account Application");
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 400, 375);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[10:n:10][110:n:110][225:n:225]", "[25:n:25][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20][20:n:20][]"));
		{
			JLabel lblPleaseFillin = new JLabel("Please Fill-in.");
			lblPleaseFillin.setFont(new Font("Tahoma", Font.PLAIN, 14));
			contentPanel.add(lblPleaseFillin, "cell 0 0 2 1,growx,aligny center");
		}
		{
			lblErrorMessage = new JLabel("");
			lblErrorMessage.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
			contentPanel.add(lblErrorMessage, "cell 2 0,alignx left,aligny center");
		}
		{
			lblFirstName = new JLabel("First Name:");
			contentPanel.add(lblFirstName, "cell 1 1,alignx left,aligny center");
		}
		{
			txtfldFirstName = new JTextField();
			lblFirstName.setLabelFor(txtfldFirstName);
			contentPanel.add(txtfldFirstName, "cell 2 1,growx,aligny center");
			txtfldFirstName.setColumns(10);
		}
		{
			lblLastName = new JLabel("Last Name:");
			contentPanel.add(lblLastName, "cell 1 2,alignx left,aligny center");
		}
		{
			txtfldLastName = new JTextField();
			lblLastName.setLabelFor(txtfldLastName);
			contentPanel.add(txtfldLastName, "cell 2 2,growx,aligny center");
			txtfldLastName.setColumns(10);
		}
		{
			lblUserName = new JLabel("User Name:");
			contentPanel.add(lblUserName, "cell 1 4,alignx left,aligny center");
		}
		{
			txtfldUserName = new JTextField();
			lblUserName.setLabelFor(txtfldUserName);
			contentPanel.add(txtfldUserName, "cell 2 4,growx,aligny center");
			txtfldUserName.setColumns(10);
		}
		{
			lblPassword = new JLabel("Password:");
			contentPanel.add(lblPassword, "cell 1 5,alignx left,aligny center");
		}
		{
			txtfldPassword = new JPasswordField();
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
			txtfldConfirmPassword = new JPasswordField();
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
			txtfldEmailAddress = new JTextField();
			lblEmailAddress.setLabelFor(txtfldEmailAddress);
			contentPanel.add(txtfldEmailAddress, "cell 2 8,growx,aligny center");
			txtfldEmailAddress.setColumns(10);
		}
		{
			lblContactNumber = new JLabel("Contact Number:");
			contentPanel.add(lblContactNumber, "cell 1 9,alignx left,aligny center");
		}
		{
			txtfldContactNumber = new JTextField();
			contentPanel.add(txtfldContactNumber, "cell 2 9,growx,aligny center");
			txtfldContactNumber.setColumns(10);
		}
		{
			lblAddress = new JLabel("Address:");
			contentPanel.add(lblAddress, "cell 1 10,alignx left,aligny center");
		}
		{
			txtfldAddress = new JTextField();
			lblAddress.setLabelFor(txtfldAddress);
			contentPanel.add(txtfldAddress, "cell 2 10,growx,aligny center");
			txtfldAddress.setColumns(10);
		}
	
		JPanel buttonPane = new JPanel();
		FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.CENTER);
		fl_buttonPane.setAlignOnBaseline(true);
		buttonPane.setLayout(fl_buttonPane);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			btnSubmit = new JButton("Submit");
			btnSubmit.setActionCommand("Submit");
			buttonPane.add(btnSubmit);
			getRootPane().setDefaultButton(btnSubmit);
		}
		{
			btnCancel = new JButton("Cancel");
			btnCancel.setActionCommand("Cancel");
			buttonPane.add(btnCancel);
		}
	}
	
	public void addSubmitListener(ActionListener submit) {
		btnSubmit.addActionListener(submit);
	}
	
	public void addCancelListener(ActionListener cancel) {
		btnCancel.addActionListener(cancel);
	}
}
