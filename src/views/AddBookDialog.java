package views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import models.Book;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

import javax.swing.SpinnerNumberModel;

import utilities.ErrorLabel;
import utilities.IsbnUtil;
import utilities.MyJSpinner;
import utilities.MyTextArea;
import utilities.MyTextField;
import utilities.Strings;

public class AddBookDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = new JPanel();
	private MyTextField titleTextField = new MyTextField(100);
	private MyTextField authorTextField = new MyTextField(100);
	private MyTextField yearPublishTextField = new MyTextField(4);
	private MyTextField publisherTextField = new MyTextField(100);
	private MyTextField isbnTextField = new MyTextField(13);
	private MyTextField editionTextField = new MyTextField(30);
	private MyTextArea descriptionTextArea = new MyTextArea(1000);
	private ErrorLabel errorMessageLabel = new ErrorLabel();
	private MyJSpinner copyValSpinner = new MyJSpinner();
	private JButton addButton;
	private JButton cancelButton;
	private JLabel editionLabel;

	/**
	 * Create the dialog.
	 */
	public AddBookDialog() {
		setModal(true);
		setTitle("Add Book");
		setBounds(100, 100, 450, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[fill][grow]",
				"[20.00][][20.00][20.00][20.00][20.00][20.00,grow][20.00]"));

		JLabel titleLabel = new JLabel(Strings.TITLE_LABEL + ":*");
		contentPanel.add(titleLabel, "cell 0 0,alignx left");
		titleTextField.setToolTipText("*Maximum of 100 characters.");

		titleTextField.setName("addTitleTextField");
		contentPanel.add(titleTextField, "cell 1 0,growx");

		editionLabel = new JLabel(Strings.EDITION_LABEL);
		contentPanel.add(editionLabel, "cell 0 1,alignx left");

		editionTextField.setName("editionTextField");
		contentPanel.add(editionTextField, "cell 1 1,growx");
		editionTextField.setColumns(10);

		JLabel authorLabel = new JLabel(Strings.AUTHOR_LABEL + "*");
		contentPanel.add(authorLabel, "cell 0 2,alignx left");
		authorTextField.setToolTipText("*Maximum of 100 characters.");

		authorTextField.setName("addAuthorTextField");
		contentPanel.add(authorTextField, "cell 1 2,growx");
		authorTextField.setColumns(10);

		JLabel yearPublishLabel = new JLabel(Strings.YEAR_LABEL);
		contentPanel.add(yearPublishLabel, "cell 0 3,alignx left");

		yearPublishTextField.setName("addYearPublishTextField");
		contentPanel.add(yearPublishTextField, "cell 1 3,growx");
		yearPublishTextField.setColumns(10);

		JLabel publisherLabel = new JLabel(Strings.PUBLISHER_LABEL);
		contentPanel.add(publisherLabel, "cell 0 4,alignx left");

		publisherTextField.setName("publisherTextField");
		contentPanel.add(publisherTextField, "cell 1 4,growx");
		publisherTextField.setColumns(10);

		JLabel isbnLabel = new JLabel(Strings.ISBN_LABEL + ":*");
		contentPanel.add(isbnLabel, "cell 0 5,alignx left");
		isbnTextField.setToolTipText("*Must be a valid ISBN");

		isbnTextField.setName("isbnTextField");
		contentPanel.add(isbnTextField, "cell 1 5,growx");
		isbnTextField.setColumns(10);

		JLabel descriptionLabel = new JLabel(Strings.DESCRIPTION_LABEL);
		contentPanel.add(descriptionLabel, "cell 0 6,alignx left,aligny top");

		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "cell 1 6,grow");

		descriptionTextArea.setName("descriptionTextArea");
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		scrollPane.setViewportView(descriptionTextArea);

		JLabel copiesLabel = new JLabel(Strings.COPIES_LABEL);
		contentPanel.add(copiesLabel, "cell 0 7,alignx left");

		copyValSpinner.setModel(new SpinnerNumberModel(1, 0, 1000, 1));
		contentPanel.add(copyValSpinner, "cell 1 7,alignx left");

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane
				.setLayout(new MigLayout("", "[298.00][47px][65px]", "[23px]"));

		buttonPane.add(errorMessageLabel, "cell 0 0,alignx left,aligny center");

		addButton = new JButton(Strings.ADD_BOOK_BUTTON);
		addButton.setActionCommand("OK");
		buttonPane.add(addButton, "cell 1 0,alignx left,aligny top");
		getRootPane().setDefaultButton(addButton);

		cancelButton = new JButton(Strings.CANCEL_BUTTON);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton, "cell 2 0,alignx left,aligny top");

		this.setResizable(false);
	}

	public void addFocusListeners(FocusListener isbn10Listener,
			FocusListener titleListener, FocusListener authorListener,
			FocusListener yearCheckListener) {

		titleTextField.addFocusListener(titleListener);
		authorTextField.addFocusListener(authorListener);
		yearPublishTextField.addFocusListener(yearCheckListener);
		isbnTextField.addFocusListener(isbn10Listener);
	}

	public String getTitle() {
		return titleTextField.getText().trim();
	}

	public void hasTitleError(boolean error) {
		titleTextField.hasError(error);
	}

	public String getAuthor() {
		return authorTextField.getText().trim();
	}

	public void hasAuthorError(boolean error) {
		authorTextField.hasError(error);
	}

	public String getYearPublished() {
		return yearPublishTextField.getText();
	}

	public void hasYearPublishedError(boolean error) {
		yearPublishTextField.hasError(error);
	}

	public String getIsbn() {
		return isbnTextField.getText();
	}

	public void hasIsbnError(boolean error) {
		isbnTextField.hasError(error);
	}

	public ErrorLabel getErrorMessageLabel() {
		return errorMessageLabel;
	}

	public void addBookActionListener(ActionListener add) {
		addButton.addActionListener(add);
	}

	public void addCancelActionListener(ActionListener cancel) {
		cancelButton.addActionListener(cancel);
	}

	public void clearBookFields() {
		descriptionTextArea.setText("");
		authorTextField.setText("");
		editionTextField.setText("");
		isbnTextField.setText("");
		publisherTextField.setText("");
		titleTextField.setText("");
		yearPublishTextField.setText("");
		copyValSpinner.getModel().setValue(1);
	}

	public Book getBookInfo() {
		Book newBook = new Book();
		newBook.setAuthor(authorTextField.getText().trim());
		newBook.setCopies(Integer.parseInt(copyValSpinner.getModel().getValue()
				.toString()));
		newBook.setDescription(descriptionTextArea.getText().trim());
		newBook.setIsbn10(isbnTextField.getText());
		newBook.setPublisher(publisherTextField.getText().trim());
		newBook.setTitle(titleTextField.getText().trim());
		newBook.setEdition(editionTextField.getText().trim());
		if (yearPublishTextField.getText().length() > 0) {
			newBook.setYearPublish(Integer.parseInt(yearPublishTextField
					.getText()));
		}
		if (isbnTextField.getText().length() == 10) {
			newBook.setIsbn10(isbnTextField.getText());
			newBook.setIsbn13(IsbnUtil.convert(isbnTextField.getText()));
		} else {
			newBook.setIsbn13(isbnTextField.getText());
			newBook.setIsbn10(IsbnUtil.convert(isbnTextField.getText()));
		}
		return newBook;
	}
}
