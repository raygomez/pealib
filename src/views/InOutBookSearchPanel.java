package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;


public class InOutBookSearchPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtfldSearch;
	private JButton btnSubmit;
	private JButton btnClear;
	private JLabel lblSearch;
	private JTable tblResults;
	private JLabel lblTotal;
	/**
	 * Create the panel.
	 */
	public InOutBookSearchPanel() {
		setLayout(new MigLayout("", "[55:n:55][500:n:500][100:n:100][100:n:100]", "[][25:n:25][15:n:15][25:n:25][280:n:280]"));
		
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
		
		tblResults = new JTable();
		JScrollPane sclpnResults = new JScrollPane(tblResults);
		add(sclpnResults, "cell 0 3 4 2,grow");
	}
	
	/* Event Listeners */
	public void setEventListeners(ActionListener submit, ActionListener clear, KeyListener enter, MouseListener select) {
		btnSubmit.addActionListener(submit);
		btnClear.addActionListener(clear);
		txtfldSearch.addKeyListener(enter);
		tblResults.addMouseListener(select);
	}
	
	/* Getters */
	public JTextField getTxtfldSearch() {
		return txtfldSearch;
	}

	public JLabel getLblTotal() {
		return lblTotal;
	}

	public JTable getTblResults() {
		return tblResults;
	}
}
