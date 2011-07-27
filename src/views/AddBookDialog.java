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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private MyTextField titleTextField = new MyTextField();
	private MyTextField authorTextField = new MyTextField();
	private MyTextField yearPublishTextField = new MyTextField();
	private MyTextField publisherTextField = new MyTextField();
	private MyTextField isbnTextField = new MyTextField();
	private MyTextField editionTextField = new MyTextField();
	private MyTextArea descriptionTextArea = new MyTextArea();
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

		titleTextField.setName("titleTextField");
		contentPanel.add(titleTextField, "cell 1 0,growx");
		titleTextField.setColumns(10);
		
		lblEdition = new JLabel("Edition:");
		contentPanel.add(lblEdition, "cell 0 1,alignx left");
		
		contentPanel.add(editionTextField, "cell 1 1,growx");
		editionTextField.setColumns(10);

		JLabel lblAuthor = new JLabel("Author:");
		contentPanel.add(lblAuthor, "cell 0 2,alignx left");

		authorTextField.setName("authorTextField");
		contentPanel.add(authorTextField, "cell 1 2,growx");
		authorTextField.setColumns(10);

		JLabel lblYearPublish = new JLabel("Year Published:");
		contentPanel.add(lblYearPublish, "cell 0 3,alignx left");

		yearPublishTextField.setName("yearPublishTextField");
		contentPanel.add(yearPublishTextField, "cell 1 3,growx");
		yearPublishTextField.setColumns(10);

		JLabel lblPublisher = new JLabel("Publisher:");
		contentPanel.add(lblPublisher, "cell 0 4,alignx left");

		publisherTextField.setName("publisherTextField");
		contentPanel.add(publisherTextField, "cell 1 4,growx");
		publisherTextField.setColumns(10);

		JLabel lblIsbn = new JLabel("ISBN:");
		contentPanel.add(lblIsbn, "cell 0 5,alignx left");

		isbnTextField.setName("isbnTextField");
		contentPanel.add(isbnTextField, "cell 1 5,growx");
		isbnTextField.setColumns(10);

		JLabel lblDescription = new JLabel("Description:");
		contentPanel.add(lblDescription, "cell 0 6,alignx left,aligny top");

		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, "cell 1 6,grow");

		descriptionTextArea.setName("descriptionTextArea");
		scrollPane.setViewportView(descriptionTextArea);

		JLabel lblCopies = new JLabel("Copies:");
		contentPanel.add(lblCopies, "cell 0 7,alignx left");

		copyValSpinner.setModel(new SpinnerNumberModel(1, 0, 2147483647, 1));
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
		newBook.setAuthor(authorTextField.getText());
		newBook.setCopies(Integer.parseInt(copyValSpinner.getModel().getValue()
				.toString()));
		newBook.setDescription(descriptionTextArea.getText());
		newBook.setIsbn(isbnTextField.getText());
		newBook.setPublisher(publisherTextField.getText());
		newBook.setTitle(titleTextField.getText());
		newBook.setEdition(editionTextField.getText());
		if(yearPublishTextField.getText().length() > 0){
			newBook.setYearPublish(Integer.parseInt(yearPublishTextField.getText()));
		}
		return newBook;
	}
}
