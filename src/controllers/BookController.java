package controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import utilities.CrashHandler;
import utilities.IsbnUtil;
import utilities.MyTextField;
import utilities.Task;
import views.AddBookDialog;
import views.BookInfoPanel;
import views.BookSearchPanel;

public class BookController {

	private static BookSearchPanel bookSearch;
	private static BookInfoPanel bookInfo;
	private AddBookDialog addBook;
	private int currTableRowSelection;
	private String currISBN10;
	private String currISBN13;
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

	private void initialize() throws Exception {

		bookLayoutPanel = new JPanel(new MigLayout("wrap 2", "[grow][]",
				"[grow]"));
		bookSearch = new BookSearchPanel(currentUser);

		bookSearch.setTextFieldListener(new TextFieldListener());
		bookSearch.setClearButtonListener(new ClearButtonListener());
		bookSearch.setSearchButtonListener(new SearchButtonListener());
		bookSearch.setAddBookButtonListener(new AddBookButtonListener());
		bookSearch.addBookSelectionListener(new BookListSelectionListener());

		bookInfo = new BookInfoPanel(new Book(), currentUser);
		bookInfo.setDeleteButtonEnable(false);
		bookInfo.setSaveButtonEnable(false);

		bookInfo.addSaveListener(new SaveButtonListener());
		bookInfo.addDeleteListener(new DeleteButtonListener());
		bookInfo.addBorrowListener(new BorrowButtonListener());
		bookInfo.addReserveListener(new ReserveButtonListener());

		bookInfo.addFocusListeners(new IsbnCheckFocusListener(),
				new IsbnCheckFocusListener(), new TitleCheckFocusListener(),
				new AuthorCheckFocusListener(), new YearCheckFocusListener());

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
			currISBN10 = bookList.get(0).getIsbn10();
			bookInfo.setBookInfoData(bookList.get(0));

			if (bookList.get(0).getCopies() == 0) {
				bookInfo.setDeleteButtonEnable(false);
			}
		}

		currTableRowSelection = 0;

		if (bookList != null && !bookList.isEmpty()) {
			bookSearch.getTableBookList().addRowSelectionInterval(
					currTableRowSelection, currTableRowSelection);
			setButtons(true);
		} else {
			setButtons(false);
		}

		reset();
		setButtons();
	}

	private void searchBooks() throws Exception {

		Callable<Void> toDo = new Callable<Void>() {

			@Override
			public Void call() throws Exception {

				String strSearch = bookSearch.getSearchWord();
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
					bookInfo.setDeleteButtonEnable(false);
					bookInfo.setSaveButtonEnable(false);
				} else {
					bookSearch.getTableBookList().addRowSelectionInterval(0, 0);
					bookInfo.setDeleteButtonEnable(true);
					bookInfo.setSaveButtonEnable(true);
					currISBN10 = bookList.get(0).getIsbn10();
					bookInfo.setBookInfoData(bookList.get(0));
					int availableCopy = TransactionDAO
							.getAvailableCopies(bookList.get(0));
					if (bookList.get(0).getCopies() == 0
							|| bookList.get(0).getCopies() != availableCopy) {
						bookInfo.setDeleteButtonEnable(false);
					}
				}

				return null;
			}
		};

		Task<Void, Object> task = new Task<Void, Object>(toDo, toDoAfter);
		LoadingControl.init(task, PeaLibrary.getMainFrame()).executeLoading();

	}

	public void setButtons(boolean value) {
		bookInfo.setBorrowButtonEnable(value);
		bookInfo.setReserveButtonEnable(value);

	}

	public JPanel getBookLayoutPanel() throws Exception {

		initialize();
		return bookLayoutPanel;
	}

	class AddBookButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			addBook = new AddBookDialog();
			addBook.addBookActionListener(new AddBookListener());
			addBook.addCancelActionListener(new CancelListener());
			addBook.getTxtFldIsbn().addFocusListener(
					new IsbnCheckFocusListener());
			addBook.getTxtFldTitle().addFocusListener(
					new TitleCheckFocusListener());
			addBook.getTxtFldAuthor().addFocusListener(
					new AuthorCheckFocusListener());
			addBook.getTxtFldYearPublish().addFocusListener(
					new YearCheckFocusListener());
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

							bookSearch.setColumnRender(bookSearch
									.getTableBookList());
							if (bookList != null && !bookList.isEmpty())
								bookSearch.getTableBookList()
										.addRowSelectionInterval(currRow,
												currRow);

							bookInfo.setBookInfoData(bookList.get(currRow));
						} else {
							addBook.getLblErrorMsg().makeError(
									"ISBN already exist");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					addBook.getLblErrorMsg().makeError("Error Found!");
				}
			}
		}

		private void clearBookFields() {
			addBook.getTxtAreaDescription().setText("");
			addBook.getTxtFldAuthor().setText("");
			addBook.getTxtFldEdition().setText("");
			addBook.getTxtFldIsbn().setText("");
			addBook.getTxtFldPublisher().setText("");
			addBook.getTxtFldTitle().setText("");
			addBook.getTxtFldYearPublish().setText("");
			addBook.getCopyValSpinner().getModel().setValue(1);
		}

		private boolean validateAddBook() {
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

					for (int i = validCopy + 1; i <= spinCopy; i++) {
						if (TransactionDAO.isBookReservedByOtherUsers(bookInfo
								.getCurrBook())) {
							TransactionDAO.passToNextUser(bookInfo
									.getCurrBook());
						}
					}

					BookDAO.editBook(bookInfo.getCurrBook());
					bookList = BookDAO.searchBook(currSearchString);
					bookSearch.getTableBookList().setModel(
							new BookListModel(bookList));

					bookSearch.setColumnRender(bookSearch.getTableBookList());
					if (bookList != null && !bookList.isEmpty())
						bookSearch.getTableBookList().addRowSelectionInterval(
								currRow, currRow);

					bookInfo.setBookInfoData(bookList.get(currRow));
					JOptionPane.showMessageDialog(bookInfo, "Book updated.",
							"Information", JOptionPane.INFORMATION_MESSAGE);
					bookInfo.getLblErrorMsg().clear();
					if (bookList.get(currRow).getCopies() == 0
							|| bookList.get(currRow).getCopies() != availableCopy) {
						bookInfo.setDeleteButtonEnable(false);
					} else
						bookInfo.setDeleteButtonEnable(true);
				} else {
					bookInfo.getLblErrorMsg().makeError("Invalid Input");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		private boolean validateEditBook(int spinCopy, int validCopy)
				throws Exception {
			boolean validate = true;
			int flag = 0;
			if (bookInfo.getTitle().isEmpty()
					|| bookInfo.getTitle().length() > 100) {
				bookInfo.hasTitleError(true);
				validate = false;
			} else
				bookInfo.hasTitleError(false);

			if (bookInfo.getAuthor().isEmpty()
					|| bookInfo.getAuthor().length() > 100) {
				bookInfo.hasAuthorError(true);
				validate = false;
			} else
				bookInfo.hasAuthorError(false);

			if (!bookInfo.getYearPublished().trim().isEmpty()) {
				if (!bookInfo.getYearPublished().matches(
						Constants.YEAR_PUBLISH_FORMAT)) {
					bookInfo.hasYearPublishedError(true);
					validate = false;
				} else
					bookInfo.hasYearPublishedError(false);
			} else
				bookInfo.hasYearPublishedError(false);

			if (bookInfo.getPublisher().length() > 100) {
				bookInfo.hasPublisherError(true);
				validate = false;
			} else
				bookInfo.hasPublisherError(false);

			if (!IsbnUtil.isIsbnValid(bookInfo.getIsbn10())) {
				bookInfo.hasIsbn10Error(true);
				validate = false;
				flag = 1;
			} else
				bookInfo.hasIsbn10Error(false);

			if (!IsbnUtil.isIsbnValid(bookInfo.getIsbn13())) {
				bookInfo.hasIsbn13Error(true);
				validate = false;
				flag = 1;
			} else
				bookInfo.hasIsbn10Error(false);

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

			if (!currISBN10.equals(bookInfo.getIsbn10()) && flag == 0) {
				String otherISBN = IsbnUtil.convert(bookInfo.getIsbn10());
				if (BookDAO.isIsbnExisting(bookInfo.getIsbn10())
						|| otherISBN != bookInfo.getIsbn13()) {
					bookInfo.hasIsbn10Error(true);
					validate = false;
				} else
					bookInfo.hasIsbn10Error(false);
			}
			if (!currISBN13.equals(bookInfo.getIsbn13())
					&& flag == 0) {
				String otherISBN = IsbnUtil.convert(bookInfo.getIsbn13());
				if (BookDAO
						.isIsbnExisting(bookInfo.getIsbn13())
						|| otherISBN != bookInfo.getIsbn13()) {
					bookInfo.hasIsbn13Error(true);
					validate = false;
				} else
					bookInfo.hasIsbn10Error(false);
			}

			if (spinCopy < validCopy) {
				bookInfo.getSpinCopyVal().hasError(true);
				validate = false;
			} else {
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

					if (bookInfo.getCurrBook().getCopies() == TransactionDAO
							.getAvailableCopies(bookInfo.getCurrBook())) {
						BookDAO.deleteBook(bookInfo.getCurrBook());
					} else {
						JOptionPane
								.showMessageDialog(
										null,
										"Someone might have borrowed the book!\n"
												+ "before the delete transaction have been completed.",
										"", JOptionPane.ERROR_MESSAGE);
					}

					bookList = BookDAO.searchBook(currSearchString);
					bookInfo.setBookInfoData(bookList.get(currRow));
					bookSearch.getTableBookList().setModel(
							new BookListModel(bookList));

					bookSearch.setColumnRender(bookSearch.getTableBookList());
					if (bookList != null && !bookList.isEmpty())
						bookSearch.getTableBookList().addRowSelectionInterval(
								currRow, currRow);
					bookInfo.setDeleteButtonEnable(false);

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
				if (TransactionDAO.getAvailableCopies(bookInfo.getCurrBook()) > 0) {
					int request = TransactionDAO.requestBook(
							bookList.get(currRow), currentUser);
					if (request == 1) {
						JOptionPane
								.showMessageDialog(bookLayoutPanel,
										"The book has been successfully added to your Requests List!");
					}
				} else {
					bookInfo.setReserveButtonEnable(true);
					JOptionPane
							.showMessageDialog(
									null,
									"Someone might have borrowed the book!\n"
											+ "before this transaction have been completed.",
									"", JOptionPane.ERROR_MESSAGE);
				}
				bookList = BookDAO.searchBookForUser(currSearchString);
				bookSearch.getTableBookList().setModel(
						new BookListModel(bookList));

				bookSearch.setColumnRender(bookSearch.getTableBookList());
				if (bookList != null && !bookList.isEmpty())
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
				if (bookList != null && !bookList.isEmpty())
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
			DefaultListSelectionModel dlSelectionModel = (DefaultListSelectionModel) e
					.getSource();
			int tableRow = dlSelectionModel.getLeadSelectionIndex();
			currTableRowSelection = tableRow;

			if (tableRow < 0) {
				return;
			}

			if (tableRow < bookList.size()) {
				currTableRowSelection = tableRow;

				try {
					if (currentUser.getType().equals("Librarian")) {
						bookList = BookDAO.searchBook(bookSearch
								.getSearchWord());
					} else {
						bookList = BookDAO.searchBookForUser(bookSearch
								.getSearchWord());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				Book displayBook = bookList.get(tableRow);
				currISBN10 = displayBook.getIsbn10();
				currISBN13 = displayBook.getIsbn13();
				bookInfo.resetErrors();
				// System.out.println(displayBook.getIsbn());
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
				rowData.add(bookList.get(i).getIsbn10() + " / "
						+ bookList.get(i).getIsbn13());
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
			bookSearch.clearSearchField();
			try {
				searchBooks();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void reset() {
		bookInfo.setSaveButtonEnable(false);
		bookInfo.setDeleteButtonEnable(false);
		bookInfo.setBorrowButtonEnable(false);
		bookInfo.setReserveButtonEnable(false);
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
		if (bookList.size() != 0) {
			if (currentUser.getType().equals("User")) {
				if (bookList != null
						&& !bookList.isEmpty()
						&& !TransactionDAO.isBorrowedByUser(
								bookList.get(currTableRowSelection),
								currentUser)
						&& !TransactionDAO.isReservedByUser(
								bookList.get(currTableRowSelection),
								currentUser)) {
					if (TransactionDAO.getAvailableCopies(bookList
							.get(currTableRowSelection)) > 0) {
						bookInfo.setBorrowButtonEnable(true);
					} else {
						bookInfo.setReserveButtonEnable(true);
						bookInfo.getLblErrorMsg().makeError(
								"No available copies at this time");
					}
				} else if (bookList != null && !bookList.isEmpty()) {
					bookInfo.getLblErrorMsg()
							.makeError(
									"You already have a pending transaction for the following book: ");
				}
			}

			if (currentUser.getType().equals("Librarian")) {
				bookInfo.setSaveButtonEnable(true);
				int availableCopy = TransactionDAO.getAvailableCopies(bookList
						.get(tableRow));
				if (bookList.get(tableRow).getCopies() == 0
						|| bookList.get(tableRow).getCopies() != availableCopy) {
					bookInfo.setDeleteButtonEnable(false);
				} else
					bookInfo.setDeleteButtonEnable(true);
			}
		}
	}

	public BookSearchPanel getBookSearch() {
		return bookSearch;
	}

	public BookInfoPanel getBookInfo() {
		return bookInfo;
	}

	class IsbnCheckFocusListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent isbn) {
			MyTextField stringIsbn = (MyTextField) isbn.getSource();
			String bookIsbn = stringIsbn.getText();
			int flag = 0;

			if (!IsbnUtil.isIsbnValid(bookIsbn)) {
				if (stringIsbn.getName() == "isbnTextField") {
					addBook.getLblErrorMsg().makeError("ISBN is not correct.");
				} else {
					bookInfo.getLblErrorMsg().makeError("ISBN is not correct.");
				}
				stringIsbn.hasError(true);
			} else {
				flag = 1;
				if (stringIsbn.getName() == "isbnTextField") {
					addBook.getLblErrorMsg().clear();
				} else {
					bookInfo.getLblErrorMsg().clear();
				}
				stringIsbn.hasError(false);
			}

			if (flag == 1) {
				try {
					if (BookDAO.isIsbnExisting(bookIsbn)
							&& (currISBN10 == bookIsbn || currISBN13 == bookIsbn)) {
						if (stringIsbn.getName() == "isbnTextField") {
							addBook.getLblErrorMsg().makeError(
									"ISBN already exist.");
						} else {
							bookInfo.getLblErrorMsg().makeError(
									"ISBN already exist.");
						}
						stringIsbn.hasError(true);
					} else {
						if (stringIsbn.getName() == "isbnTextField") {
							addBook.getLblErrorMsg().clear();
						} else {
							bookInfo.getLblErrorMsg().clear();
							if (bookIsbn.length() == 10) {
								bookInfo.setIsbn13(
										IsbnUtil.convert(bookIsbn));
							} else {
								bookInfo.setIsbn10(IsbnUtil.convert(bookIsbn));
							}
						}
						stringIsbn.hasError(false);
					}
				} catch (Exception e) {
					CrashHandler.handle(e);
				}
			}
		}

	}

	class TitleCheckFocusListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent title) {
			MyTextField titleField = (MyTextField) title.getSource();
			if (titleField.getText().length() == 0) {
				if (titleField.getName() == "addTitleTextField") {
					addBook.getLblErrorMsg().makeError(
							"Title field cannot be empty.");
				} else {
					bookInfo.getLblErrorMsg().makeError(
							"Title field cannot be empty.");
				}
				titleField.hasError(true);
			} else {
				if (titleField.getName() == "addTitleTextField") {
					addBook.getLblErrorMsg().clear();
				} else {
					bookInfo.getLblErrorMsg().clear();
				}
				titleField.hasError(false);
			}
		}
	}

	class AuthorCheckFocusListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent author) {
			MyTextField authorField = (MyTextField) author.getSource();
			if (authorField.getText().length() == 0) {
				if (authorField.getName() == "addAuthorTextField") {
					addBook.getLblErrorMsg().makeError(
							"Author field cannot be empty.");
				} else {
					bookInfo.getLblErrorMsg().makeError(
							"Author field cannot be empty.");
				}
				authorField.hasError(true);
			} else {
				if (authorField.getName() == "addAuthorTextField") {
					addBook.getLblErrorMsg().clear();
				} else {
					bookInfo.getLblErrorMsg().clear();
				}
				authorField.hasError(false);
			}
		}
	}

	class YearCheckFocusListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent year) {
			MyTextField yearField = (MyTextField) year.getSource();
			if (yearField.getText().length() > 0
					&& !yearField.getText().matches(
							Constants.YEAR_PUBLISH_FORMAT)) {
				if (yearField.getName() == "addYearPublishTextField") {
					addBook.getLblErrorMsg().makeError(
							"Year field is not correct.");
				} else {
					bookInfo.getLblErrorMsg().makeError(
							"Year field is not correct.");
				}
				yearField.hasError(true);
			} else {
				if (yearField.getName() == "addYearPublishTextField") {
					addBook.getLblErrorMsg().clear();
				} else {
					bookInfo.getLblErrorMsg().clear();
				}
				yearField.hasError(false);
			}
		}
	}
}
