package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JViewport;
import javax.swing.table.TableModel;

public class BookSearchPanel extends JPanel {
	private JTextField textFieldSearch;
	private JTable tableBookList;
	private JButton btnSearch;
	private JButton btnClear;

	public BookSearchPanel() {
		displayBookSearch();
	}

	private void displayBookSearch(){
		setLayout(new MigLayout("", "[grow][grow 10][grow 10]", "[][][][grow]"));
		
		textFieldSearch = new JTextField();
		textFieldSearch.setName("textSearch");
		add(textFieldSearch, "cell 0 0 3 1,grow");
		textFieldSearch.setColumns(10);
		
		btnSearch = new JButton("Search");
		add(btnSearch, "cell 1 1,grow");
		
		btnClear = new JButton("Clear");
		add(btnClear, "cell 2 1,grow");
		tableBookList = new JTable();
		tableBookList.setName("tableList");
		
		add(new JScrollPane(tableBookList), "cell 0 3 3 1,grow");
	}
	
	public void setSearchButtonListener(ActionListener search) {
		btnSearch.addActionListener(search);
	}
	
	public void setClearButtonListener(ActionListener clear){
		btnClear.addActionListener(clear);
	}
	
	public void setTextFieldListener(KeyListener textfield) {
		textFieldSearch.addKeyListener(textfield);
	}
	
	public void setMouseListener(MouseListener table) {
		tableBookList.addMouseListener(table);
	}
	
	public void setTextFieldSearch(String textFieldSearch) {
		this.textFieldSearch.setText(textFieldSearch);
	}

	public String getTextFieldSearch() {
		return textFieldSearch.getText();
	}
	
	public void setTableListModel(TableModel model) {
		tableBookList.setModel(model);
	}
	
}
