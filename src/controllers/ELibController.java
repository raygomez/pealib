package controllers;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import controllers.UserController.UserSearchTableModel;

import utilities.Connector;
import views.ELibTabbedPanel;
import models.Book;
import models.BorrowTransaction;
import models.ReserveTransaction;
import models.TransactionDAO;
import models.User;
import models.UserDAO;

import java.awt.event.*;
import java.util.ArrayList;

public class ELibController {
	
	//TODO temporary User
	private User user = new User();
	
	private ELibTabbedPanel tabpane;
	private int tab;
	
	private static JFrame frame;
	/* ..TODO
	 * For visual testing purposes only
	 */
	public static void main(String[] args) {
		
		new ELibController();					
	   // frame.setContentPane(tabpane);		
	}
	
	ELibController(){
		tabpane = new ELibTabbedPanel();
		tabpane.addListener(new TabListener());				
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setResizable(false);
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	      
	    frame.setBounds(0,0,screenSize.width, screenSize.height);	      
		frame.setContentPane(tabpane);	
		
		user.setUserId(6);
		user.setUserName("sample");
		user.setFirstName("SAMMPLE");
		user.setLastName("Test");
		user.setType("User");
	}

	class TabListener extends MouseAdapter{
		@Override
		public void mouseReleased(MouseEvent e) {
			JTabbedPane tp = (JTabbedPane) e.getSource();
			int tab = tp.indexAtLocation( e.getX(), e.getY() );
			
			System.out.println("SELECTED tab: "+ tab);
			ELibTableModel model = new ELibTableModel(tab);
			tabpane.setTableModel(tab, model);
		}			
	}

	class ELibTableModel extends AbstractTableModel{
		/**
		 *  TableModel for ELib Tabs 
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<String> columns;
		private ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();
		private ArrayList<BorrowTransaction> bookData = new ArrayList<BorrowTransaction>();
		private ArrayList<ReserveTransaction> bookDataReserve = new ArrayList<ReserveTransaction>();
		private int tab=0;
		
		public ELibTableModel(int tab){
			//TODO change if using another db
			new Connector("test.config"); 
			this.tab = tab;
			switch (tab){
			case 0:
				requestData();
				break;
				
			case 1:
				reserveData();
				break;
			
			case 2:
				onloanData();
				break;
			
			case 3:
				historyData();
				break;			
			}
						
			//TODO change if going to use another DB
			new Connector("test.config"); 
		}
		
		private void requestData(){
			columns = new ArrayList<String>();
			columns.add("ISBN");
			columns.add("Title");
			columns.add("Author");
			columns.add("Date Requested");
			
			try{
				//TODO get bookData from transaction dao
				bookData = TransactionDAO.getRequestedBooks(user);
			} catch(Exception e){ System.out.println("ELibController: requestData: "+e);}
			
			if(bookData != null){				
				for(BorrowTransaction i : bookData){
					ArrayList<String> rowData = new ArrayList<String>();								
					rowData.add(i.getBook().getIsbn());
					rowData.add(i.getBook().getTitle());
					rowData.add(i.getBook().getAuthor());
					String temp = ""+ i.getDateRequested();
					rowData.add(temp);
					tableData.add(rowData);
				}			
			}			
		}
		
		private void reserveData(){
			columns = new ArrayList<String>();
			columns.add("ISBN");
			columns.add("Title");
			columns.add("Author");
			columns.add("Date Reserved");
			columns.add("Queue Number");
			
			try{
				//TODO get bookData from transaction dao
				bookDataReserve = TransactionDAO.getReservedBooks(user);
			} catch(Exception e){ System.out.println("ELibController: reserveData: "+e);}
			
			if(bookDataReserve != null){
				for(ReserveTransaction i : bookDataReserve){
					ArrayList<String> rowData = new ArrayList<String>();								
					//rowData.add(i.getUserName());
					//rowData.add(i.getFirstName()+" "+i.getLastName());
					tableData.add(rowData);
				}
			}
		}
		
		private void onloanData(){
			columns = new ArrayList<String>();
			columns.add("ISBN");
			columns.add("Title");
			columns.add("Author");
			columns.add("Date Borrowed");
			columns.add("Date Due");
			
			try{
				//TODO get bookData from transaction dao
				bookData = TransactionDAO.getOnLoanBooks(user);
			} catch(Exception e){ System.out.println("ELibController: onloanData: "+e);}
			
			if(bookData!=null){
				for(BorrowTransaction i : bookData){
					ArrayList<String> rowData = new ArrayList<String>();								
					//rowData.add(i.getUserName());
					//rowData.add(i.getFirstName()+" "+i.getLastName());
					tableData.add(rowData);
				}
			}
		}
		
		private void historyData(){
			columns = new ArrayList<String>();
			columns.add("ISBN");
			columns.add("Title");
			columns.add("Author");
			columns.add("Date Borrowed");
			columns.add("Date Returned");
			
			try{
				//TODO get bookData from transaction dao
				bookData = TransactionDAO.getHistory(user);
			} catch(Exception e){ System.out.println("ELibController: historyData: "+e);}
			
			if(bookData!=null){
				for(BorrowTransaction i : bookData){
					ArrayList<String> rowData = new ArrayList<String>();								
					//rowData.add(i.getUserName());
					//rowData.add(i.getFirstName()+" "+i.getLastName());
					tableData.add(rowData);
				}
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
}
