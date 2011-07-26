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
	private JTextField searchTextField;
	private JButton submitButton;
	private JButton clearButton;
	private JLabel searchLabel;
	private JTable resultsTable;
	private JLabel totalLabel;
	/**
	 * Create the panel.
	 */
	public InOutBookSearchPanel() {
		setLayout(new MigLayout("", "[55:n:55][500:n:500][100:n:100][100:n:100]", "[][25:n:25][15:n:15][25:n:25][280:n:280]"));
		
		searchLabel = new JLabel("Search");
		add(searchLabel, "cell 0 0,alignx right");
		
		searchTextField = new JTextField();
		searchTextField.setName("searchTextField");
		searchLabel.setLabelFor(searchTextField);
		add(searchTextField, "cell 1 0 3 1,growx");
		searchTextField.setColumns(10);
		
		submitButton = new JButton("Submit");
		add(submitButton, "cell 2 1,growx");
		
		clearButton = new JButton("Clear");
		add(clearButton, "cell 3 1,growx");
		
		totalLabel = new JLabel("");
		totalLabel.setName("totalLabel");
		add(totalLabel, "cell 0 2 2 1");
		
		resultsTable = new JTable();
		JScrollPane sclpnResults = new JScrollPane(resultsTable);
		add(sclpnResults, "cell 0 3 4 2,grow");
	}
	
	/* Event Listeners */
	public void setEventListeners(ActionListener submit, ActionListener clear, KeyListener enter, MouseListener select) {
		submitButton.addActionListener(submit);
		clearButton.addActionListener(clear);
		searchTextField.addKeyListener(enter);
		resultsTable.addMouseListener(select);
	}
	
	/* Getters */
	public JTextField getTxtfldSearch() {
		return searchTextField;
	}

	public JLabel getLblTotal() {
		return totalLabel;
	}

	public JTable getTblResults() {
		return resultsTable;
	}
}
