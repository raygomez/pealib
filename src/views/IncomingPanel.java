package views;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;


public class IncomingPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private InOutBookSearchPanel searchPanel;
	private JLabel lblDaysOverdue;
	private JButton btnReturn;
	
	public IncomingPanel() {
		setLayout(new MigLayout("", "[25:n:25][640:n:640][100:n:100]", "[65:n:65][325:n:325][25:n:25]"));
		
		searchPanel = new InOutBookSearchPanel();
		add(searchPanel, "cell 0 0 3 2,grow");
		
		lblDaysOverdue = new JLabel("");
		lblDaysOverdue.setForeground(Color.BLACK);
		add(lblDaysOverdue, "cell 0 2 2 1,alignx left");
		
		btnReturn = new JButton("Return");
		btnReturn.setEnabled(false);
		add(btnReturn, "cell 2 2,growx");
	}

	/* Event Listener */
	public void setEventListener(ActionListener returnBook) {
		btnReturn.addActionListener(returnBook);
	}

	/* Getters */
	public InOutBookSearchPanel getSearchPanel() {
		return searchPanel;
	}

	public JLabel getLblDaysOverdue() {
		return lblDaysOverdue;
	}

	public JButton getBtnReturn() {
		return btnReturn;
	}
}
