package controllers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
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
import views.InOutTabbedPane;
import views.IncomingPanel;
import views.OutgoingPanel;
//# remove these
import utilities.Connector;
import utilities.Constants;
import utilities.CrashHandler;

public class TransactionController {
	private boolean isIncoming = true; // isOutgoing = false
	private TableModel bookResultsModel;
	private String[] tableHeader;
	private ArrayList<BorrowTransaction> searchResults;
	private ArrayList<BorrowTransaction> selectedBookTransactions;
	private int[] selectedRows;

	private InOutTabbedPane tabbedPane;
	private IncomingPanel inPanel;
	private OutgoingPanel outPanel;

	// # remove this
	public static void main(String[] args) throws Exception {
		new Connector(Constants.APP_CONFIG);

		TransactionController librarianTransactions = new TransactionController();

		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.setVisible(true);
		testFrame.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		testFrame.setBounds(0, 0, screenSize.width * 3 / 4,
				screenSize.height * 3 / 4);
		testFrame.setContentPane(librarianTransactions.getTabbedPane());

		new TransactionController();
	}

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
				new BookTransactionResultsMouseListener());
		inPanel.getSearchPanel().setEventListeners(
				new BookTransactionSearchSubmitListener(),
				new BookTransactionSearchKeyListener(),
				new BookTransactionResultsMouseListener());

		/* Default Operation */
		searchBookTransaction();
	}

	public InOutTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	class TabChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			if (tabbedPane.getSelectedIndex() == 0) {
				isIncoming = true;
				inPanel.getBtnReturn().setEnabled(false);
				inPanel.getLblDaysOverdue().setText("");
			} else if (tabbedPane.getSelectedIndex() == 1) {
				isIncoming = false;
				outPanel.getGrantButton().setEnabled(false);
				outPanel.getDenyButton().setEnabled(false);
			}
			System.out.println(tabbedPane.getSelectedIndex());
			try {
				searchBookTransaction();
			} catch (Exception e) {
				CrashHandler.handle();
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
				CrashHandler.handle();
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
				CrashHandler.handle();
			}
		}
	}

	private void grantBorrowRequest() throws Exception {
		System.out.println("number of rows: " + selectedRows.length);
		for (int i = 0; i < selectedRows.length; i++) {
			TransactionDAO.borrowBook(selectedBookTransactions.get(i));
			System.out.println(">" + selectedRows[i] + ": Grant: " + selectedBookTransactions.get(i).getBook().getTitle());
		}
		outPanel.getGrantButton().setEnabled(false);
		outPanel.getDenyButton().setEnabled(false);
	}

	private void denyBorrowRequest() throws Exception {
		System.out.println("number of rows: " + selectedRows.length);
		int passCounter = 0;
		for (int i = 0; i < selectedRows.length; i++) {
			TransactionDAO.denyBookRequest(selectedBookTransactions.get(i));

			/* check if the book to be denied is reserved by other users */
			Book deniedBook = selectedBookTransactions.get(i).getBook();
			if (TransactionDAO.isBookReservedByOtherUsers(deniedBook)) {
				TransactionDAO.passToNextUser(deniedBook);
				passCounter++;
			}
			System.out.println(">" + selectedRows[i] + ": Deny: " + selectedBookTransactions.get(i).getBook().getTitle());
		}
		if (passCounter == 0) {
			JOptionPane.showMessageDialog(tabbedPane,
					"Refused to lend " + selectedRows.length + " book(s).",
					"Borrow Request Denied", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(tabbedPane,
					"<html>Refused to lend " + selectedRows.length + " book(s).<br>"
					+ passCounter + " book(s) have pending reservations.<br>"
					+ "See Outgoing tab for details.",
					"Borrow Request Denied", JOptionPane.INFORMATION_MESSAGE);
		}
		outPanel.getGrantButton().setEnabled(false);
		outPanel.getDenyButton().setEnabled(false);
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
				CrashHandler.handle();
			}
		}
	}

	private void returnBook() throws Exception {
		System.out.println("number of rows: " + selectedRows.length);
		int passCounter = 0;
		for (int i = 0; i < selectedRows.length; i++) {
			TransactionDAO.returnBook(selectedBookTransactions.get(i));

			/* check if the book to be returned is reserved by other users */
			Book returnedBook = selectedBookTransactions.get(i).getBook();
			if (TransactionDAO.isBookReservedByOtherUsers(returnedBook)) {
				TransactionDAO.passToNextUser(returnedBook);
				passCounter++;
			}
			System.out.println(">" + selectedRows[i] + ": Return: " + selectedBookTransactions.get(i).getBook().getTitle());
		}
		if (passCounter == 0) {
			JOptionPane.showMessageDialog(tabbedPane,
					"Successfully returned " + selectedRows.length + " book(s).",
					"Borrowed Book Returned",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(tabbedPane,
					"<html>Successfully returned " + selectedRows.length + " book(s).<br>"
					+ passCounter + " book(s) have pending reservations.<br>"
					+ "See Outgoing tab for details.",
					"Borrowed Book Returned", JOptionPane.INFORMATION_MESSAGE);
		}
		inPanel.getBtnReturn().setEnabled(false);
		inPanel.getLblDaysOverdue().setText("");
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
				CrashHandler.handle();
			}
			if (isIncoming) {
				inPanel.getBtnReturn().setEnabled(false);
				inPanel.getLblDaysOverdue().setText("");
				inPanel.getLblDaysOverdue().setText("");
			} else {
				outPanel.getGrantButton().setEnabled(false);
				outPanel.getDenyButton().setEnabled(false);
			}
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
					CrashHandler.handle();
				}
				if (isIncoming) {
					inPanel.getBtnReturn().setEnabled(false);
					inPanel.getLblDaysOverdue().setText("");
					inPanel.getLblDaysOverdue().setText("");
				} else {
					outPanel.getGrantButton().setEnabled(false);
					outPanel.getDenyButton().setEnabled(false);
				}
			}
		});

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() != KeyEvent.VK_ENTER) {
				if (timer.isRunning()) {
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
				CrashHandler.handle();
			}
		}
	}

	private void searchBookTransaction() throws Exception {
		String[] incomingTableHeader = { "ISBN", "Title", "Author",
				"Username", "Date Borrowed" };
		String[] outgoingTableHeader = { "ISBN", "Title", "Author",
				"Username", "Date Requested" };

		String keyword;
		if (isIncoming) {
			keyword = inPanel.getSearchPanel().getTxtfldSearch().getText();
			if (keyword.isEmpty()) {
				keyword = "*";
			}
			searchResults = TransactionDAO.searchIncomingBook(keyword);
			tableHeader = incomingTableHeader;
		} else {
			keyword = outPanel.getSearchPanel().getTxtfldSearch().getText();
			if (keyword.isEmpty()) {
				keyword = "*";
			}
			searchResults = TransactionDAO.searchOutgoingBook(keyword);
			tableHeader = outgoingTableHeader;
		}

		if (searchResults.size() != 0) {
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
					case 0:
						objData = searchResults.get(row).getBook()
								.getIsbn();
						break;
					case 1:
						objData = searchResults.get(row).getBook()
								.getTitle();
						break;
					case 2:
						objData = searchResults.get(row).getBook()
								.getAuthor();
						break;
					case 3:
						String user = searchResults.get(row).getUser()
						.getUserName() + " (" + searchResults.get(row)
						.getUser().getFirstName() + " " + searchResults
						.get(row).getUser().getLastName() + ")";
						objData = user;
						break;
					case 4:
						if (isIncoming) {
							objData = searchResults.get(row)
									.getDateBorrowed();
						} else {
							objData = searchResults.get(row)
									.getDateRequested();
						}
						break;
					}
					return objData;
				}
			};
		} else {
			String[][] emptyTable = {};
			bookResultsModel = new DefaultTableModel(emptyTable,
					tableHeader) {
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
		if (isIncoming) {
			inPanel.getSearchPanel().getTblResults()
					.setModel(bookResultsModel);
			inPanel.getSearchPanel()
					.getLblTotal()
					.setText("Total Matches: " + bookResultsModel.getRowCount());
			inPanel.getSearchPanel().getTblResults().getColumnModel()
					.getColumn(0).setPreferredWidth(20);
			inPanel.getSearchPanel().getTblResults().getColumnModel()
					.getColumn(1).setPreferredWidth(200);
			inPanel.getSearchPanel().getTblResults().getColumnModel()
					.getColumn(2).setPreferredWidth(70);
			inPanel.getSearchPanel().getTblResults().getColumnModel()
					.getColumn(3).setPreferredWidth(30);
			inPanel.getSearchPanel().getTblResults().getColumnModel()
					.getColumn(4).setPreferredWidth(20);
			inPanel.getSearchPanel().repaint();
		} else {
			outPanel.getSearchPanel().getTblResults()
					.setModel(bookResultsModel);
			outPanel.getSearchPanel()
					.getLblTotal()
					.setText("Total Matches: " + bookResultsModel.getRowCount());
			outPanel.getSearchPanel().getTblResults().getColumnModel()
					.getColumn(0).setPreferredWidth(20);
			outPanel.getSearchPanel().getTblResults().getColumnModel()
					.getColumn(1).setPreferredWidth(200);
			outPanel.getSearchPanel().getTblResults().getColumnModel()
					.getColumn(2).setPreferredWidth(70);
			outPanel.getSearchPanel().getTblResults().getColumnModel()
					.getColumn(3).setPreferredWidth(30);
			outPanel.getSearchPanel().getTblResults().getColumnModel()
					.getColumn(4).setPreferredWidth(20);
			outPanel.getSearchPanel().repaint();
		}
	}

	private void setSelectedItem() throws Exception {
		if (isIncoming) {
			selectedRows = inPanel.getSearchPanel().getTblResults().getSelectedRows();
			
			if (selectedRows.length != 0) {
				inPanel.getBtnReturn().setEnabled(true);
				getBookTransactionDetails();
				int daysOverdue = TransactionDAO.getDaysOverdue(
						selectedBookTransactions.get(selectedRows.length - 1));
				inPanel.getLblDaysOverdue().setText(
						"Days Overdue: " + daysOverdue);
				if (daysOverdue > 0) {
					inPanel.getLblDaysOverdue().setForeground(Color.RED);
				} else {
					inPanel.getLblDaysOverdue().setForeground(Color.BLACK);
				}
			}
		} else {
			selectedRows = outPanel.getSearchPanel().getTblResults().getSelectedRows();
			getBookTransactionDetails();

			if (selectedRows.length != 0) {
				outPanel.getGrantButton().setEnabled(true);
				outPanel.getDenyButton().setEnabled(true);
			}
		}
	}
}
