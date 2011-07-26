package views;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public class IncomingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private InOutBookSearchPanel searchPanel;
	private JLabel daysOverdueLabel;
	private JButton returnButton;

	public IncomingPanel() {
		setLayout(new MigLayout("", "[676.00,grow][100:n:100,grow][3:n:3,grow]", "[70:n:70][340:n:340][25:n:25]"));

		searchPanel = new InOutBookSearchPanel();
		searchPanel.setName("searchPanel");
		add(searchPanel, "cell 0 0 3 2,grow");

		daysOverdueLabel = new JLabel("");
		daysOverdueLabel.setName("daysOverdueLabel");
		daysOverdueLabel.setForeground(Color.BLACK);
		add(daysOverdueLabel, "cell 0 2,alignx left");

		returnButton = new JButton("Return");
		returnButton.setEnabled(false);
		add(returnButton, "cell 1 2,growx");
	}

	/* Event Listener */
	public void setEventListener(ActionListener returnBook) {
		returnButton.addActionListener(returnBook);
	}

	/* Getters */
	public InOutBookSearchPanel getSearchPanel() {
		return searchPanel;
	}

	public JLabel getLblDaysOverdue() {
		return daysOverdueLabel;
	}

	public JButton getBtnReturn() {
		return returnButton;
	}
}
