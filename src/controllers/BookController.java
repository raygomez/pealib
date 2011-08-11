package controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import models.Book;
import models.BookDAO;
import models.TransactionDAO;
import models.User;
import models.UserDAO;
import net.miginfocom.swing.MigLayout;
import pealib.PeaLibrary;
import utilities.Connector;
import utilities.Constants;
import utilities.IsbnUtil;
import utilities.Task;
import views.AddBookDialog;
import views.BookInfoPanel;
import views.BookSearchPanel;

public class BookController {

	private static BookSearchPanel bookSearch;
	private static BookInfoPanel bookInfo;
	private int currTableRowSelection;
	private String currISBN;
	private String currSearchString;
	private JPanel bookLayoutPanel;
	private User currentUser;
	private ArrayList<Book> bookList;

	public BookController(User user) throws Exception {
		currentUser = user;
		initialize();
		initializePanelContent();
	}
	
	public static void main(String args[]) throws Exception {
		Connector.init(Constants.TEST_CONFIG);
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

	private void initialize() throws Exception{
		
		bookLayoutPanel = new JPanel(new MigLayout("wrap 2", "[grow][]",
		"[grow]"));
		bookSearch = new BookSearchPanel(currentUser);
				
		bookSearch.setTextFieldListener(new TextFieldListener());
		bookSearch.setClearButtonListener(new ClearButtonListener());
		bookSearch.setSearchButtonListener(new SearchButtonListener());
		bookSearch.setAddBookButtonListener(new AddBookButtonListener());
		bookSearch.addBookSelectionListener(new BookListSelectionListener());
		
		bookInfo = new BookInfoPanel(new Book(), currentUser);
		bookInfo.getBtnDelete().setEnabled(false);
		bookInfo.getBtnSave().setEnabled(false);
		
		bookInfo.addSaveListener(new SaveButtonListener());
		bookInfo.addDeleteListener(new DeleteButtonListener());
		bookInfo.addBorrowListener(new BorrowButtonListener());
		bookInfo.addReserveListener(new ReserveButtonListener());
		
		bookLayoutPanel.add(bookSearch, "grow");
		bookLayoutPanel.add(bookInfo, "grow");

	}
	
	public void initializePanelContent() throws Exception {
		
		if (currentUser.getType().equals("Librarian")) {
			bookList = BookDAO.searchBook("");
		} else {
			bookList = BookDAO.searchBookForUser("");
		}
		
		currSearchString = "";
		currTableRowSelection = 0;
		bookSearch.getTableBookList().setModel(new BookListModel(bookList));
		bookSearch.setColumnRender(bookSearch.getTableBookList());
		
		if (bookList.size() > 0) {
			currISBN = bookList.get(0).getIsbn10();
			bookInfo.setBookInfoData(bookList.get(0));
			
			if (bookList.get(0).getCopies() == 0) {
				bookInfo.getBtnDelete().setEnabled(false);
			}
		}
		
		currTableRowSelection = 0;
		
		//TODO added conditions in case empty table
		if(bookList != null && !bookList.isEmpty()) { 
			bookSearch.getTableBookList().addRowSelectionInterval(currTableRowSelection, currTableRowSelection);
			setButtons(true);
		}		
		else{ setButtons(false);}
		
		reset();
		setButtons();
	}

	private void searchBooks() throws Exception{

		Callable<Void> toDo = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				
				String strSearch = bookSearch.getTextFieldSearch().getText();
				currSearchString = strSearch;
				if (currentUser.getType().equals("Librarian")) {
					bookList = BookDAO.searchBook(strSearch);
				} else {
					bookList = BookDAO.searchBookForUser(strSearch);
				}
				
				return null;
			}
		};
		
		Callable<Void> toDoAfter = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				
				bookSearch.getTableBookList().setModel(
						new BookListModel(bookList));

				bookSearch.setColumnRender(bookSearch.getTableBookList());
				if (bookList.size() == 0) {
					bookInfo.getBtnDelete().setEnabled(false);
					bookInfo.getBtnSave().setEnabled(false);
				} else {
					bookSearch.getTableBookList().addRowSelectionInterval(
							0, 0);
					bookInfo.getBtnDelete().setEnabled(true);
					bookInfo.getBtnSave().setEnabled(true);
					currISBN = bookList.get(0).getIsbn10();
					bookInfo.setBookInfoData(bookList.get(0));
					int availableCopy = TransactionDAO
							.getAvailableCopies(bookList.get(0));
					if (bookList.get(0).getCopies() == 0
							|| bookList.get(0).getCopies() != availableCopy) {
						bookInfo.getBtnDelete().setEnabled(false);
					}
				}
				
				return null;
			}
		};
		
		Task<Void, Object> task = new Task<Void, Object>(toDo, toDoAfter);
		LoadingControl.init(task, PeaLibrary.getMainFrame()).executeLoading();	
		
	}
	
	public void setButtons(boolean value) {
		bookInfo.getBtnBorrow().setEnabled(value);
		bookInfo.getBtnReserve().setEnabled(value);

	}

	public JPanel getBookLayoutPanel() throws Exception {
		
		initialize();		
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
				int currRow = currTableRowSelection;
				boolean validate = validateAddBook();
				if (validate) {
					try {
						if (!BookDAO.isIsbnExisting(addBook.getBookInfo()
								.getIsbn10())) {
							BookDAO.addBook(addBook.getBookInfo());
							addBook.getLblErrorMsg().makeSuccess(
									"ISBN: "
											+ addBook.getTxtFldIsbn().getText()
											+ " was added");
							clearBookFields();
							bookList = BookDAO.searchBook(currSearchString);
							bookSearch.getTableBookList().setModel(
									new BookListModel(bookList));
						
							bookSearch.setColumnRender(bookSearch.getTableBookList());
							if(bookList != null && !bookList.isEmpty()) 
								bookSearch.getTableBookList().addRowSelectionInterval(
										currRow, currRow);
							
							bookInfo.setBookInfoData(bookList.get(currRow));
						} else {
							addBook.getLblErrorMsg().makeError(
									"ISBN already exist");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					addBook.getLblErrorMsg().makeError("Invalid Input");
				}
			}
		}
		
		private void clearBookFields(){
			addBook.getTxtAreaDescription().setText("");
			addBook.getTxtFldAuthor().setText("");
			addBook.getTxtFldEdition().setText("");
			addBook.getTxtFldIsbn().setText("");
			addBook.getTxtFldPublisher().setText("");
			addBook.getTxtFldTitle().setText("");
			addBook.getTxtFldYearPublish().setText("");
			addBook.getCopyValSpinner().getModel().setValue(1);
		}

		private boolean validateAddBook(){
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

			if (!IsbnUtil.isIsbnValid(addBook.getTxtFldIsbn().getText())) {
				addBook.getTxtFldIsbn().hasError(true);
				validate = false;
			} else
				addBook.getTxtFldIsbn().hasError(false);

			if (addBook.getTxtFldPublisher().getText().length() > 100) {
				addBook.getTxtFldPublisher().hasError(true);
				validate = false;
			} else
				addBook.getTxtFldPublisher().hasError(false);

			if (addBook.getTxtAreaDescription().getText().length() > 1000) {
				addBook.getTxtAreaDescription().hasError(true);
				validate = false;
			} else
				addBook.getTxtAreaDescription().hasError(false);

			if (addBook.getTxtFldEdition().getText().length() > 30) {
				addBook.getTxtFldEdition().hasError(true);
				validate = false;
			} else
				addBook.getTxtFldEdition().hasError(false);
			
			return validate;
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
				int spinCopy = Integer.parseInt(bookInfo.getSpinCopyVal()
						.getModel().getValue().toString());
				int availableCopy = TransactionDAO.getAvailableCopies(bookInfo
						.getCurrBook());
				int validCopy = bookInfo.getCurrBook().getCopies()
						- availableCopy;
				boolean validate = validateEditBook(spinCopy, validCopy);
				
				if (validate) {

					for (int i = validCopy + 1; i <= spinCopy; i++){
						if (TransactionDAO.isBookReservedByOtherUsers(
								bookInfo.getCurrBook())) {
							TransactionDAO.passToNextUser(bookInfo.getCurrBook());
						}
					}

					BookDAO.editBook(bookInfo.getCurrBook());
					bookList = BookDAO.searchBook(currSearchString);
					bookSearch.getTableBookList().setModel(
							new BookListModel(bookList));
		
					bookSearch.setColumnRender(bookSearch.getTableBookList());
					if(bookList != null && !bookList.isEmpty()) 
						bookSearch.getTableBookList().addRowSelectionInterval(
								currRow, currRow);

					bookInfo.setBookInfoData(bookList.get(currRow));
					JOptionPane.showMessageDialog(bookInfo, "Book updated.",
							"Information", JOptionPane.INFORMATION_MESSAGE);
					bookInfo.getLblErrorMsg().clear();
					if (bookList.get(currRow).getCopies() == 0
							|| bookList.get(currRow).getCopies() != availableCopy) {
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
		
		private boolean validateEditBook(int spinCopy, int validCopy) throws Exception{
			boolean validate = true;
			int flag = 0;
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
					bookInfo.getTxtFldYrPublished().hasError(true);
					validate = false;
				} else
					bookInfo.getTxtFldYrPublished().hasError(false);
			} else
				bookInfo.getTxtFldYrPublished().hasError(false);

			if (bookInfo.getTxtFldPublisher().getText().length() > 100) {
				bookInfo.getTxtFldPublisher().hasError(true);
				validate = false;
			} else
				bookInfo.getTxtFldPublisher().hasError(false);

			//TODO ISBN CALCULATE VALIDATION
			if (!IsbnUtil
					.isIsbnValid(bookInfo.getTxtFldISBN10().getText())) {
				bookInfo.getTxtFldISBN10().hasError(true);
				validate = false;
				flag = 1;
			} else
				bookInfo.getTxtFldISBN10().hasError(false);

			if (bookInfo.getTxtFldDescription().getText().length() > 1000) {
				bookInfo.getTxtFldDescription().hasError(true);
				validate = false;
			} else
				bookInfo.getTxtFldDescription().hasError(false);


			if (bookInfo.getTxtFldEdition().getText().length() > 30) {
				validate = false;
				bookInfo.getTxtFldEdition().hasError(true);
			} else
				bookInfo.getTxtFldEdition().hasError(false);

			//TODO existing ISBN
			if (!currISBN.equals(bookInfo.getTxtFldISBN10().getText())
					&& flag == 0) {
				if (BookDAO.isIsbnExisting(bookInfo.getTxtFldISBN10()
						.getText())) {
					bookInfo.getTxtFldISBN10().hasError(true);
					validate = false;
				} else
					bookInfo.getTxtFldISBN10().hasError(false);
			}
			if (spinCopy < validCopy) {
				bookInfo.getSpinCopyVal().hasError(true);
				validate = false;
			} else{
				bookInfo.getSpinCopyVal().hasError(false);
			}
			
			return validate;
		}

	}

	class DeleteButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int currRow = currTableRowSelection;
			try {
				int optConfirm = JOptionPane.showConfirmDialog(bookInfo,
						"Do you really want to delete this book?", "Confirm",
						JOptionPane.YES_NO_OPTION);
				
				if (optConfirm == 0) {

					if (bookInfo.getCurrBook().getCopies() == 
						TransactionDAO.getAvailableCopies(bookInfo.getCurrBook())){
						BookDAO.deleteBook(bookInfo.getCurrBook());
					}
					else {
						JOptionPane.showMessageDialog(null, "Someone might have borrowed the book!\n"
								+ "before the delete transaction have been completed.", "",
								JOptionPane.ERROR_MESSAGE);
					}
					
					bookList = BookDAO.searchBook(currSearchString);
					bookInfo.setBookInfoData(bookList.get(currRow));
					bookSearch.getTableBookList().setModel(
							new BookListModel(bookList));
			
					bookSearch.setColumnRender(bookSearch.getTableBookList());
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
				if (TransactionDAO.getAvailableCopies(bookInfo.getCurrBook()) > 0){
					int request = TransactionDAO.requestBook(bookList.get(currRow),
							currentUser);
					if (request == 1) {
						JOptionPane
								.showMessageDialog(bookLayoutPanel,
										"The book has been successfully added to your Requests List!");
					}
				}
				else {
					bookInfo.getBtnReserve().setEnabled(true);
					JOptionPane.showMessageDialog(null, "Someone might have borrowed the book!\n"
							+ "before this transaction have been completed.", "",
							JOptionPane.ERROR_MESSAGE);
				}
				bookList = BookDAO.searchBookForUser(currSearchString);			
				bookSearch.getTableBookList().setModel(
						new BookListModel(bookList));
		
				bookSearch.setColumnRender(bookSearch.getTableBookList());
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
				int reserve = TransactionDAO.reserveBook(bookList.get(currRow),
						currentUser);
				if (reserve == 1) {
					JOptionPane
							.showMessageDialog(bookLayoutPanel,
									"The book has been successfully added to your Rervations List!");
				}
				bookList = BookDAO.searchBookForUser(currSearchString);			
				bookSearch.getTableBookList().setModel(
						new BookListModel(bookList));
		
				bookSearch.setColumnRender(bookSearch.getTableBookList());
				if(bookList != null && !bookList.isEmpty()) 
					bookSearch.getTableBookList().addRowSelectionInterval(
							currRow, currRow);
				bookInfo.setBookInfoData(bookList.get(currRow));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	class BookListSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			reset();
			DefaultListSelectionModel dlSelectionModel = (DefaultListSelectionModel) e.getSource();
			int tableRow = dlSelectionModel.getLeadSelectionIndex();
			currTableRowSelection = tableRow;
			
			if(tableRow < 0){
				return;
			}		
			
			//TODO check in order to avoid exception index outofbounds
			if(tableRow < bookList.size()){
				currTableRowSelection = tableRow;
				
				try {
					if(currentUser.getType().equals("Librarian")){
						bookList = BookDAO.searchBook(bookSearch.getTextFieldSearch().getText());
					}else{
						bookList = BookDAO.searchBookForUser(bookSearch.getTextFieldSearch().getText());
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Book displayBook = bookList.get(tableRow);
				currISBN = displayBook.getIsbn10();
				//TODO 
				bookInfo.resetErrors();
			//	System.out.println(displayBook.getIsbn());
				bookInfo.setBookInfoData(displayBook);
			}
			
			try {
				setButtons();

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
				rowData.add(bookList.get(i).getIsbn10()+" / "+bookList.get(i).getIsbn13());
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
				} else if (availableCopies < 0) {
					rowData.add("<html><font color='red'>count error</font></html>");
				} else if (availableCopies == 1) {
					rowData.add("<html><font color='green'>1 copy available</font></html>");
				} else {
					rowData.add("<html><font color='green'>" + availableCopies
							+ " copies available</font></html>");
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
				searchBooks();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	class ClearButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			bookSearch.getTextFieldSearch().setText("");
			try {
				searchBooks();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void reset() {
		bookInfo.getBtnSave().setEnabled(false);
		bookInfo.getBtnDelete().setEnabled(false);
		bookInfo.getBtnBorrow().setEnabled(false);
		bookInfo.getBtnReserve().setEnabled(false);
		bookInfo.getLblErrorMsg().setText("");
	}

	class TextFieldListener extends KeyAdapter {

		Timer timer = new Timer(Constants.TIMER_DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				timer.stop();
				try {
					searchBooks();
				} catch (Exception f) {
					f.printStackTrace();
				}
			}
		});

		@Override
		public void keyReleased(KeyEvent key) {
			if (key.getKeyCode() != KeyEvent.VK_ENTER) {
				if (timer.isRunning())
					timer.restart();
				else
					timer.start();
			}

		}
	}
	
	private void setButtons() throws Exception {
		int tableRow = currTableRowSelection;
		if (bookList.size() != 0){
			if (currentUser.getType().equals("User")) {
				if (bookList != null
						&& !bookList.isEmpty()
						&& !TransactionDAO.isBorrowedByUser(
						bookList.get(currTableRowSelection), currentUser)
						&& !TransactionDAO.isReservedByUser(
								bookList.get(currTableRowSelection), currentUser)) {
					if (TransactionDAO.getAvailableCopies(bookList
							.get(currTableRowSelection)) > 0) {
						bookInfo.getBtnBorrow().setEnabled(true);
					} else {
						bookInfo.getBtnReserve().setEnabled(true);
						bookInfo.getLblErrorMsg().makeError("No available copies at this time");
					}
				} else if (bookList != null && !bookList.isEmpty()) {
					bookInfo.getLblErrorMsg().makeError("You already have a pending transaction for the following book: ");
				}
			}
			
			if (currentUser.getType().equals("Librarian")) {
				bookInfo.getBtnSave().setEnabled(true);
				int availableCopy = TransactionDAO
						.getAvailableCopies(bookList.get(tableRow));
				if (bookList.get(tableRow).getCopies() == 0
						|| bookList.get(tableRow).getCopies() != availableCopy) {
					bookInfo.getBtnDelete().setEnabled(false);
				} else
					bookInfo.getBtnDelete().setEnabled(true);
			}
		}
	}

	public BookSearchPanel getBookSearch() {
		return bookSearch;
	}

	public BookInfoPanel getBookInfo() {
		return bookInfo;
	}

}
