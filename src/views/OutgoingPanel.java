package views;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

public class OutgoingPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	public OutgoingPanel() {
		setLayout(new MigLayout("", "[25:n:25][266:n:266][100:n:100][100:n:100]", "[65:n:65][325:n:325][25:n:25]"));
		
		JPanel searchPanel = new InOutBookSearchPanel();
		add(searchPanel, "cell 0 0 4 2,grow");
		
		JButton btnGrant = new JButton("Grant");
		add(btnGrant, "cell 2 2,growx");
		
		JButton btnDeny = new JButton("Deny");
		add(btnDeny, "cell 3 2,growx");
	}
}
