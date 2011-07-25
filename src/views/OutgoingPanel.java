package views;

import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;

import net.miginfocom.swing.MigLayout;


public class OutgoingPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private InOutBookSearchPanel searchPanel;
	private JButton btnGrant;
	private JButton btnDeny;
	
	public OutgoingPanel() {
		setLayout(new MigLayout("", "[25:n:25][536:n:536][100:n:100][100:n:100]", "[65:n:65][325:n:325][25:n:25]"));
		
		searchPanel = new InOutBookSearchPanel();
		add(searchPanel, "cell 0 0 4 2,grow");
		
		btnGrant = new JButton("Grant");
		btnGrant.setEnabled(false);
		add(btnGrant, "cell 2 2,growx");
		
		btnDeny = new JButton("Deny");
		btnDeny.setEnabled(false);
		add(btnDeny, "cell 3 2,growx");
	}

	/* Event Listeners */
	public void setEventListener(ActionListener grant, ActionListener deny) {
		btnGrant.addActionListener(grant);
		btnDeny.addActionListener(deny);
	}
		
	/* Getters */
	public InOutBookSearchPanel getSearchPanel() {
		return searchPanel;
	}

	public JButton getBtnGrant() {
		return btnGrant;
	}

	public JButton getBtnDeny() {
		return btnDeny;
	}
}
