package views;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import utilities.ErrorLabel;

import models.Book;
import models.User;
import net.miginfocom.swing.MigLayout;

public class BookInfoPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField titleTextField;
	private JTextField authorTextField;
	private JTextField yearPublishTextField;
	private JTextField publisherTextField;
	private JTextField isbnTextField;
	private JButton saveButton;
	private JButton deleteButton;
	private JButton borrowButton;
	private JButton reserveButton;
	private User currentUser;
	private Book currentBook;
	private JLabel errorMessageLabel;
	private JTextArea descriptionTextArea;
	private JSpinner copiesValSpinner;
	private JLabel lblCopies;
	private JTextField editionTextField;
	private JLabel lblEdition;

	/**
	 * Create the panel.
	 */

	public BookInfoPanel(Book book, User user) {
		this.currentUser = user;
		this.currentBook = book;
		displayBookInfo();
	}

	private void displayBookInfo() {

		setLayout(new MigLayout("", "[14.00][38.00][13.00][143.00,grow][144]", "[40][][][][][][][grow][48.00][34.00][13.00][]"));

		errorMessageLabel = new ErrorLabel("");
		add(errorMessageLabel, "cell 3 0 2 1,alignx center,aligny center");

		JLabel lblTitle = new JLabel("Title:");
		add(lblTitle, "cell 1 1");

		titleTextField = new JTextField(currentBook.getTitle());
		titleTextField.setName("titleTextField");
		titleTextField.setEditable(false);
		add(titleTextField, "cell 3 1 2 1,growx");
		titleTextField.setColumns(10);
		
		lblEdition = new JLabel("Edition:");
		add(lblEdition, "cell 1 2");
		
		editionTextField = new JTextField(currentBook.getEdition());
		editionTextField.setEditable(false);
		add(editionTextField, "cell 3 2 2 1,growx");
		editionTextField.setColumns(10);

		JLabel lblAuthor = new JLabel("Author:");
		add(lblAuthor, "cell 1 3");

		authorTextField = new JTextField(currentBook.getAuthor());
		authorTextField.setName("authorTextField");
		authorTextField.setEditable(false);
		add(authorTextField, "cell 3 3 2 1,growx");
		authorTextField.setColumns(10);

		JLabel lblYearPublished = new JLabel("Year Published:");
		add(lblYearPublished, "cell 1 4");

		yearPublishTextField = new JTextField(Integer.toString(currentBook
				.getYearPublish()));
		yearPublishTextField.setName("yearPublishTextField");
		yearPublishTextField.setEditable(false);
		add(yearPublishTextField, "cell 3 4 2 1,growx");
		yearPublishTextField.setColumns(10);

		JLabel lblPublisher = new JLabel("Publisher:");
		add(lblPublisher, "cell 1 5");

		publisherTextField = new JTextField(currentBook.getPublisher());
		publisherTextField.setName("publisherTextField");
		publisherTextField.setEditable(false);
		add(publisherTextField, "cell 3 5 2 1,growx");
		publisherTextField.setColumns(10);

		JLabel lblIsbn = new JLabel("ISBN:");
		add(lblIsbn, "cell 1 6");

		isbnTextField = new JTextField(currentBook.getIsbn());
		isbnTextField.setName("isbnTextField");
		isbnTextField.setEditable(false);
		add(isbnTextField, "cell 3 6 2 1,growx");
		isbnTextField.setColumns(10);

		JLabel lblDescription = new JLabel("Description:");
		add(lblDescription, "cell 1 7");

		descriptionTextArea = new JTextArea(currentBook.getDescription());
		descriptionTextArea.setName("descriptionTextArea");
		descriptionTextArea.setEditable(false);
		add(descriptionTextArea, "cell 3 7 2 3,grow");

		//JScrollPane scrollDes = new JScrollPane(descriptionTextArea);
		//add(scrollDes, "cell 3 6 2 3,grow");

		lblCopies = new JLabel("Copies:");

		copiesValSpinner = new JSpinner();
		copiesValSpinner.setModel(new SpinnerNumberModel(0, 0, 2147483647, 1));
		copiesValSpinner.getModel().setValue(currentBook.getCopies());

		saveButton = new JButton("Save", new ImageIcon("resources/images/save32x32.png"));
		deleteButton = new JButton("Delete", new ImageIcon("resources/images/delete.png"));
		borrowButton = new JButton("Borrow", new ImageIcon("resources/images/request.png"));
		reserveButton = new JButton("Reserve", new ImageIcon("resources/images/reserve.png"));

		if (currentUser.getType().equals("Librarian")) {
			add(saveButton, "cell 3 10,alignx right");
			add(deleteButton, "cell 4 10");
			authorTextField.setEditable(true);
			descriptionTextArea.setEditable(true);
			isbnTextField.setEditable(true);
			publisherTextField.setEditable(true);
			titleTextField.setEditable(true);
			yearPublishTextField.setEditable(true);
			editionTextField.setEditable(true);
			add(lblCopies, "cell 1 9,alignx left,aligny center");
			add(copiesValSpinner, "cell 3 9,alignx left,aligny center");
		}
		if (currentUser.getType().equals("User")) {
			add(borrowButton, "cell 3 10,alignx right");
			add(reserveButton, "cell 4 10");
		}

	}

	public JButton getBtnBorrow() {
		return borrowButton;
	}

	public JButton getBtnSave() {
		return saveButton;
	}

	public void setBtnSave(JButton btnSave) {
		this.saveButton = btnSave;
	}

	public JButton getBtnDelete() {
		return deleteButton;
	}

	public void setBtnDelete(JButton btnDelete) {
		this.deleteButton = btnDelete;
	}

	public void setBtnBorrow(JButton btnBorrow) {
		this.borrowButton = btnBorrow;
	}

	public JButton getBtnReserve() {
		return reserveButton;
	}

	public void setBtnReserve(JButton btnReserve) {
		this.reserveButton = btnReserve;
	}

	public void addSaveListener(ActionListener saveInfo) {
		saveButton.addActionListener(saveInfo);
	}

	public void addDeleteListener(ActionListener deleteRecord) {
		deleteButton.addActionListener(deleteRecord);
	}

	public void addBorrowListener(ActionListener borrowBook) {
		borrowButton.addActionListener(borrowBook);
	}

	public void addReserveListener(ActionListener reserveBook) {
		reserveButton.addActionListener(reserveBook);
	}

	public Book getCurrBook() {
		currentBook.setTitle(titleTextField.getText());
		currentBook.setAuthor(authorTextField.getText());
		currentBook.setYearPublish(Integer.parseInt(yearPublishTextField
				.getText()));
		currentBook.setPublisher(publisherTextField.getText());
		currentBook.setIsbn(isbnTextField.getText());
		currentBook.setDescription(descriptionTextArea.getText());
		currentBook.setCopies(Integer.parseInt(copiesValSpinner.getModel()
				.getValue().toString()));
		currentBook.setEdition(editionTextField.getText());
		return currentBook;
	}

	public void setBookInfoData(Book book) {
		currentBook = book;
		titleTextField.setText(book.getTitle());
		authorTextField.setText(book.getAuthor());
		yearPublishTextField.setText(Integer.toString(book.getYearPublish()));
		publisherTextField.setText(book.getPublisher());
		isbnTextField.setText(book.getIsbn());
		descriptionTextArea.setText(book.getDescription());
		copiesValSpinner.getModel().setValue(book.getCopies());
		editionTextField.setText(book.getEdition());
	}

	public JLabel getLblErrorMsg() {
		return errorMessageLabel;
	}

	public JTextField getTxtFldTitle() {
		return titleTextField;
	}

	public JTextField getTxtFldAuthor() {
		return authorTextField;
	}

	public JTextField getTxtFldYrPublished() {
		return yearPublishTextField;
	}

	public JTextField getTxtFldPublisher() {
		return publisherTextField;
	}

	public JTextField getTxtFldISBN() {
		return isbnTextField;
	}

	public JTextArea getTxtFldDescription() {
		return descriptionTextArea;
	}

	public JTextField getTxtFldEdition() {
		return editionTextField;
	}

	public void setLblErrorMsg(JLabel lblErrorMsg) {
		this.errorMessageLabel = lblErrorMsg;
	}

	public JSpinner getSpinCopyVal() {
		return copiesValSpinner;
	}

}