package controllers;

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

import models.BorrowTransaction;
import models.TransactionDAO;

import views.InOutTabbedPane;
import views.IncomingPanel;
import views.OutgoingPanel;
//# remove these
import utilities.Connector;
import utilities.Constants;


public class TransactionController {
	private boolean isIncoming = true; //isOutgoing = false
	private TableModel bookResultsModel;
	private String[] tableHeader;
	private String[] incomingTableHeader = {"ISBN", "Title", "Author", "Username", "Date Borrowed"};
	private String[] outgoingTableHeader = {"ISBN", "Title", "Author", "Username", "Date Requested"};
	private ArrayList<BorrowTransaction> searchResults;
	private int selectedRow; 
	
	private static InOutTabbedPane tabbedPane;
	private IncomingPanel inPanel;
	private OutgoingPanel outPanel;


	public static void main(String[] args) {
		//# remove this
		new Connector(Constants.TEST_CONFIG);
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.setVisible(true);
		testFrame.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		testFrame.setBounds(0,0,screenSize.width, screenSize.height * 3/4);
		
		tabbedPane = new InOutTabbedPane();
		testFrame.setContentPane(tabbedPane);
		
		try {
			new TransactionController();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * LIBRARIAN Book Transactions
	 */
	public TransactionController() throws Exception {
		/* Load Event Listeners */
		/* InOutTabbedPane */
		tabbedPane.setEventListener(new TabChangeListener());
		/* OutgoingPanel */
		outPanel = tabbedPane.getOutgoingPanel();
		outPanel.setEventListener(new OutgoingBooksGrantListener(), new OutgoingBooksDenyListener());
		/* IncomingPanel */
		inPanel = tabbedPane.getIncomingPanel();
		inPanel.setEventListener(new IncomingBooksReturnListener());
		/* InOutBookSearchPanel */
		outPanel.getSearchPanel().setEventListeners(new BookTransactionSearchSubmitListener(), 
				new BookTransactionSearchClearListener(), new BookTransactionSearchKeyListener(),
				new BookTransactionResultsMouseListener());
		inPanel.getSearchPanel().setEventListeners(new BookTransactionSearchSubmitListener(), 
				new BookTransactionSearchClearListener(), new BookTransactionSearchKeyListener(),
				new BookTransactionResultsMouseListener());
		
		/* default operation for Incoming */
		searchBookTransaction();
	}
	
	class TabChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			if (tabbedPane.getSelectedIndex() == 0) {
				isIncoming = true;
			} else if (tabbedPane.getSelectedIndex() == 1) {
				isIncoming = false;
			} else { //-1
				//# error
				System.out.println(tabbedPane.getSelectedIndex());
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
			affectedRows = TransactionDAO.acceptBookRequest(getBookTransactionDetails());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (affectedRows != 1) {
			//# error! do something
		}
	}
	
	private void denyBorrowRequest() {
		try {
			TransactionDAO.denyBookRequest(getBookTransactionDetails());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		//# check if reserved by other users
		try {
			TransactionDAO.receiveBook(getBookTransactionDetails());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Return: " + getBookTransactionDetails().getBook().getTitle());
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
		}
	}
	
	class BookTransactionSearchClearListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (isIncoming) {
				inPanel.getSearchPanel().getTxtfldSearch().setText("");
			} else {
				outPanel.getSearchPanel().getTxtfldSearch().setText("");
			}
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
							objData = searchResults.get(row).getBook().getIsbn();
							break;
						case 1:
							objData = searchResults.get(row).getBook().getTitle();
							break;
						case 2:
							objData = searchResults.get(row).getBook().getAuthor();
							break;
						case 3:
							objData = searchResults.get(row).getUser().getUserName();
							break;
						case 4:
							if (isIncoming) {
								objData = searchResults.get(row).getDateBorrowed();
							} else {
								objData = searchResults.get(row).getDateRequested();
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
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
			}
			if (isIncoming) {
				inPanel.getSearchPanel().getTblResults().setModel(bookResultsModel);
				inPanel.getSearchPanel().getLblTotal().setText("Total Matches: " + bookResultsModel.getRowCount());
				inPanel.getSearchPanel().getTblResults().getColumnModel().getColumn(0).setPreferredWidth(20);
				inPanel.getSearchPanel().getTblResults().getColumnModel().getColumn(1).setPreferredWidth(200);
				inPanel.getSearchPanel().getTblResults().getColumnModel().getColumn(2).setPreferredWidth(70);
				inPanel.getSearchPanel().getTblResults().getColumnModel().getColumn(3).setPreferredWidth(30);
				inPanel.getSearchPanel().getTblResults().getColumnModel().getColumn(4).setPreferredWidth(20);
				
				inPanel.getSearchPanel().repaint();
			} else {
				outPanel.getSearchPanel().getTblResults().setModel(bookResultsModel);
				outPanel.getSearchPanel().getLblTotal().setText("Total Matches: " + bookResultsModel.getRowCount());
				outPanel.getSearchPanel().getTblResults().getColumnModel().getColumn(0).setPreferredWidth(20);
				outPanel.getSearchPanel().getTblResults().getColumnModel().getColumn(1).setPreferredWidth(200);
				outPanel.getSearchPanel().getTblResults().getColumnModel().getColumn(2).setPreferredWidth(70);
				outPanel.getSearchPanel().getTblResults().getColumnModel().getColumn(3).setPreferredWidth(30);
				outPanel.getSearchPanel().getTblResults().getColumnModel().getColumn(4).setPreferredWidth(20);
				outPanel.getSearchPanel().repaint();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setSelectedItem() {
		//# conflict with buttons and label messages
		if (isIncoming) {
			selectedRow = inPanel.getSearchPanel().getTblResults().getSelectedRow();
			
			if (selectedRow >= 0) {
				inPanel.getBtnReturn().setEnabled(true);
				getBookTransactionDetails();
				try {
					int daysOverdue = TransactionDAO.getDaysOverdue(getBookTransactionDetails().getBook(),
							getBookTransactionDetails().getUser());
					inPanel.getLblDaysOverdue().setText(daysOverdue + " days Overdue.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			selectedRow = outPanel.getSearchPanel().getTblResults().getSelectedRow();
			
			if (selectedRow >= 0) {
				outPanel.getBtnGrant().setEnabled(true);
				outPanel.getBtnDeny().setEnabled(true);
			}
		}
	}
}
