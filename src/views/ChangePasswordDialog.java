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

			setOldPasswordLabel(new JLabel("Old Password:"));
			getContentPanel().add(getOldPasswordLabel(), "cell 0 0,alignx trailing");
			setOldPasswordField(new MyPasswordField(20));
			getOldPasswordField().setName("oldpassword");
			getContentPanel().add(getOldPasswordField(), "cell 1 0,growx");

			JLabel lblNewLabel_1 = new JLabel("New Password:");
			getContentPanel().add(lblNewLabel_1, "cell 0 1,alignx trailing");
			setNewPasswordField(new MyPasswordField(20));
			getNewPasswordField().setName("newpassword");
			getContentPanel().add(getNewPasswordField(), "cell 1 1,growx");

			JLabel lblNewLabel_2 = new JLabel("Repeat Password:");
			lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
			getContentPanel().add(lblNewLabel_2, "cell 0 2,alignx trailing");
			setRepeatPasswordField(new MyPasswordField(20));
			getRepeatPasswordField().setName("repeatpassword");
			getContentPanel().add(getRepeatPasswordField(), "cell 1 2,growx");

			setErrorLabel(new ErrorLabel(" "));
			errorLabel.setName("errorLabel");
			getContentPanel().add(getErrorLabel(), "cell 0 3 2 1, shrink");

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			setChangePasswordButton(new JButton("Change Password"));
			buttonPane.add(getChangePasswordButton());
			getRootPane().setDefaultButton(getChangePasswordButton());

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

		public MyPasswordField getOldPasswordField() {
			return oldPasswordField;
		}

		public void setOldPasswordField(MyPasswordField oldPasswordField) {
			this.oldPasswordField = oldPasswordField;
		}

		public MyPasswordField getNewPasswordField() {
			return newPasswordField;
		}

		public void setNewPasswordField(MyPasswordField newPasswordField) {
			this.newPasswordField = newPasswordField;
		}

		public MyPasswordField getRepeatPasswordField() {
			return repeatPasswordField;
		}

		public void setRepeatPasswordField(MyPasswordField repeatPasswordField) {
			this.repeatPasswordField = repeatPasswordField;
		}

		/**
		 * @return the changePasswordButton_
		 */
		public JButton getChangePasswordButton() {
			return changePasswordButton;
		}

		/**
		 * @param changePasswordButton
		 *            the changePasswordButton to set
		 */
		public void setChangePasswordButton(JButton changePasswordButton) {
			this.changePasswordButton = changePasswordButton;
		}

		/**
		 * @return the errorLabel
		 */
		public JLabel getErrorLabel() {
			return errorLabel;
		}

		/**
		 * @param errorLabel
		 *            the errorLabel to set
		 */
		public void setErrorLabel(JLabel errorLabel) {
			this.errorLabel = errorLabel;
		}

		public void addChangePasswordListener(ActionListener actionListener) {
			getChangePasswordButton().addActionListener(actionListener);
		}

		/**
		 * @return the contentPanel
		 */
		public JPanel getContentPanel() {
			return contentPanel;
		}
		
		/**
		 * @return the oldPasswordLabel
		 */
		public JLabel getOldPasswordLabel() {
			return oldPasswordLabel;
		}

		/**
		 * @param oldPasswordLabel the oldPasswordLabel to set
		 */
		public void setOldPasswordLabel(JLabel oldPasswordLabel) {
			this.oldPasswordLabel = oldPasswordLabel;
		}

		public void removeOldPassword(){
			getOldPasswordField().setVisible(false);
			getOldPasswordLabel().setVisible(false);			
			getOldPasswordField().setEnabled(false);
		}				

		public void displayError(int error) {
			// TODO Auto-generated method stub
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
