package views;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
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
		setLayout(new MigLayout("", "[grow]", "[grow][]"));

		searchPanel = new InOutBookSearchPanel();
		searchPanel.setName("searchPanel");
		add(searchPanel, "cell 0 0,grow");

		JPanel buttonPanel = new JPanel();
		add(buttonPanel, "cell 0 1,alignx right,growy");
		buttonPanel.setLayout(new MigLayout("", "[][]", "[]"));

		daysOverdueLabel = new JLabel("");
		daysOverdueLabel.setName("daysOverdueLabel");
		daysOverdueLabel.setForeground(Color.BLACK);
		buttonPanel.add(daysOverdueLabel, "cell 0 0,alignx left,aligny center");

		returnButton = new JButton("Return", new ImageIcon(
				"resources/images/return32x32.png"));
		buttonPanel.add(returnButton, "cell 1 0,alignx left,aligny top");
		returnButton.setEnabled(false);
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
