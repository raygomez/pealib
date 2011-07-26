package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;


public class InOutBookSearchPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField searchTextField;
	private JButton submitButton;
	private JTable resultsTable;
	private JLabel totalLabel;
	/**
	 * Create the panel.
	 */
	public InOutBookSearchPanel() {
		setLayout(new MigLayout("", "[grow][grow][10:n:10,grow 10][120:n:120,grow][grow 50]", "[][][][][grow]"));
		
		searchTextField = new JTextField();
		searchTextField.setName("searchTextField");
		add(searchTextField, "cell 1 1,growx");
		searchTextField.setColumns(10);
		
		submitButton = new JButton("Search", new ImageIcon("resources/images/search32x32.png"));
		add(submitButton, "cell 3 1,grow");
		
		totalLabel = new JLabel("");
		totalLabel.setName("totalLabel");
		add(totalLabel, "cell 0 2 2 1");
		
		resultsTable = new JTable();
		JScrollPane sclpnResults = new JScrollPane(resultsTable);
		add(sclpnResults, "cell 0 3 5 2,grow");
	}
	
	/* Event Listeners */
	public void setEventListeners(ActionListener submit, KeyListener enter, MouseListener select) {
		submitButton.addActionListener(submit);
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
