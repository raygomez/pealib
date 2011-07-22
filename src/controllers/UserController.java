package controllers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import models.UserDAO;
import models.User;
import net.miginfocom.swing.MigLayout;
import utilities.Constants;
import views.ChangePasswordDialog;
import views.UserInfoPanel;
import views.UserSearch;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class UserController {

	private UserSearch userSearch;
	private UserInfoPanel userInfoPanel;
	private ChangePasswordDialog changePasswordDialog;
	private UserDAO userDao;
	private User currentUser;
	
	private JPanel layoutPanel;
	
	/*
	 * For visual testing purposes only
	 */
	public static void main(String[] args) {
		
		User user = new User(1011, "jjlim", "1234567", "Janine June", 
				"Lim", "jaja.lim@yahoo.com", "UP Ayala Technohub", "09171234567", 1, "Librarian");
		
		UserController userController = new UserController(user);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setResizable(false);
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	      
	    frame.setBounds(0,0,screenSize.width, screenSize.height);
	      
	    frame.setContentPane(userController.getUserPanel());
		
	}
	
	public UserController(User user){
		try {
			userDao = new UserDAO();
		} catch (Exception e) {
			System.out.println("UserDAO " + e);
		}
		
		this.currentUser = user;
		
		layoutPanel = new JPanel(new MigLayout("wrap 2", "[grow][grow]"));		
		
		userSearch = new UserSearch(new PendingApplicationModel());
		userInfoPanel = new UserInfoPanel();
		
		generateLayoutPanel();
		
		//userSearch.setTableModel(new PendingApplicationModel());
		//test
		//testingPurposes();
	}
	
	private void generateLayoutPanel() {
		
		layoutPanel.add(userSearch, "grow");
		layoutPanel.add(userInfoPanel, "grow");
		
		userInfoPanel.addSaveListener(save);
		userInfoPanel.addChangePasswordListner(showChangePassword);
		
	}

	//UserTableModel
	class PendingApplicationModel extends AbstractTableModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();
		
		public PendingApplicationModel(){
			init();
		}
		
		private void init(){
			//Adding Columns
			
			columns.add("Username");
			columns.add("Name");
			
			//Adding Data:
			ArrayList<String> rowData = new ArrayList<String>();
			rowData.add("wertyu123");
			rowData.add("Miku"+" "+"Hatsune");
			
			tableData.add(rowData);
			
			rowData = new ArrayList<String>();
			rowData.add("luka123");
			rowData.add("Luka"+" "+"Megurine");
			tableData.add(rowData);
			
			rowData = new ArrayList<String>();
			rowData.add("kl8ss");
			rowData.add("Kaito"+" "+"Vocaloid");
			tableData.add(rowData);
		}
		
		 //for column names
		 public String getColumnName(int col) {
	         return columns.get(col);
	     }
		 
		 @Override
		public int getColumnCount() {
			return columns.size();
		}

		@Override
		public int getRowCount() {
			return tableData.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			//return tableData.elementAt(arg0).elementAt(arg1);
			return tableData.get(row).get(col);
		}
			
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex){
			return false;
		}		
	}
	
	public JPanel getUserPanel(){
		return layoutPanel;
	}
	
	private boolean validateUpdateProfile(String firstName,
			String lastName, String email, String address, String contactNo) {
			// TODO Auto-generated method stub
				
			boolean pass = true;
			ArrayList<Integer> tempErrors = new ArrayList<Integer>();
			
			if(!firstName.matches(Constants.NAME_FORMAT)){
				tempErrors.add(Constants.FIRST_NAME_FORMAT_ERROR);
				pass = false;
			}
			
			if(!lastName.matches(Constants.NAME_FORMAT)){
				tempErrors.add(Constants.LAST_NAME_FORMAT_ERROR);
				pass = false;
			}
			
			if(!email.matches(Constants.EMAIL_FORMAT)){
				tempErrors.add(Constants.EMAIL_FORMAT_ERROR);
				pass = false;
			}
			
			if(!contactNo.matches(Constants.CONTACT_NUMBER_FORMAT)){
				tempErrors.add(Constants.CONTACT_NUMBER_FORMAT_ERROR);
				pass = false;
			}
			
			int[] errors = new int[tempErrors.size()];
			for(int i = 0; i < tempErrors.size(); i++){
				errors[i] = tempErrors.get(i);
			}
			
			userInfoPanel.displayErrors(errors);
			
			return pass;
			
		};	
	
	
	private ActionListener save = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			int userId = Integer.parseInt(userInfoPanel.getIdNumber());
			String userName = userInfoPanel.getUsername();
			String firstName = userInfoPanel.getFirstName();
			String lastName = userInfoPanel.getLastName();
			String email = userInfoPanel.getEmail();
			String address = userInfoPanel.getAddress();
			String contactNo = userInfoPanel.getContactNumber();
			// TODO Auto-generated method stub
			
			if(validateUpdateProfile(firstName,lastName, email, address, contactNo)){
				User user = new User(userId, userName, firstName, 
					lastName, email, address, contactNo);
				
				try {
					userDao.updateUser(user);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
		
	private ActionListener showChangePassword = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			changePasswordDialog = new ChangePasswordDialog((JFrame)userInfoPanel.getParent());
			
			if(currentUser.getType().equals("Librarian") && !userInfoPanel.getIdNumber().isEmpty() &&
					currentUser.getUserId() != Integer.parseInt(userInfoPanel.getIdNumber())){
				changePasswordDialog.removeOldPassword();
			}
		}
	};
}
