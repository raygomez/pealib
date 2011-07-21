package views;

import java.awt.event.ActionListener;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JButton;
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
		add(tableBookList, "cell 0 3 3 1,grow");
	}
	
	public void addSearchButtonListener(ActionListener search) {
		btnSearch.addActionListener(search);
	}
	
	public void addClearButtonListener(ActionListener clear){
		btnClear.addActionListener(clear);
	}

	public String getTextFieldSearch() {
		return textFieldSearch.getText();
	}
	
	public void setModel(TableModel model) {
		tableBookList.setModel(model);
	}

	
}
