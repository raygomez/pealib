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

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import models.Book;
import models.BorrowTransaction;
import models.ReserveTransaction;
import models.TransactionDAO;
import models.User;

import views.InOutTabbedPane;
import views.IncomingPanel;
import views.OutgoingPanel;
//# remove these
import utilities.Connector;
import utilities.Constants;

public class TransactionController {
	private boolean isIncoming = true; // isOutgoing = false
	private TableModel bookResultsModel;
	private String[] tableHeader;
	private String[] incomingTableHeader = { "ISBN", "Title", "Author",
			"Username", "Date Borrowed" };
	private String[] outgoingTableHeader = { "ISBN", "Title", "Author",
			"Username", "Date Requested" };
	private ArrayList<BorrowTransaction> searchResults;
	private int selectedRow;

	private InOutTabbedPane tabbedPane;
	private IncomingPanel inPanel;
	private OutgoingPanel outPanel;

	// # remove this
	public static void main(String[] args) {
		new Connector(Constants.TEST_CONFIG);

		TransactionController librarianTransactions = new TransactionController();

		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.setVisible(true);
		testFrame.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		testFrame.setBounds(0, 0, screenSize.width, screenSize.height * 3 / 4);
		testFrame.setContentPane(librarianTransactions.getTabbedPane());

		try {
			new TransactionController();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * LIBRARIAN Book Transactions
	 */
	public TransactionController() {
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
				new BookTransactionSearchClearListener(),
				new BookTransactionSearchKeyListener(),
				new BookTransactionResultsMouseListener());
		inPanel.getSearchPanel().setEventListeners(
				new BookTransactionSearchSubmitListener(),
				new BookTransactionSearchClearListener(),
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
			searchBookTransaction();
		}
	}

	/*
	 * Outgoing Transactions
	 */
	class OutgoingBooksGrantListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			grantBorrowRequest();
			searchBookTransaction();
		}
	}

	class OutgoingBooksDenyListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			denyBorrowRequest();
			searchBookTransaction();
		}
	}

	private void grantBorrowRequest() {
		int affectedRows = 0;
		try {
			affectedRows = TransactionDAO
					.borrowBook(getBookTransactionDetails());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (affectedRows != 1) {
			// # error! do something
		}
		outPanel.getGrantButton().setEnabled(false);
		outPanel.getDenyButton().setEnabled(false);
	}

	private void denyBorrowRequest() {
		try {
			TransactionDAO.denyBookRequest(getBookTransactionDetails());
		} catch (Exception e) {
			e.printStackTrace();
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
			returnBook();
			searchBookTransaction();
		}
	}

	private void returnBook() {
		try {
			TransactionDAO.returnBook(getBookTransactionDetails());

			/* check if the book to be returned is reserved by other users */
			Book returnedBook = getBookTransactionDetails().getBook();
			if (TransactionDAO.isBookReservedbyOtherUsers(returnedBook)) {
				/* get the first user in queue */
				User nextUser = TransactionDAO.getNextUser(returnedBook);
				/* create borrow transaction */
				TransactionDAO.requestBook(getBookTransactionDetails()
						.getBook(), nextUser);
				/* delete reservation transaction */
				ReserveTransaction nextUserReserveTransaction = TransactionDAO
						.getReserveTransaction(nextUser, returnedBook);
				TransactionDAO.cancelReservation(nextUserReserveTransaction);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		inPanel.getBtnReturn().setEnabled(false);
		inPanel.getLblDaysOverdue().setText("");
	}

	private BorrowTransaction getBookTransactionDetails() {
		return searchResults.get(selectedRow);
	}

	/*
	 * Book Search and Results Table (for Borrowed and Pending books)
	 */
	class BookTransactionSearchSubmitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			searchBookTransaction();
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

	class BookTransactionSearchClearListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (isIncoming) {
				inPanel.getSearchPanel().getTxtfldSearch().setText("");
				inPanel.getBtnReturn().setEnabled(false);
				inPanel.getLblDaysOverdue().setText("");
				inPanel.getLblDaysOverdue().setText("");
			} else {
				outPanel.getSearchPanel().getTxtfldSearch().setText("");
				outPanel.getGrantButton().setEnabled(false);
				outPanel.getDenyButton().setEnabled(false);
			}
			searchBookTransaction();
		}
	}

	class BookTransactionSearchKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			final int enterKey = 10;
			int userKey = e.getKeyCode();

			if (userKey == enterKey) {
				searchBookTransaction();
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

		@Override
		public void keyTyped(KeyEvent e) {
		}
	}

	class BookTransactionResultsMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			setSelectedItem();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}

	private void searchBookTransaction() {
		try {
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
							objData = searchResults.get(row).getUser()
									.getUserName();
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
						default:
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
						.setText(
								"Total Matches: "
										+ bookResultsModel.getRowCount());
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
						.setText(
								"Total Matches: "
										+ bookResultsModel.getRowCount());
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setSelectedItem() {
		if (isIncoming) {
			selectedRow = inPanel.getSearchPanel().getTblResults()
					.getSelectedRow();

			if (selectedRow >= 0) {
				inPanel.getBtnReturn().setEnabled(true);
				getBookTransactionDetails();
				try {
					int daysOverdue = TransactionDAO.getDaysOverdue(
							getBookTransactionDetails().getBook(),
							getBookTransactionDetails().getUser());
					inPanel.getLblDaysOverdue().setText(
							"Days Overdue: " + daysOverdue);
					if (daysOverdue > 0) {
						inPanel.getLblDaysOverdue().setForeground(Color.RED);
					} else {
						inPanel.getLblDaysOverdue().setForeground(Color.BLACK);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			selectedRow = outPanel.getSearchPanel().getTblResults()
					.getSelectedRow();

			if (selectedRow >= 0) {
				outPanel.getGrantButton().setEnabled(true);
				outPanel.getDenyButton().setEnabled(true);
			}
		}
	}
}
