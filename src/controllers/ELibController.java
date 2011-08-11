package controllers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Callable;

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

import pealib.PeaLibrary;

import utilities.Connector;
import utilities.Constants;
import utilities.CrashHandler;
import utilities.Task;
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
	private int tab;
	private ELibTableModel model;

	public static void main(String[] args) throws Exception {
		Connector.init(Constants.TEST_CONFIG);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0, screenSize.width, screenSize.height);

		User user = UserDAO.getUserById(1);
		frame.setContentPane(new ELibController(user).getTabPane());

		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public ELibController(User user) throws Exception {
		setUser(user);

		tabpane = new ELibTabbedPanel();
		tabpane.addChangeTabListener(new TabChangeListener());

		ELibTableModel model = new ELibTableModel(0);
		tabpane.setTableModel(0, model);
		tabpane.setCellRenderer(0, new CancelButtonRenderer());
		tabpane.setCellEditor(0, new CancelRequestButtonEditor());
	}

	/**
	 * @return the bookDataReserve
	 */
	private ArrayList<ReserveTransaction> getBookDataReserve() {
		return bookDataReserve;
	}

	/**
	 * @param bookDataReserve
	 *            the bookDataReserve to set
	 */
	public void setBookDataReserve(ArrayList<ReserveTransaction> bookDataReserve) {
		this.bookDataReserve = bookDataReserve;
	}

	public ELibTabbedPanel getTabPane() throws Exception {
		return tabpane;
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
			try {
				update();
			} catch (Exception e1) {
				CrashHandler.handle(e1);
			}
		}
	}

	public void update() throws Exception {
		
		tabpane.getRequestTable().setVisible(false);
		Callable<Void> toDo = new Callable<Void>() {

			@Override
			public Void call() throws Exception {

				tab = tabpane.getSelectedTab();
				model = new ELibTableModel(tab);
				
				return null;				
			}
		};
		
		Callable<Void> toDoAfter = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				
				tabpane.setTableModel(tab, model);

				tabpane.setCellRenderer(tab, new CancelButtonRenderer());
				if (tab == REQUEST) {
					tabpane.setCellEditor(tab, new CancelRequestButtonEditor());
				} else if (tab == RESERVE) {
					tabpane.setCellEditor(tab, new CancelReservationButtonEditor());
				}
				
				tabpane.getRequestTable().setVisible(true);
				return null;
			}
		};
		
		Task<Void, Object> task = new Task<Void, Object>(toDo, toDoAfter);
		LoadingControl.init(task, PeaLibrary.getMainFrame()).executeLoading();
		
	}

	class ELibTableModel extends AbstractTableModel {
		/**
		 * TableModel for ELib Tabs
		 */
		private static final long serialVersionUID = 1L;
		private String[] columns;
		private ArrayList<ArrayList<Object>> tableData;

		public ELibTableModel(int tab) throws Exception {
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

		private void requestData() throws Exception {
			columns = new String[] { "ISBN", "Title", "Author",
					"Date Requested", "Cancel" };

			bookData = TransactionDAO.getRequestedBooks(getUser());

			if (bookData.size() != 0) {
				for (BorrowTransaction i : bookData) {
					ArrayList<Object> rowData = new ArrayList<Object>(
							columns.length);
					rowData.add(i.getBook().getIsbn10());
					rowData.add(i.getBook().getTitle());
					rowData.add(i.getBook().getAuthor());
					rowData.add(i.getDateRequested().toString());
					rowData.add(new JButton("Cancel"));
					tableData.add(rowData);
				}
			}
		}

		private void reserveData() throws Exception {
			columns = new String[] { "ISBN", "Title", "Author",
					"Date Reserved", "Queue Number", "Cancel" };

			setBookDataReserve(TransactionDAO.getReservedBooks(getUser()));

			if (getBookDataReserve().size() != 0) {
				for (ReserveTransaction i : getBookDataReserve()) {
					ArrayList<Object> rowData = new ArrayList<Object>(
							columns.length);
					DateTime reservedDate = new DateTime(i.getDateReserved()
							.getTime());

					rowData.add(i.getBook().getIsbn10());
					rowData.add(i.getBook().getTitle());
					rowData.add(i.getBook().getAuthor());
					rowData.add(reservedDate.toString("y-MM-dd h:mm a"));
					rowData.add(TransactionDAO.getQueueInReservation(
							i.getBook(), i.getUser())
							+ "");
					rowData.add(new JButton("Cancel"));
					tableData.add(rowData);
				}
			}
		}

		private void onloanData() throws Exception {
			columns = new String[] { "ISBN", "Title", "Author",
					"Date Borrowed", "Due Date" };

			bookData = TransactionDAO.getOnLoanBooks(getUser());

			if (bookData.size() != 0) {
				for (BorrowTransaction i : bookData) {
					DateTime dueDate = new DateTime(i.getDateBorrowed()
							.getTime()).plusDays(14);
					String due="";			
					
					if(dueDate.isBeforeNow()){
						due = "<html><font color='red'>"+dueDate.toString("y-MM-dd")+"</font></html>";
					}
					else due = dueDate.toString("y-MM-dd");
					
					ArrayList<Object> rowData = new ArrayList<Object>(
							columns.length);
					rowData.add(i.getBook().getIsbn10());
					rowData.add(i.getBook().getTitle());
					rowData.add(i.getBook().getAuthor());
					rowData.add(i.getDateBorrowed().toString());
					rowData.add(due);
					tableData.add(rowData);
				}
			}
		}

		private void historyData() throws Exception {
			columns = new String[] { "ISBN", "Title", "Author",
					"Date Borrowed", "Date Returned" };

			bookData = TransactionDAO.getHistory(getUser());

			if (bookData.size() != 0) {
				for (BorrowTransaction i : bookData) {
					ArrayList<Object> rowData = new ArrayList<Object>(
							columns.length);
					rowData.add(i.getBook().getIsbn10());
					rowData.add(i.getBook().getTitle());
					rowData.add(i.getBook().getAuthor());
					rowData.add(i.getDateBorrowed().toString());
					rowData.add(i.getDateReturned().toString());
					tableData.add(rowData);
				}
			}
		}

		@Override
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
		public Object getValueAt(int row, int col) {

			return tableData.get(row).get(col);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == 4 && tabpane.getSelectedTab() == REQUEST)
				return true;
			else if (columnIndex == 5 && tabpane.getSelectedTab() == RESERVE)
				return true;

			return false;
		}
	}

	public class CancelButtonRenderer implements TableCellRenderer {
		@Override
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
				@Override
				public void actionPerformed(ActionEvent e) {
					int response = JOptionPane.showConfirmDialog(null,
							Constants.CONFIRM_CANCEL_REQUEST, "Confirm",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {

						try {
							TransactionDAO.denyBookRequest(selectedBook);
							if (TransactionDAO
									.isBookReservedByOtherUsers(selectedBook
											.getBook())) {
								TransactionDAO.passToNextUser(selectedBook
										.getBook());
							}
							update();
						} catch (Exception e1) {
							CrashHandler.handle(e1);
						}

					}
					fireEditingStopped();
				}
			});
		}

		@Override
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
				@Override
				public void actionPerformed(ActionEvent e) {
					int response = JOptionPane.showConfirmDialog(null,
							Constants.CONFIRM_CANCEL_RESERVATION, "Confirm",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {

						try {
							TransactionDAO.cancelReservation(selectedBook);
							update();
						} catch (Exception e1) {
							CrashHandler.handle(e1);
						}
					}
					fireEditingStopped();
				}
			});
		}

		@Override
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
