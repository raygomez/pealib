package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import models.User;
import models.UserDAO;
import net.miginfocom.swing.MigLayout;
import pealib.PeaLibrary;
import utilities.Constants;
import utilities.CrashHandler;
import utilities.Emailer;
import utilities.Task;
import views.ChangePasswordDialog;
import views.UserInfoPanel;
import views.UserSearchPanel;

public class UserController {

	private final static int USER = 0;
	//private final static int PENDING = 1;

	private UserSearchPanel userSearch;
	private UserInfoPanel userInfoPanel;
	private ChangePasswordDialog changePasswordDialog;
	private User currentUser;
	private JPanel layoutPanel;

	private String searchText = "";
	private ArrayList<User> searchedUsers;
	private ArrayList<User> searchedPending;
	private ArrayList<Integer> checkList;
	
	private AbstractTableModel model;

	/*
	  ..TODO For visual testing purposes only
	
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
		frame.setContentPane(userController.getLayoutPanel());

	}
	*/
 
	/**
	 * Constructor
	 */
	public UserController(User user) throws Exception {
		this.currentUser = user;
		
		searchedUsers = new ArrayList<User>();
		searchedPending = new ArrayList<User>();
		checkList = new ArrayList<Integer>();
		
		layoutPanel = new JPanel(new MigLayout("wrap 2", "[grow][grow]","[grow]"));
		userSearch = new UserSearchPanel();
		userSearch.setModelUsers(new UserSearchTableModel(USER, ""));
 
		userSearch.usersPanel();
		userSearch.pendingAppPanel();
		userSearch.addListeners(new SearchListener(),
				new SearchKeyListener(), new TabChangeListener(),
				new UserSelectionListener(), new CheckBoxListener(),
				new AcceptListener(), new DenyListener());
		
		userInfoPanel = new UserInfoPanel();
		userInfoPanel.addSaveListener(save);
		userInfoPanel.addChangePasswordListener(showChangePassword);

		generateLayoutPanel();
	}

	/**
	 * Getters - Setters
	 */

	public User getCurrentUser() { return currentUser; }

	public JPanel getLayoutPanel() {
		generateLayoutPanel();
		searchUsers();
		return layoutPanel;
	}
	

	public UserSearchPanel getUserSearch() { return userSearch; }
	
	public UserInfoPanel getUserInfoPanel() { return userInfoPanel;};

	/**
	 * Listener: Select All checkbox
	 */
	class CheckBoxListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			if (userSearch.getCbAll().isSelected()){
				int lastRow = userSearch.getPendingTable().getModel().getRowCount();			   
				userSearch.getPendingTable().getSelectionModel().setSelectionInterval(0, lastRow-1);
			}
			else {				
				setInitSelectPending();
			}
			userSearch.setTableModel(1, model);
		}
	}
	/**
	 * Listener: Accept button
	 */
	class AcceptListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			processPend(true);
		}
	}
	/**
	 * Listener: Deny button
	 */
	class DenyListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			processPend(false);
		}
	}
	/**
	 * Method: Process pending applications (ie, accept or deny)
	 * Used by Accept & Deny Listener
	 */	
	private void processPend(boolean process) {		
		String info = "";
		int numberOfSuccessful = 0;
		
			boolean isSuccessful = false;
			
	
		int[] selected = userSearch.getPendingTable().getSelectedRows();

		
		for(int index : selected){
			User userContainer = searchedPending.get(index);
			
			userContainer.setType("User");
			
			try {
				if (process) {
					UserDAO.updateUser(userContainer);
					isSuccessful = Emailer.sendAcceptedEmail(userContainer);
				} else {
					UserDAO.denyPendingUser(userContainer);
					isSuccessful = Emailer.sendRejectEmail(userContainer);
				}
			} catch (Exception e1) {
				CrashHandler.handle(e1);
			}		
			
			if(isSuccessful)
				numberOfSuccessful++;
			
		}
		
		
		
		info = (process? "Successfully added " +userSearch.getPendingTable().getSelectedRowCount()+" users." 
						: "Denied " +userSearch.getPendingTable().getSelectedRowCount()+" users.");
		
		info += "\nYou were able to send " + numberOfSuccessful + "notifications to the users.";
		JOptionPane.showMessageDialog(userSearch, info);
		userSearch.getCbAll().setSelected(false);
		checkList.clear();	
		searchUsers();
		configurePendingUI();		
		userSearch.resetTabPane();
	}
	/**
	 * Method: Enabling/Disabling buttons in Pending - Search & Info
	 */	
	private void configurePendingUI(){
		if (!searchedPending.isEmpty()) {
			userSearch.getCbAll().setEnabled(true);
			userSearch.togglePendingButtons(true);					
		} else {
			userSearch.toggleAllPendingComp(false);
			userInfoPanel.toggleButton(false);
			userInfoPanel.clearFields();
			userInfoPanel.setEnableFields(false);
		}
	}
	/**
	 * Method: Select the first row in table under the Users Tab
	 * note: to avoid exceptions from list selection listener
	 */	
	private void setInitSelectUser() {
		if (!searchedUsers.isEmpty()) {
			userSearch.getUsersTable().getSelectionModel().setSelectionInterval(0, 0);
			//userSearch.getUsersTable().addRowSelectionInterval(0, 0);
		}
	}
	/**
	 * Method: Select the first row in table under the Pending Tab
	 * note: to avoid exceptions from list selection listener
	 */	

	public void setInitSelectPending() {
		if (!searchedPending.isEmpty()) {
			userSearch.getPendingTable().getSelectionModel().setSelectionInterval(0, 0);
			//userSearch.getPendingTable().addRowSelectionInterval(0, 0);
		}
	}

	/**
	 * Method: search for users & generating a new table model
	 */
	private void searchUsers() {
		
		final int tab = userSearch.getSelectedTab();
		
		if(tab == USER) userSearch.getUsersTable().setVisible(false);
		else userSearch.getPendingTable().setVisible(false);
				
		Callable<Void> toDo = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				
				searchText = userSearch.getFieldSearch().getText();
				
				try {
					
					model = new UserSearchTableModel(tab, searchText);

				} catch (Exception e) {  CrashHandler.handle(e); }
				
				return null;
			}
		};
		
		Callable<Void> toDoAfter = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				userSearch.setTableModel(tab, model);					
				
				if (tab == USER ) {
					userSearch.getUsersTable().setVisible(true);
					setInitSelectUser();
				} else{
					checkList.clear();
					userSearch.getCbAll().setSelected(false);
					userSearch.getPendingTable().setVisible(true);
					setInitSelectPending();
					configurePendingUI();
				}
				return null;
			}
		};
		
		Task<Void, Object> task = new Task<Void, Object>(toDo, toDoAfter);
		
		LoadingControl.init(task, PeaLibrary.getMainFrame()).executeLoading();
		
	}
	/**
	 * Method: Listener when changing tabs - refresh table
	 */
	class TabChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			JTabbedPane temp = (JTabbedPane) e.getSource();
			int index = temp.getSelectedIndex();
			//TODO
			searchUsers();
			
			if (index == 0) {
				setInitSelectUser();	
				userInfoPanel.setEnableFields(true);
				
			} else {
				setInitSelectPending();
				configurePendingUI();							
			}
		}
	}
	/**
	 * Method: Listener for Search Button 
	 */
	class SearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			searchUsers();
		}
	}
	/**
	 * Method: Listener for auto-complete search : searches every key pressed
	 */
	class SearchKeyListener extends KeyAdapter {

		Timer timer = new Timer(Constants.TIMER_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				searchText = userSearch.getFieldSearch().getText();
				
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

	/**
	 * Table Model for User Search (Pending & Active User Accounts)
	 */
	class UserSearchTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[] columns;
		private ArrayList<ArrayList<Object>> tableData;
		private int mode;
		private String searchStr = "";
		
		/*
		 * Figures out what tab is currently selected, to determine what table model
		 * to make : users table or pending table 
		 */
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

		/*
		 * Filling up table data for Pending
		 */
		private void pending() {
			columns = new String[] { "Username", "Name" };

			try {
				searchedPending = UserDAO.searchAllPending(searchStr);

				if (!searchedPending.isEmpty()) {
					for (User i : searchedPending) {
						ArrayList<Object> rowData = new ArrayList<Object>();
						rowData.add(i.getUserName());
						rowData.add(i.getFirstName() + " " + i.getLastName());
						//rowData.add(new Boolean(false));               //removing checkbox
						tableData.add(rowData);
					}
				}

			} catch (Exception e) {
				CrashHandler.handle(e);
			}
		}
		/*
		 * For SelectAll Check Box in Pending: Make every checkbox true/false
		 */		
		public void toggleAllCheckBox(boolean value) {
			for (int i = 0; i < getRowCount(); i++) {
				setValueAt(new Boolean(value), i, 2);
			}
		}
		/*
		 * Filling up table data for Users
		 */
		private void userAcct() throws Exception {
			columns = new String[] { "Username", "Name" };

			searchedUsers  = UserDAO.searchActiveUsers(searchStr);

			if (!searchedUsers.isEmpty()) {
				for (User i : searchedUsers) {
	
					ArrayList<Object> rowData = new ArrayList<Object>();
					rowData.add(i.getUserName());
					rowData.add(i.getFirstName() + " " + i.getLastName());
					tableData.add(rowData);
				}
			}
		}
		/*
		 * Renderer for checkbox column
		 
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
		*/
		/*
		 * Setting value for checkbox
		
		public void setValueAt(Object value, int row, int col) {

			tableData.get(row).set(col, value);

			if (!(Boolean) value) {
				userSearch.getCbAll().setSelected(false);

				if (checkList.contains(row)) {
					checkList.remove((Object) row);
				}
				
			} else {
				if (!checkList.contains(row)) {
					checkList.add(row);
				}
			}

			if (!checkList.isEmpty()){
				userSearch.togglePendingButtons(true);
			 
				if (checkList.size() == searchedPending.size())
					userSearch.getCbAll().setSelected(true);
			}
			else
				userSearch.togglePendingButtons(false);
			
			fireTableCellUpdated(row, col);
		}
		 */

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
		/*
		 * Make only checkbox column editable
		 */
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
//			if (columnIndex < 2)
				return false;
//			else
//				return true;
		}
	} // end of table model

	// Generate Layout
	private void generateLayoutPanel() {
		layoutPanel.add(userSearch, "grow");
		layoutPanel.add(userInfoPanel, "grow");

		if(!searchedUsers.isEmpty()){
			//userSearch.getUsersTable().getSelectionModel().setSelectionInterval(0, 0);
			setInitSelectUser();
		}
	}

//	public JPanel getUserPanel() {
//		return getLayoutPanel();
//	}
	
	private boolean isEmailUnique(String email, String username){
		
		boolean temp = true;
		
		try {
			temp = !UserDAO.isEmailExisting(email, username);
		} catch (Exception e) {
			CrashHandler.handle(e);
		}
						
		return temp;
	}

	private boolean validateUpdateProfile(String firstName, String lastName,
			String email, String address, String contactNo, String userName) {

		boolean pass = true;
		boolean flag = true;
		ArrayList<Integer> tempErrors = new ArrayList<Integer>();

		if (!firstName.matches(Constants.NAME_FORMAT)) {
			tempErrors.add(Constants.FIRST_NAME_FORMAT_ERROR);
			pass = false;
		}

		if (!lastName.matches(Constants.NAME_FORMAT)) {
			tempErrors.add(Constants.LAST_NAME_FORMAT_ERROR);
			pass = false;
		}

		Matcher matcher = Pattern.compile(Constants.ADDRESS_FORMAT, Pattern.DOTALL).matcher(address);
		
		if (!matcher.matches()) {
			tempErrors.add(Constants.ADDRESS_FORMAT_ERROR);
			pass = false;
		}
 
		if (!contactNo.matches(Constants.CONTACT_NUMBER_FORMAT)) {
			tempErrors.add(Constants.CONTACT_NUMBER_FORMAT_ERROR);
			pass = false;
		}
		
		if (!email.matches(Constants.EMAIL_FORMAT)) {
			tempErrors.add(Constants.EMAIL_FORMAT_ERROR);
			pass = false;
			flag = false;
		}
		
		if (!isEmailUnique(email, userName) && flag) {
			tempErrors.add(Constants.EMAIL_EXIST_ERROR);
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
				int row = dlSelectionModel.getLeadSelectionIndex();
				
				int tab = userSearch.getSelectedTab();
	
				User user = null;
				// This is a simple check to see if row is negative.
				if (row < 0)
					return;
	
				if (tab == USER) {				
					user = searchedUsers.get(row);
			
				} else {
					user = searchedPending.get(row);
				}
	
				if (user != null) {
					userInfoPanel.resetErrorMessages();
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
					contactNo, userName)) {

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
