package views;

import java.sql.SQLException;

import javax.swing.JTabbedPane;

import models.TransactionDAO;
import models.User;
import models.UserDAO;

public class InOutTabbedPane extends JTabbedPane {

	private IncomingPanel incomingPanel;
	private OutgoingPanel outgoingPanel;

	public InOutTabbedPane(User user, UserDAO userDAO,
			TransactionDAO transactionDAO) throws SQLException {

		incomingPanel = new IncomingPanel();
		outgoingPanel = new OutgoingPanel();

		addTab("Incoming", incomingPanel);
		addTab("Outgoing", outgoingPanel);

	}
}
