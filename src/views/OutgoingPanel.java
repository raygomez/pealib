package views;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;

import net.miginfocom.swing.MigLayout;


public class OutgoingPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private InOutBookSearchPanel searchPanel;
	private JButton grantButton;
	private JButton denyButton;
	private JPanel panel;
	
	public OutgoingPanel() {
		setLayout(new MigLayout("debug", "[grow][grow][grow][]", "[grow][][]"));
		
		searchPanel = new InOutBookSearchPanel();
		searchPanel.setName("searchPanel");
		add(searchPanel, "cell 0 0 4 1,grow");
		
		panel = new JPanel();
		add(panel, "cell 0 1 4 1,alignx right,growy");
		panel.setLayout(new MigLayout("", "[][]", "[]"));
		
		grantButton = new JButton("Grant", new ImageIcon("resources/images/Apply.png"));
		panel.add(grantButton, "cell 0 0,alignx left,aligny top");
		grantButton.setEnabled(false);
		
		denyButton = new JButton("Deny", new ImageIcon("resources/images/Delete.png"));
		panel.add(denyButton, "cell 1 0,alignx left,aligny top");
		denyButton.setEnabled(false);
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
