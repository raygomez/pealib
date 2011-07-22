package controllers;

import java.awt.*;
import java.util.ArrayList;

import models.UserDAO;
import views.UserSearch;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;


public class UserController {
	private UserSearch userSearch;
	private UserDAO userDao;
	
	private JFrame frame;
	
	public static void main(String[] args) {
		
		
		try {
			new UserController();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void testingPurposes(){
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setResizable(false);
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	      
	    frame.setBounds(0,0,screenSize.width, screenSize.height);
	      
	    frame.setContentPane(userSearch);
	}
	
	public UserController(){
		try {
			userDao = new UserDAO();
		} catch (Exception e) {
			System.out.println("UserDAO " + e);
		}
		
		userSearch = new UserSearch(new UserSearchTableModel(0));
		//userSearch.setTableModel(new PendingApplicationModel());
		//test
		testingPurposes();
	}
	
	//UserTableModel
	class UserSearchTableModel extends AbstractTableModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<String> columns = new ArrayList<String>();
		private ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();
		
		public UserSearchTableModel(int tab){
			if(tab==0) userAcct();
			else pending(); 
		}
		
		private void pending(){}
		private void userAcct(){
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
		 //for table to make how many columns
		 @Override
		public int getColumnCount() {
			return columns.size();
		}
		 //for table to make how many rows
		@Override
		public int getRowCount() {
			return tableData.size();
		}
		//for table to add data to each cell
		@Override
		public Object getValueAt(int row, int col) {
			return tableData.get(row).get(col);
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex){
			return false;
		}		
	}
}
