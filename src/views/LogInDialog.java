package views;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class LogInDialog extends JDialog {

	/**
	 *  Log In Dialog
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel labelUsername = new JLabel("Username");
	private JLabel labelPassword = new JLabel("Password");
	private JTextField fieldUsername = new JTextField(20);
	private JPasswordField fieldPassword = new JPasswordField(20);
	private JLabel labelError = new JLabel("");
	
	private JButton buttonSignUp = new JButton("Sign Up");
	private JButton buttonLogIn = new JButton("Log In");

	/**
	 * Create the dialog.
	 */
	public LogInDialog() {			
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setTitle("Log In");
		setModal(true);
		setBounds((screenSize.width/3), (screenSize.height/3), 400, 230);	
		getContentPane().add(contentPanel);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new MigLayout("", "20px[100px]10px[200px][]", "20px[20px]15px[]10px[]15px[]"));
		
		labelError.setName("labelError");
		labelError.setForeground(Color.red);		
		contentPanel.add(labelError,"cell 0 0 2 0, alignx center");
		
		fieldUsername.setName("username");
		setUsernameColor(false);
		labelUsername.setName("lblUsername");
	    contentPanel.add(labelUsername, "cell 0 1, alignx center");
	    contentPanel.add(fieldUsername, "cell 1 1, growx");
	    
	    fieldPassword.setName("password");
	    setPasswordColor(false);
	    labelPassword.setName("lblPassword");
	    contentPanel.add(labelPassword, "cell 0 2, alignx center");
	    contentPanel.add(fieldPassword, "cell 1 2, growx");
	    
	    contentPanel.add(buttonLogIn, "cell 1 3, alignx center");
		contentPanel.add(buttonSignUp, "cell 1 3, alignx center");
	}
	
	protected JLabel getLabelError() {
		return labelError;
	}

	public void setLabelError(String error) {
		labelError.setText(error);
		validate();
		repaint();
	}
	
	public void setUsernameColor(boolean error){
		if(error) //labelUsername.setForeground(Color.red);
			fieldUsername.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, Color.getHSBColor((float)0.0,(float) 0.6, (float)1)));
		else //labelUsername.setForeground(Color.black);
			fieldUsername.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}
	
	public void setPasswordColor(boolean error){
		if(error) //labelPassword.setForeground(Color.red);
			fieldPassword.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, Color.getHSBColor((float)0.0,(float) 0.6, (float)1)));
		else //labelPassword.setForeground(Color.black);
			fieldPassword.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}

	public JTextField getFieldUsername() {
		return fieldUsername;
	}

	public JTextField getFieldPassword() {
		return fieldPassword;
	}
	
	public void setActionListeners(ActionListener signUp, ActionListener logIn, KeyListener enter){
		buttonSignUp.addActionListener(signUp);
		buttonLogIn.addActionListener(logIn);
		fieldUsername.addKeyListener(enter);
		fieldPassword.addKeyListener(enter);
	}
	

}
