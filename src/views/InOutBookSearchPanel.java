package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.table.TableModel;

public class InOutBookSearchPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtfldSearch;
	private JButton btnSubmit;
	private JButton btnClear;
	private JLabel lblSearch;
	private JTable tblResults;
	private JLabel lblTotal;
	private TableModel transactionTable;
	/**
	 * Create the panel.
	 */
	public InOutBookSearchPanel() {
		setLayout(new MigLayout("", "[55:n:55][230:n:230][100:n:100][100:n:100]", "[][25:n:25][15:n:15][25:n:25][280:n:280]"));
		
		lblSearch = new JLabel("Search");
		add(lblSearch, "cell 0 0,alignx right");
		
		txtfldSearch = new JTextField();
		lblSearch.setLabelFor(txtfldSearch);
		add(txtfldSearch, "cell 1 0 3 1,growx");
		txtfldSearch.setColumns(10);
		
		btnSubmit = new JButton("Submit");
		add(btnSubmit, "cell 2 1,growx");
		
		btnClear = new JButton("Clear");
		add(btnClear, "cell 3 1,growx");
		
		lblTotal = new JLabel("");
		add(lblTotal, "cell 0 2 2 1");
		
		tblResults = new JTable(transactionTable);
		JScrollPane sclpnResults = new JScrollPane(tblResults);
		add(sclpnResults, "cell 0 3 4 2,grow");
	}
	
	/* Listeners */
	public void setActionListeners(ActionListener submit, ActionListener clear, KeyListener enter) {
		btnSubmit.addActionListener(submit);
		btnClear.addActionListener(clear);
		txtfldSearch.addKeyListener(enter);
	}
	
	/* Getters and Setters */
	public TableModel getTransactionTable() {
		return transactionTable;
	}
	
	public void setTransactionTable(TableModel transactionTable) {
		this.transactionTable = transactionTable;
	}

	public JTextField getTxtfldSearch() {
		return txtfldSearch;
	}

	public JLabel getLblTotal() {
		return lblTotal;
	}
}
