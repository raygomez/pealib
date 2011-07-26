package controllers;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import models.UserDAO;
import models.User;
import net.miginfocom.swing.MigLayout;
import utilities.Connector;
import utilities.Constants;
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
	private User selectedUser;

	private JPanel layoutPanel;

	private String searchText;
	private ArrayList<User> searchedUsers;
	private ArrayList<User> searchedPending;
	private ArrayList<Integer> checkList;

	/*
	 * ..TODO For visual testing purposes only
	 */
	public static void main(String[] args) {
		
		new Connector(Constants.TEST_CONFIG);
//		new Connector();

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
	 *  Constructor
	 */
	public UserController(User user) {
		this.setCurrentUser(user);
		setLayoutPanel(new JPanel(new MigLayout("wrap 2", "[grow][grow]")));

		setUserSearch(new UserSearchPanel(new UserSearchTableModel(USER, ""),
				new UserSearchTableModel(PENDING, "")));
		getUserSearch().addListeners(new SearchListener(),
				new SearchKeyListener(), new TabChangeListener(),
				new UserSelectionListener(), new CheckBoxListener(), new AcceptListener(), new DenyListener());
		setUserInfoPanel(new UserInfoPanel());
		
		generateLayoutPanel();
	}

	/*
	 *  Getters - Setters
	 */

	public User getSelectedUser() { return selectedUser; }

	public void setSelectedUser(User selectedUser) { this.selectedUser = selectedUser; }

	public User getCurrentUser() { return currentUser; }

	public void setCurrentUser(User currentUser) { this.currentUser = currentUser; }

	public String getSearchText() { return searchText; }

	public void setSearchText(String searchText) { this.searchText = searchText; }

	public JPanel getLayoutPanel() { return layoutPanel; }

	public void setLayoutPanel(JPanel layoutPanel) { this.layoutPanel = layoutPanel; }

	public UserInfoPanel getUserInfoPanel() { return userInfoPanel; }

	public void setUserInfoPanel(UserInfoPanel userInfoPanel) { this.userInfoPanel = userInfoPanel; }

	public UserSearchPanel getUserSearch() { return userSearch; }

	public void setUserSearch(UserSearchPanel userSearch) { this.userSearch = userSearch; }
	
	public ArrayList<User> getSearchedPending() { return searchedPending; }

	public void setSearchedPending(ArrayList<User> searchedPending) {
		//this.searchedPending = new ArrayList<User>();
		if(searchedPending.size()>0){
			this.searchedPending = searchedPending;
			getUserSearch().togglePendingButtons(true);
		}
		else{
			this.searchedPending = null;
			getUserSearch().togglePending(false);
		}
	}

	public ArrayList<User> getSearchedUsers() { return searchedUsers; }

	public void setSearchedUsers(ArrayList<User> searchedUsers) { this.searchedUsers = searchedUsers; }
	
	public ArrayList<Integer> getCheckList() { return checkList; }
	
	public void setCheckList(ArrayList<Integer> checkList) { this.checkList = checkList; }

	/*
	 *  Listeners : Search
	 */
	class CheckBoxListener extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent arg0) { 
			UserSearchTableModel model = new UserSearchTableModel(1, getSearchText());
			
			if(getUserSearch().getCbAll().isSelected())   
				model.toggleAllCheckBox(true);		
			else   
				model.toggleAllCheckBox(false);	
			
			getUserSearch().setTableModel(1, model);	
		}		
	}
		
	class AcceptListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(getSearchedPending().size() == getCheckList().size()){
				getUserInfoPanel().toggleButton(false);
				getUserInfoPanel().clearFields();
			}
			
			for(int i = 0; i < getSearchedPending().size(); i++){
				if(getCheckList().contains(i)) {
					User temp = getSearchedPending().get(i);
					temp.setType("User");
					
					try { UserDAO.updateUser(temp);} 
					catch (Exception e1) { e1.printStackTrace(); }
					
					getUserSearch().getCbAll().setSelected(false);															
				}
			}
			JOptionPane.showMessageDialog(getUserSearch(), "Successfully accepted ("+getCheckList().size()+") application/s.");
			
			getCheckList().clear();			
			searchUsers();
			getUserSearch().resetTable();
			getUserSearch().togglePendingButtons(false);
			
		}
	}
	
	class DenyListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(getSearchedPending().size() == getCheckList().size()){
				getUserInfoPanel().toggleButton(false);
				getUserInfoPanel().clearFields();
			}
			
			for(int i = 0; i<getSearchedPending().size(); i++){
				if(getCheckList().contains(i)) {
					User temp = getSearchedPending().get(i);					
					
					try { UserDAO.denyPendingUser(temp);} 
					catch (Exception e1) { e1.printStackTrace(); }
					
					getUserSearch().getCbAll().setSelected(false);															
				}
			}
			JOptionPane.showMessageDialog(getUserSearch(), "Successfully denied ("+getCheckList().size()+") application/s.");
			
			getCheckList().clear();			
			searchUsers();
			getUserSearch().resetTable();
			getUserSearch().togglePendingButtons(false);
		}
	}
	
	private void searchUsers() {
		int tab = getUserSearch().getSelectedTab();
		setSearchText(getUserSearch().getFieldSearch().getText());
		
		UserSearchTableModel model = new UserSearchTableModel(tab,getSearchText());
		getUserSearch().setTableModel(tab, model);
	}
	
	class TabChangeListener implements ChangeListener {
		//TODO 
		@Override
		public void stateChanged(ChangeEvent e) {
			JTabbedPane temp = (JTabbedPane)e.getSource();
			int index = temp.getSelectedIndex();
			System.out.println(index);
			searchUsers(); 
			
			if(index == 0){
				getUserSearch().getUsersTable().getSelectionModel().setSelectionInterval(0, 0);
				getUserSearch().getUsersTable().addRowSelectionInterval(0, 0);
			}
			else if(index == 1){
				if(getSearchedPending()!=null){								
					getUserSearch().getPendingTable().getSelectionModel().setSelectionInterval(0, 0);						
					getUserSearch().getPendingTable().addRowSelectionInterval(0, 0);
					
					if(getCheckList().size()>0) getUserSearch().togglePendingButtons(true);
					else getUserSearch().togglePendingButtons(false);
				}
				else{
					getUserSearch().togglePending(false);			
					getUserInfoPanel().toggleButton(false);
					getUserInfoPanel().clearFields();
				}
			}					
		}
	}

	class SearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) { searchUsers(); }
	}

	class SearchKeyListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				searchUsers();
				
			} else {
				setSearchText(getUserSearch().getFieldSearch().getText());
				if (getSearchText().length() > 2 || getSearchText().length() == 0)
					searchUsers();
			}
		}
	}

	/**
	 *  Table Model for User Search (Pending & Active User Accounts)
	 */
	class UserSearchTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private ArrayList<String> columns;	
		private ArrayList<ArrayList<Object>> tableData;		
		private ArrayList<Integer> selectList;
		private int mode;
		private String searchStr = "";

		public UserSearchTableModel(int tab, String str) {
			this.mode = tab;
			this.searchStr = str;
			
			selectList = new ArrayList<Integer>();
			setCheckList(selectList);
			tableData = new ArrayList<ArrayList<Object>>();

			if (mode == USER){
				userAcct();
			}
			else{
				pending();
			}
		}

		private void pending() {
			columns = new ArrayList<String>();				
			columns.add("Username");
			columns.add("Name");
			columns.add("Accept");

			try {
				setSearchedPending(UserDAO.searchAllPending(searchStr));
				
				if(getSearchedPending() != null || !getSearchedPending().isEmpty()){
					for (User i : getSearchedPending()) {
						ArrayList<Object> rowData = new ArrayList<Object>();
						rowData.add(i.getUserName());
						rowData.add(i.getFirstName() + " " + i.getLastName());				
						rowData.add(new Boolean(false));
						tableData.add(rowData);
					}
				}
			} catch (Exception e) {
				System.out.println("UserController: pending: " + e);
			}			
		}
				
		public void toggleAllCheckBox(boolean value){			
			for(int i = 0; i<getRowCount(); i++){
				setValueAt(new Boolean(value), i, 2);
			}			
		}

		private void userAcct() {
			columns = new ArrayList<String>();				
			columns.add("Username");
			columns.add("Name");
	
			try {
				setSearchedUsers(UserDAO.searchActiveUsers(searchStr));
				
				for (User i : getSearchedUsers()) {

					ArrayList<Object> rowData = new ArrayList<Object>();
					rowData.add(i.getUserName());
					rowData.add(i.getFirstName() + " " + i.getLastName());				
					tableData.add(rowData);
				}
			} catch (Exception e) {
				System.out.println("UserController: userAcct: " + e);
			}			
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Class getColumnClass(int c) { return getValueAt(0, c).getClass();}
		
		 public void setValueAt(Object value, int row, int col) {
			 
			 tableData.get(row).set(col, value);
			 
			 if(!(Boolean)value){
				 getUserSearch().getCbAll().setSelected(false);
				 
				 if(selectList.contains(row)){ selectList.remove((Object)row); }				 
			 }
			 else{
				 if(!selectList.contains(row)){ selectList.add(row); }
			 }		
			 			 			 
			 setCheckList(selectList);
			 
			 if(getCheckList().size() > 0)  getUserSearch().togglePendingButtons(true);
			 else  getUserSearch().togglePendingButtons(false);
			 fireTableCellUpdated(row, col);			 			 
	     }
		
		@Override
		public String getColumnName(int col) { return columns.get(col);}

		@Override
		public int getColumnCount() { return columns.size();}

		@Override
		public int getRowCount() { return tableData.size();}

		@Override
		public Object getValueAt(int row, int col) { return tableData.get(row).get(col);}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if(columnIndex<2) return false;
			else return true;
		}
	} // end of table model
	
	//TODO
	private void generateLayoutPanel() {
		getLayoutPanel().add(getUserSearch(), "grow");
		getLayoutPanel().add(getUserInfoPanel(), "grow");
		getUserInfoPanel().addSaveListener(save);
		getUserInfoPanel().addChangePasswordListener(showChangePassword);
		//getUserInfoPanel().toggleButton(false);
		getUserSearch().getUsersTable().getSelectionModel().setSelectionInterval(0, 0);
	}

	public JPanel getUserPanel() { return getLayoutPanel();}

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

			if (tab == USER) {				
				user = getSearchedUsers().get(row);
			} else {				
				if(getSearchedPending() != null)
					user = getSearchedPending().get(row);
			}

			if(user!=null){
				setSelectedUser(user);
				getUserInfoPanel().toggleButton(true);
				getUserInfoPanel().setFields(user.getType(), "" + user.getUserId(),
					user.getUserName(), user.getFirstName(),
					user.getLastName(), user.getAddress(), user.getContactNo(),
					user.getEmail());
			}		
		}
	}

	//TODO refresh after save : table and userinfo
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
			String type = getUserInfoPanel().getAccountType();

			if (validateUpdateProfile(firstName, lastName, email, address,
					contactNo)) {
				
				User user = new User(userId, userName, firstName, lastName, email, address, contactNo, type);

				try {
					UserDAO.updateUser(user);
					JOptionPane.showMessageDialog(layoutPanel, "Record successfully updated!");
					searchUsers();
					
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
                
                if(currentUser.getType().equals("Librarian") && !userInfoPanel.getIdNumber().isEmpty() &&
                                currentUser.getUserId() != Integer.parseInt(userInfoPanel.getIdNumber())){
                        changePasswordDialog.removeOldPassword();
                }                
                changePasswordDialog.addChangePasswordListener(changePassword);
                
               // if(getSelectedUser() == null)
                changePasswordDialog.setVisible(true);
        }
    };

	private ActionListener changePassword = new ActionListener() {        
        boolean correctPassword;
        boolean isMatchingPassword;
        
        @Override
        public void actionPerformed(ActionEvent arg0) {
               
                int userID;
                if(changePasswordDialog.getOldPasswordField().isEnabled()){
                        String oldPassword = new String(changePasswordDialog.getOldPasswordField().getPassword());
                        userID = currentUser.getUserId();
                        
                        try {
                                correctPassword = UserDAO.checkPassword(userID, oldPassword);
                        } catch (Exception e) {
                                
                                e.printStackTrace();
                                correctPassword = false;
                        }
                }
                else{
                        userID = Integer.parseInt(userInfoPanel.getIdNumber());
                }
                String newPassword1 = new String(changePasswordDialog.getNewPasswordField().getPassword());
                String newPassword2 = new String(changePasswordDialog.getRepeatPasswordField().getPassword());
                isMatchingPassword = newPassword1.equals(newPassword2);
                
                if(!correctPassword) changePasswordDialog.displayError(Constants.INCORRECT_PASSWORD_ERROR);
                else if (!isMatchingPassword) changePasswordDialog.displayError(Constants.PASSWORD_NOT_MATCH_ERROR);
                else{
                        try {
                                UserDAO.changePassword(userID, newPassword1);
                                changePasswordDialog.dispose();
                                JOptionPane.showMessageDialog(layoutPanel, "Password successfully changed!");
                        } catch (Exception e) {
                               
                                e.printStackTrace();
                                changePasswordDialog.displayError(Constants.DEFAULT_ERROR);
                        }
                        
                }
        	}
		};
	}
/*
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
*/
