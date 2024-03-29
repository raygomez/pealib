package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.apache.commons.lang.RandomStringUtils;

import models.User;
import models.UserDAO;
//import utilities.Connector;
import utilities.Constants;
import utilities.Emailer;
import utilities.Task;
import views.LogInDialog;
import views.SignUpDialog;

public class AuthenticationController {
	private static LogInDialog login;
	private User user;

	private String loginUser;
	private String loginPassword;

	private static SignUpDialog signUp;
	private String sUpFirstName;
	private String sUpLastName;
	private String sUpUserName;
	private String sUpPassword;
	private String sUpConfirmPassword;
	private String sUpEmailAddress;
	private String sUpContactNumber;
	private String sUpAddress;

	// public static void main(String[] args) {
	// try {
	// new Connector(Constants.APP_CONFIG);
	// new AuthenticationController();
	// AuthenticationController.getLogin().setVisible(true);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public AuthenticationController() {
		setLogin(new LogInDialog());
		getLogin().setActionListeners(new SignUpListener(),
				new SubmitListener(), new SubmitKeyAdapter(),
				forgotPasswordListener);
		// new ForgotPasswordListener());
		getLogin().setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}

	/**
	 * @return the login
	 */
	public static LogInDialog getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public static void setLogin(LogInDialog login) {
		AuthenticationController.login = login;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/*
	 * LOG IN LISTENERS
	 */

	// SignUp Listener
	class SignUpListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setSignUp(new SignUpDialog());
			signUp.setActionListeners(new SignUpSubmitListener(),
					new SignUpCancelListener(), new SignUpEnterAdapter(),
					new SignUpUsernameKeyAdapter());
			signUp.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			signUp.setVisible(true);
		}
	}

	// Submit Listener
	class SubmitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			submit();
		}
	}

	// SubmitKeyAdaptor for Keyboard
	class SubmitKeyAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_ENTER) {
				submit();
			} else {
				setUsernamePassword();

				if (loginUser.length() > 0)
					validateUsername(loginUser);
				if (loginPassword.length() > 0)
					validatePassword(loginPassword);
			}
		}
	}

	private boolean validateUsername(String username) {
		boolean result = (username.matches(Constants.USERNAME_FORMAT));
		getLogin().hasUserNameError(!result);
		return result;
	}

	private boolean validatePassword(String password) {
		boolean result = (password.matches(Constants.PASSWORD_FORMAT));
		getLogin().hasPasswordError(!result);

		return result;
	}

	private void setUsernamePassword() {
		loginUser = getLogin().getUsername();
		loginPassword = getLogin().getPassword();
	}

	private void retrieveUser() throws Exception {
		setUser(UserDAO.getUser(loginUser, loginPassword));

		if (user == null) {
			getLogin().setLabelError("Username/Password Mismatch");
			getLogin().clearPassword();
		} else if (user.getType().equals("Pending")) {
			getLogin()
					.setLabelError(
							"<html><center>Account still being processed.<br/>"
									+ "Ask Librarian for further inquiries.</center></html>");
			setUser(null);
			getLogin().clearPassword();
		} else {
			getLogin().dispose();
		}
	}

	private void submit() {
		Callable<Void> toDo = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				retrieveUser();
				return null;
			}
		};

		Task<Void, Void> task = new Task<Void, Void>(toDo);

		setUsernamePassword();
		if (loginUser.equals("") || loginPassword.equals("")) {

			getLogin().setLabelError("Incomplete fields");
			getLogin().hasUserNameError(loginUser.equals(""));
			getLogin().hasPasswordError(loginPassword.equals(""));

		} else if (!validateUsername(loginUser)
				|| !validatePassword(loginPassword)) {
			getLogin().setLabelError("Invalid input");

		} else {

			LoadingControl.init(task, getLogin()).executeLoading();
		}
	}

	/*
	 * SIGN UP LISTENERS
	 */
	// Submit Listener
	class SignUpSubmitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			signUpSubmit();
		}
	}

	// Cancel Listener
	class SignUpCancelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			signUpCancel();
		}
	}

	// Enter Key Adapter (Submit through Enter)
	class SignUpEnterAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				signUpSubmit();
			}
		}
	}

	// User name Key Listener (every input)
	class SignUpUsernameKeyAdapter extends KeyAdapter {
		Timer timer = new Timer(Constants.TIMER_DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				timer.stop();
				getUserName();
				isUserNameValid();
			}
		});

		@Override
		public void keyReleased(KeyEvent arg0) {
			if (arg0.getKeyCode() != KeyEvent.VK_ENTER) {
				if (timer.isRunning())
					timer.restart();
				else
					timer.start();
			}
		}
	}

	private void submitRegistration() {
		User newUser = new User(-1, sUpUserName, sUpPassword, sUpFirstName,
				sUpLastName, sUpEmailAddress, sUpAddress, sUpContactNumber,
				"Pending");
		try {
			UserDAO.saveUser(newUser);
			JOptionPane
					.showMessageDialog(
							signUp.getContentPane(),
							"<html>Your account has been created.<br>"
									+ "Please wait for the Librarian to activate your account.",
							"Sign-up Successful",
							JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			signUpFailed();
			e.printStackTrace();
		}
		signUpCancel();
	}

	private void signUpSubmit() {
		Callable<Void> toDo = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				submitRegistration();
				return null;
			}
		};

		int maskedLabel = 0;
		signUp.setFieldBorderColor(maskedLabel);
		signUp.setErrorMessage("");

		getSignUpData();
		if (!isSignUpFieldComplete(maskedLabel)) {
			signUp.setErrorMessage("Cannot leave mandatory fields empty.");
		} else if (!isUserNameValid()) {
			/* action is handled by isUserNameValid() */
		} else if (!isEmailAddressValid()) {
			/* action is handled by isEmailAddressValid() */
		} else if (!isSignUpInputValid(maskedLabel)) {
			signUp.setErrorMessage("Invalid Input.");
		} else if (!sUpConfirmPassword.equals(sUpPassword)) {
			signUp.setErrorMessage("Mismatch in Confirm Password.");
			signUp.setFieldBorderColor(SignUpDialog.PASSWORD_FLAG
					| SignUpDialog.CONFIRM_PASSWORD_FLAG);
			signUp.clearPassword();
			signUp.clearConfirmPassword();
		} else {
			Task<Void, Void> task = new Task<Void, Void>(toDo);
			LoadingControl.init(task, getLogin()).executeLoading();
		}
	}

	private void getSignUpData() {
		sUpFirstName = signUp.getFirstName();
		sUpLastName = signUp.getLastName();
		getUserName();
		sUpPassword = signUp.getPassword();
		sUpConfirmPassword = signUp.getConfirmPassword();
		sUpEmailAddress = signUp.getEmailAddress();
		sUpContactNumber = signUp.getContactNumber();
		sUpAddress = signUp.getAddress();
	}

	private void getUserName() {
		sUpUserName = signUp.getUserName();
	}

	private boolean isSignUpFieldComplete(int maskedLabel) {
		if (sUpFirstName.trim().isEmpty()) {
			maskedLabel |= SignUpDialog.FIRSTNAME_FLAG;
		}
		if (sUpLastName.trim().isEmpty()) {
			maskedLabel |= SignUpDialog.LASTNAME_FLAG;
		}
		if (sUpUserName.isEmpty()) {
			maskedLabel |= SignUpDialog.USERNAME_FLAG;
		}
		if (sUpPassword.isEmpty()) {
			maskedLabel |= SignUpDialog.PASSWORD_FLAG;
		}
		if (sUpConfirmPassword.isEmpty()) {
			maskedLabel |= SignUpDialog.CONFIRM_PASSWORD_FLAG;
		}
		if (sUpEmailAddress.isEmpty()) {
			maskedLabel |= SignUpDialog.EMAIL_ADDRESS_FLAG;
		}
		/* Contact Number is not a mandatory field */
		if (sUpAddress.trim().isEmpty()) {
			maskedLabel |= SignUpDialog.ADDRESS_FLAG;
		}

		if (maskedLabel != 0) {
			signUp.setFieldBorderColor(maskedLabel);
			return false;
		}
		return true;
	}

	private boolean isSignUpInputValid(int maskedLabel) {
		boolean isFirstNameValid = sUpFirstName.matches(Constants.NAME_FORMAT);
		boolean isLastNameValid = sUpLastName.matches(Constants.NAME_FORMAT);
		boolean isPasswordValid = sUpPassword
				.matches(Constants.PASSWORD_FORMAT);
		boolean isContactNumberValid = sUpContactNumber
				.matches(Constants.CONTACT_NUMBER_FORMAT);
		boolean isAddressValid = Pattern
				.compile(Constants.ADDRESS_FORMAT, Pattern.DOTALL)
				.matcher(sUpAddress).matches();

		if (!isFirstNameValid) {
			maskedLabel |= SignUpDialog.FIRSTNAME_FLAG;
		}

		if (!isLastNameValid) {
			maskedLabel |= SignUpDialog.LASTNAME_FLAG;
		}

		if (!isPasswordValid) {
			maskedLabel |= (SignUpDialog.PASSWORD_FLAG | SignUpDialog.CONFIRM_PASSWORD_FLAG);
			signUp.clearPassword();
			signUp.clearConfirmPassword();
		}

		if (!isContactNumberValid) {
			maskedLabel |= SignUpDialog.CONTACT_NUMBER_FLAG;
		}

		if (!isAddressValid) {
			maskedLabel |= SignUpDialog.ADDRESS_FLAG;
		}

		if (maskedLabel != 0) {
			signUp.setFieldBorderColor(maskedLabel);
			return false;
		}
		return true;
	}

	private boolean isUserNameValid() {
		signUp.setErrorMessage("");
		signUp.setFieldBorderColor(SignUpDialog.EMPTY_FLAG);

		boolean isValid = sUpUserName.matches(Constants.USERNAME_FORMAT);
		if (!isValid) {
			signUp.setErrorMessage("Invalid Input.");
			signUp.setFieldBorderColor(SignUpDialog.USERNAME_FLAG);
			return false;
		}

		try {
			if (UserDAO.isUsernameExisting(sUpUserName)) {
				signUp.setErrorMessage("User name is already in use.");
				signUp.setFieldBorderColor(SignUpDialog.USERNAME_FLAG);
				return false;
			}
		} catch (Exception e) {
			signUpFailed();
			signUpCancel();
		}

		return true;
	}

	private boolean isEmailAddressValid() {
		boolean isValid = sUpEmailAddress.matches(Constants.EMAIL_FORMAT);
		if (!isValid || (sUpEmailAddress.length() > 254)) {
			signUp.setErrorMessage("Invalid Input.");
			signUp.setFieldBorderColor(SignUpDialog.EMAIL_ADDRESS_FLAG);
			return false;
		}

		try {
			if (UserDAO.isEmailExisting(sUpEmailAddress, sUpUserName)) {
				signUp.setErrorMessage("E-mail address is already in use.");
				signUp.setFieldBorderColor(SignUpDialog.EMAIL_ADDRESS_FLAG);
				return false;
			}
		} catch (Exception e) {
			signUpFailed();
			signUpCancel();
		}

		return true;
	}

	private void signUpCancel() {
		sUpFirstName = "";
		sUpLastName = "";
		sUpUserName = "";
		sUpPassword = "";
		sUpConfirmPassword = "";
		sUpEmailAddress = "";
		sUpContactNumber = "";
		sUpAddress = "";
		signUp.dispose();
	}

	private void signUpFailed() {
		JOptionPane.showMessageDialog(signUp.getContentPane(),
				"<html>An error was encountered while creating your account.<br>"
						+ "Please try again later.", "Sign-up Failed",
				JOptionPane.ERROR_MESSAGE);
	}

	public static void setSignUp(SignUpDialog signUp) {
		AuthenticationController.signUp = signUp;
	}

	public static SignUpDialog getSignUp() {
		return signUp;
	}

	// class ForgotPasswordListener extends MouseAdapter{
	MouseAdapter forgotPasswordListener = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent e) {
			String userOrEmail = (String) JOptionPane.showInputDialog(
					getLogin(), "Please enter your username or email",
					"Forgot Password", JOptionPane.PLAIN_MESSAGE);
			if (userOrEmail != null && userOrEmail.length() > 0) {
				try {
					User user = UserDAO.getUserByEmailOrUsername(userOrEmail);
					if (user != null) {
						user.setPassword(RandomStringUtils
								.randomAlphanumeric(8));
						boolean boolConnect = Emailer
								.sendForgetPasswordEmail(user);
						System.out.println(boolConnect);
						if (boolConnect) {
							UserDAO.changePassword(user.getUserId(),
									user.getPassword());
							JOptionPane
									.showMessageDialog(
											getLogin(),
											"Kindly check your email for your new password.",
											"Forgot Password",
											JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane
									.showMessageDialog(
											getLogin(),
											"Internet Connection Error:\n"
													+ "Please check if you have a internet connection.",
											"Forgot Password",
											JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(getLogin(),
								"Username or email is invalid.",
								"Forget Password", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(getLogin(),
							"There is something wrong with your connection.",
							"Forget Password", JOptionPane.ERROR_MESSAGE);
				}
			} else if (userOrEmail != null) {
				JOptionPane.showMessageDialog(getLogin(),
						"Please specify your username or email.",
						"Forget Password", JOptionPane.ERROR_MESSAGE);
			}
		}
	};
}
