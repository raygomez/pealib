package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import net.miginfocom.swing.MigLayout;


public class InOutBookSearchPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField searchTextField;
	private JButton submitButton;
	private JTable resultsTable;
	private JLabel totalLabel;

	public InOutBookSearchPanel() {
		setLayout(new MigLayout("", "[grow]", "[grow][grow]"));

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new MigLayout("", "[grow][grow][grow][grow]", "[grow]"));
		add(searchPanel, "cell 0 0, grow");
		
		searchTextField = new JTextField();
		searchTextField.setName("searchTextField");
		searchTextField.setColumns(10);
		searchPanel.add(searchTextField, "cell 1 0 2 1, growx, aligny center");
		
		submitButton = new JButton("Search", new ImageIcon(
				"resources/images/search32x32.png"));
		searchPanel.add(submitButton, "cell 3 0, alignx left, aligny center");

		
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new MigLayout("", "[grow]", "[grow][grow]"));
		add(resultsPanel, "cell 0 1, grow");

		totalLabel = new JLabel("");
		totalLabel.setName("totalLabel");
		resultsPanel.add(totalLabel, "cell 0 0, alignx left, aligny top");

		resultsTable = new JTable();
		resultsTable.getTableHeader().setReorderingAllowed(false);
		resultsTable.getTableHeader().setResizingAllowed(false);
		resultsTable.setRowHeight(28);
		JScrollPane sclpnResults = new JScrollPane(resultsTable);
		resultsPanel.add(sclpnResults, "cell 0 1, grow");
	}

	/* Event Listeners */
	public void setEventListeners(ActionListener submit, KeyListener enter,
			MouseListener select, KeyListener shift) {
		submitButton.addActionListener(submit);
		searchTextField.addKeyListener(enter);
		resultsTable.addMouseListener(select);
		resultsTable.addKeyListener(shift);
	}

	/* Getters */
	
	public String getSearchWord(){
		return searchTextField.getText();
	}
	
	public void setTotal(String total){
		totalLabel.setText(total);
	}

	public JTable getTableResults() {
		return resultsTable;
	}
	
	/* Setter */
	public void setColumnRender(int columnSize){
		DefaultTableCellRenderer trender = new DefaultTableCellRenderer();
		trender.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < columnSize; i++) {
			resultsTable.getColumn(resultsTable.getColumnName(i)).setCellRenderer(trender);
		}
	}
}
