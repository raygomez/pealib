package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import models.User;
import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

public class BookSearchPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textFieldSearch;
	private JTable tableBookList;
	private JButton btnSearch;
	private JButton btnClear;
	private JButton btnAddBook;

	public BookSearchPanel(User user) {
		displayBookSearch();
		if(user.getType().equals("Librarian")){
			btnAddBook.setVisible(true);
		}
	}

	private void displayBookSearch(){
		setLayout(new MigLayout("", "[grow][grow 10][grow 10]", "[][][20px:20:20px][][grow]"));
		
		textFieldSearch = new JTextField();
		textFieldSearch.setName("textSearch");
		add(textFieldSearch, "cell 0 0 3 1,grow");
		textFieldSearch.setColumns(10);
		
		btnAddBook = new JButton("Add Book", new ImageIcon(
				"resources/images/add.png"));
		btnAddBook.setVisible(false);
		add(btnAddBook, "cell 0 1,alignx left,aligny center");
		
		btnSearch = new JButton("Search", new ImageIcon("resources/images/search32x32.png"));
		add(btnSearch, "cell 1 1,grow");
		
		btnClear = new JButton("Clear", new ImageIcon("resources/images/edit_clear.png"));
		add(btnClear, "cell 2 1,grow");
		setTableBookList(new JTable());
		getTableBookList().setName("tableList");
		getTableBookList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableBookList().setRowHeight(28);
		
		add(new JScrollPane(getTableBookList()), "cell 0 4 3 1,grow");
		getTableBookList().getTableHeader().setReorderingAllowed(false);
		getTableBookList().getTableHeader().setResizingAllowed(false);		
	}
	
	public void setSearchButtonListener(ActionListener search) {
		btnSearch.addActionListener(search);
	}
	
	public void setClearButtonListener(ActionListener clear){
		btnClear.addActionListener(clear);
	}
	
	public void setAddBookButtonListener(ActionListener add){
		btnAddBook.addActionListener(add);
	}
	
	public void setTextFieldListener(KeyListener textfield) {
		textFieldSearch.addKeyListener(textfield);
	}

	public JTextField getTextFieldSearch() {
		return textFieldSearch;
	}

	public JTable getTableBookList() {
		return tableBookList;
	}
	
	public void setColumnRender(JTable table){
		//TODO
		DefaultTableCellRenderer trender = new DefaultTableCellRenderer();
		trender.setHorizontalAlignment(SwingConstants.CENTER);
				
		table.getColumn(table.getColumnName(0)).setCellRenderer(trender);
		table.getColumn(table.getColumnName(1)).setCellRenderer(trender);
		table.getColumn(table.getColumnName(2)).setCellRenderer(trender);		
	}
	
	public void addBookSelectionListener(ListSelectionListener listener){
		getTableBookList().getSelectionModel().addListSelectionListener(listener);
	}

	public void setTableBookList(JTable tableBookList) {
		this.tableBookList = tableBookList;
	}
	
}
