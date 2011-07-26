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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
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
		//User user = new User(2, "mutya", "mutya", "Anmuary", "Pantaleon", "anmuary.pantaleon@gmail.com", "USA", "09175839123", "User");
		User user = new User(3, "niel", "121111", "Reiniel Adam", "Lozada", "reiniel_lozada@yahoo.com", "secret", "8194000", "User");
		//User user = new User(4, "dota", "dota", "Domingo", "Tanael", "dota@gmail.com", "USA", "09187658790", 1, "Librarian");
		BookController bookController = null;
		try {
			bookController = new BookController(user);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.setVisible(true);
		testFrame.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		testFrame.setBounds(0,0,screenSize.width, screenSize.height);
		try {
			testFrame.setContentPane(bookController.getBookLayoutPanel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BookController(User user) throws Exception{
		currentUser = user;
		generateBookLayoutPanel();
	}
	
	private void generateBookLayoutPanel() throws Exception{
		bookList = BookDAO.searchBook("*");
		currSearchString = "*";
		bookLayoutPanel = new JPanel(new MigLayout("wrap 2", "[grow][grow]"));
		bookSearch = new BookSearchPanel(currentUser);
		if(bookList.size() == 0){
			bookInfo = new BookInfoPanel(new Book(), currentUser);
			bookInfo.getBtnDelete().setEnabled(false);
			bookInfo.getBtnSave().setEnabled(false);
		}else{
			bookInfo = new BookInfoPanel(bookList.get(0), currentUser);
			if(bookList.get(0).getCopies() == 0){
				bookInfo.getBtnDelete().setEnabled(false);
			}
		}
		currTableRowSelection = 0;
		bookLayoutPanel.add(bookSearch, "grow");
		bookLayoutPanel.add(bookInfo, "grow");
		
		bookSearch.setTextFieldListener(new TextFieldListener());
		bookSearch.setClearButtonListener(new ClearButtonListener());
		bookSearch.setSearchButtonListener(new SearchButtonListener());
		bookSearch.setAddBookButtonListener(new AddBookButtonListener());
		bookSearch.getTableBookList().setModel(new BookListModel(bookList));
		bookSearch.setMouseListener(new BookListMouseListener());
		bookInfo.addSaveListener(new SaveButtonListener());
		bookInfo.addDeleteListener(new DeleteButtonListener());
		bookInfo.addBorrowListener(new BorrowButtonListener());
		bookInfo.addReserveListener(new ReserveButtonListener());
	}
	
	public JPanel getBookLayoutPanel() throws Exception {
		generateBookLayoutPanel();
		return bookLayoutPanel;
	}
	
	class AddBookButtonListener implements ActionListener{
		private AddBookDialog addBook;
		public void actionPerformed(ActionEvent arg0) {
			addBook = new AddBookDialog();
			addBook.addBookActionListener(new AddBookListener());
			addBook.addCancelActionListener(new CancelListener());
			addBook.setVisible(true);
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
				
				if(!addBook.getTxtFldIsbn().getText().matches(Constants.ISBN_FORMAT_1) && 
						!addBook.getTxtFldIsbn().getText().matches(Constants.ISBN_FORMAT_2)){
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
				boolean validate = true;
				Border redBorder = BorderFactory.createLineBorder(Color.red);
				Border defaultBorder = BorderFactory.createLineBorder(Color.gray);
				
				if(bookInfo.getTxtFldTitle().getText().trim().isEmpty() || bookInfo.getTxtFldTitle().getText().length() > 100){
					bookInfo.getTxtFldTitle().setBorder(redBorder);
					validate = false;
				}else bookInfo.getTxtFldTitle().setBorder(defaultBorder);
				
				if(bookInfo.getTxtFldAuthor().getText().trim().isEmpty() || bookInfo.getTxtFldAuthor().getText().length() > 100){
					bookInfo.getTxtFldAuthor().setBorder(redBorder);
					validate = false;
				}else bookInfo.getTxtFldAuthor().setBorder(defaultBorder);
				
				if(!bookInfo.getTxtFldYrPublished().getText().trim().isEmpty()){
					if(!bookInfo.getTxtFldYrPublished().getText().matches(Constants.YEAR_PUBLISH_FORMAT)){
						validate = false;
						bookInfo.getTxtFldYrPublished().setBorder(redBorder);
					}else bookInfo.getTxtFldYrPublished().setBorder(defaultBorder);
				}else bookInfo.getTxtFldYrPublished().setBorder(defaultBorder);
				
				if(bookInfo.getTxtFldPublisher().getText().length() > 100){
					validate = false;
					bookInfo.getTxtFldPublisher().setBorder(redBorder);
				}else bookInfo.getTxtFldPublisher().setBorder(defaultBorder);
				
				if(!bookInfo.getTxtFldISBN().getText().matches(Constants.ISBN_FORMAT_1) && 
						!bookInfo.getTxtFldISBN().getText().matches(Constants.ISBN_FORMAT_2)){
					validate = false;
					bookInfo.getTxtFldISBN().setBorder(redBorder);
				}else bookInfo.getTxtFldISBN().setBorder(defaultBorder);

				if(bookInfo.getTxtFldDescription().getText().length() > 300){
					validate = false;
					bookInfo.getTxtFldDescription().setBorder(redBorder);
				}else bookInfo.getTxtFldDescription().setBorder(defaultBorder);
				
				int spinCopy = Integer.parseInt(bookInfo.getSpinCopyVal().getModel().getValue().toString());
				int availableCopy = TransactionDAO.getAvailableCopies(bookInfo.getCurrBook());
				int validCopy = bookInfo.getCurrBook().getCopies() - availableCopy;
				if(spinCopy < validCopy){
					validate = false;
					bookInfo.getSpinCopyVal().setBorder(redBorder);
				}else bookInfo.getSpinCopyVal().setBorder(defaultBorder);
				
				if(validate){
					BookDAO.editBook(bookInfo.getCurrBook());
					bookList = BookDAO.searchBook(currSearchString);
					bookSearch.getTableBookList().setModel(new BookListModel(bookList));
					bookSearch.getTableBookList().addRowSelectionInterval(currTableRowSelection, currTableRowSelection);
					bookInfo.setBookInfoData(bookList.get(currTableRowSelection));
					bookInfo.getLblErrorMsg().setText("Book Updated");
					if(bookList.get(currTableRowSelection).getCopies() > 0){
						bookInfo.getBtnDelete().setEnabled(true);
					}
				}else{
					bookInfo.getLblErrorMsg().setText("Invalid Input");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	
	class DeleteButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int availableCopy = TransactionDAO.getAvailableCopies(bookInfo.getCurrBook());
				if(availableCopy == bookList.get(currTableRowSelection).getCopies()){
					BookDAO.deleteBook(bookInfo.getCurrBook());
					bookList = BookDAO.searchBook(currSearchString);
					bookSearch.getTableBookList().setModel(new BookListModel(bookList));
					bookSearch.getTableBookList().addRowSelectionInterval(currTableRowSelection, currTableRowSelection);
					bookInfo.setBookInfoData(bookList.get(currTableRowSelection));
					bookInfo.getLblErrorMsg().setText("Book Deleted");
					bookInfo.getBtnDelete().setEnabled(false);
				}else{
					bookInfo.getLblErrorMsg().setText("Book cannot be deleted");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	class BorrowButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				reset();
				int request = TransactionDAO.requestBook(bookList.get(currTableRowSelection), currentUser);
				if (request == 1){
					JOptionPane.showMessageDialog(bookLayoutPanel, "The book has been successfully added to your Requests List!");
				}
				bookList = BookDAO.searchBook(currSearchString);
				bookSearch.getTableBookList().setModel(new BookListModel(bookList));
				bookSearch.getTableBookList().addRowSelectionInterval(currTableRowSelection, currTableRowSelection);
				bookInfo.setBookInfoData(bookList.get(currTableRowSelection));
			
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	
	class ReserveButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				reset();
				int reserve = TransactionDAO.reserveBook(bookList.get(currTableRowSelection), currentUser);
				if (reserve == 1){
					JOptionPane.showMessageDialog(bookLayoutPanel, "The book has been successfully added to your Rervations List!");
				}
				bookList = BookDAO.searchBook(currSearchString);
				bookSearch.getTableBookList().setModel(new BookListModel(bookList));
				bookSearch.getTableBookList().addRowSelectionInterval(currTableRowSelection, currTableRowSelection);
				bookInfo.setBookInfoData(bookList.get(currTableRowSelection));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	
	class BookListMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			reset();
			JTable table = (JTable) e.getSource();
			int tableRow = table.getSelectedRow();
			currTableRowSelection = tableRow;
			Book displayBook = bookList.get(tableRow);
			bookInfo.setBookInfoData(displayBook);
			try {
				if(currentUser.getType().equals("Librarian")){
					bookInfo.getBtnSave().setEnabled(true);
					if(bookList.get(tableRow).getCopies() == 0){
						bookInfo.getBtnDelete().setEnabled(false);
					}else bookInfo.getBtnDelete().setEnabled(true);
				}
				if (currentUser.getType().equals("User")){
					if (!TransactionDAO.isBorrowedByUser(bookList.get(currTableRowSelection), currentUser) && 
							!TransactionDAO.isReservedByUser(bookList.get(currTableRowSelection), currentUser)){
						if (TransactionDAO.getAvailableCopies(bookList.get(currTableRowSelection)) > 0){
							bookInfo.getBtnBorrow().setEnabled(true);
						} else {
							if (bookList.get(currTableRowSelection).getCopies() > 0){
								bookInfo.getBtnReserve().setEnabled(true);
								bookInfo.getLblErrorMsg().setText("No available copies at this time");
								bookInfo.getLblErrorMsg().setForeground(Color.RED);
							} else {
								bookInfo.getLblErrorMsg().setText("Not yet available in the library");
							}
						}
					} else {
						bookInfo.getLblErrorMsg().setText("You already have a pending transaction for the following book: ");
						bookInfo.getLblErrorMsg().setForeground(Color.RED);
					}
				}
				
			} catch (Exception ex){
				
			}
		
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
			int availableCopies = 0;
			for(int i = 0; i < bookList.size(); i++){
				rowData = new ArrayList<String>();
				rowData.add(bookList.get(i).getIsbn());
				rowData.add(bookList.get(i).getTitle()+", "+bookList.get(i).getAuthor());
				
				try {
					availableCopies = TransactionDAO.getAvailableCopies(bookList.get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(availableCopies == 0){
					rowData.add("<html><font color='red'>unavailable</font></html>");
				}else if(availableCopies == 1){
					rowData.add("<html><font color='green'>1 copy available</font></html>");
				}else{
					rowData.add("<html><font color='green'>"+availableCopies+" copies available</font></html>");
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
					bookSearch.getTableBookList().setModel(new BookListModel(bookList));

					if(bookList.size() == 0){
						bookInfo.setBookInfoData(new Book());
						bookInfo.getBtnDelete().setEnabled(false);
						bookInfo.getBtnSave().setEnabled(false);
					}else{
						bookInfo.getBtnDelete().setEnabled(true);
						bookInfo.getBtnSave().setEnabled(true);
						bookInfo.setBookInfoData(bookList.get(0));
						if(bookList.get(0).getCopies() == 0){
							bookInfo.getBtnDelete().setEnabled(false);
						}
					}
				}else{
					strSearch = "*";
					currSearchString = strSearch;
					bookList = BookDAO.searchBook(strSearch);
					bookSearch.getTableBookList().setModel(new BookListModel(bookList));

					if(bookList.size() == 0){
						bookInfo.setBookInfoData(new Book());
						bookInfo.getBtnDelete().setEnabled(false);
						bookInfo.getBtnSave().setEnabled(false);
					}else{
						bookInfo.getBtnDelete().setEnabled(true);
						bookInfo.getBtnSave().setEnabled(true);
						bookInfo.setBookInfoData(bookList.get(0));
						if(bookList.get(0).getCopies() == 0){
							bookInfo.getBtnDelete().setEnabled(false);
						}
					}
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
	private void reset() {
		bookInfo.getBtnSave().setEnabled(false);
		bookInfo.getBtnDelete().setEnabled(false);
		bookInfo.getBtnBorrow().setEnabled(false);
		bookInfo.getBtnReserve().setEnabled(false);
		bookInfo.getLblErrorMsg().setText("");
	}

	
	class TextFieldListener implements KeyListener{

		Timer timer = new Timer(Constants.TIMER_DELAY, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				timer.stop();
				try {
					String strSearch = bookSearch.getTextFieldSearch();
					if(!strSearch.trim().isEmpty()){
						currSearchString = strSearch;
						bookList = BookDAO.searchBook(strSearch);
						bookSearch.getTableBookList().setModel(new BookListModel(bookList));
				
						if(bookList.size() == 0){
							bookInfo.getBtnDelete().setEnabled(false);
							bookInfo.getBtnSave().setEnabled(false);
						}else{
							bookInfo.getBtnDelete().setEnabled(true);
							bookInfo.getBtnSave().setEnabled(true);
							bookInfo.setBookInfoData(bookList.get(0));
							if(bookList.get(0).getCopies() == 0){
								bookInfo.getBtnDelete().setEnabled(false);
							}
						}
					}else{
						strSearch = "*";
						currSearchString = strSearch;
						bookList = BookDAO.searchBook(strSearch);
						bookSearch.getTableBookList().setModel(new BookListModel(bookList));

						if(bookList.size() == 0){
							bookInfo.setBookInfoData(new Book());
							bookInfo.getBtnDelete().setEnabled(false);
							bookInfo.getBtnSave().setEnabled(false);
						}else{
							bookInfo.getBtnDelete().setEnabled(true);
							bookInfo.getBtnSave().setEnabled(true);
							bookInfo.setBookInfoData(bookList.get(0));
							if(bookList.get(0).getCopies() == 0){
								bookInfo.getBtnDelete().setEnabled(false);
							}
						}
					}
				} catch (Exception f) {
					f.printStackTrace();
				}
			}
		});
		
		@Override
		public void keyPressed(KeyEvent key) {
		}

		@Override
		public void keyReleased(KeyEvent key) {
			if(key.getKeyCode() != KeyEvent.VK_ENTER){
				if(timer.isRunning())
					timer.restart();
				else
					timer.start();
			}
			
		}

		@Override
		public void keyTyped(KeyEvent key) {			
		}
		
	}
}
