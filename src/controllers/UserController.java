package controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import models.User;
import models.UserDAO;
import net.miginfocom.swing.MigLayout;
import utilities.Connector;
import utilities.Constants;
import views.ChangePasswordDialog;
import views.UserInfoPanel;
import views.UserSearchPanel;

public class UserController {

	private final static int USER = 0;
	private final static int PENDING = 1;

	private UserSearchPanel userSearch;
	private UserInfoPanel userInfoPanel;
	private ChangePasswordDialog changePasswordDialog;
	private User currentUser;
	private User selectedUser;

	private JPanel layoutPanel;

	private String searchText;
	private ArrayList<User> searchedUsers;
	private ArrayList<User> searchedPending;

	/*
	 * ..TODO For visual testing purposes only
	 */
	public static void main(String[] args) {

		new Connector(Constants.TEST_CONFIG);

		User user = new User(1011, "jjlim", "1234567", "Janine June", "Lim",
				"jaja.lim@yahoo.com", "UP Ayala Technohub", "09171234567", 1,
				"Librarian");

		UserController userController = new UserController(user);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0, screenSize.width, screenSize.height);
		frame.setContentPane(userController.getUserPanel());

	}

	// CONSTRUCTOR
	public UserController(User user) {

		this.setCurrentUser(user);
		setLayoutPanel(new JPanel(new MigLayout("wrap 2", "[grow][grow]")));

		setUserSearch(new UserSearchPanel(new UserSearchTableModel(USER, ""),
				new UserSearchTableModel(PENDING, "")));
		getUserSearch().addListeners(new SearchListener(),
				new SearchKeyListener(), new TabChangeListener(),
				new UserSelectionListener());
		setUserInfoPanel(new UserInfoPanel());
		generateLayoutPanel();
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	/**
	 * @return the currentUser
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser
	 *            the currentUser to set
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * @return the searchText
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * @param searchText
	 *            the searchText to set
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	/**
	 * @return the layoutPanel
	 */
	public JPanel getLayoutPanel() {
		return layoutPanel;
	}

	/**
	 * @param layoutPanel
	 *            the layoutPanel to set
	 */
	public void setLayoutPanel(JPanel layoutPanel) {
		this.layoutPanel = layoutPanel;
	}

	/**
	 * @return the userInfoPanel
	 */
	public UserInfoPanel getUserInfoPanel() {
		return userInfoPanel;
	}

	/**
	 * @param userInfoPanel
	 *            the userInfoPanel to set
	 */
	public void setUserInfoPanel(UserInfoPanel userInfoPanel) {
		this.userInfoPanel = userInfoPanel;
	}

	/**
	 * @return the userSearch
	 */
	public UserSearchPanel getUserSearch() {
		return userSearch;
	}

	/**
	 * @param userSearch
	 *            the userSearch to set
	 */
	public void setUserSearch(UserSearchPanel userSearch) {
		this.userSearch = userSearch;
	}

	/**
	 * @return the searchedPending
	 */
	public ArrayList<User> getSearchedPending() {
		return searchedPending;
	}

	/**
	 * @param searchedPending
	 *            the searchedPending to set
	 */
	public void setSearchedPending(ArrayList<User> searchedPending) {
		this.searchedPending = searchedPending;
	}

	/**
	 * @return the searchedUsers
	 */
	public ArrayList<User> getSearchedUsers() {
		return searchedUsers;
	}

	/**
	 * @param searchedUsers
	 *            the searchedUsers to set
	 */
	public void setSearchedUsers(ArrayList<User> searchedUsers) {
		this.searchedUsers = searchedUsers;
	}

	private void generateLayoutPanel() {

		getLayoutPanel().add(getUserSearch(), "grow");
		getLayoutPanel().add(getUserInfoPanel(), "grow");
		getUserInfoPanel().addSaveListener(save);
		getUserInfoPanel().addChangePasswordListener(showChangePassword);
	}

	class TabChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			searchUsers();
		}
	}

	private void searchUsers() {
		int tab = getUserSearch().getSelectedTab();
		setSearchText(getUserSearch().getFieldSearch().getText());
		UserSearchTableModel model = new UserSearchTableModel(tab,
				getSearchText());
		getUserSearch().setTableModel(tab, model);
	}

	class SearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			searchUsers();
		}
	}

	class SearchKeyListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				searchUsers();
			} else {
				setSearchText(getUserSearch().getFieldSearch().getText());
				if (getSearchText().length() > 2
						|| getSearchText().length() == 0)
					searchUsers();

			}
		}
	}

	class UserSearchTableModel extends AbstractTableModel {
		/**
		 * TableModel for User Search Panel/Tabs
		 */
		private static final long serialVersionUID = 1L;
		private String[] columns = { "Username", "Name" };
		private ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();
		private int mode;
		private String searchStr = "";

		public UserSearchTableModel(int tab, String str) {
			this.mode = tab;
			this.searchStr = str;

			if (mode == USER)
				userAcct();
			else
				pending();
		}

		private void pending() {
			try {
				setSearchedPending(UserDAO.searchAllPending(searchStr));
			} catch (Exception e) {
				System.out.println("UserController: userAcct: " + e);
			}

			for (User i : getSearchedPending()) {
				ArrayList<String> rowData = new ArrayList<String>();
				rowData.add(i.getUserName());
				rowData.add(i.getFirstName() + " " + i.getLastName());
				tableData.add(rowData);
			}
		}

		private void userAcct() {
			try {
				setSearchedUsers(UserDAO.searchUsers(searchStr));
			} catch (Exception e) {
				System.out.println("UserController: userAcct: " + e);
			}
			for (User i : getSearchedUsers()) {
				ArrayList<String> rowData = new ArrayList<String>();
				rowData.add(i.getUserName());
				rowData.add(i.getFirstName() + " " + i.getLastName());
				tableData.add(rowData);
			}

		}

		public String getColumnName(int col) {
			return columns[col];
		}

		public int getColumnCount() {
			return columns.length;
		}

		public int getRowCount() {
			return tableData.size();
		}

		public Object getValueAt(int row, int col) {
			return tableData.get(row).get(col);
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	}

	public JPanel getUserPanel() {
		return getLayoutPanel();
	}

	private boolean validateUpdateProfile(String firstName, String lastName,
			String email, String address, String contactNo) {

		boolean pass = true;
		ArrayList<Integer> tempErrors = new ArrayList<Integer>();

		if (!firstName.matches(Constants.NAME_FORMAT)) {
			tempErrors.add(Constants.FIRST_NAME_FORMAT_ERROR);
			pass = false;
		}

		if (!lastName.matches(Constants.NAME_FORMAT)) {
			tempErrors.add(Constants.LAST_NAME_FORMAT_ERROR);
			pass = false;
		}

		if (!email.matches(Constants.EMAIL_FORMAT)) {
			tempErrors.add(Constants.EMAIL_FORMAT_ERROR);
			pass = false;
		}

		if (!contactNo.matches(Constants.CONTACT_NUMBER_FORMAT)) {
			tempErrors.add(Constants.CONTACT_NUMBER_FORMAT_ERROR);
			pass = false;
		}

		int[] errors = new int[tempErrors.size()];
		for (int i = 0; i < tempErrors.size(); i++) {
			errors[i] = tempErrors.get(i);
		}

		getUserInfoPanel().displayErrors(errors);
		return pass;

	}

	private class UserSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			DefaultListSelectionModel dlSelectionModel = (DefaultListSelectionModel) e
					.getSource();
			int row = dlSelectionModel.getAnchorSelectionIndex();
			int tab = getUserSearch().getSelectedTab();

			User user = null;

			// This is a simple check to see if row is negative.
			if (row < 0)
				return;

			if (tab == 0) {
				user = getSearchedUsers().get(row);
			} else {
				user = getSearchedPending().get(row);
			}
			
			setSelectedUser(user);
			getUserInfoPanel().setFields(user.getType(), "" + user.getUserId(),
					user.getUserName(), user.getFirstName(),
					user.getLastName(), user.getAddress(), user.getContactNo(),
					user.getEmail());

		}
	}

	private ActionListener save = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			int userId = Integer.parseInt(getUserInfoPanel().getIdNumber());
			String userName = getUserInfoPanel().getUsername();
			String firstName = getUserInfoPanel().getFirstName();
			String lastName = getUserInfoPanel().getLastName();
			String email = getUserInfoPanel().getEmail();
			String address = getUserInfoPanel().getAddress();
			String contactNo = getUserInfoPanel().getContactNumber();

			if (validateUpdateProfile(firstName, lastName, email, address,
					contactNo)) {
				User user = new User(userId, userName, firstName, lastName,
						email, address, contactNo);

				try {
					UserDAO.updateUser(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	private ActionListener showChangePassword = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			changePasswordDialog = new ChangePasswordDialog();

			if (getCurrentUser().getType().equals("Librarian")
					&& !getUserInfoPanel().getIdNumber().isEmpty()
					&& getCurrentUser().getUserId() != Integer
							.parseInt(getUserInfoPanel().getIdNumber())) {
				changePasswordDialog.removeOldPassword();
			}
			changePasswordDialog
					.addChangePasswordListener(new ChangePasswordListener());
			changePasswordDialog.setVisible(true);
		}
	};

	private class ChangePasswordListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String newPassword = new String(changePasswordDialog
					.getNewPasswordField().getPassword());
			String passwordAgain = new String(changePasswordDialog
					.getRepeatPasswordField().getPassword());

			if (newPassword.isEmpty() || passwordAgain.isEmpty()) {
				changePasswordDialog.getErrorLabel().setText(
						"Enter New Password twice.");
			} else if (newPassword.equals(passwordAgain)) {

				try {
					UserDAO.changePassword(getSelectedUser().getUserId(),
							newPassword);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null,
						"Password Change Successful.", "Success!",
						JOptionPane.PLAIN_MESSAGE);
				changePasswordDialog.dispose();
			} else {
				changePasswordDialog
						.displayError(Constants.PASSWORD_NOT_MATCH_ERROR);
			}
		}
	}
}
