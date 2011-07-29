package controllers;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import models.UserDAO;
import models.User;
import net.miginfocom.swing.MigLayout;
import utilities.Connector;
import utilities.Constants;
import utilities.CrashHandler;
import utilities.Emailer;
import views.ChangePasswordDialog;
import views.UserInfoPanel;
import views.UserSearchPanel;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

public class UserController {

	private final static int USER = 0;
	private final static int PENDING = 1;

	private UserSearchPanel userSearch;
	private UserInfoPanel userInfoPanel;
	private ChangePasswordDialog changePasswordDialog;
	private User currentUser;
	private JPanel layoutPanel;

	private String searchText = "";
	private ArrayList<User> searchedUsers;
	private ArrayList<User> searchedPending;
	private ArrayList<Integer> checkList;

	/*
	 * ..TODO For visual testing purposes only
	 */
	public static void main(String[] args) throws Exception {

		Connector.init(Constants.TEST_CONFIG);
		// new Connector();

		User user = new User(1011, "jjlim", "1234567", "Janine June", "Lim",
				"jaja.lim@yahoo.com", "UP Ayala Technohub", "09171234567",
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

	/*
	 * Constructor
	 */
	public UserController(User user) throws Exception {
		this.currentUser = user;
		
		searchedUsers = new ArrayList<User>();
		searchedPending = new ArrayList<User>();
		checkList = new ArrayList<Integer>();
		
		layoutPanel = new JPanel(new MigLayout("wrap 2", "[grow][grow]","[grow]"));
		userSearch = new UserSearchPanel(new UserSearchTableModel(USER, ""), new UserSearchTableModel(PENDING, ""));
		
		userSearch.addListeners(new SearchListener(),
				new SearchKeyListener(), new TabChangeListener(),
				new UserSelectionListener(), new CheckBoxListener(),
				new AcceptListener(), new DenyListener());
		
		userInfoPanel = new UserInfoPanel();
		userInfoPanel.addSaveListener(save);
		userInfoPanel.addChangePasswordListener(showChangePassword);

		generateLayoutPanel();
	}

	/*
	 * Getters - Setters
	 */

	public User getCurrentUser() { return currentUser; }

	public JPanel getLayoutPanel() {
		generateLayoutPanel();
		return layoutPanel;
	}

	public UserSearchPanel getUserSearch() { return userSearch; }
	
	public UserInfoPanel getUserInfoPanel() { return userInfoPanel;};

	/*
	 * Listeners : Search
	 */
	class CheckBoxListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			UserSearchTableModel model = null;
			
			try {
				model = new UserSearchTableModel(1, searchText);
			} catch (Exception e1) { CrashHandler.handle(e1); }

			if (userSearch.getCbAll().isSelected())
				model.toggleAllCheckBox(true);
			else
				model.toggleAllCheckBox(false);

			userSearch.setTableModel(1, model);
		}
	}

	// TODO pending process
	class AcceptListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			processPend(true);
		}
	}

	class DenyListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			processPend(false);
		}
	}
	
	private void processPend(boolean process) {
		if (searchedPending.size() == checkList.size()) {
			userInfoPanel.toggleButton(false);
			userInfoPanel.clearFields();
		}
		
		String info = "";
		for (int i = 0; i < searchedPending.size(); i++) {
			if (checkList.contains(i)) {
				User temp = searchedPending.get(i);
				temp.setType("User");

				try {
					if (process) {
						UserDAO.updateUser(temp);
						info = "Successfully accepted ("
								+ checkList.size() + ") application/s.";
						Emailer.sendAcceptedEmail(temp);
					} else {
						UserDAO.denyPendingUser(temp);
						info = "Successfully denied (" + checkList.size()
								+ ") application/s.";
						Emailer.sendRejectEmail(temp);
					}
				} catch (Exception e1) {
					CrashHandler.handle(e1);
				}
			}
		}

		JOptionPane.showMessageDialog(userSearch, info);
		userSearch.getCbAll().setSelected(false);
		checkList.clear();	
		searchUsers();

		if (searchedPending.isEmpty())
			userSearch.toggleAllPendingComp(false);
		else {
			setInitSelectPending();
			userSearch.togglePendingButtons(false);
		}
		
		userSearch.resetTabPane();
	}


	private void searchUsers() {
		int tab = userSearch.getSelectedTab();
		searchText = userSearch.getFieldSearch().getText();

		try {
			UserSearchTableModel model = new UserSearchTableModel(tab, searchText);
			userSearch.setTableModel(tab, model);	

			if (tab == USER ) {
				setInitSelectUser();
			} else if (tab == PENDING)
				setInitSelectPending();

		} catch (Exception e) {  CrashHandler.handle(e); }
	}

	private void setInitSelectUser() {
		if (!searchedUsers.isEmpty()) {
			userSearch.getUsersTable().getSelectionModel().setSelectionInterval(0, 0);
			userSearch.getUsersTable().addRowSelectionInterval(0, 0);
		}
	}

	private void setInitSelectPending() {
		if (!searchedPending.isEmpty()) {
			userSearch.getPendingTable().getSelectionModel().setSelectionInterval(0, 0);
			userSearch.getPendingTable().addRowSelectionInterval(0, 0);
		}
	}

	class TabChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			JTabbedPane temp = (JTabbedPane) e.getSource();
			int index = temp.getSelectedIndex();
			searchUsers();

			if (index == 0) {
				setInitSelectUser();	
				userInfoPanel.setEnableFields(true);
				
			} else if (index == 1) {
				userSearch.getCbAll().setSelected(false);
				checkList.clear();
				
				//TODO
				setInitSelectPending();
				if (!searchedPending.isEmpty()) {														
					userSearch.togglePendingButtons(false);					
				} else {
					userSearch.toggleAllPendingComp(false);
					userInfoPanel.toggleButton(false);
					userInfoPanel.clearFields();
					userInfoPanel.setEnableFields(false);
				}
			}
		}
	}

	class SearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			searchUsers();
		}
	}

	class SearchKeyListener extends KeyAdapter {

		Timer timer = new Timer(Constants.TIMER_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				searchText = userSearch.getFieldSearch().getText();
				//TODO 
				if (searchText.length() >= 0)
					searchUsers();
			}
		});

		@Override
		public void keyReleased(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				searchUsers();

			} else {
				if (timer.isRunning())
					timer.restart();
				else
					timer.start();
			}
		}
	}

	//TODO Table Model
	/**
	 * Table Model for User Search (Pending & Active User Accounts)
	 */
	class UserSearchTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[] columns;
		private ArrayList<ArrayList<Object>> tableData;
		private int mode;
		private String searchStr = "";

		public UserSearchTableModel(int tab, String str) throws Exception {
			this.mode = tab;
			this.searchStr = str;

			tableData = new ArrayList<ArrayList<Object>>();

			if (mode == USER) {
				userAcct();
			} else {
				pending();
			}
		}

		private void pending() {
			columns = new String[] { "Username", "Name", "Accept" };

			try {
				searchedPending = UserDAO.searchAllPending(searchStr);

				//TODO
				if (!searchedPending.isEmpty()) {
					for (User i : searchedPending) {
						ArrayList<Object> rowData = new ArrayList<Object>();
						rowData.add(i.getUserName());
						rowData.add(i.getFirstName() + " " + i.getLastName());
						rowData.add(new Boolean(false));
						tableData.add(rowData);
					}
				}

			} catch (Exception e) {
				CrashHandler.handle(e);
			}
		}

		public void toggleAllCheckBox(boolean value) {
			for (int i = 0; i < getRowCount(); i++) {
				setValueAt(new Boolean(value), i, 2);
			}
		}

		private void userAcct() throws Exception {
			columns = new String[] { "Username", "Name" };

			searchedUsers  = UserDAO.searchActiveUsers(searchStr);

			//TODO
			if (!searchedUsers.isEmpty()) {
				for (User i : searchedUsers) {
	
					ArrayList<Object> rowData = new ArrayList<Object>();
					rowData.add(i.getUserName());
					rowData.add(i.getFirstName() + " " + i.getLastName());
					tableData.add(rowData);
				}
			}
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public void setValueAt(Object value, int row, int col) {

			tableData.get(row).set(col, value);

			if (!(Boolean) value) {
				userSearch.getCbAll().setSelected(false);

				//TODO
				if (checkList.contains(row)) {
					checkList.remove((Object) row);
				}
			} else {
				//TODO
				if (!checkList.contains(row)) {
					checkList.add(row);
				}
			}

			if (checkList.size() > 0){
				userSearch.togglePendingButtons(true);
			 
				if (checkList.size() == searchedPending.size())
					userSearch.getCbAll().setSelected(true);
			}
			else
				userSearch.togglePendingButtons(false);
			fireTableCellUpdated(row, col);
		}

		@Override
		public String getColumnName(int col) {
			return columns[col];
		}

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public int getRowCount() {
			return tableData.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			return tableData.get(row).get(col);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex < 2)
				return false;
			else
				return true;
		}
	} // end of table model

	// Generate Layout
	private void generateLayoutPanel() {
		layoutPanel.add(userSearch, "grow");
		layoutPanel.add(userInfoPanel, "grow");

		userSearch.getUsersTable().getSelectionModel()
				.setSelectionInterval(0, 0);
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

		if (!address.matches(Constants.ADDRESS_FORMAT)) {
			tempErrors.add(Constants.ADDRESS_FORMAT_ERROR);
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

		userInfoPanel.displayErrors(errors);
		userInfoPanel.revalidate();

		return pass;

	}

	//TODO UserSelectionList
	private class UserSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (! e.getValueIsAdjusting()) {
				DefaultListSelectionModel dlSelectionModel = (DefaultListSelectionModel) e
						.getSource();
				int row = dlSelectionModel.getAnchorSelectionIndex();
				int tab = userSearch.getSelectedTab();
	
				User user = null;
	
				System.out.println("listener: SELECT ROW: "+row);
				// This is a simple check to see if row is negative.
				if (row < 0)
					return;
	
				if (tab == USER) {
					if (!searchedUsers.isEmpty()){
						System.out.println("Setting row to "+ row);
						user = searchedUsers.get(row);
					}
				} else {
					if (!searchedPending.isEmpty()){
						user = searchedPending.get(row);
					}
				}
	
				if (user != null) {
					userInfoPanel.toggleButton(true);
					userInfoPanel.setFields(user);
					userInfoPanel.setFirstNameEnabled(true);
					userInfoPanel.setLastNameEnabled(true);
				}
			}
		}
	}

	private ActionListener save = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			userInfoPanel.resetErrorMessages();

			int userId = Integer.parseInt(userInfoPanel.getIdNumber());
			String userName = userInfoPanel.getUsername();
			String firstName = userInfoPanel.getFirstName();
			String lastName = userInfoPanel.getLastName();
			String email = userInfoPanel.getEmail();
			String address = userInfoPanel.getAddress();
			String contactNo = userInfoPanel.getContactNumber();
			String type = userInfoPanel.getAccountType();

			if (validateUpdateProfile(firstName, lastName, email, address,
					contactNo)) {

				User user = new User(userId, userName, firstName, lastName,
						email, address, contactNo, type);

				try {
					UserDAO.updateUser(user);
					JOptionPane.showMessageDialog(layoutPanel,
							"Record successfully updated!");
					if (currentUser.getUserId() == userId) {
						currentUser = user;
					} else {
						searchUsers();
					}

				} catch (Exception e) {
					CrashHandler.handle(e);
				}
			}
		}
	};
	private ActionListener showChangePassword = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			changePasswordDialog = new ChangePasswordDialog();

			if (currentUser.getType().equals("Librarian")
					&& !userInfoPanel.getIdNumber().isEmpty()
					&& currentUser.getUserId() != Integer
							.parseInt(userInfoPanel.getIdNumber())) {
				changePasswordDialog.removeOldPassword();
			}
			changePasswordDialog.addChangePasswordListener(changePassword);
			changePasswordDialog.setVisible(true);
		}
	};

	private ActionListener changePassword = new ActionListener() {
		boolean correctPassword;
		boolean isMatchingPassword;
		boolean isPasswordFormatSatisfied;

		@Override
		public void actionPerformed(ActionEvent arg0) {

			int userID;
			if (changePasswordDialog.getOldPasswordField().isEnabled()) {
				String oldPassword = new String(changePasswordDialog
						.getOldPasswordField().getPassword());
				userID = currentUser.getUserId();

				try {
					correctPassword = UserDAO
							.checkPassword(userID, oldPassword);
				} catch (Exception e) {
					CrashHandler.handle(e);
				}
			} else {
				userID = Integer.parseInt(userInfoPanel.getIdNumber());
				correctPassword = true;
			}
			String newPassword1 = new String(changePasswordDialog
					.getNewPasswordField().getPassword());
			String newPassword2 = new String(changePasswordDialog
					.getRepeatPasswordField().getPassword());
			
			isPasswordFormatSatisfied = newPassword1.matches(Constants.PASSWORD_FORMAT);
			isMatchingPassword = newPassword1.equals(newPassword2);

			if (!correctPassword)
				changePasswordDialog
						.displayError(Constants.INCORRECT_PASSWORD_ERROR);
			else if (!isPasswordFormatSatisfied){
				changePasswordDialog.displayError(Constants.PASSWORD_FORMAT_ERROR);
			}
			else if (!isMatchingPassword)
				changePasswordDialog
						.displayError(Constants.PASSWORD_NOT_MATCH_ERROR);
			else {
				try {
					UserDAO.changePassword(userID, newPassword1);
					changePasswordDialog.dispose();
					JOptionPane.showMessageDialog(layoutPanel,
							"Password successfully changed!");
				} catch (Exception e) {
					CrashHandler.handle(e);
				}
			}
		}
	};
}
