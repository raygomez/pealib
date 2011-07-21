package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;

import models.User;
import models.UserDAO;

import utilities.Constants;

import views.LogInDialog;

public class AuthenticationController {
	private static LogInDialog login;
	private User user;
	private UserDAO userDao;
	
	String login_user;
	String login_pass;

	public static void main(String[] args) {
		try {
			new AuthenticationController();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AuthenticationController() {

		try {
			userDao = new UserDAO();
		} catch (Exception e) {
			System.out.println("UserDAO " + e);
		}

		login = new LogInDialog();
		login.setActionListeners(new SignUpListener(), new SubmitListener(),
				new SubmitKeyListener());
		login.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		login.setVisible(true);

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
			// open signup pane
		}
	}

	// Submit Listener
	class SubmitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			submit();
		}
	}

	// Submit Listener for Keyboard
	class SubmitKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();

			if (keyCode == 10) {
				submit();
			} else {
				setUsernamePassword();

				if (login_user.length() > 0)
					validateUsername(login_user);
				if (login_pass.length() > 0)
					validatePassword(login_pass);
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	}

	private boolean validateUsername(String username) {
		boolean result = (username.matches(Constants.USERNAME_FORMAT));

		login.getFieldUsername().hasError(!result);

		return result;
	}

	private boolean validatePassword(String password) {
		boolean result = (password.matches(Constants.PASSWORD_FORMAT));
		
		login.getFieldPassword().hasError(!result);		

		return result;
	}
	
	private void setUsernamePassword(){
		login_user = login.getFieldUsername().getText();
		login_pass = new String(login.getFieldPassword().getPassword());
	}

	private void submit() {
		setUsernamePassword();
		if (login_user.equals("") || login_pass.equals("")) {
			
			login.setLabelError("Incomplete fields");
			login.getFieldUsername().hasError(login_user.equals("") || !validateUsername(login_user));
			login.getFieldPassword().hasError(login_pass.equals("") || !validatePassword(login_pass));
			
		} else if (!validateUsername(login_user) || !validatePassword(login_pass)) {
			login.setLabelError("Invalid input");
		}

		else if (validateUsername(login_user) && validatePassword(login_pass)) {
			try {
				setUser(userDao.getUser(login_user, login_pass));
			} catch (Exception e) {
				System.out.println("AuthenticationController getUser: " + e);
			}

			if (user != null) {
				// login --> call main frame

				System.out.println("LOGIN");
				login.dispose();
			} else {
				login.setLabelError("Username/Password Mismatch");
				login.getFieldPassword().setText("");
			}
		}
	}
}
