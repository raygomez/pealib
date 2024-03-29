package views;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utilities.ErrorLabel;

import net.miginfocom.swing.MigLayout;

public class ConfigureDBConnectionDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel layoutPanel;
	
	private JTextField databaseHostField;
	private JTextField databaseNameField;
	private JTextField databaseUsernameField;
	private JPasswordField databasePasswordField;
	
	private JLabel tempLabel;
	private ErrorLabel errorLabel;
	
	private JButton saveButton;
	private JButton testConnectionButton;
	private JButton cancelButton;
	
	private ConfigureDBConnectionDialog(){
		
		setUndecorated(true);
		setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
				
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width/3), (screenSize.height/3), 200, 50);
				
		layoutPanel = new JPanel(new MigLayout());
		
		errorLabel = new ErrorLabel();
		tempLabel = new JLabel("Database Host");
		databaseHostField = new JTextField(20);
		databaseHostField.setName("databaseHostField");
		
		layoutPanel.add(errorLabel, "wrap");
		layoutPanel.add(tempLabel);
		layoutPanel.add(databaseHostField, "wrap");
		
		tempLabel = new JLabel("Database Name");
		databaseNameField = new JTextField(20);
		databaseNameField.setName("databaseNameField");
		
		layoutPanel.add(tempLabel);
		layoutPanel.add(databaseNameField, "wrap");
		
		tempLabel = new JLabel("Database Username");
		databaseUsernameField = new JTextField(20);
		databaseUsernameField.setName("databaseUsernameField");
		
		layoutPanel.add(tempLabel);
		layoutPanel.add(databaseUsernameField, "wrap");
		
		tempLabel = new JLabel("Database Password");
		databasePasswordField = new JPasswordField(20);
		databasePasswordField.setName("databasePasswordField");
		
		layoutPanel.add(tempLabel);
		layoutPanel.add(databasePasswordField, "wrap");
		
		saveButton = new JButton("Save Configuration");
		cancelButton = new JButton("Cancel");
		testConnectionButton = new JButton("Test Connection");
		
		cancelButton.addActionListener(cancel);
		
		layoutPanel.add(testConnectionButton);
		layoutPanel.add(saveButton);
		layoutPanel.add(cancelButton);
		
		getContentPane().add(layoutPanel);
		
		setLocationRelativeTo(null);
		pack();	
		
	}
	
	private void clearFields(){
		databaseHostField.setText("");
		databaseNameField.setText("");
		databaseUsernameField.setText("");
		databasePasswordField.setText("");
		errorLabel.setText("");
	}
	
	public String getDatabaseHost(){
		return databaseHostField.getText();
	}
	
	public String getDatabaseName(){
		return databaseNameField.getText();
	}
	
	public String getDatabaseUsername(){
		return databaseUsernameField.getText();
	}
	
	public String getDatabasePassword(){
		return new String(databasePasswordField.getPassword());
	}
	
	public static ConfigureDBConnectionDialog getConfigureDBConnectionDialog(){
		DialogHolder.instance.clearFields();
		return DialogHolder.instance;
	}
	
	public void addSaveButtonListener(ActionListener save){
		saveButton.addActionListener(save);
	}
	
	public void addTestConnectionButtonListener(ActionListener testConnection){
		testConnectionButton.addActionListener(testConnection);
	}
	
	private ActionListener cancel = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			clearFields();
			setVisible(false);
		}
	};
	
	private static class DialogHolder{
		public static final ConfigureDBConnectionDialog instance = new ConfigureDBConnectionDialog();
	}
}
