package controllers;

import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Callable;

//import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import models.Book;
import models.BorrowTransaction;
import models.TransactionDAO;
import pealib.PeaLibrary;
import utilities.Constants;
import utilities.CrashHandler;
import utilities.Strings;
import utilities.Task;
//import utilities.Connector;
import views.InOutTabbedPane;
import views.IncomingPanel;
import views.OutgoingPanel;

public class TransactionController {
	private boolean isIncoming = true; /* isIncoming = false -> Outgoing tab */
	private TableModel bookResultsModel;
	private String[] tableHeader;
	private ArrayList<BorrowTransaction> searchResults;
	private ArrayList<BorrowTransaction> selectedBookTransactions;
	private int[] selectedRows;
	private InOutTabbedPane tabbedPane;
	private IncomingPanel inPanel;
	private OutgoingPanel outPanel;
	private int passCounter;

//	public static void main(String[] args) throws Exception {
//		Connector.init(Constants.TEST_CONFIG);
//
//		TransactionController librarianTransactions = new TransactionController();
//
//		JFrame testFrame = new JFrame();
//		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		testFrame.setVisible(true);
//		testFrame.setResizable(false);
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		testFrame.setBounds(0, 0, screenSize.width * 3 / 4,
//				screenSize.height * 3 / 4);
//		testFrame.setContentPane(librarianTransactions.getTabbedPane());
//
//		new TransactionController();
//	}

	/*
	 * LIBRARIAN Book Transactions
	 */
	public TransactionController() throws Exception {
		/* Create UI */
		tabbedPane = new InOutTabbedPane();

		/* Load Event Listeners */
		/* InOutTabbedPane */
		tabbedPane.setEventListener(new TabChangeListener());
		/* OutgoingPanel */
		outPanel = tabbedPane.getOutgoingPanel();
		outPanel.setEventListener(new OutgoingBooksGrantListener(),
				new OutgoingBooksDenyListener());
		/* IncomingPanel */
		inPanel = tabbedPane.getIncomingPanel();
		inPanel.setEventListener(new IncomingBooksReturnListener());
		/* InOutBookSearchPanel */
		outPanel.getSearchPanel().setEventListeners(
				new BookTransactionSearchSubmitListener(),
				new BookTransactionSearchKeyListener(),
				new BookTransactionResultsMouseListener(),
				new BookTransactionResultsKeyListener());
		inPanel.getSearchPanel().setEventListeners(
				new BookTransactionSearchSubmitListener(),
				new BookTransactionSearchKeyListener(),
				new BookTransactionResultsMouseListener(),
				new BookTransactionResultsKeyListener());

		/* Default Operation */
		searchBookTransaction();
	}

	public InOutTabbedPane getTabbedPane() throws Exception {
		/* Added search operation so that table is refreshed whenever
		 * Book Transactions button is clicked in the Librarian side bar */
		searchBookTransaction();
		return tabbedPane;
	}

	class TabChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			isIncoming = (tabbedPane.getSelectedIndex() == 0);
			setButtonsStatus(false);
			try {
				searchBookTransaction();
			} catch (Exception e) {
				CrashHandler.handle(e);
			}
		}
	}

	/*
	 * Outgoing Transactions
	 */
	class OutgoingBooksGrantListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				grantBorrowRequest();
				searchBookTransaction();
			} catch (Exception e) {
				CrashHandler.handle(e);
			}
		}
	}

	class OutgoingBooksDenyListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				denyBorrowRequest();
				searchBookTransaction();
			} catch (Exception e) {
				CrashHandler.handle(e);
			}
		}
	}

	private void grantBorrowRequest() {
		Callable<Void> toDo = new Callable<Void>(){
			@Override
			public Void call() throws Exception {
				for (int i = 0; i < selectedRows.length; i++) {
					TransactionDAO.borrowBook(selectedBookTransactions.get(i));
				}
				return null;
			}
		};
		LoadingControl.init(new Task<Void, Object>(toDo), PeaLibrary.getMainFrame()).executeLoading();
			
		JOptionPane.showMessageDialog(tabbedPane, Strings.GRANT_BOOK_MESSAGE
				+ selectedRows.length + Strings.BOOK_S,
				Strings.GRANT_BOOK_TITLE, JOptionPane.INFORMATION_MESSAGE);
		setButtonsStatus(false);
	}

	private void denyBorrowRequest() throws Exception {
		Callable<Void> toDo = new Callable<Void>(){
			@Override
			public Void call() throws Exception {
				passCounter = 0;
				for (int i = 0; i < selectedRows.length; i++) {
					TransactionDAO.denyBookRequest(selectedBookTransactions.get(i));

					/* Check if the book to be denied is reserved by other users */
					Book deniedBook = selectedBookTransactions.get(i).getBook();
					if (true == TransactionDAO.isBookReservedByOtherUsers(deniedBook)) {
						TransactionDAO.passToNextUser(deniedBook);
						passCounter++;
					}
				}
				return null;
			}
		};
		LoadingControl.init(new Task<Void, Object>(toDo), PeaLibrary.getMainFrame()).executeLoading();
		
		if (0 == passCounter) {
			JOptionPane.showMessageDialog(tabbedPane, Strings.DENY_BOOK_MESSAGE
					+ selectedRows.length + Strings.BOOK_S,
					Strings.DENY_BOOK_TITLE, JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(tabbedPane, Strings.DENY_BOOK_MESSAGE
					+ selectedRows.length + Strings.BOOK_S + passCounter
					+ Strings.PENDING_RESERVATIONS, Strings.DENY_BOOK_TITLE,
					JOptionPane.INFORMATION_MESSAGE);
		}
		setButtonsStatus(false);
	}

	/*
	 * Incoming Transactions
	 */
	class IncomingBooksReturnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				returnBook();
				searchBookTransaction();
			} catch (Exception e) {
				CrashHandler.handle(e);
			}
		}
	}

	private void returnBook() throws Exception {
		Callable<Void> toDo = new Callable<Void>(){
			@Override
			public Void call() throws Exception {
				passCounter = 0;
				for (int i = 0; i < selectedRows.length; i++) {
					TransactionDAO.returnBook(selectedBookTransactions.get(i));

					/* Check if the book to be returned is reserved by other users */
					Book returnedBook = selectedBookTransactions.get(i).getBook();
					if (true == TransactionDAO.isBookReservedByOtherUsers(returnedBook)) {
						TransactionDAO.passToNextUser(returnedBook);
						passCounter++;
					}
				}
				return null;
			}
		};
		LoadingControl.init(new Task<Void, Object>(toDo), PeaLibrary.getMainFrame()).executeLoading();
		
		if (0 == passCounter) {
			JOptionPane.showMessageDialog(tabbedPane,
					Strings.RETURN_BOOK_MESSAGE	+ selectedRows.length
					+ Strings.BOOK_S, Strings.RETURN_BOOK_TITLE,
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(tabbedPane,
					Strings.RETURN_BOOK_MESSAGE	+ selectedRows.length
					+ Strings.BOOK_S + passCounter
					+ Strings.PENDING_RESERVATIONS, Strings.RETURN_BOOK_TITLE,
					JOptionPane.INFORMATION_MESSAGE);
		}
		setButtonsStatus(false);
	}

	private void getBookTransactionDetails() {
		selectedBookTransactions = new ArrayList<BorrowTransaction>();
		for (int i = 0; i < selectedRows.length; i++) {
			selectedBookTransactions.add(searchResults.get(selectedRows[i]));
		}
	}

	/*
	 * Book Search and Results Table (for Borrowed and Pending books)
	 */
	class BookTransactionSearchSubmitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				searchBookTransaction();
			} catch (Exception e) {
				CrashHandler.handle(e);
			}
			setButtonsStatus(false);
		}
	}

	class BookTransactionSearchKeyListener extends KeyAdapter {
		Timer timer = new Timer(Constants.TIMER_DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				timer.stop();
				try {
					searchBookTransaction();
				} catch (Exception e) {
					CrashHandler.handle(e);
				}
				setButtonsStatus(false);
			}
		});

		@Override
		public void keyReleased(KeyEvent key) {
			if (KeyEvent.VK_ENTER != key.getKeyCode()) {
				if (true == timer.isRunning()) {
					timer.restart();
				} else {
					timer.start();
				}
			}
		}
	}

	class BookTransactionResultsMouseListener extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent arg0) {
			try {
				setSelectedItem();
			} catch (Exception e) {
				CrashHandler.handle(e);
			}
		}
	}

	class BookTransactionResultsKeyListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent key) {
			if (KeyEvent.VK_SHIFT == key.getKeyCode()) {
				try {
					setSelectedItem();
				} catch (Exception e) {
					CrashHandler.handle(e);
				}
			}
		}
	}
	
	private void searchBookTransaction() throws Exception {
		Callable<Void> toDo = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				String keyword;
				if (true == isIncoming) {
					inPanel.getSearchPanel().getTblResults().setVisible(false);
					keyword = inPanel.getSearchPanel().getTxtfldSearch().getText();
					tableHeader = Strings.INCOMING_TABLE_HEADER;
					searchResults = TransactionDAO.searchIncomingBook(keyword);
				} else {
					outPanel.getSearchPanel().getTblResults().setVisible(false);
					keyword = outPanel.getSearchPanel().getTxtfldSearch().getText();
					tableHeader = Strings.OUTGOING_TABLE_HEADER;
					searchResults = TransactionDAO.searchOutgoingBook(keyword);
				}
				return null;
			}
		};
		
		Callable<Void> toDoAfter = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				if (0 != searchResults.size()) {
					bookResultsModel = new AbstractTableModel() {
						private static final long serialVersionUID = 1L;

						@Override
						public int getColumnCount() {
							return tableHeader.length;
						}

						public String getColumnName(int column) {
							return tableHeader[column];
						}

						@Override
						public int getRowCount() {
							return searchResults.size();
						}

						public boolean isCellEditable(int row, int column) {
							return false;
						}

						@Override
						public Object getValueAt(int row, int column) {
							Object objData = null;
							switch (column) {
							case 0: /* ISBN */
								objData = searchResults.get(row).getBook().getIsbn13();
								break;
							case 1: /* Title */
								objData = searchResults.get(row).getBook().getTitle();
								break;
							case 2: /* Author */
								objData = searchResults.get(row).getBook().getAuthor();
								break;
							case 3: /* User Name (First Name + Last Name) */
								String user = searchResults.get(row).getUser()
										.getUserName()
										+ " ("
										+ searchResults.get(row).getUser()
										.getFirstName()
										+ " "
										+ searchResults.get(row).getUser()
										.getLastName() + ")";
								objData = user;
								break;
							case 4: /* Date Borrowed [Incoming tab] / Date Requested [Outgoing tab] */
								if (true == isIncoming) {
									objData = searchResults.get(row).getDateBorrowed();
								} else {
									objData = searchResults.get(row).getDateRequested();
								}
								break;
							case 5: /* Date Due [Incoming tab] */
								if (true == isIncoming) {
									Date dateBorrowed = searchResults.get(row).getDateBorrowed();
									Calendar dueDate = Calendar.getInstance();
									dueDate.setTime(dateBorrowed);
									dueDate.add(Calendar.WEEK_OF_MONTH, 2);
									objData = new Date(dueDate.getTime().getTime());
								}
								break;
							default:
								break;
							}
							return objData;
						}
					};
				} else {
					String[][] emptyTable = {};
					bookResultsModel = new DefaultTableModel(emptyTable, tableHeader) {
						private static final long serialVersionUID = 1L;
					};
				}
				
				if (true == isIncoming) {
					inPanel.getSearchPanel().getTblResults().setModel(bookResultsModel);
					inPanel.getSearchPanel().getLblTotal()
							.setText(Strings.TOTAL_MATCHES + bookResultsModel.getRowCount());

					int[] incomingTableColumnSizes = { 20, 200, 70, 30, 20, 20 };
					for (int i = 0; i < incomingTableColumnSizes.length; i++) {
						inPanel.getSearchPanel().getTblResults().getColumnModel()
								.getColumn(i).setPreferredWidth(incomingTableColumnSizes[i]);
					}
					inPanel.getSearchPanel().setColumnRender(incomingTableColumnSizes.length);
					inPanel.getSearchPanel().repaint();
				} else {
					outPanel.getSearchPanel().getTblResults()
							.setModel(bookResultsModel);
					outPanel.getSearchPanel().getLblTotal()
							.setText(Strings.TOTAL_MATCHES + bookResultsModel.getRowCount());

					int[] outgoingTableColumnSizes = { 20, 200, 70, 30, 20 };
					for (int i = 0; i < outgoingTableColumnSizes.length; i++) {
						outPanel.getSearchPanel().getTblResults().getColumnModel()
								.getColumn(i).setPreferredWidth(outgoingTableColumnSizes[i]);
					}
					outPanel.getSearchPanel().setColumnRender(outgoingTableColumnSizes.length);
					outPanel.getSearchPanel().repaint();
				}
				
				inPanel.getSearchPanel().getTblResults().setVisible(true);
				outPanel.getSearchPanel().getTblResults().setVisible(true);
				return null;
			}
		};
		LoadingControl.init(new Task<Void, Object>(toDo, toDoAfter), PeaLibrary.getMainFrame()).executeLoading();
	}

	private void setSelectedItem() throws Exception {
		selectedRows = null;
		if (true == isIncoming) {
			selectedRows = inPanel.getSearchPanel().getTblResults()
					.getSelectedRows();

			if (0 != selectedRows.length) {
				getBookTransactionDetails();
				int daysOverdue = TransactionDAO
						.getDaysOverdue(selectedBookTransactions
								.get(selectedRows.length - 1));
				if (daysOverdue < 0) {
					daysOverdue = 0;
				}
				inPanel.getLblDaysOverdue().setText(
						Strings.DAYS_OVERDUE + daysOverdue);
				if (daysOverdue > 0) {
					inPanel.getLblDaysOverdue().setForeground(Color.RED);
				} else {
					inPanel.getLblDaysOverdue().setForeground(Color.BLACK);
				}
			}
		} else {
			selectedRows = outPanel.getSearchPanel().getTblResults()
					.getSelectedRows();
			getBookTransactionDetails();
		}
		setButtonsStatus(true);
	}

	private void setButtonsStatus(boolean status) {
		if (true == isIncoming) {
			inPanel.getBtnReturn().setEnabled(status);
			if (false == status) {
				inPanel.getLblDaysOverdue().setText("");
			}
		} else {
			outPanel.getGrantButton().setEnabled(status);
			outPanel.getDenyButton().setEnabled(status);
		}
	}
}
