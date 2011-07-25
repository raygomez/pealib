package controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

import models.BorrowTransaction;
import models.ReserveTransaction;
import models.TransactionDAO;
import models.User;
import models.UserDAO;

import org.joda.time.DateTime;

import utilities.Connector;
import utilities.Constants;
import views.ELibTabbedPanel;

public class ELibController {

	// TODO temporary User
	private User user;

	private ELibTabbedPanel tabpane;
	private int tab;

	/*
	 * ..TODO For visual testing purposes only
	 */
	public static void main(String[] args) throws Exception {
		new Connector(Constants.TEST_CONFIG);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0, screenSize.width, screenSize.height);

		User user = UserDAO.getUserById(2);
		frame.setContentPane(new ELibController(user).tabpane);

		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	ELibController(User user) {
		setUser(user);
		tabpane = new ELibTabbedPanel();
		tabpane.addChangeTabListener(new TabChangeListener());
		ELibTableModel model = new ELibTableModel(0);
		tabpane.setTableModel(0, model);
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	class TabChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			tab = tabpane.getSelectedTab();
			ELibTableModel model = new ELibTableModel(tab);
			tabpane.setTableModel(tab, model);
		}
	}

	class ELibTableModel extends AbstractTableModel {
		/**
		 * TableModel for ELib Tabs
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<String> columns;
		private ArrayList<ArrayList<String>> tableData;
		private ArrayList<BorrowTransaction> bookData;
		private ArrayList<ReserveTransaction> bookDataReserve;

		public ELibTableModel(int tab) {
			switch (tab) {
			case 0:
				requestData();
				break;

			case 1:
				reserveData();
				break;

			case 2:
				onloanData();
				break;

			case 3:
				historyData();
				break;
			}
		}

		private void requestData() {
			columns = new ArrayList<String>();
			columns.add("ISBN");
			columns.add("Title");
			columns.add("Author");
			columns.add("Date Requested");

			tableData = new ArrayList<ArrayList<String>>();
			try {
				bookData = TransactionDAO.getRequestedBooks(getUser());
			} catch (Exception e) {
				System.out.println("ELibController: requestData: " + e);
			}

			if (bookData.size() != 0) {
				for (BorrowTransaction i : bookData) {
					ArrayList<String> rowData = new ArrayList<String>();
					rowData.add(i.getBook().getIsbn());
					rowData.add(i.getBook().getTitle());
					rowData.add(i.getBook().getAuthor());
					rowData.add(i.getDateRequested().toString());
					tableData.add(rowData);
				}
			}
		}

		private void reserveData() {
			columns = new ArrayList<String>();
			columns.add("ISBN");
			columns.add("Title");
			columns.add("Author");
			columns.add("Date Reserved");
			columns.add("Queue Number");

			tableData = new ArrayList<ArrayList<String>>();
			try {
				bookDataReserve = TransactionDAO.getReservedBooks(getUser());

				if (bookDataReserve.size() != 0) {
					for (ReserveTransaction i : bookDataReserve) {
						ArrayList<String> rowData = new ArrayList<String>(5);
						rowData.add(i.getBook().getIsbn());
						rowData.add(i.getBook().getTitle());
						rowData.add(i.getBook().getAuthor());
						rowData.add(i.getDateReserved().toString());
						rowData.add(""
								+ TransactionDAO.getQueueInReservation(
										i.getBook(), i.getUser()));
						tableData.add(rowData);
					}
				}
			} catch (Exception e) {
				System.out.println("ELibController: reserveData: " + e);
			}
		}

		private void onloanData() {
			columns = new ArrayList<String>();
			columns.add("ISBN");
			columns.add("Title");
			columns.add("Author");
			columns.add("Date Borrowed");
			columns.add("Date Due");

			tableData = new ArrayList<ArrayList<String>>();
			try {
				bookData = TransactionDAO.getOnLoanBooks(getUser());
			} catch (Exception e) {
				System.out.println("ELibController: onloanData: " + e);
			}

			if (bookData.size() != 0) {
				for (BorrowTransaction i : bookData) {
					DateTime dueDate = new DateTime(i.getDateBorrowed()
							.getTime()).plusDays(14);
					ArrayList<String> rowData = new ArrayList<String>();
					rowData.add(i.getBook().getIsbn());
					rowData.add(i.getBook().getTitle());
					rowData.add(i.getBook().getAuthor());
					rowData.add(i.getDateBorrowed().toString());
					rowData.add(dueDate.toString("y-MM-dd"));
					tableData.add(rowData);
				}
			}
		}

		private void historyData() {
			columns = new ArrayList<String>();
			columns.add("ISBN");
			columns.add("Title");
			columns.add("Author");
			columns.add("Date Borrowed");
			columns.add("Date Returned");

			tableData = new ArrayList<ArrayList<String>>();
			try {
				bookData = TransactionDAO.getHistory(getUser());
			} catch (Exception e) {
				System.out.println("ELibController: historyData: " + e);
			}

			if (bookData.size() != 0) {
				for (BorrowTransaction i : bookData) {
					ArrayList<String> rowData = new ArrayList<String>();
					rowData.add(i.getBook().getIsbn());
					rowData.add(i.getBook().getTitle());
					rowData.add(i.getBook().getAuthor());
					rowData.add(i.getDateBorrowed().toString());
					rowData.add(i.getDateReturned().toString());
					tableData.add(rowData);
				}
			}
		}

		public String getColumnName(int col) {
			return columns.get(col);
		}

		@Override
		public int getColumnCount() {
			return columns.size();
		}

		@Override
		public int getRowCount() {
			return tableData.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			return tableData.get(row).get(col);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	}
}
