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

		User user = UserDAO.getUserById(1);
		frame.setContentPane(new ELibController(user).tabpane);

		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	ELibController(User user) {
		tabpane = new ELibTabbedPanel();
		tabpane.addChangeTabListener(new TabChangeListener());
		setUser(user);
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
		private ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();
		private ArrayList<BorrowTransaction> bookData = new ArrayList<BorrowTransaction>();
		private ArrayList<ReserveTransaction> bookDataReserve = new ArrayList<ReserveTransaction>();
		private int tab = 0;

		public ELibTableModel(int tab) {
			// TODO change if using another db
			new Connector("test.config");
			this.tab = tab;
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

			// TODO change if going to use another DB
			new Connector("test.config");
		}

		private void requestData() {
			columns = new ArrayList<String>();
			columns.add("ISBN");
			columns.add("Title");
			columns.add("Author");
			columns.add("Date Requested");

			try {
				// TODO get bookData from transaction dao
				bookData = TransactionDAO.getRequestedBooks(getUser());
			} catch (Exception e) {
				System.out.println("ELibController: requestData: " + e);
			}

			if (bookData != null) {
				for (BorrowTransaction i : bookData) {
					ArrayList<String> rowData = new ArrayList<String>();
					rowData.add(i.getBook().getIsbn());
					rowData.add(i.getBook().getTitle());
					rowData.add(i.getBook().getAuthor());
					String temp = "" + i.getDateRequested();
					rowData.add(temp);
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

			try {
				// TODO get bookData from transaction dao
				bookDataReserve = TransactionDAO.getReservedBooks(getUser());
			} catch (Exception e) {
				System.out.println("ELibController: reserveData: " + e);
			}

			if (bookDataReserve != null) {
				for (ReserveTransaction i : bookDataReserve) {
					ArrayList<String> rowData = new ArrayList<String>();
					// rowData.add(i.getUserName());
					// rowData.add(i.getFirstName()+" "+i.getLastName());
					tableData.add(rowData);
				}
			}
		}

		private void onloanData() {
			columns = new ArrayList<String>();
			columns.add("ISBN");
			columns.add("Title");
			columns.add("Author");
			columns.add("Date Borrowed");
			columns.add("Date Due");

			try {
				// TODO get bookData from transaction dao
				bookData = TransactionDAO.getOnLoanBooks(getUser());
			} catch (Exception e) {
				System.out.println("ELibController: onloanData: " + e);
			}

			if (bookData != null) {
				for (BorrowTransaction i : bookData) {
					ArrayList<String> rowData = new ArrayList<String>();
					// rowData.add(i.getUserName());
					// rowData.add(i.getFirstName()+" "+i.getLastName());
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

			try {
				// TODO get bookData from transaction dao
				bookData = TransactionDAO.getHistory(getUser());
			} catch (Exception e) {
				System.out.println("ELibController: historyData: " + e);
			}

			if (bookData != null) {
				for (BorrowTransaction i : bookData) {
					ArrayList<String> rowData = new ArrayList<String>();
					// rowData.add(i.getUserName());
					// rowData.add(i.getFirstName()+" "+i.getLastName());
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
