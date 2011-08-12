package views;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;

import net.miginfocom.swing.MigLayout;


public class OutgoingPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private InOutBookSearchPanel searchPanel;
	private JButton grantButton;
	private JButton denyButton;
	
	public OutgoingPanel() {
		setLayout(new MigLayout("", "[grow]", "[grow][]"));
		
		searchPanel = new InOutBookSearchPanel();
		searchPanel.setName("searchPanel");
		add(searchPanel, "cell 0 0,grow");
		
		JPanel buttonPanel = new JPanel();
		add(buttonPanel, "cell 0 1,alignx right,growy");
		buttonPanel.setLayout(new MigLayout("", "[][]", "[]"));
		
		grantButton = new JButton("Grant", new ImageIcon("resources/images/Apply.png"));
		buttonPanel.add(grantButton, "cell 0 0,alignx left,aligny center");
		grantButton.setEnabled(false);
		
		denyButton = new JButton("Deny", new ImageIcon("resources/images/Delete.png"));
		buttonPanel.add(denyButton, "cell 1 0,alignx left,aligny center");
		denyButton.setEnabled(false);
	}

	public void enableGrantButton(boolean enable){
		grantButton.setEnabled(enable);
	}
	
	public void enableDenyButton(boolean enable){
		denyButton.setEnabled(enable);
	}
	
	public void enableButtons(boolean enable){
		enableGrantButton(enable);
		enableDenyButton(enable);
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
