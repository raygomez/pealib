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

	private static final int REQUEST = 0;
	private static final int RESERVE = 1;
	private static final int ON_LOAN = 2;
	private static final int HISTORY = 3;

	private User user;
	private ELibTabbedPanel tabpane;
	private ArrayList<BorrowTransaction> bookData;
	private ArrayList<ReserveTransaction> bookDataReserve;

	public static void main(String[] args) throws Exception {
		new Connector(Constants.TEST_CONFIG);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0, screenSize.width, screenSize.height);

		User user = UserDAO.getUserById(1);
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
		getTabpane().setCellRenderer(0, new CancelButtonRenderer());
		getTabpane().setCellEditor(0, new CancelRequestButtonEditor());
	}

	/**
	 * @return the bookDataReserve
	 */
	public ArrayList<ReserveTransaction> getBookDataReserve() {
		return bookDataReserve;
	}

	/**
	 * @param bookDataReserve
	 *            the bookDataReserve to set
	 */
	public void setBookDataReserve(ArrayList<ReserveTransaction> bookDataReserve) {
		this.bookDataReserve = bookDataReserve;
	}

	/**
	 * @return the tabpane
	 */
	public ELibTabbedPanel getTabpane() {
		return tabpane;
	}

	public ELibTabbedPanel getView(){
		update();
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
			update();
		}
	}

	private void update() {
		int tab = getTabpane().getSelectedTab();
		ELibTableModel model = new ELibTableModel(tab);
		getTabpane().setTableModel(tab, model);

		getTabpane().setCellRenderer(tab, new CancelButtonRenderer());
		if (tab == REQUEST) {
			getTabpane().setCellEditor(tab, new CancelRequestButtonEditor());
		} else if (tab == RESERVE) {
			getTabpane()
					.setCellEditor(tab, new CancelReservationButtonEditor());
		}
	}

	class ELibTableModel extends AbstractTableModel {
		/**
		 * TableModel for ELib Tabs
		 */
		private static final long serialVersionUID = 1L;
		private String[] columns;
		private ArrayList<ArrayList<Object>> tableData;

		public ELibTableModel(int tab) {
			tableData = new ArrayList<ArrayList<Object>>();

			switch (tab) {
			case REQUEST:
				requestData();
				break;
			case RESERVE:
				reserveData();
				break;
			case ON_LOAN:
				onloanData();
				break;
			case HISTORY:
				historyData();
				break;
			}
		}

		private void requestData() {
			columns = new String[] { "ISBN", "Title", "Author",
					"Date Requested", "Cancel" };

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
			columns = new String[] { "ISBN", "Title", "Author",
					"Date Reserved", "Queue Number", "Cancel" };

			try {
				setBookDataReserve(TransactionDAO.getReservedBooks(getUser()));

				if (getBookDataReserve().size() != 0) {
					for (ReserveTransaction i : getBookDataReserve()) {
						ArrayList<Object> rowData = new ArrayList<Object>(5);
						rowData.add(i.getBook().getIsbn());
						rowData.add(i.getBook().getTitle());
						rowData.add(i.getBook().getAuthor());
						rowData.add(i.getDateReserved().toString());
						rowData.add(""
								+ TransactionDAO.getQueueInReservation(
										i.getBook(), i.getUser()));
						rowData.add(new JButton("Cancel"));
						tableData.add(rowData);
					}
				}
			} catch (Exception e) {
				System.out.println("ELibController: reserveData: " + e);
			}
		}

		private void onloanData() {
			columns = new String[] { "ISBN", "Title", "Author",
					"Date Borrowed", "Due Date" };

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
			columns = new String[] { "ISBN", "Title", "Author",
					"Date Borrowed", "Date Returned" };

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
			return columns[col];
		}

		@Override
		public int getColumnCount() {
			return columns.length;
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
			if (columnIndex == 4 && getTabpane().getSelectedTab() == REQUEST)
				return true;
			else if (columnIndex == 5
					&& getTabpane().getSelectedTab() == RESERVE)
				return true;

			return false;
		}
	}

	public class CancelButtonRenderer implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			return (JButton) (value);
		}
	}

	public class CancelRequestButtonEditor extends AbstractCellEditor implements
			TableCellEditor {
		private static final long serialVersionUID = 1L;
		protected JButton button;
		private BorrowTransaction selectedBook;

		public CancelRequestButtonEditor() {
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
							if (TransactionDAO.isBookReservedByOtherUsers(selectedBook.getBook())){
								TransactionDAO.passToNextUser(selectedBook.getBook());
							}
							update();
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
				selectedBook = bookData.get(row);
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

	public class CancelReservationButtonEditor extends AbstractCellEditor
			implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		protected JButton button;
		private ReserveTransaction selectedBook;

		public CancelReservationButtonEditor() {
			button = new JButton("Cancel");
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int response = JOptionPane.showConfirmDialog(null,
							"Do you really want to cancel the reservation",
							"Confirm", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {

						try {
							TransactionDAO.cancelReservation(selectedBook);
							update();
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
				selectedBook = getBookDataReserve().get(row);
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
