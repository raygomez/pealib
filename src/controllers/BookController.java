package controllers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;
import utilities.Connector;
import utilities.Constants;
import utilities.IsbnChecker;
import views.AddBookDialog;
import views.BookInfoPanel;
import views.BookSearchPanel;
import models.Book;
import models.BookDAO;
import models.TransactionDAO;
import models.User;
import models.UserDAO;

public class BookController {

	private static BookSearchPanel bookSearch;
	private static BookInfoPanel bookInfo;
	private int currTableRowSelection;
	private String currISBN;
	private String currSearchString;
	private JPanel bookLayoutPanel;
	private User currentUser;
	private ArrayList<Book> bookList;

	public static void main(String args[]) throws Exception {
		new Connector(Constants.TEST_CONFIG);
		User user = UserDAO.getUserById(2);

		BookController bookController = null;
		bookController = new BookController(user);

		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		testFrame.setBounds(0, 0, screenSize.width, screenSize.height);
		testFrame.setContentPane(bookController.getBookLayoutPanel());
		
		testFrame.setVisible(true);
		testFrame.setResizable(false);
	}

	public BookController(User user) throws Exception {
		currentUser = user;
		generateBookLayoutPanel();
	}

	private void generateBookLayoutPanel() throws Exception {
		if (currentUser.getType().equals("Librarian")){
			bookList = BookDAO.searchBook("");
		} else {
			bookList = BookDAO.searchBookForUser("");			
		}
		currSearchString = "";
		currTableRowSelection = 0;
		bookLayoutPanel = new JPanel(new MigLayout("wrap 2", "[grow][grow]", "[grow]"));
		bookSearch = new BookSearchPanel(currentUser);
		if (bookList.size() == 0) {
			bookInfo = new BookInfoPanel(new Book(), currentUser);
			bookInfo.getBtnDelete().setEnabled(false);
			bookInfo.getBtnSave().setEnabled(false);
		} else {
			currISBN = bookList.get(0).getIsbn();
			bookInfo = new BookInfoPanel(bookList.get(0), currentUser);
			if (bookList.get(0).getCopies() == 0) {
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
		
		//TODO added conditions in case empty table
		if(bookList != null && !bookList.isEmpty()) { 
			bookSearch.getTableBookList().addRowSelectionInterval(currTableRowSelection, currTableRowSelection);
			setButtons(true);
		}		
		else{ setButtons(false);}

		bookSearch.addBookSelectionListener(new BookListSelectionListener());
		bookInfo.addSaveListener(new SaveButtonListener());
		bookInfo.addDeleteListener(new DeleteButtonListener());
		bookInfo.addBorrowListener(new BorrowButtonListener());
		bookInfo.addReserveListener(new ReserveButtonListener());
	}

	public void setButtons(boolean value){		
		bookInfo.getBtnBorrow().setEnabled(value);
		bookInfo.getBtnReserve().setEnabled(value);
	
	}
	
	public JPanel getBookLayoutPanel() throws Exception {
		generateBookLayoutPanel();
		return bookLayoutPanel;
	}

	class AddBookButtonListener implements ActionListener {
		private AddBookDialog addBook;

		public void actionPerformed(ActionEvent arg0) {
			addBook = new AddBookDialog();
			addBook.addBookActionListener(new AddBookListener());
			addBook.addCancelActionListener(new CancelListener());
			addBook.setVisible(true);
		}

		class AddBookListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean validate = true;

				if (addBook.getTxtFldTitle().getText().trim().isEmpty()
						|| addBook.getTxtFldTitle().getText().length() > 100) {
					addBook.getTxtFldTitle().hasError(true);
					validate = false;
				} else
					addBook.getTxtFldTitle().hasError(false);

				if (addBook.getTxtFldAuthor().getText().trim().isEmpty()
						|| addBook.getTxtFldAuthor().getText().length() > 100) {
					addBook.getTxtFldAuthor().hasError(true);
					validate = false;
				} else
					addBook.getTxtFldAuthor().hasError(false);

				if (!addBook.getTxtFldYearPublish().getText().trim().isEmpty()) {
					if (!addBook.getTxtFldYearPublish().getText()
							.matches(Constants.YEAR_PUBLISH_FORMAT)) {
						addBook.getTxtFldYearPublish().hasError(true);
						validate = false;
					} else
						addBook.getTxtFldYearPublish().hasError(false);
				} else
					addBook.getTxtFldYearPublish().hasError(false);

				if (!IsbnChecker.isIsbnValid(addBook.getTxtFldIsbn().getText())) {
					addBook.getTxtFldIsbn().hasError(true);
					validate = false;
				} else
					addBook.getTxtFldIsbn().hasError(false);

				if (addBook.getTxtFldPublisher().getText().length() > 100) {
					addBook.getTxtFldPublisher().hasError(true);
					validate = false;
				} else
					addBook.getTxtFldPublisher().hasError(false);

				if (addBook.getTxtAreaDescription().getText().length() > 300) {
					addBook.getTxtAreaDescription().hasError(true);
					validate = false;
				} else
					addBook.getTxtAreaDescription().hasError(false);
				
				if (addBook.getTxtFldEdition().getText().length() > 30) {
					addBook.getTxtFldEdition().hasError(true);
					validate = false;
				} else
					addBook.getTxtFldEdition().hasError(false);

				if (validate) {
					try {
						if (!BookDAO.isIsbnExisting(addBook.getBookInfo()
								.getIsbn())) {
							BookDAO.addBook(addBook.getBookInfo());
							addBook.getLblErrorMsg().makeSuccess("ISBN: "+addBook.getTxtFldIsbn().getText()+" was added");
							addBook.getTxtAreaDescription().setText("");
							addBook.getTxtFldAuthor().setText("");
							addBook.getTxtFldEdition().setText("");
							addBook.getTxtFldIsbn().setText("");
							addBook.getTxtFldPublisher().setText("");
							addBook.getTxtFldTitle().setText("");
							addBook.getTxtFldYearPublish().setText("");
							addBook.getCopyValSpinner().getModel().setValue(1);
							if (currentUser.getType().equals("Librarian")){
								bookList = BookDAO.searchBook(currSearchString);
							} else {
								bookList = BookDAO.searchBookForUser(currSearchString);			
							}
							bookSearch.getTableBookList().setModel(
									new BookListModel(bookList));
							
							if(bookList != null && !bookList.isEmpty()) 
								bookSearch.getTableBookList().addRowSelectionInterval(
										currTableRowSelection, currTableRowSelection);
							
							bookInfo.setBookInfoData(bookList.get(currTableRowSelection));
						} else {
							addBook.getLblErrorMsg().makeError("ISBN already exist");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					addBook.getLblErrorMsg().makeError("Invalid Input");
				}
			}
		}

		class CancelListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				addBook.dispose();
			}
		}
	}

	class SaveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int currRow = currTableRowSelection;
			try {
				boolean validate = true;

				if (bookInfo.getTxtFldTitle().getText().trim().isEmpty()
						|| bookInfo.getTxtFldTitle().getText().length() > 100) {
					bookInfo.getTxtFldTitle().hasError(true);
					validate = false;
				} else
					bookInfo.getTxtFldTitle().hasError(false);

				if (bookInfo.getTxtFldAuthor().getText().trim().isEmpty()
						|| bookInfo.getTxtFldAuthor().getText().length() > 100) {
					bookInfo.getTxtFldAuthor().hasError(true);
					validate = false;
				} else
					bookInfo.getTxtFldAuthor().hasError(false);

				if (!bookInfo.getTxtFldYrPublished().getText().trim().isEmpty()) {
					if (!bookInfo.getTxtFldYrPublished().getText()
							.matches(Constants.YEAR_PUBLISH_FORMAT)) {
						validate = false;
						bookInfo.getTxtFldYrPublished().hasError(true);
					} else
						bookInfo.getTxtFldYrPublished().hasError(false);
				} else
					bookInfo.getTxtFldYrPublished().hasError(false);

				if (bookInfo.getTxtFldPublisher().getText().length() > 100) {
					validate = false;
					bookInfo.getTxtFldPublisher().hasError(true);
				} else
					bookInfo.getTxtFldPublisher().hasError(false);

				if (!IsbnChecker.isIsbnValid(bookInfo.getTxtFldISBN().getText())) {
					validate = false;
					bookInfo.getTxtFldISBN().hasError(true);
				} else bookInfo.getTxtFldISBN().hasError(false);

				if (bookInfo.getTxtFldDescription().getText().length() > 300) {
					validate = false;
					bookInfo.getTxtFldDescription().hasError(true);
				} else
					bookInfo.getTxtFldDescription().hasError(false);

				int spinCopy = Integer.parseInt(bookInfo.getSpinCopyVal()
						.getModel().getValue().toString());
				int availableCopy = TransactionDAO.getAvailableCopies(bookInfo
						.getCurrBook());
				int validCopy = bookInfo.getCurrBook().getCopies()
						- availableCopy;
				if (spinCopy < validCopy) {
					validate = false;
					bookInfo.getSpinCopyVal().hasError(true);
				} else
					bookInfo.getSpinCopyVal().hasError(false);
				
				if (bookInfo.getTxtFldEdition().getText().length() > 30) {
					validate = false;
					bookInfo.getTxtFldEdition().hasError(true);
				} else
					bookInfo.getTxtFldEdition().hasError(false);
				
				if(!currISBN.equals(bookInfo.getTxtFldISBN().getText())){
					if(BookDAO.isIsbnExisting(bookInfo.getTxtFldISBN().getText())){
						bookInfo.getTxtFldISBN().hasError(true);
						validate = false;
					} else
						bookInfo.getTxtFldISBN().hasError(false);
				}

				if (validate) {
					BookDAO.editBook(bookInfo.getCurrBook());
					if (currentUser.getType().equals("Librarian")){
						bookList = BookDAO.searchBook(currSearchString);
					} else {
						bookList = BookDAO.searchBookForUser(currSearchString);			
					}
					bookSearch.getTableBookList().setModel(
							new BookListModel(bookList));
					
					if(bookList != null && !bookList.isEmpty()) 
						bookSearch.getTableBookList().addRowSelectionInterval(
								currRow, currRow);
					
					bookInfo.setBookInfoData(bookList
							.get(currRow));
					JOptionPane.showMessageDialog(bookInfo,
						    "Book updated.",
						    "Information",
						    JOptionPane.INFORMATION_MESSAGE);
					bookInfo.getLblErrorMsg().clear();
					if (bookList.get(currRow).getCopies() == 0 || bookList.get(currRow).getCopies() != availableCopy) {
						bookInfo.getBtnDelete().setEnabled(false);
					} else
						bookInfo.getBtnDelete().setEnabled(true);
				} else {
					bookInfo.getLblErrorMsg().makeError("Invalid Input");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	class DeleteButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int currRow = currTableRowSelection;
			try {
				int optConfirm = JOptionPane.showConfirmDialog(
						bookInfo,
					    "Do you really want to delete this book?",
					    "Confirm",
					    JOptionPane.YES_NO_OPTION);
				if(optConfirm == 0){
					BookDAO.deleteBook(bookInfo.getCurrBook());
					if (currentUser.getType().equals("Librarian")){
						bookList = BookDAO.searchBook(currSearchString);
					} else {
						bookList = BookDAO.searchBookForUser(currSearchString);			
					}
					bookInfo.setBookInfoData(bookList
							.get(currRow));
					bookSearch.getTableBookList().setModel(
							new BookListModel(bookList));
					
					if(bookList != null && !bookList.isEmpty()) 
						bookSearch.getTableBookList().addRowSelectionInterval(
								currRow, currRow);
					bookInfo.getBtnDelete().setEnabled(false);
					
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	class BorrowButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int currRow = currTableRowSelection;
			try {
				reset();
				int request = TransactionDAO.requestBook(
						bookList.get(currRow), currentUser);
				if (request == 1) {
					JOptionPane
							.showMessageDialog(bookLayoutPanel,
									"The book has been successfully added to your Requests List!");
				}
				if (currentUser.getType().equals("Librarian")){
					bookList = BookDAO.searchBook(currSearchString);
				} else {
					bookList = BookDAO.searchBookForUser(currSearchString);			
				}
				bookSearch.getTableBookList().setModel(
						new BookListModel(bookList));
				
				if(bookList != null && !bookList.isEmpty()) 
					bookSearch.getTableBookList().addRowSelectionInterval(
							currRow, currRow);
				
				bookInfo.setBookInfoData(bookList.get(currRow));

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	class ReserveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int currRow = currTableRowSelection;
			try {
				reset();
				int reserve = TransactionDAO.reserveBook(
						bookList.get(currRow), currentUser);
				if (reserve == 1) {
					JOptionPane
							.showMessageDialog(bookLayoutPanel,
									"The book has been successfully added to your Rervations List!");
				}
				if (currentUser.getType().equals("Librarian")){
					bookList = BookDAO.searchBook(currSearchString);
				} else {
					bookList = BookDAO.searchBookForUser(currSearchString);			
				}
				bookSearch.getTableBookList().setModel(
						new BookListModel(bookList));
				
				if(bookList != null && !bookList.isEmpty()) 
					bookSearch.getTableBookList().addRowSelectionInterval(
							currRow, currRow);
				bookInfo.setBookInfoData(bookList.get(currRow));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}
	
	class BookListSelectionListener implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {
			DefaultListSelectionModel dlSelectionModel = (DefaultListSelectionModel) e
			.getSource();
			int tableRow = dlSelectionModel.getLeadSelectionIndex();
			System.out.println(tableRow);
			currTableRowSelection = tableRow;
			if(tableRow < 0){
				return;
			}
			
			currTableRowSelection = tableRow;
			Book displayBook = bookList.get(tableRow);
			currISBN = displayBook.getIsbn();
			bookInfo.setBookInfoData(displayBook);
			try {
				if (currentUser.getType().equals("Librarian")) {
					bookInfo.getBtnSave().setEnabled(true);
					int availableCopy = TransactionDAO.getAvailableCopies(bookList.get(tableRow));
					if (bookList.get(tableRow).getCopies() == 0 || bookList.get(tableRow).getCopies() != availableCopy) {
						bookInfo.getBtnDelete().setEnabled(false);
					} else
						bookInfo.getBtnDelete().setEnabled(true);
				}
				if (currentUser.getType().equals("User")) {
					if (!TransactionDAO.isBorrowedByUser(
							bookList.get(currTableRowSelection), currentUser)
							&& !TransactionDAO.isReservedByUser(
									bookList.get(currTableRowSelection),
									currentUser)) {
						if (TransactionDAO.getAvailableCopies(bookList
								.get(currTableRowSelection)) > 0) {
							bookInfo.getBtnBorrow().setEnabled(true);
						} else {
							bookInfo.getBtnReserve().setEnabled(true);
							bookInfo.getLblErrorMsg().setText(
									"No available copies at this time");
							bookInfo.getLblErrorMsg().setForeground(
									Color.RED);
						}
					} else {
						bookInfo.getLblErrorMsg()
								.setText(
										"You already have a pending transaction for the following book: ");
						bookInfo.getLblErrorMsg().setForeground(Color.RED);
					}
				}

			} catch (Exception ex) {

			}

			
			
		}
		
	}

	class BookListModel extends AbstractTableModel {
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
			for (int i = 0; i < bookList.size(); i++) {
				rowData = new ArrayList<String>();
				rowData.add(bookList.get(i).getIsbn());
				rowData.add(bookList.get(i).getTitle() + ", "
						+ bookList.get(i).getAuthor());

				try {
					availableCopies = TransactionDAO
							.getAvailableCopies(bookList.get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (availableCopies == 0) {
					rowData.add("<html><font color='red'>unavailable</font></html>");
				} else if (availableCopies == 1) {
					rowData.add("<html><font color='green'>1 copy available</font></html>");
				} else {
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
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

	}

	class SearchButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String strSearch = bookSearch.getTextFieldSearch();
				currSearchString = strSearch;
				if (currentUser.getType().equals("Librarian")){
					bookList = BookDAO.searchBook(strSearch);
				} else {
					bookList = BookDAO.searchBookForUser(strSearch);			
				}
				bookSearch.getTableBookList().setModel(
						new BookListModel(bookList));
				if (bookList.size() == 0) {
					bookInfo.setBookInfoData(new Book());
					bookInfo.getBtnDelete().setEnabled(false);
					bookInfo.getBtnSave().setEnabled(false);
				} else {
					bookSearch.getTableBookList().addRowSelectionInterval(0, 0);
					bookInfo.getBtnDelete().setEnabled(true);
					bookInfo.getBtnSave().setEnabled(true);
					currISBN = bookList.get(0).getIsbn();
					bookInfo.setBookInfoData(bookList.get(0));
					int availableCopy = TransactionDAO.getAvailableCopies(bookList.get(0));
					if (bookList.get(0).getCopies() == 0 || bookList.get(0).getCopies() != availableCopy) {
						bookInfo.getBtnDelete().setEnabled(false);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	class ClearButtonListener implements ActionListener {
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

	class TextFieldListener implements KeyListener {

		Timer timer = new Timer(Constants.TIMER_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				timer.stop();
				try {
					String strSearch = bookSearch.getTextFieldSearch();
					currSearchString = strSearch;
					if (currentUser.getType().equals("Librarian")){
						bookList = BookDAO.searchBook(strSearch);
					} else {
						bookList = BookDAO.searchBookForUser(strSearch);			
					}
					bookSearch.getTableBookList().setModel(
								new BookListModel(bookList));
					if (bookList.size() == 0) {
						bookInfo.getBtnDelete().setEnabled(false);
						bookInfo.getBtnSave().setEnabled(false);
					} else {
						bookSearch.getTableBookList().addRowSelectionInterval(0, 0);
						bookInfo.getBtnDelete().setEnabled(true);
						bookInfo.getBtnSave().setEnabled(true);
						currISBN = bookList.get(0).getIsbn();
						bookInfo.setBookInfoData(bookList.get(0));
						int availableCopy = TransactionDAO.getAvailableCopies(bookList.get(0));
						if (bookList.get(0).getCopies() == 0 || bookList.get(0).getCopies() != availableCopy) {
								bookInfo.getBtnDelete().setEnabled(false);
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
			if (key.getKeyCode() != KeyEvent.VK_ENTER) {
				if (timer.isRunning())
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
