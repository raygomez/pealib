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
				String user = login.getFieldUsername().getText();
				String pass = login.getFieldPassword().getText();

				if (user.length() > 0)
					validateUsername(user);
				if (pass.length() > 0)
					validatePassword(pass);
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	}

	private boolean validateUsername(String username) {
		boolean result = (username.matches(Constants.USERNAME_FORMAT));

		if (result)
			login.setUsernameColor(false);
		else
			login.setUsernameColor(true);

		return result;
	}

	private boolean validatePassword(String password) {
		boolean result = (password.matches(Constants.PASSWORD_FORMAT));
		if (result)
			login.setPasswordColor(false);
		else
			login.setPasswordColor(true);

		return result;
	}

	private void submit() {
		String username = login.getFieldUsername().getText();
		String password = login.getFieldPassword().getText();

		if (username.equals("") || password.equals("")) {
			login.setLabelError("Incomplete fields");

			if (username.equals(""))
				login.setUsernameColor(true);
			else
				login.setUsernameColor(false);

			if (password.equals(""))
				login.setPasswordColor(true);
			else
				login.setPasswordColor(false);
		} else if (!validateUsername(username) || !validatePassword(password)) {
			login.setLabelError("Invalid input");
		}

		else if (validateUsername(username) && validatePassword(password)) {
			try {
				setUser(userDao.getUser(username, password));
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
