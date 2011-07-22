package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;
import utilities.Connector;
import utilities.Constants;
import views.BookInfoPanel;
import views.BookSearchPanel;
import models.Book;
import models.BookDAO;

public class BookController {

	private static BookSearchPanel bookSearch;
	private static BookInfoPanel bookInfo;

	public static void main(String args[]){
		new Connector(Constants.APP_CONFIG);
		try{
			new BookController();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public BookController() throws Exception {
		bookSearch = new BookSearchPanel();
		bookSearch.setTextFieldListener(new TextFieldListener());
		bookSearch.setClearButtonListener(new ClearButtonListener());
		bookSearch.setSearchButtonListener(new SearchButtonListener());
		bookSearch.setTableListModel(new BookListModel(BookDAO.searchBook("*")));
		
		//tochange
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.add(bookSearch);
		testFrame.setVisible(true);
	}
	
	class BookListModel extends AbstractTableModel{		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ArrayList<String> columnName = new ArrayList<String>();
		ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();
		
		public BookListModel(ArrayList<Book> bookList) {
			columnName.add("ISBN No.");
			columnName.add("Book");
			columnName.add("Status");
			
			for(int i = 0; i < bookList.size(); i++){
				ArrayList<String> rowData = new ArrayList<String>();
				rowData.add(bookList.get(i).getIsbn());
				rowData.add(bookList.get(i).getTitle()+", "+bookList.get(i).getAuthor());
				
				if(bookList.get(i).getCopies() == 0){
					rowData.add("unavailable");
				}else if(bookList.get(i).getCopies() == 1){
					rowData.add("1 copy available");
				}else{
					rowData.add(bookList.get(i).getCopies()+" copies available");
				}
				
				tableData.add(rowData);
			}
		}

		public String getColumnName(int col) {
	         return columnName.get(col);
	     }
		
		@Override
		public int getColumnCount() {
			return columnName.size();
		}

		@Override
		public int getRowCount() {
			return tableData.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return tableData.get(rowIndex).get(columnIndex);
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex){
			return false;
		}
		
	}
	
	class SearchButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String strSearch = bookSearch.getTextFieldSearch();
				if(!strSearch.trim().isEmpty()){
					bookSearch.setTableListModel(new BookListModel(BookDAO.searchBook(strSearch)));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	class ClearButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			bookSearch.setTextFieldSearch("");
		}
		
	}
	
	class TextFieldListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent key) {
		}

		@Override
		public void keyReleased(KeyEvent key) {
			if(key.getKeyCode() == KeyEvent.VK_ENTER){
				try {
					String strSearch = bookSearch.getTextFieldSearch();
					if(!strSearch.trim().isEmpty()){
						bookSearch.setTableListModel(new BookListModel(BookDAO.searchBook(strSearch)));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}

		@Override
		public void keyTyped(KeyEvent key) {			
		}
		
	}
	
}
