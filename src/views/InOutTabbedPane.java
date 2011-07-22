package views;

import javax.swing.JTabbedPane;

public class InOutTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private IncomingPanel incomingPanel;
	private OutgoingPanel outgoingPanel;

	public InOutTabbedPane() {
		setVisible(true);
		incomingPanel = new IncomingPanel();
		outgoingPanel = new OutgoingPanel();

		addTab("Incoming", incomingPanel);
		addTab("Outgoing", outgoingPanel);
		System.out.println("InOutTabbedPane");
	}

	public IncomingPanel getIncomingPanel() {
		return incomingPanel;
	}

	public void setIncomingPanel(IncomingPanel incomingPanel) {
		this.incomingPanel = incomingPanel;
	}

	public OutgoingPanel getOutgoingPanel() {
		return outgoingPanel;
	}

	public void setOutgoingPanel(OutgoingPanel outgoingPanel) {
		this.outgoingPanel = outgoingPanel;
	}
}
