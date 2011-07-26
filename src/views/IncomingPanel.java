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
	private JPanel panel_1;

	public IncomingPanel() {
		setLayout(new MigLayout("", "[grow][][]", "[grow][][]"));

		searchPanel = new InOutBookSearchPanel();
		searchPanel.setName("searchPanel");
		add(searchPanel, "cell 0 0 3 1,grow");

		panel_1 = new JPanel();
		add(panel_1, "cell 2 1,alignx right,growy");

		returnButton = new JButton("Return", new ImageIcon(
				"resources/images/return32x32.png"));
		panel_1.add(returnButton);
		returnButton.setEnabled(false);

		daysOverdueLabel = new JLabel("");
		daysOverdueLabel.setName("daysOverdueLabel");
		daysOverdueLabel.setForeground(Color.BLACK);
		panel_1.add(daysOverdueLabel, "cell 0 2,alignx left,aligny top");
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
