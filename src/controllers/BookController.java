package controllers;

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
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;
import utilities.Connector;
import utilities.Constants;
import views.BookInfoPanel;
import views.BookSearchPanel;
import models.Book;
import models.BookDAO;
import models.User;

public class BookController {

	private static BookSearchPanel bookSearch;
	private static BookInfoPanel bookInfo;
	private int currTableRowSelection;
	private String currSearchString;
	private JPanel bookLayoutPanel;
	private User currentUser;
	private ArrayList<Book> bookList;

	public static void main(String args[]) throws Exception {
		new Connector(Constants.TEST_CONFIG);
		User user = new User(19, "niel", "121111", "Reiniel Adam", "Lozada",
				"reiniel_lozada@yahoo.com", "secret", "8194000", 1, "User");
		BookController bookController = new BookController(user);
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		testFrame.setVisible(true);
		testFrame.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		testFrame.setBounds(0, 0, screenSize.width, screenSize.height);
		testFrame.setContentPane(bookController.getBookLayoutPanel());
	}

	public BookController(User user) throws Exception {
		currentUser = user;
		generateBookLayoutPanel();
	}

	private void generateBookLayoutPanel() throws Exception {
		bookList = BookDAO.searchBook("*");
		currSearchString = "*";
		bookLayoutPanel = new JPanel(new MigLayout("wrap 2", "[grow][grow]"));
		bookSearch = new BookSearchPanel();
		bookInfo = new BookInfoPanel(bookList.get(0), currentUser);
		currTableRowSelection = 0;
		bookLayoutPanel.add(bookSearch, "grow");
		bookLayoutPanel.add(bookInfo, "grow");

		bookSearch.setTextFieldListener(new TextFieldAdapter());
		bookSearch.setClearButtonListener(new ClearButtonListener());
		bookSearch.setSearchButtonListener(new SearchButtonListener());
		bookSearch
				.setTableListModel(new BookListModel(BookDAO.searchBook("*")));
		bookSearch.setMouseListener(new BookListMouseAdapter());
		bookInfo.addSaveListener(new SaveButtonListener());
		bookInfo.addDeleteListener(new DeleteButtonListener());
	}

	public JPanel getBookLayoutPanel() {
		return bookLayoutPanel;
	}

	class SaveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				BookDAO.editBook(bookInfo.getCurrBook());
				bookList = BookDAO.searchBook(currSearchString);
				bookSearch.setTableListModel(new BookListModel(bookList));
				bookInfo.setBookInfoData(bookList.get(currTableRowSelection));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	class DeleteButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				BookDAO.deleteBook(bookInfo.getCurrBook());
				bookList = BookDAO.searchBook(currSearchString);
				bookSearch.setTableListModel(new BookListModel(bookList));
				bookInfo.setBookInfoData(bookList.get(currTableRowSelection));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	class BookListMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			JTable table = (JTable) e.getSource();
			int tableRow = table.getSelectedRow();
			currTableRowSelection = tableRow;
			bookInfo.setBookInfoData(bookList.get(tableRow));
		}

	}

	class BookListModel extends AbstractTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		String[] columnName = { "ISBN No.", "Book", "Status" };
		ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();

		public BookListModel(ArrayList<Book> bookList) {
			ArrayList<String> rowData = new ArrayList<String>();

			for (int i = 0; i < bookList.size(); i++) {
				rowData = new ArrayList<String>();
				rowData.add(bookList.get(i).getIsbn());
				rowData.add(bookList.get(i).getTitle() + ", "
						+ bookList.get(i).getAuthor());

				if (bookList.get(i).getCopies() == 0) {
					rowData.add("unavailable");
				} else if (bookList.get(i).getCopies() == 1) {
					rowData.add("1 copy available");
				} else {
					rowData.add(bookList.get(i).getCopies()
							+ " copies available");
				}

				tableData.add(rowData);
			}
		}

		public String getColumnName(int col) {
			return columnName[col];
		}

		@Override
		public int getColumnCount() {
			return columnName.length;
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
				String strSearch = bookSearch.getTextFieldSearch();
				if (!strSearch.trim().isEmpty()) {
					currSearchString = strSearch;
					bookList = BookDAO.searchBook(strSearch);
					bookSearch.setTableListModel(new BookListModel(bookList));
					bookInfo.setBookInfoData(bookList.get(0));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	class ClearButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			bookSearch.setTextFieldSearch("");
		}

	}

	class TextFieldAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent key) {
			if (key.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					String strSearch = bookSearch.getTextFieldSearch();
					if (!strSearch.trim().isEmpty()) {
						currSearchString = strSearch;
						bookList = BookDAO.searchBook(strSearch);
						bookSearch
								.setTableListModel(new BookListModel(bookList));
						bookInfo.setBookInfoData(bookList.get(0));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}
}
