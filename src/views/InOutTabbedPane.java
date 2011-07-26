package views;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

public class InOutTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private IncomingPanel incomingPanel;
	private OutgoingPanel outgoingPanel;

	public InOutTabbedPane() {
		setVisible(true);
		incomingPanel = new IncomingPanel();
		outgoingPanel = new OutgoingPanel();

		addTab("Incoming", new ImageIcon("resources/images/incoming.png"),
				incomingPanel);
		addTab("Outgoing", new ImageIcon("resources/images/outgoing.png"),
				outgoingPanel);
	}

	/* Event Listener */
	public void setEventListener(ChangeListener tab) {
		addChangeListener(tab);
	}

	/* Getters */
	public IncomingPanel getIncomingPanel() {
		return incomingPanel;
	}

	public OutgoingPanel getOutgoingPanel() {
		return outgoingPanel;
	}
}
