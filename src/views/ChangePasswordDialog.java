package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import utilities.Constants;
import utilities.ErrorLabel;
import utilities.MyPasswordField;

	@SuppressWarnings("serial")
	public class ChangePasswordDialog extends JDialog {

		private final JPanel contentPanel = new JPanel();
		private MyPasswordField oldPasswordField;
		private MyPasswordField newPasswordField;
		private MyPasswordField repeatPasswordField;
		private JButton changePasswordButton;
		private JLabel errorLabel;
		private JLabel oldPasswordLabel;

		/**
		 * Create the dialog.
		 */
		public ChangePasswordDialog() {
			setModal(true);
			createGUI();
		}

		public void createGUI() {

			setResizable(false);
			getContentPane().setLayout(new BorderLayout());
			getContentPanel().setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(getContentPanel(), BorderLayout.CENTER);
			getContentPanel().setLayout(new MigLayout("", "[96.00][grow]",
					"[][][][]"));

			oldPasswordLabel = new JLabel("Old Password:");
			getContentPanel().add(oldPasswordLabel, "cell 0 0,alignx trailing");
			oldPasswordField = new MyPasswordField(20);
			oldPasswordField.setName("oldpassword");
			getContentPanel().add(oldPasswordField, "cell 1 0,growx");

			JLabel lblNewLabel_1 = new JLabel("New Password:");
			getContentPanel().add(lblNewLabel_1, "cell 0 1,alignx trailing");
			newPasswordField = new MyPasswordField(20);
			newPasswordField.setName("newpassword");
			getContentPanel().add(newPasswordField, "cell 1 1,growx");

			JLabel lblNewLabel_2 = new JLabel("Repeat Password:");
			lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
			getContentPanel().add(lblNewLabel_2, "cell 0 2,alignx trailing");
			repeatPasswordField = new  MyPasswordField(20);
			repeatPasswordField.setName("repeatpassword");
			getContentPanel().add(repeatPasswordField, "cell 1 2,growx");

			errorLabel = new ErrorLabel(" ");
			errorLabel.setName("errorLabel");
			getContentPanel().add(errorLabel, "cell 0 3 2 1, shrink");

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			changePasswordButton = new JButton("Change Password");
			buttonPane.add(changePasswordButton);
			getRootPane().setDefaultButton(changePasswordButton);

			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			buttonPane.add(cancelButton);
			setLocationRelativeTo(null);
			pack();
		}

		public String getOldPassword(){
			return new String(oldPasswordField.getPassword());
		}

		public boolean isOldPasswordIsEnabled(){
			return oldPasswordField.isEnabled();
		}

		public String getNewPassword(){
			return new String(newPasswordField.getPassword());
		}

		public String getRepeatPassword(){
			return new String(repeatPasswordField.getPassword());
		}
		
		public void addChangePasswordListener(ActionListener actionListener) {
			changePasswordButton.addActionListener(actionListener);
		}

		/**
		 * @return the contentPanel
		 */
		public JPanel getContentPanel() {
			return contentPanel;
		}
		
		public void clearOldPassword(){
			oldPasswordField.setVisible(false);
			oldPasswordField.setVisible(false);			
			oldPasswordField.setEnabled(false);
		}				

		public void displayError(int error) {
			switch (error){
				case Constants.DEFAULT_ERROR:
					errorLabel.setText("An error has occured");
					break;
				case Constants.INCORRECT_PASSWORD_ERROR:
					errorLabel.setText("Incorrect password");
					break;
				case Constants.PASSWORD_FORMAT_ERROR:
					errorLabel.setText(Constants.PASSWORD_FORMAT_ERROR_MESSAGE);
					break;
				case Constants.PASSWORD_NOT_MATCH_ERROR:
					errorLabel.setText("New passwords do not match");
					break;
			}
			
		}
	}
