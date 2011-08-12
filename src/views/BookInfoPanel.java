package views;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import models.Book;
import models.User;
import net.miginfocom.swing.MigLayout;
import utilities.ErrorLabel;
import utilities.MyJSpinner;
import utilities.MyTextArea;
import utilities.MyTextField;

public class BookInfoPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyTextField titleTextField = new MyTextField(100);
	private MyTextField authorTextField = new MyTextField(100);
	private MyTextField yearPublishTextField = new MyTextField(4);
	private MyTextField publisherTextField = new MyTextField(100);
	private MyTextField isbn10TextField = new MyTextField(10);
	private MyTextField isbn13TextField = new MyTextField(13);
	private MyTextField editionTextField = new MyTextField(30);
	private MyTextArea descriptionTextArea = new MyTextArea("", 20, 20, 1000);
	private MyJSpinner copiesValSpinner = new MyJSpinner();
	private JButton saveButton;
	private JButton deleteButton;
	private JButton borrowButton;
	private JButton reserveButton;
	private User currentUser;
	private Book currentBook;
	private ErrorLabel errorMessageLabel = new ErrorLabel();
	private JLabel copiesLabel;
	private JLabel editionLabel;

	/**
	 * Create the panel.
	 */

	public BookInfoPanel(Book book, User user) {
		isbn13TextField.setColumns(10);
		this.currentUser = user;
		this.currentBook = book;
		displayBookInfo();
	}

	private void displayBookInfo() {

		setLayout(new MigLayout("", "[14.00][38.00][13.00][143.00,grow][][][]",
				"[40][][][][][][][][grow][48.00][34.00][][13.00][]"));
		errorMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);

		add(errorMessageLabel, "cell 0 0, span 6 1,alignx center,aligny center");

		JLabel lblTitle = new JLabel("Title:");
		add(lblTitle, "cell 1 1");

		titleTextField.setText(currentBook.getTitle());
		titleTextField.setName("titleTextField");
		titleTextField.setEditable(false);
		add(titleTextField, "cell 3 1 2 1,growx");
		titleTextField.setColumns(10);

		editionLabel = new JLabel("Edition:");
		add(editionLabel, "cell 1 2");

		editionTextField.setText(currentBook.getEdition());
		editionTextField.setName("editionTextField");
		editionTextField.setEditable(false);
		add(editionTextField, "cell 3 2 2 1,growx");
		editionTextField.setColumns(10);

		JLabel authorLabel = new JLabel("Author:");
		add(authorLabel, "cell 1 3");

		authorTextField.setText(currentBook.getAuthor());
		authorTextField.setName("authorTextField");
		authorTextField.setEditable(false);
		add(authorTextField, "cell 3 3 2 1,growx");
		authorTextField.setColumns(10);

		JLabel yearPublishedLabel = new JLabel("Year Published:");
		add(yearPublishedLabel, "cell 1 4");

		if (currentBook.getYearPublish() == 0) {
			yearPublishTextField.setText("");
		} else
			yearPublishTextField.setText(Integer.toString(currentBook
					.getYearPublish()));

		yearPublishTextField.setName("yearPublishTextField");
		yearPublishTextField.setEditable(false);
		add(yearPublishTextField, "cell 3 4 2 1,growx");
		yearPublishTextField.setColumns(10);

		JLabel publisherLabel = new JLabel("Publisher:");
		add(publisherLabel, "cell 1 5");

		publisherTextField.setText(currentBook.getPublisher());
		publisherTextField.setName("publisherTextField");
		publisherTextField.setEditable(false);
		add(publisherTextField, "cell 3 5 2 1,growx");
		publisherTextField.setColumns(10);

		JLabel isbn10Label = new JLabel("ISBN10:");
		add(isbn10Label, "cell 1 6");

		isbn10TextField.setText(currentBook.getIsbn10());
		isbn10TextField.setName("isbnTextField10");
		isbn10TextField.setEditable(false);
		add(isbn10TextField, "cell 3 6 2 1,growx");
		isbn10TextField.setColumns(10);

		JLabel isbn13Label = new JLabel("ISBN13:");
		add(isbn13Label, "cell 1 7");

		isbn13TextField.setText(currentBook.getIsbn13());
		isbn13TextField.setName("isbnTextField13");
		isbn13TextField.setEditable(false);
		add(isbn13TextField, "cell 3 7 2 1,growx");
		isbn10TextField.setColumns(13);

		JLabel descriptionLabel = new JLabel("Description:");
		add(descriptionLabel, "cell 1 8");

		descriptionTextArea.setText(currentBook.getDescription());
		descriptionTextArea.setName("descriptionTextArea");
		descriptionTextArea.setEditable(false);

		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(descriptionTextArea);
		scrollPane.setViewportView(descriptionTextArea);
		add(descriptionTextArea, "cell 3 8 2 3,grow");

		saveButton = new JButton("Save", new ImageIcon(
				"resources/images/save32x32.png"));
		deleteButton = new JButton("Delete", new ImageIcon(
				"resources/images/delete.png"));
		borrowButton = new JButton("Borrow", new ImageIcon(
				"resources/images/request.png"));
		reserveButton = new JButton("Reserve", new ImageIcon(
				"resources/images/reserve.png"));

		JScrollPane scrollDes = new JScrollPane(descriptionTextArea);
		add(scrollDes, "cell 3 8 2 3,grow");

		copiesLabel = new JLabel("Copies:");

		copiesValSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
		copiesValSpinner.getModel().setValue(currentBook.getCopies());

		if (currentUser.getType().equals("Librarian")) {
			add(saveButton, "cell 3 13,alignx right");
			add(deleteButton, "cell 4 13");
			authorTextField.setEditable(true);
			descriptionTextArea.setEditable(true);
			isbn10TextField.setEditable(true);
			isbn13TextField.setEditable(true);
			publisherTextField.setEditable(true);
			titleTextField.setEditable(true);
			yearPublishTextField.setEditable(true);
			editionTextField.setEditable(true);
			add(copiesValSpinner, "cell 3 12,alignx left,aligny center");
			add(copiesLabel, "cell 1 12,alignx left,aligny center");
		}
		if (currentUser.getType().equals("User")) {
			add(borrowButton, "cell 3 13,alignx right");
			add(reserveButton, "cell 4 13");
		}

	}

	public void setBorrowButtonEnable(boolean isEnabled) {
		borrowButton.setEnabled(isEnabled);
	}

	public void setReserveButtonEnable(boolean isEnabled) {
		reserveButton.setEnabled(isEnabled);
	}

	public void setSaveButtonEnable(boolean isEnabled) {
		saveButton.setEnabled(isEnabled);
	}

	public void setDeleteButtonEnable(boolean isEnabled) {
		deleteButton.setEnabled(isEnabled);
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

	public void addFocusListeners(FocusListener isbn10Listener,
			FocusListener isbn13Listener, FocusListener titleListener,
			FocusListener authorListener, FocusListener yearCheckListener) {

			titleTextField.addFocusListener(titleListener);
			authorTextField.addFocusListener(authorListener);
			yearPublishTextField.addFocusListener(yearCheckListener);
			isbn10TextField.addFocusListener(isbn10Listener);
			isbn13TextField.addFocusListener(isbn13Listener);
	}

	public Book getCurrBook() {
		currentBook.setTitle(titleTextField.getText().trim());
		currentBook.setAuthor(authorTextField.getText().trim());
		currentBook.setPublisher(publisherTextField.getText().trim());
		currentBook.setIsbn10(isbn10TextField.getText());
		currentBook.setIsbn13(isbn13TextField.getText());
		currentBook.setDescription(descriptionTextArea.getText().trim());
		currentBook.setCopies(Integer.parseInt(copiesValSpinner.getModel()
				.getValue().toString()));
		currentBook.setEdition(editionTextField.getText().trim());
		if (yearPublishTextField.getText().trim().length() > 0) {
			currentBook.setYearPublish(Integer.parseInt(yearPublishTextField
					.getText()));
		} else {
			currentBook.setYearPublish(0);
		}
		return currentBook;
	}

	public void setBookInfoData(Book book) {
		currentBook = book;
		titleTextField.setText(book.getTitle());
		authorTextField.setText(book.getAuthor());
		publisherTextField.setText(book.getPublisher());
		isbn10TextField.setText(book.getIsbn10());
		isbn13TextField.setText(book.getIsbn13());
		descriptionTextArea.setText(book.getDescription());
		copiesValSpinner.getModel().setValue(book.getCopies());
		editionTextField.setText(book.getEdition());

		if (book.getYearPublish() == 0) {
			yearPublishTextField.setText("");
		} else {
			yearPublishTextField
					.setText(Integer.toString(book.getYearPublish()));
		}
	}

	public void resetErrors() {
		errorMessageLabel.setText("");
		titleTextField.hasError(false);
		authorTextField.hasError(false);
		yearPublishTextField.hasError(false);
		publisherTextField.hasError(false);
		isbn10TextField.hasError(false);
		isbn13TextField.hasError(false);
		descriptionTextArea.hasError(false);
		editionTextField.hasError(false);
		copiesValSpinner.hasError(false);
	}

	public ErrorLabel getLblErrorMsg() {
		return errorMessageLabel;
	}

	public String getTitle() {
		return titleTextField.getText().trim();
	}
	
	public void hasTitleError(boolean error){
		titleTextField.hasError(error);
	}

	public String getAuthor(){
		return authorTextField.getText().trim();
	}

	public void hasAuthorError(boolean error){
		authorTextField.hasError(error);
	}
	
	public String getYearPublished(){
		return yearPublishTextField.getText();
	}
	
	public void hasYearPublishedError(boolean error){
		yearPublishTextField.hasError(error);
	}
	
	public String getIsbn10(){
		return isbn10TextField.getText();
	}
	
	public void setIsbn10(String isbn10){
		isbn10TextField.setText(isbn10);
	}
	
	public void hasIsbn10Error(boolean error){
		isbn10TextField.hasError(error);
	}
	
	public String getIsbn13(){
		return isbn13TextField.getText();
	}

	public void setIsbn13(String isbn13){
		isbn13TextField.setText(isbn13);
	}
	
	public void hasIsbn13Error(boolean error){
		isbn13TextField.hasError(error);
	}

	public int getCopy(){
		return (Integer)copiesValSpinner.getValue();
	}
	
	public void hasCopyError(boolean error){
		copiesValSpinner.hasError(error);
	}

}