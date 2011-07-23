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
import views.UserSearch;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class UserController {

	private UserSearch userSearch;
	private UserInfoPanel userInfoPanel;
	private ChangePasswordDialog changePasswordDialog;
	private User currentUser;
	
	private JPanel layoutPanel;
	
	private String searchText;
	
	/* ..TODO
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
	
	//CONSTRUCTOR
	public UserController(User user){	
		
		this.currentUser = user;
		
		layoutPanel = new JPanel(new MigLayout("wrap 2", "[grow][grow]"));		
		
		userSearch = new UserSearch(new UserSearchTableModel(0,""), new UserSearchTableModel(1,""));
		userSearch.addListeners(new SearchListener(), new SearchKeyListener(), new TabListener());
		userInfoPanel = new UserInfoPanel();
		
		generateLayoutPanel();
	}
	
	private void generateLayoutPanel() {
		
		layoutPanel.add(userSearch, "grow");
		layoutPanel.add(userInfoPanel, "grow");
		
		userInfoPanel.addSaveListener(save);
		userInfoPanel.addChangePasswordListner(showChangePassword);		
	}
	
	class TabListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {}

		@Override
		public void mouseEntered(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent e) {
			//JTabbedPane tp = (JTabbedPane) e.getSource();
			//int tab = tp.indexAtLocation( e.getX(), e.getY() );
			searchUsers();
		}		
	}

	
	private void searchUsers(){
		int tab = userSearch.getSelectedTab();
		
		UserSearchTableModel model = new UserSearchTableModel(tab, searchText);
		userSearch.setTableModel(tab, model);
	}
	
	class SearchListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			searchUsers();
		}
	}

	class SearchKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();

			if (keyCode == 10) {
				searchUsers();
			} 
			else {				
				searchText = userSearch.getFieldSearch().getText();
	
				if(searchText.length()>0)   searchUsers();
			}
		}
		@Override
		public void keyTyped(KeyEvent e) {}
	}


	class UserSearchTableModel extends AbstractTableModel{
		/**
		 *  TableModel for User Search Panel/Tabs 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<String> columns = new ArrayList<String>();
		private ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();
		private ArrayList<User> searchUsers = new ArrayList<User>();
		private int mode;
		private String searchStr="";
		
		public UserSearchTableModel(int tab, String str){
			this.mode = tab;
			this.searchStr = str;
					
			columns.add("Username");
			columns.add("Name");
			
			//TODO change if going to use another DB
			new Connector("test.config"); 
			
			if(mode==0) userAcct();
			else pending(); 
		}
		
		private void pending(){
			try{
				searchUsers = UserDAO.searchAllPending(searchStr);
			} catch(Exception e){ System.out.println("UserController: userAcct: "+e);}
			
			for(User i : searchUsers){
				ArrayList<String> rowData = new ArrayList<String>();
				rowData.add(i.getUserName());
				rowData.add(i.getFirstName()+" "+i.getLastName());
				tableData.add(rowData);
			}		
		}
		
		private void userAcct(){
			try{
				searchUsers = UserDAO.searchUsers(searchStr);
			} catch(Exception e){ System.out.println("UserController: userAcct: "+e);}
			
			for(User i : searchUsers){
				if(i.getType().equals("Pending")) continue;
				ArrayList<String> rowData = new ArrayList<String>();				
				rowData.add(i.getUserName());
				rowData.add(i.getFirstName()+" "+i.getLastName());
				tableData.add(rowData);
			}							
		}		

		 public String getColumnName(int col) { return columns.get(col);}

		 @Override
		public int getColumnCount() { return columns.size();}
		
		@Override
		public int getRowCount() { return tableData.size();}

		@Override
		public Object getValueAt(int row, int col) { return tableData.get(row).get(col);}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex){ return false;}		
	}
	
	public JPanel getUserPanel(){
		return layoutPanel;
	}
	
	private boolean validateUpdateProfile(String firstName,
			String lastName, String email, String address, String contactNo) {
				
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
					UserDAO.updateUser(user);
				} catch (Exception e) {
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
