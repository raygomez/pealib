package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

public class IncomingPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private InOutBookSearchPanel searchPanel;
	private JButton btnReturn;
	
	public IncomingPanel() {
		setLayout(new MigLayout("", "[25:n:25][370:n:370][100:n:100]", "[65:n:65][325:n:325][25:n:25]"));
		
		searchPanel = new InOutBookSearchPanel();
		add(searchPanel, "cell 0 0 3 2,grow");
		
		btnReturn = new JButton("Return");
		add(btnReturn, "cell 2 2,growx");
	}

	public InOutBookSearchPanel getSearchPanel() {
		return searchPanel;
	}

	public void setActionListener(ActionListener returnBook, KeyListener enter) {
		btnReturn.addActionListener(returnBook);
		btnReturn.addKeyListener(enter);
	}
}
