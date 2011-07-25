package controllers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;
import utilities.Connector;
import utilities.Constants;
import views.AddBookDialog;
import views.BookInfoPanel;
import views.BookSearchPanel;
import models.Book;
import models.BookDAO;
import models.TransactionDAO;
import models.User;

public class BookController {

	private static BookSearchPanel bookSearch;
	private static BookInfoPanel bookInfo;
	private int currTableRowSelection;
	private String currSearchString;
	private JPanel bookLayoutPanel;
	private User currentUser;
	private ArrayList<Book> bookList;

	public static void main(String args[]){
		new Connector(Constants.TEST_CONFIG);
		User user = new User(19, "niel", "121111", "Reiniel Adam", "Lozada", "reiniel_lozada@yahoo.com", "secret", "8194000", 1, "User");
		BookController bookController = new BookController(user);
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.setVisible(true);
		testFrame.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		testFrame.setBounds(0,0,screenSize.width, screenSize.height);
		testFrame.setContentPane(bookController.getBookLayoutPanel());
	}
	
	public BookController(User user){
		currentUser = user;
		try {
			generateBookLayoutPanel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void generateBookLayoutPanel() throws Exception{
		bookList = BookDAO.searchBook("*");
		currSearchString = "*";
		bookLayoutPanel = new JPanel(new MigLayout("wrap 2", "[grow][grow]"));
		bookSearch = new BookSearchPanel(currentUser);
		if(bookList.size() == 0){
			bookInfo = new BookInfoPanel(new Book(), currentUser);
		}else{
			bookInfo = new BookInfoPanel(bookList.get(0), currentUser);
		}
		currTableRowSelection = 0;
		bookLayoutPanel.add(bookSearch, "grow");
		bookLayoutPanel.add(bookInfo, "grow");
		
		bookSearch.setTextFieldListener(new TextFieldListener());
		bookSearch.setClearButtonListener(new ClearButtonListener());
		bookSearch.setSearchButtonListener(new SearchButtonListener());
		bookSearch.setAddBookButtonListener(new AddBookButtonListener());
		bookSearch.setTableListModel(new BookListModel(BookDAO.searchBook("*")));
		bookSearch.setMouseListener(new BookListMouseListener());
		bookInfo.addSaveListener(new SaveButtonListener());
		bookInfo.addDeleteListener(new DeleteButtonListener());
		bookInfo.addBorrowListener(new BorrowButtonListener());
		bookInfo.addReserveListener(new ReserveButtonListener());
	}
	
	public JPanel getBookLayoutPanel() {
		return bookLayoutPanel;
	}
	
	class AddBookButtonListener implements ActionListener{
		private AddBookDialog addBook;
		public void actionPerformed(ActionEvent arg0) {
			addBook = new AddBookDialog();
			addBook.setVisible(true);
			addBook.addBookActionListener(new AddBookListener());
			addBook.addCancelActionListener(new CancelListener());
		}
		
		class AddBookListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean validate = true;
				Border redBorder = BorderFactory.createLineBorder(Color.red);
				Border defaultBorder = BorderFactory.createLineBorder(Color.gray);
				
				if(addBook.getTxtFldTitle().getText().trim().isEmpty() || addBook.getTxtFldTitle().getText().length() > 100){
					addBook.getTxtFldTitle().setBorder(redBorder);
					validate = false;
				}else addBook.getTxtFldTitle().setBorder(defaultBorder);
				
				if(addBook.getTxtFldAuthor().getText().trim().isEmpty() || addBook.getTxtFldAuthor().getText().length() > 100){
					addBook.getTxtFldAuthor().setBorder(redBorder);
					validate = false;
				}else addBook.getTxtFldAuthor().setBorder(defaultBorder);
				
				if(!addBook.getTxtFldYearPublish().getText().trim().isEmpty()){
					if(!addBook.getTxtFldYearPublish().getText().matches(Constants.YEAR_PUBLISH_FORMAT)){
						addBook.getTxtFldYearPublish().setBorder(redBorder);
						validate = false;
					}else addBook.getTxtFldYearPublish().setBorder(defaultBorder);
				}else addBook.getTxtFldYearPublish().setBorder(defaultBorder);
				
				if(!addBook.getTxtFldIsbn().getText().matches(Constants.ISBN_FORMAT)){
					addBook.getTxtFldIsbn().setBorder(redBorder);
					validate = false;
				}else addBook.getTxtFldIsbn().setBorder(defaultBorder);
				
				if(addBook.getTxtFldPublisher().getText().length() > 100){
					addBook.getTxtFldPublisher().setBorder(redBorder);
					validate = false;
				}else addBook.getTxtFldPublisher().setBorder(defaultBorder);
				
				if(addBook.getTxtAreaDescription().getText().length() > 300){
					addBook.getTxtAreaDescription().setBorder(redBorder);
					validate = false;
				}else addBook.getTxtAreaDescription().setBorder(defaultBorder);
				
				if(validate){
					try {
						if(!BookDAO.isIsbnExisting(addBook.getBookInfo().getIsbn())){
							BookDAO.addBook(addBook.getBookInfo());
							addBook.getLblErrorMsg().setText("Book Added");
						}else{
							addBook.getLblErrorMsg().setText("ISBN Already Exist");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					addBook.getLblErrorMsg().setText("Invalid Input");
				}
			}
		}

		class CancelListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				addBook.dispose();
			}
			
		}
	}
	
	class SaveButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				BookDAO.editBook(bookInfo.getCurrBook());
				bookList = BookDAO.searchBook(currSearchString);
				bookSearch.setTableListModel(new BookListModel(bookList));
				bookInfo.setBookInfoData(bookList.get(currTableRowSelection));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	
	class DeleteButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				BookDAO.deleteBook(bookInfo.getCurrBook());
				bookList = BookDAO.searchBook(currSearchString);
				bookSearch.setTableListModel(new BookListModel(bookList));
				bookInfo.setBookInfoData(bookList.get(currTableRowSelection));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	class BorrowButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				TransactionDAO.borrowBook(bookList.get(currTableRowSelection), currentUser);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	
	class ReserveButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				TransactionDAO.reserveBook(bookList.get(currTableRowSelection), currentUser);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	
	class BookListMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			resetButton();
			JTable table = (JTable) e.getSource();
			int tableRow = table.getSelectedRow();
			currTableRowSelection = tableRow;
			bookInfo.setBookInfoData(bookList.get(tableRow));
			try {
				if (!TransactionDAO.isBorrowedByUser(bookList.get(currTableRowSelection), currentUser) && 
						!TransactionDAO.isReservedByUser(bookList.get(currTableRowSelection), currentUser)){
					if (TransactionDAO.getAvailableCopies(bookList.get(currTableRowSelection)) > 0){
						bookInfo.getBtnBorrow().setEnabled(true);
					} else {
						bookInfo.getBtnReserve().setEnabled(true);
						bookInfo.getLblErrorMsg().setText("No available copies");
					}
				} else {
					bookInfo.getLblErrorMsg().setText("You already have a pending transaction for the following book: ");
				}
				
			} catch (Exception ex){
				
			}
		
		}

		private void resetButton() {
			bookInfo.getBtnSave().setEnabled(false);
			bookInfo.getBtnDelete().setEnabled(false);
			bookInfo.getBtnBorrow().setEnabled(false);
			bookInfo.getBtnReserve().setEnabled(false);
			bookInfo.getLblErrorMsg().setText("");
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {			
		}

		@Override
		public void mouseReleased(MouseEvent e) {	
		}
	}

	class BookListModel extends AbstractTableModel{		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ArrayList<String> columnName = new ArrayList<String>();
		ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();
		
		public BookListModel(ArrayList<Book> bookList) {
			ArrayList<String> rowData = new ArrayList<String>();
			columnName.add("ISBN No.");
			columnName.add("Book");
			columnName.add("Status");
			
			for(int i = 0; i < bookList.size(); i++){
				rowData = new ArrayList<String>();
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
					currSearchString = strSearch;
					bookList = BookDAO.searchBook(strSearch);
					bookSearch.setTableListModel(new BookListModel(bookList));
					bookInfo.setBookInfoData(bookList.get(0));
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
						currSearchString = strSearch;
						bookList = BookDAO.searchBook(strSearch);
						bookSearch.setTableListModel(new BookListModel(bookList));
						if(bookList.size() == 0){
							bookInfo.setBookInfoData(new Book());
						}else{
							bookInfo.setBookInfoData(bookList.get(0));
						}
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
