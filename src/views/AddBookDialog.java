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
import javax.swing.SpinnerNumberModel;

import utilities.ErrorLabel;
import utilities.MyJSpinner;
import utilities.MyTextArea;
import utilities.MyTextField;

public class AddBookDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private MyTextField titleTextField = new MyTextField(100);
	private MyTextField authorTextField = new MyTextField(100);
	private MyTextField yearPublishTextField = new MyTextField(4);
	private MyTextField publisherTextField = new MyTextField(100);
	private MyTextField isbnTextField = new MyTextField(13);
	private MyTextField editionTextField = new MyTextField(30);
	private MyTextArea descriptionTextArea = new MyTextArea(1000);
	private ErrorLabel errorMessageLabel = new ErrorLabel();
	private JButton addButton;
	private JButton cancelButton;
	private MyJSpinner copyValSpinner = new MyJSpinner();
	private JLabel lblEdition;

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
		contentPanel.setLayout(new MigLayout("", "[fill][grow]", "[20.00][][20.00][20.00][20.00][20.00][20.00,grow][20.00]"));

		JLabel lblTitle = new JLabel("Title:");
		contentPanel.add(lblTitle, "cell 0 0,alignx left");
		titleTextField.setToolTipText("*Maximum of 100 characters.");

		titleTextField.setName("addTitleTextField");
		contentPanel.add(titleTextField, "cell 1 0,growx");
		
		lblEdition = new JLabel("Edition:");
		contentPanel.add(lblEdition, "cell 0 1,alignx left");
		
		editionTextField.setName("editionTextField");
		contentPanel.add(editionTextField, "cell 1 1,growx");
		editionTextField.setColumns(10);

		JLabel lblAuthor = new JLabel("Author:");
		contentPanel.add(lblAuthor, "cell 0 2,alignx left");
		authorTextField.setToolTipText("*Maximum of 100 characters.");

		authorTextField.setName("addAuthorTextField");
		contentPanel.add(authorTextField, "cell 1 2,growx");
		authorTextField.setColumns(10);

		JLabel lblYearPublish = new JLabel("Year Published:");
		contentPanel.add(lblYearPublish, "cell 0 3,alignx left");

		yearPublishTextField.setName("addYearPublishTextField");
		contentPanel.add(yearPublishTextField, "cell 1 3,growx");
		yearPublishTextField.setColumns(10);

		JLabel lblPublisher = new JLabel("Publisher:");
		contentPanel.add(lblPublisher, "cell 0 4,alignx left");

		publisherTextField.setName("publisherTextField");
		contentPanel.add(publisherTextField, "cell 1 4,growx");
		publisherTextField.setColumns(10);

		JLabel lblIsbn = new JLabel("ISBN:");
		contentPanel.add(lblIsbn, "cell 0 5,alignx left");
		isbnTextField.setToolTipText("*Must be a valid ISBN");

		
		isbnTextField.setName("isbnTextField");
		contentPanel.add(isbnTextField, "cell 1 5,growx");
		isbnTextField.setColumns(10);

		JLabel lblDescription = new JLabel("Description:");
		contentPanel.add(lblDescription, "cell 0 6,alignx left,aligny top");

		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "cell 1 6,grow");

        descriptionTextArea.setName("descriptionTextArea");
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        scrollPane.setViewportView(descriptionTextArea);
                                        		

		JLabel lblCopies = new JLabel("Copies:");
		contentPanel.add(lblCopies, "cell 0 7,alignx left");

		copyValSpinner.setModel(new SpinnerNumberModel(1, 0, 1000, 1));
		contentPanel.add(copyValSpinner, "cell 1 7,alignx left");

		JPanel buttonPane = new JPanel();
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane
				.setLayout(new MigLayout("", "[298.00][47px][65px]", "[23px]"));

		buttonPane.add(errorMessageLabel, "cell 0 0,alignx left,aligny center");

		addButton = new JButton("Add");
		addButton.setActionCommand("OK");
		buttonPane.add(addButton, "cell 1 0,alignx left,aligny top");
		getRootPane().setDefaultButton(addButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton, "cell 2 0,alignx left,aligny top");

		this.setResizable(false);
	}

	public MyTextField getTxtFldTitle() {
		return titleTextField;
	}

	public MyTextField getTxtFldAuthor() {
		return authorTextField;
	}

	public MyTextField getTxtFldYearPublish() {
		return yearPublishTextField;
	}

	public MyTextField getTxtFldPublisher() {
		return publisherTextField;
	}

	public MyTextField getTxtFldIsbn() {
		return isbnTextField;
	}

	public MyTextArea getTxtAreaDescription() {
		return descriptionTextArea;
	}

	public MyTextField getTxtFldEdition() {
		return editionTextField;
	}

	public ErrorLabel getLblErrorMsg() {
		return errorMessageLabel;
	}
	
	public MyJSpinner getCopyValSpinner() {
		return copyValSpinner;
	}

	public void addBookActionListener(ActionListener add) {
		addButton.addActionListener(add);
	}

	public void addCancelActionListener(ActionListener cancel) {
		cancelButton.addActionListener(cancel);
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
		if(yearPublishTextField.getText().length() > 0){
			newBook.setYearPublish(Integer.parseInt(yearPublishTextField.getText()));
		}
		return newBook;
	}
}
