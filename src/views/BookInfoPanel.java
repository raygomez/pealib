package views;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import models.Book;
import models.User;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JButton;

public class BookInfoPanel extends JPanel {
	
	private JTextField txtFldTitle;
	private JTextField txtFldAuthor;
	private JTextField txtFldYrPublished;
	private JTextField txtFldPublisher;
	private JTextField txtFldISBN;
	private JTextField txtFldDescription;
	private JButton btnSave;
	private JButton btnDelete;
	private JLabel lblCopiesValue;
	private Connection conn;
	private User currUser;
	private Book currBook;
	
	/**
	 * Create the panel.
	 */

	public BookInfoPanel(ArrayList<Book> book, Connection con, User user,
			JFrame frame) {
		this.conn = con;
		this.currUser = user;
		this.currBook = book.get(0);
		displayBookInfo();
	}

	private void displayBookInfo() {
		
		setLayout(new MigLayout("", "[14.00][38.00][13.00][143.00][144]", "[][][][][][][][48.00][34.00][13.00][]"));
		
		JLabel lblTitle = new JLabel("Title:");
		add(lblTitle, "cell 1 1");
		
		txtFldTitle = new JTextField(currBook.getTitle());
		txtFldTitle.setEditable(false);
		add(txtFldTitle, "cell 3 1 2 1,growx");
		txtFldTitle.setColumns(10);
		
		JLabel lblAuthor = new JLabel("Author:");
		add(lblAuthor, "cell 1 2");
		
		txtFldAuthor = new JTextField(currBook.getAuthor());
		txtFldAuthor.setEditable(false);
		add(txtFldAuthor, "cell 3 2 2 1,growx");
		txtFldAuthor.setColumns(10);
		
		JLabel lblYearPublished = new JLabel("Year Published:");
		add(lblYearPublished, "cell 1 3");
		
		txtFldYrPublished = new JTextField(currBook.getYearPublish());
		txtFldYrPublished.setEditable(false);
		add(txtFldYrPublished, "cell 3 3 2 1,growx");
		txtFldYrPublished.setColumns(10);
		
		JLabel lblPublisher = new JLabel("Publisher:");
		add(lblPublisher, "cell 1 4");
		
		txtFldPublisher = new JTextField(currBook.getPublisher());
		txtFldPublisher.setEditable(false);
		add(txtFldPublisher, "cell 3 4 2 1,growx");
		txtFldPublisher.setColumns(10);
		
		JLabel lblIsbn = new JLabel("ISBN:");
		add(lblIsbn, "cell 1 5");
		
		txtFldISBN = new JTextField(currBook.getIsbn());
		txtFldISBN.setEditable(false);
		add(txtFldISBN, "cell 3 5 2 1,growx");
		txtFldISBN.setColumns(10);
		
		JLabel lblDescription = new JLabel("Description:");
		add(lblDescription, "cell 1 6");
		
		txtFldDescription = new JTextField(currBook.getDescription());
		txtFldDescription.setEditable(false);
		add(txtFldDescription, "cell 3 6 2 3,grow");
		txtFldDescription.setColumns(10);
		
		JLabel lblCopies = new JLabel("Copies:");
		add(lblCopies, "cell 1 7,aligny bottom");
		
		lblCopiesValue = new JLabel("" + currBook.getCopies());
		add(lblCopiesValue, "cell 1 8,aligny top");
		
		btnSave = new JButton("Save");
		
		btnDelete = new JButton("Delete");
		
		if (currUser.getType().equals("Librarian")){
			add(btnSave, "cell 3 10,alignx right");
			add(btnDelete, "cell 4 10");
			txtFldAuthor.setEditable(true);
			txtFldDescription.setEditable(true);
			txtFldISBN.setEditable(true);
			txtFldPublisher.setEditable(true);
			txtFldTitle.setEditable(true);
			txtFldYrPublished.setEditable(true);
		}
		
		
	}

	public void addSaveListener(ActionListener saveInfo){
		btnSave.addActionListener(saveInfo);
	}
	
	public void addDeleteListener(ActionListener deleteRecord){
		btnDelete.addActionListener(deleteRecord);
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