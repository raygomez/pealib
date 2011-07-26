package views;

import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;

import net.miginfocom.swing.MigLayout;


public class OutgoingPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private InOutBookSearchPanel searchPanel;
	private JButton grantButton;
	private JButton denyButton;
	
	public OutgoingPanel() {
		setLayout(new MigLayout("", "[grow][100:n:100,grow][100:n:100,grow][3:n:3]", "[60:n:60][350:n:350][25:n:25]"));
		
		searchPanel = new InOutBookSearchPanel();
		searchPanel.setName("searchPanel");
		add(searchPanel, "cell 0 0 4 2,grow");
		
		grantButton = new JButton("Grant");
		grantButton.setEnabled(false);
		add(grantButton, "cell 1 2,growx");
		
		denyButton = new JButton("Deny");
		denyButton.setEnabled(false);
		add(denyButton, "cell 2 2,growx");
	}

	public JButton getGrantButton() {
		return grantButton;
	}

	public JButton getDenyButton() {
		return denyButton;
	}

	/* Event Listeners */
	public void setEventListener(ActionListener grant, ActionListener deny) {
		grantButton.addActionListener(grant);
		denyButton.addActionListener(deny);
	}
		
	/* Getters */
	public InOutBookSearchPanel getSearchPanel() {
		return searchPanel;
	}

}
