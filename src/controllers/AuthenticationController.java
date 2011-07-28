package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.apache.commons.lang.RandomStringUtils;

import models.User;
import models.UserDAO;
import utilities.Connector;
import utilities.Constants;
import utilities.Emailer;
import utilities.Task;
import utilities.TaskUpdateListener;
import views.LoadingDialog;
import views.LogInDialog;
import views.SignUpDialog;

public class AuthenticationController {
	private static LogInDialog login;
	private User user;

	private String login_user;
	private String login_pass;

	private static SignUpDialog signUp;
	private String sUpFirstName;
	private String sUpLastName;
	private String sUpUserName;
	private String sUpPassword;
	private String sUpConfirmPassword;
	private String sUpEmailAddress;
	private String sUpContactNumber;
	private String sUpAddress;
	
	public static void main(String[] args) {
		try {
			new Connector(Constants.TEST_CONFIG);
			new AuthenticationController();
			AuthenticationController.getLogin().setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AuthenticationController() {
		setLogin(new LogInDialog());
		getLogin().setActionListeners(new SignUpListener(),
				new SubmitListener(), new SubmitKeyAdapter(),
				new ForgotPasswordListener());
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

				if (login_user.length() > 0)
					validateUsername(login_user);
				if (login_pass.length() > 0)
					validatePassword(login_pass);
			}
		}
	}

	private boolean validateUsername(String username) {
		boolean result = (username.matches(Constants.USERNAME_FORMAT));
		getLogin().getFieldUsername().hasError(!result);

		return result;
	}

	private boolean validatePassword(String password) {
		boolean result = (password.matches(Constants.PASSWORD_FORMAT));
		getLogin().getFieldPassword().hasError(!result);

		return result;
	}

	private void setUsernamePassword() {
		login_user = getLogin().getFieldUsername().getText();
		login_pass = new String(getLogin().getFieldPassword().getPassword());
	}

	private void retrieveUser() throws Exception{
		setUser(UserDAO.getUser(login_user, login_pass));
		
		if (user == null) {
			getLogin().setLabelError("Username/Password Mismatch");
			getLogin().getFieldPassword().setText("");
		} else if (user.getType().equals("Pending")) {
			getLogin()
					.setLabelError(
							"<html><center>Account still being processed.<br/>"
									+ "Ask Librarian for further inquiries.</center></html>");
			getLogin().getFieldPassword().setText("");
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
		task.addPropertyChangeListener(new TaskUpdateListener(new LoadingDialog(getLogin())));
		
		setUsernamePassword();
		if (login_user.equals("") || login_pass.equals("")) {

			getLogin().setLabelError("Incomplete fields");
			getLogin().getFieldUsername().hasError(
					login_user.equals("") || !validateUsername(login_user));
			getLogin().getFieldPassword().hasError(
					login_pass.equals("") || !validatePassword(login_pass));

		} else if (!validateUsername(login_user)
				|| !validatePassword(login_pass)) {
			getLogin().setLabelError("Invalid input");
		}

		else {
			try {
				task.execute();
			} catch (Exception e) {
				System.out.println("AuthenticationController getUser: " + e);
				getLogin().setLabelError("Connection Error!");
			}
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

	private void submitRegistration(){
		int maskedLabel = 0;
		signUp.setFieldBorderColor(maskedLabel);
		signUp.setLblErrorMessage("");
		
		getSignUpData();
		if (!isSignUpFieldComplete(maskedLabel)) {
			signUp.setLblErrorMessage("Cannot leave mandatory fields empty.");
		} else if (!isUserNameValid()) {
			/* action is handled by isUserNameValid() */
		} else if (!isSignUpInputValid(maskedLabel)) {
			signUp.setLblErrorMessage("Invalid Input.");
		} else if (!sUpConfirmPassword.equals(sUpPassword)) {
			signUp.setLblErrorMessage("Mismatch in Confirm Password.");
			signUp.setFieldBorderColor(SignUpDialog.PASSWORD_FLAG
					| SignUpDialog.CONFIRM_PASSWORD_FLAG);
			signUp.getTxtfldPassword().setText("");
			signUp.getTxtfldConfirmPassword().setText("");
		} else {
			User newUser = new User(-1, sUpUserName, sUpPassword, sUpFirstName,
					sUpLastName, sUpEmailAddress, sUpAddress, sUpContactNumber,
					"Pending");
			try {
				UserDAO.saveUser(newUser);
				JOptionPane.showMessageDialog(signUp.getContentPane(),
						"Your account has been created. " +
						"Please wait for the Librarian to activate your account.",
						"Sign-up Successful", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				signUpFailed();
				e.printStackTrace();
			}
			signUpCancel();
		}
	}
	
	private void signUpSubmit() {
		Callable<Void> toDo = new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				submitRegistration();
				return null;
			}
		};
		
		Task<Void, Void> task = new Task<Void, Void>(toDo);
		task.addPropertyChangeListener(new TaskUpdateListener(new LoadingDialog(getSignUp())));
		task.execute();
	}

	private void getSignUpData() {
		sUpFirstName = signUp.getTxtfldFirstName().getText();
		sUpLastName = signUp.getTxtfldLastName().getText();
		getUserName();
		sUpPassword = new String(signUp.getTxtfldPassword().getPassword());
		sUpConfirmPassword = new String(signUp.getTxtfldConfirmPassword()
				.getPassword());
		sUpEmailAddress = signUp.getTxtfldEmailAddress().getText();
		sUpContactNumber = signUp.getTxtfldContactNumber().getText();
		sUpAddress = signUp.getTxtfldAddress().getText();
	}

	private void getUserName() {
		sUpUserName = signUp.getTxtfldUserName().getText();
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
		boolean isEMailAddressValid = sUpEmailAddress
				.matches(Constants.EMAIL_FORMAT);
		boolean isContactNumberValid = sUpContactNumber
				.matches(Constants.CONTACT_NUMBER_FORMAT);
		boolean isAddressValid = sUpAddress.matches(Constants.ADDRESS_FORMAT);

		if (!isFirstNameValid) {
			maskedLabel |= SignUpDialog.FIRSTNAME_FLAG;
			signUp.getTxtfldFirstName().setText("");
		}
		if (!isLastNameValid) {
			maskedLabel |= SignUpDialog.LASTNAME_FLAG;
			signUp.getTxtfldLastName().setText("");
		}
		if (!isPasswordValid) {
			maskedLabel |= (SignUpDialog.PASSWORD_FLAG | SignUpDialog.CONFIRM_PASSWORD_FLAG);
			signUp.getTxtfldPassword().setText("");
			signUp.getTxtfldConfirmPassword().setText("");
		}
		if (!isEMailAddressValid) {
			maskedLabel |= SignUpDialog.EMAIL_ADDRESS_FLAG;
			signUp.getTxtfldEmailAddress().setText("");
		}
		if (!isContactNumberValid) {
			maskedLabel |= SignUpDialog.CONTACT_NUMBER_FLAG;
			signUp.getTxtfldContactNumber().setText("");
		}
		if (!isAddressValid) {
			maskedLabel |= SignUpDialog.ADDRESS_FLAG;
			signUp.getTxtfldAddress().setText("");
		}

		if (maskedLabel != 0) {
			signUp.setFieldBorderColor(maskedLabel);
			return false;
		}
		return true;
	}

	private boolean isUserNameValid() {
		signUp.setLblErrorMessage("");
		signUp.setFieldBorderColor(0);

		boolean isValid = sUpUserName.matches(Constants.USERNAME_FORMAT);
		if (!isValid) {
			signUp.setLblErrorMessage("Invalid Input.");
			signUp.setFieldBorderColor(SignUpDialog.USERNAME_FLAG);
			return false;
		}

		boolean isNotUnique = true;
		try {
			isNotUnique = UserDAO.isUsernameExisting(sUpUserName);
		} catch (Exception e) {
			signUpFailed();
			signUpCancel();
			e.printStackTrace();
		}

		if (isNotUnique) {
			signUp.setLblErrorMessage("User name is already in use.");
			signUp.setFieldBorderColor(SignUpDialog.USERNAME_FLAG);
			return false;
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
				"An error was encountered while creating your account. " +
				"Please try again later.",
				"Sign-up Failed", JOptionPane.ERROR_MESSAGE);
	}

	public static void setSignUp(SignUpDialog signUp) {
		AuthenticationController.signUp = signUp;
	}

	public static SignUpDialog getSignUp() {
		return signUp;
	}
	
	class ForgotPasswordListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			String userOrEmail = (String)JOptionPane.showInputDialog(
					getLogin(),
                    "Please enter your usernamer or email",
                    "Forgot Password",
                    JOptionPane.PLAIN_MESSAGE);
			if(userOrEmail != null && userOrEmail.length() > 0){
				try {
					User user = UserDAO.getUserByEmailOrUsername(userOrEmail);
					if(user != null){
						user.setPassword(RandomStringUtils.randomAlphanumeric(8));
						UserDAO.changePassword(user.getUserId(),user.getPassword());
						Emailer.sendForgetPasswordEmail(user);
						JOptionPane.showMessageDialog(getLogin(), "Check your email for your new password.", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(getLogin(), "Username or email is invalid.", "Forget Password", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if(userOrEmail != null){
				JOptionPane.showMessageDialog(getLogin(), "Please specify your username or email.", "Forget Password", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
