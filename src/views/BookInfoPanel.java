package views;

import java.awt.event.ActionListener;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import models.Book;
import models.User;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;

public class BookInfoPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtFldTitle;
	private JTextField txtFldAuthor;
	private JTextField txtFldYrPublished;
	private JTextField txtFldPublisher;
	private JTextField txtFldISBN;
	private JButton btnSave;
	private JButton btnDelete;
	private JButton btnBorrow;
	private JButton btnReserve;
	private User currUser;
	private Book currBook;
	private JLabel lblErrorMsg;
	private JTextArea txtFldDescription;
	private JSpinner spinCopyVal;
	private JFormattedTextField spinValue;
	private JLabel lblCopiesVal;

	/**
	 * Create the panel.
	 */

	public BookInfoPanel(Book book, User user) {
		this.currUser = user;
		this.currBook = book;
		displayBookInfo();
	}

	private void displayBookInfo() {
		
		setLayout(new MigLayout("", "[14.00][38.00][13.00][143.00,grow][144]", "[40][][][][][][grow][48.00][34.00][13.00][]"));
		
		lblErrorMsg = new JLabel("");
		lblErrorMsg.setForeground(Color.RED);
		add(lblErrorMsg, "cell 3 0 2 1,alignx center,aligny center");
		
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
		
		txtFldYrPublished = new JTextField(Integer.toString(currBook.getYearPublish()));
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
		
		txtFldDescription = new JTextArea(currBook.getDescription());
		txtFldDescription.setEditable(false);
		add(txtFldDescription, "cell 3 6 2 3,grow");
		
		JScrollPane scrollDes = new JScrollPane(txtFldDescription);
		add(scrollDes, "cell 3 6 2 3,grow");
		
		JLabel lblCopies = new JLabel("Copies:");
		add(lblCopies, "cell 1 9,alignx left,aligny center");
		
		spinCopyVal = new JSpinner();
		spinCopyVal.setModel(new SpinnerNumberModel(0, 0, 2147483647, 1));
		spinCopyVal.getModel().setValue(currBook.getCopies());
		
		lblCopiesVal = new JLabel(Integer.toString(currBook.getCopies()));
		
		btnSave = new JButton("Save");
		
		btnDelete = new JButton("Delete");
		
		btnBorrow = new JButton("Borrow");
		
		btnReserve = new JButton("Reserve");
		
		if (currUser.getType().equals("Librarian")){
			add(btnSave, "cell 3 10,alignx right");
			add(btnDelete, "cell 4 10");
			txtFldAuthor.setEditable(true);
			txtFldDescription.setEditable(true);
			txtFldISBN.setEditable(true);
			txtFldPublisher.setEditable(true);
			txtFldTitle.setEditable(true);
			txtFldYrPublished.setEditable(true);
			add(spinCopyVal, "cell 3 9,alignx left,aligny center");
		}
		if (currUser.getType().equals("User")){
			add(btnBorrow, "cell 3 10,alignx right");
			add(btnReserve, "cell 4 10");
			add(lblCopiesVal, "cell 3 9,alignx left,aligny center");
		}
		
		
	}

	public JButton getBtnBorrow() {
		return btnBorrow;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(JButton btnSave) {
		this.btnSave = btnSave;
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public void setBtnDelete(JButton btnDelete) {
		this.btnDelete = btnDelete;
	}

	public void setBtnBorrow(JButton btnBorrow) {
		this.btnBorrow = btnBorrow;
	}

	public JButton getBtnReserve() {
		return btnReserve;
	}

	public void setBtnReserve(JButton btnReserve) {
		this.btnReserve = btnReserve;
	}

	public void addSaveListener(ActionListener saveInfo){
		btnSave.addActionListener(saveInfo);
	}
	
	public void addDeleteListener(ActionListener deleteRecord){
		btnDelete.addActionListener(deleteRecord);
	}
	
	public void addBorrowListener(ActionListener borrowBook){
		btnBorrow.addActionListener(borrowBook);
	}
	
	public void addReserveListener(ActionListener reserveBook){
		btnReserve.addActionListener(reserveBook);
	}
	
	public Book getCurrBook() {	
		currBook.setTitle(txtFldTitle.getText());
		currBook.setAuthor(txtFldAuthor.getText());
		currBook.setYearPublish(Integer.parseInt(txtFldYrPublished.getText()));
		currBook.setPublisher(txtFldPublisher.getText());
		currBook.setIsbn(txtFldISBN.getText());
		currBook.setDescription(txtFldDescription.getText());
		currBook.setCopies(Integer.parseInt(spinCopyVal.getModel().getValue().toString()));
		return currBook;
	}
	
	public void setBookInfoData(Book book){
		currBook = book;
		txtFldTitle.setText(book.getTitle());
		txtFldAuthor.setText(book.getAuthor());
		txtFldYrPublished.setText(Integer.toString(book.getYearPublish()));
		txtFldPublisher.setText(book.getPublisher());
		txtFldISBN.setText(book.getIsbn());
		txtFldDescription.setText(book.getDescription());
		spinCopyVal.getModel().setValue(book.getCopies());
		lblCopiesVal.setText(book.getClass().toString());
	}

	public JLabel getLblErrorMsg() {
		return lblErrorMsg;
	}

	public JTextField getTxtFldTitle() {
		return txtFldTitle;
	}

	public JTextField getTxtFldAuthor() {
		return txtFldAuthor;
	}

	public JTextField getTxtFldYrPublished() {
		return txtFldYrPublished;
	}

	public JTextField getTxtFldPublisher() {
		return txtFldPublisher;
	}

	public JTextField getTxtFldISBN() {
		return txtFldISBN;
	}

	public JTextArea getTxtFldDescription() {
		return txtFldDescription;
	}

	public void setLblErrorMsg(JLabel lblErrorMsg) {
		this.lblErrorMsg = lblErrorMsg;
	}

	public JSpinner getSpinCopyVal() {
		return spinCopyVal;
	}
	
}