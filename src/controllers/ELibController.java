package controllers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

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

	private User user;
	private ELibTabbedPanel tabpane;
	private int tab;
	private ArrayList<BorrowTransaction> bookData;
	private ArrayList<ReserveTransaction> bookDataReserve;

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
		frame.setContentPane(new ELibController(user).getTabpane());

		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public ELibController(User user) {
		setUser(user);
		setTabpane(new ELibTabbedPanel());
		getTabpane().addChangeTabListener(new TabChangeListener());
		ELibTableModel model = new ELibTableModel(0);
		getTabpane().setTableModel(0, model);
		getTabpane().setCellRenderer(0, new ButtonRenderer());
		getTabpane().setCellEditor(0, new ButtonEditor());
	}

	/**
	 * @return the tabpane
	 */
	public ELibTabbedPanel getTabpane() {
		return tabpane;
	}

	/**
	 * @param tabpane
	 *            the tabpane to set
	 */
	public void setTabpane(ELibTabbedPanel tabpane) {
		this.tabpane = tabpane;
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
			tab = getTabpane().getSelectedTab();
			ELibTableModel model = new ELibTableModel(tab);
			getTabpane().setTableModel(tab, model);
			getTabpane().setCellRenderer(tab, new ButtonRenderer());
			getTabpane().setCellEditor(tab, new ButtonEditor());
		}
	}

	class ELibTableModel extends AbstractTableModel {
		/**
		 * TableModel for ELib Tabs
		 */
		private static final long serialVersionUID = 1L;
		private ArrayList<String> columns;
		private ArrayList<ArrayList<Object>> tableData;

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
			columns.add("Cancel");

			tableData = new ArrayList<ArrayList<Object>>();
			try {
				bookData = TransactionDAO.getRequestedBooks(getUser());
			} catch (Exception e) {
				System.out.println("ELibController: requestData: " + e);
			}

			if (bookData.size() != 0) {
				for (BorrowTransaction i : bookData) {
					ArrayList<Object> rowData = new ArrayList<Object>();
					rowData.add(i.getBook().getIsbn());
					rowData.add(i.getBook().getTitle());
					rowData.add(i.getBook().getAuthor());
					rowData.add(i.getDateRequested().toString());
					rowData.add(new JButton("Cancel"));
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

			tableData = new ArrayList<ArrayList<Object>>();
			try {
				bookDataReserve = TransactionDAO.getReservedBooks(getUser());

				if (bookDataReserve.size() != 0) {
					for (ReserveTransaction i : bookDataReserve) {
						ArrayList<Object> rowData = new ArrayList<Object>(5);
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

			tableData = new ArrayList<ArrayList<Object>>();
			try {
				bookData = TransactionDAO.getOnLoanBooks(getUser());
			} catch (Exception e) {
				System.out.println("ELibController: onloanData: " + e);
			}

			if (bookData.size() != 0) {
				for (BorrowTransaction i : bookData) {
					DateTime dueDate = new DateTime(i.getDateBorrowed()
							.getTime()).plusDays(14);
					ArrayList<Object> rowData = new ArrayList<Object>();
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

			tableData = new ArrayList<ArrayList<Object>>();
			try {
				bookData = TransactionDAO.getHistory(getUser());
			} catch (Exception e) {
				System.out.println("ELibController: historyData: " + e);
			}

			if (bookData.size() != 0) {
				for (BorrowTransaction i : bookData) {
					ArrayList<Object> rowData = new ArrayList<Object>();
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
		public Class<?> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		@Override
		public Object getValueAt(int row, int col) {

			return tableData.get(row).get(col);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == 4)
				return true;
			return false;
		}
	}

	public class ButtonRenderer implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			return (JButton) (value);
		}
	}

	public class ButtonEditor extends AbstractCellEditor implements
			TableCellEditor {
		private static final long serialVersionUID = 1L;
		protected JButton button;
		private BorrowTransaction selectedBook;

		public ButtonEditor() {
			button = new JButton("Cancel");
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int response = JOptionPane.showConfirmDialog(null,
							"Do you really want to cancel the request",
							"Confirm", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {

						try {
							TransactionDAO.denyBookRequest(selectedBook);
							tab = getTabpane().getSelectedTab();
							ELibTableModel model = new ELibTableModel(tab);
							getTabpane().setTableModel(tab, model);
							getTabpane().setCellRenderer(tab,
									new ButtonRenderer());
							getTabpane().setCellEditor(tab, new ButtonEditor());
						} catch (Exception e1) {
							System.out.println("CancelRequest: requestData: "
									+ e1);

						}
					}
					fireEditingStopped();
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {

			if (value != null) {
				if (getTabpane().getSelectedTab() == 0) {
					selectedBook = bookData.get(row);
				}
				return button;

			} else {
				return null;
			}
		}

		@Override
		public Object getCellEditorValue() {
			return null;
		}
	}
}
