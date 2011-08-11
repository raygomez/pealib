package views;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpinnerNumberModel;

import utilities.ErrorLabel;
import utilities.MyJSpinner;
import utilities.MyTextArea;
import utilities.MyTextField;

import models.Book;
import models.User;
import net.miginfocom.swing.MigLayout;
import javax.swing.SwingConstants;

public class BookInfoPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyTextField titleTextField = new MyTextField(100);
	private MyTextField authorTextField = new MyTextField(100);
	private MyTextField yearPublishTextField = new MyTextField(4);
	private MyTextField publisherTextField = new MyTextField(100);
	private MyTextField isbnTextField = new MyTextField(13);
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
	private JLabel lblCopies;
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

		setLayout(new MigLayout("", "[14.00][38.00][13.00][143.00,grow][][][]", "[40][][][][][][][grow][48.00][34.00][][13.00][]"));
        errorMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(errorMessageLabel, "cell 0 0, span 6 1,alignx center,aligny center");

		JLabel lblTitle = new JLabel("Title:");
		add(lblTitle, "cell 1 1");

		titleTextField.setText(currentBook.getTitle());
		titleTextField.setName("titleTextField");
		titleTextField.setEditable(false);
		add(titleTextField, "cell 3 1 2 1,growx");
		titleTextField.setColumns(10);
		
		lblEdition = new JLabel("Edition:");
		add(lblEdition, "cell 1 2");
		
		editionTextField.setText(currentBook.getEdition());
		editionTextField.setName("editionTextField");
		editionTextField.setEditable(false);
		add(editionTextField, "cell 3 2 2 1,growx");
		editionTextField.setColumns(10);

		JLabel lblAuthor = new JLabel("Author:");
		add(lblAuthor, "cell 1 3");

		authorTextField.setText(currentBook.getAuthor());
		authorTextField.setName("authorTextField");
		authorTextField.setEditable(false);
		add(authorTextField, "cell 3 3 2 1,growx");
		authorTextField.setColumns(10);

		JLabel lblYearPublished = new JLabel("Year Published:");
		add(lblYearPublished, "cell 1 4");

		if(currentBook.getYearPublish() == 0){
			yearPublishTextField.setText("");
		}else 
			yearPublishTextField.setText(Integer.toString(currentBook
				.getYearPublish()));
		
		yearPublishTextField.setName("yearPublishTextField");
		yearPublishTextField.setEditable(false);
		add(yearPublishTextField, "cell 3 4 2 1,growx");
		yearPublishTextField.setColumns(10);

		JLabel lblPublisher = new JLabel("Publisher:");
		add(lblPublisher, "cell 1 5");

		publisherTextField.setText(currentBook.getPublisher());
		publisherTextField.setName("publisherTextField");
		publisherTextField.setEditable(false);
		add(publisherTextField, "cell 3 5 2 1,growx");
		publisherTextField.setColumns(10);

		JLabel lblIsbn = new JLabel("ISBN:");
		add(lblIsbn, "cell 1 6");

		isbnTextField.setText(currentBook.getIsbn10());
		isbnTextField.setName("isbnTextField");
		isbnTextField.setEditable(false);
		add(isbnTextField, "cell 3 6 2 1,growx");
		isbnTextField.setColumns(10);

		JLabel lblDescription = new JLabel("Description:");
		add(lblDescription, "cell 1 7");
                
		descriptionTextArea.setText(currentBook.getDescription());
        descriptionTextArea.setName("descriptionTextArea");
        descriptionTextArea.setEditable(false);

        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionTextArea);		
        scrollPane.setViewportView(descriptionTextArea);
        add(descriptionTextArea, "cell 3 7 2 3,grow");

		saveButton = new JButton("Save", new ImageIcon("resources/images/save32x32.png"));
		deleteButton = new JButton("Delete", new ImageIcon("resources/images/delete.png"));
		borrowButton = new JButton("Borrow", new ImageIcon("resources/images/request.png"));
		reserveButton = new JButton("Reserve", new ImageIcon("resources/images/reserve.png"));
			
		JScrollPane scrollDes = new JScrollPane(descriptionTextArea);
		add(scrollDes, "cell 3 7 2 3,grow");
			
		lblCopies = new JLabel("Copies:");
			
		copiesValSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
		copiesValSpinner.getModel().setValue(currentBook.getCopies());
					

		if (currentUser.getType().equals("Librarian")) {
			add(saveButton, "cell 3 11,alignx right");
			add(deleteButton, "cell 4 11");
			authorTextField.setEditable(true);
			descriptionTextArea.setEditable(true);
			isbnTextField.setEditable(true);
			publisherTextField.setEditable(true);
			titleTextField.setEditable(true);
			yearPublishTextField.setEditable(true);
			editionTextField.setEditable(true);
			add(copiesValSpinner, "cell 3 10,alignx left,aligny center");
			add(lblCopies, "cell 1 10,alignx left,aligny center");
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

	public JButton getBtnDelete() {
		return deleteButton;
	}

	public JButton getBtnReserve() {
		return reserveButton;
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
		currentBook.setTitle(titleTextField.getText().trim());
		currentBook.setAuthor(authorTextField.getText().trim());
		currentBook.setPublisher(publisherTextField.getText().trim());
		currentBook.setIsbn10(isbnTextField.getText());
		currentBook.setDescription(descriptionTextArea.getText().trim());
		currentBook.setCopies(Integer.parseInt(copiesValSpinner.getModel()
				.getValue().toString()));
		currentBook.setEdition(editionTextField.getText().trim());
		if(yearPublishTextField.getText().trim().length() > 0){
			currentBook.setYearPublish(Integer.parseInt(yearPublishTextField
					.getText()));
		}else{
			currentBook.setYearPublish(0);
		}
		return currentBook;
	}

	public void setBookInfoData(Book book) {
		currentBook = book;
		titleTextField.setText(book.getTitle());
		authorTextField.setText(book.getAuthor());
		publisherTextField.setText(book.getPublisher());
		isbnTextField.setText(book.getIsbn10());
		descriptionTextArea.setText(book.getDescription());
		copiesValSpinner.getModel().setValue(book.getCopies());
		editionTextField.setText(book.getEdition());
		
		if(book.getYearPublish() == 0){
			yearPublishTextField.setText("");
		}else{
			yearPublishTextField.setText(Integer.toString(book.getYearPublish()));
		}
	}
	
	public void resetErrors(){
		errorMessageLabel.setText("");
		titleTextField.hasError(false);
		authorTextField.hasError(false);
		yearPublishTextField.hasError(false);
		publisherTextField.hasError(false);
		isbnTextField.hasError(false);
		descriptionTextArea.hasError(false);
		editionTextField.hasError(false);	
		copiesValSpinner.hasError(false);
	}

	public ErrorLabel getLblErrorMsg() {
		return errorMessageLabel;
	}

	public MyTextField getTxtFldTitle() {
		return titleTextField;
	}

	public MyTextField getTxtFldAuthor() {
		return authorTextField;
	}

	public MyTextField getTxtFldYrPublished() {
		return yearPublishTextField;
	}

	public MyTextField getTxtFldPublisher() {
		return publisherTextField;
	}

	public MyTextField getTxtFldISBN() {
		return isbnTextField;
	}

	public MyTextArea getTxtFldDescription() {
		return descriptionTextArea;
	}

	public MyTextField getTxtFldEdition() {
		return editionTextField;
	}

	public MyJSpinner getSpinCopyVal() {
		return copiesValSpinner;
	}

}