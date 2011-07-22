package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import utilities.ErrorLabel;

	@SuppressWarnings("serial")
	public class ChangePasswordDialog extends JDialog {

		private final JPanel contentPanel = new JPanel();
		private JPasswordField oldPasswordField;
		private JPasswordField newPasswordField;
		private JPasswordField repeatPasswordField;
		private JButton changePasswordButton;
		private JLabel errorLabel;
		private JLabel oldPasswordLabel;

		/**
		 * Create the dialog.
		 */
		public ChangePasswordDialog(JFrame frame) {
			super(frame, "Change Password", true);
			createGUI();
		}

		public void createGUI() {

			getContentPane().setLayout(new BorderLayout());
			getContentPanel().setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(getContentPanel(), BorderLayout.CENTER);
			getContentPanel().setLayout(new MigLayout("", "[96.00][grow]",
					"[][][][]"));

			setOldPasswordLabel(new JLabel("Old Password:"));
			getContentPanel().add(getOldPasswordLabel(), "cell 0 0,alignx trailing");
			setOldPasswordField(new JPasswordField());
			getOldPasswordField().setName("oldpassword");
			getContentPanel().add(getOldPasswordField(), "cell 1 0,growx");

			JLabel lblNewLabel_1 = new JLabel("New Password:");
			getContentPanel().add(lblNewLabel_1, "cell 0 1,alignx trailing");
			setNewPasswordField(new JPasswordField());
			getNewPasswordField().setName("newpassword");
			getContentPanel().add(getNewPasswordField(), "cell 1 1,growx");

			JLabel lblNewLabel_2 = new JLabel("Repeat Password:");
			lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
			getContentPanel().add(lblNewLabel_2, "cell 0 2,alignx trailing");
			setRepeatPasswordField(new JPasswordField());
			getRepeatPasswordField().setName("repeatpassword");
			getContentPanel().add(getRepeatPasswordField(), "cell 1 2,growx");

			setErrorLabel(new ErrorLabel(" "));
			getContentPanel().add(getErrorLabel(), "cell 0 3 2 1");

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

		public JPasswordField getOldPasswordField() {
			return oldPasswordField;
		}

		public void setOldPasswordField(JPasswordField oldPasswordField) {
			this.oldPasswordField = oldPasswordField;
		}

		public JPasswordField getNewPasswordField() {
			return newPasswordField;
		}

		public void setNewPasswordField(JPasswordField newPasswordField) {
			this.newPasswordField = newPasswordField;
		}

		public JPasswordField getRepeatPasswordField() {
			return repeatPasswordField;
		}

		public void setRepeatPasswordField(JPasswordField repeatPasswordField) {
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

		public void addchangePasswordListener(ActionListener actionListener) {
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
			remove(getOldPasswordField());
			remove(getOldPasswordLabel());
		}
	}