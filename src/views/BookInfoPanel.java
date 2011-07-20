package views;

import javax.swing.JPanel;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;

public class BookInfoPanel extends JPanel {
	
	private JTextField txtFldTitle;
	private JTextField txtFldAuthor;
	private JTextField txtFldYrPublished;
	private JTextField txtFldPublisher;
	private JTextField txtFldISBN;
	private JTextField txtFldDescription;
	
	public BookInfoPanel() {
		
		setLayout(new MigLayout("", "[14.00][38.00][13.00][287.00]", "[][][][][][][][48.00][34.00]"));
		
		JLabel lblTitle = new JLabel("Title:");
		add(lblTitle, "cell 1 1");
		
		txtFldTitle = new JTextField();
		add(txtFldTitle, "cell 3 1,growx");
		txtFldTitle.setColumns(10);
		
		JLabel lblAuthor = new JLabel("Author:");
		add(lblAuthor, "cell 1 2");
		
		txtFldAuthor = new JTextField();
		add(txtFldAuthor, "cell 3 2,growx");
		txtFldAuthor.setColumns(10);
		
		JLabel lblYearPublished = new JLabel("Year Published:");
		add(lblYearPublished, "cell 1 3");
		
		txtFldYrPublished = new JTextField();
		add(txtFldYrPublished, "cell 3 3,growx");
		txtFldYrPublished.setColumns(10);
		
		JLabel lblPublisher = new JLabel("Publisher:");
		add(lblPublisher, "cell 1 4");
		
		txtFldPublisher = new JTextField();
		add(txtFldPublisher, "cell 3 4,growx");
		txtFldPublisher.setColumns(10);
		
		JLabel lblIsbn = new JLabel("ISBN:");
		add(lblIsbn, "cell 1 5");
		
		txtFldISBN = new JTextField();
		add(txtFldISBN, "cell 3 5,growx");
		txtFldISBN.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description:");
		add(lblDescription, "cell 1 6");
		
		txtFldDescription = new JTextField();
		add(txtFldDescription, "cell 3 6 1 3,grow");
		txtFldDescription.setColumns(10);
		
		JLabel lblCopies = new JLabel("Copies:");
		add(lblCopies, "cell 1 7,aligny bottom");
		
		JLabel lblCopiesValue = new JLabel("");
		add(lblCopiesValue, "cell 1 8,aligny top");
	}

	public JTextField getTxtFldTitle() {
		return txtFldTitle;
	}

	public void setTxtFldTitle(JTextField txtFldTitle) {
		this.txtFldTitle = txtFldTitle;
	}

	public JTextField getTxtFldAuthor() {
		return txtFldAuthor;
	}

	public void setTxtFldAuthor(JTextField txtFldAuthor) {
		this.txtFldAuthor = txtFldAuthor;
	}

	public JTextField getTxtFldYrPublished() {
		return txtFldYrPublished;
	}

	public void setTxtFldYrPublished(JTextField txtFldYrPublished) {
		this.txtFldYrPublished = txtFldYrPublished;
	}

	public JTextField getTxtFldPublisher() {
		return txtFldPublisher;
	}

	public void setTxtFldPublisher(JTextField txtFldPublisher) {
		this.txtFldPublisher = txtFldPublisher;
	}

	public JTextField getTxtFldISBN() {
		return txtFldISBN;
	}

	public void setTxtFldISBN(JTextField txtFldISBN) {
		this.txtFldISBN = txtFldISBN;
	}

	public JTextField getTxtFldDescription() {
		return txtFldDescription;
	}

	public void setTxtFldDescription(JTextField txtFldDescription) {
		this.txtFldDescription = txtFldDescription;
	}
	
}
