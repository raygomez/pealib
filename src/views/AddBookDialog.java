package views;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import models.Book;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class AddBookDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtFldTitle;
	private JTextField txtFldAuthor;
	private JTextField txtFldYearPublish;
	private JTextField txtFldPublisher;
	private JTextField txtFldIsbn;
	private JTextArea txtAreaDescription;
	private JLabel lblErrorMsg;
	private JButton btnAdd;
	private JButton btnCancel;
	private JSpinner spinCopyVal;
	/**
	 * Create the dialog.
	 */
	public AddBookDialog() {
		setTitle("Add Book");
		setBounds(100, 100, 450, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[fill][grow]", "[20.00][20.00][20.00][20.00][20.00][20.00,grow][20.00]"));
		{
			JLabel lblTitle = new JLabel("Title:");
			contentPanel.add(lblTitle, "cell 0 0");
		}
		{
			txtFldTitle = new JTextField();
			contentPanel.add(txtFldTitle, "cell 1 0,growx");
			txtFldTitle.setColumns(10);
		}
		{
			JLabel lblAuthor = new JLabel("Author:");
			contentPanel.add(lblAuthor, "cell 0 1");
		}
		{
			txtFldAuthor = new JTextField();
			contentPanel.add(txtFldAuthor, "cell 1 1,growx");
			txtFldAuthor.setColumns(10);
		}
		{
			JLabel lblYearPublish = new JLabel("Year Published:");
			contentPanel.add(lblYearPublish, "cell 0 2");
		}
		{
			txtFldYearPublish = new JTextField();
			contentPanel.add(txtFldYearPublish, "cell 1 2,growx");
			txtFldYearPublish.setColumns(10);
		}
		{
			JLabel lblPublisher = new JLabel("Publisher:");
			contentPanel.add(lblPublisher, "cell 0 3");
		}
		{
			txtFldPublisher = new JTextField();
			contentPanel.add(txtFldPublisher, "cell 1 3,growx");
			txtFldPublisher.setColumns(10);
		}
		{
			JLabel lblIsbn = new JLabel("ISBN:");
			contentPanel.add(lblIsbn, "cell 0 4");
		}
		{
			txtFldIsbn = new JTextField();
			contentPanel.add(txtFldIsbn, "cell 1 4,growx");
			txtFldIsbn.setColumns(10);
		}
		{
			JLabel lblDescription = new JLabel("Description:");
			contentPanel.add(lblDescription, "cell 0 5,aligny top");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 1 5,grow");
			{
				txtAreaDescription = new JTextArea();
				scrollPane.setViewportView(txtAreaDescription);
			}
		}
		{
			JLabel lblCopies = new JLabel("Copies:");
			contentPanel.add(lblCopies, "cell 0 6");
		}
		{
			spinCopyVal = new JSpinner();
			spinCopyVal.setModel(new SpinnerNumberModel(1, 0, 2147483647, 1));
			contentPanel.add(spinCopyVal, "cell 1 6,alignx left");
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new MigLayout("", "[298.00][47px][65px]", "[23px]"));
			{
				lblErrorMsg = new JLabel("");
				lblErrorMsg.setForeground(Color.RED);
				buttonPane.add(lblErrorMsg, "cell 0 0,alignx left,aligny center");
			}
			{
				btnAdd = new JButton("Add");
				btnAdd.setActionCommand("OK");
				buttonPane.add(btnAdd, "cell 1 0,alignx left,aligny top");
				getRootPane().setDefaultButton(btnAdd);
			}
			{
				btnCancel = new JButton("Cancel");
				btnCancel.setActionCommand("Cancel");
				buttonPane.add(btnCancel, "cell 2 0,alignx left,aligny top");
			}
			this.setResizable(false);
		}
	}
	public JTextField getTxtFldTitle() {
		return txtFldTitle;
	}
	public JTextField getTxtFldAuthor() {
		return txtFldAuthor;
	}
	public JTextField getTxtFldYearPublish() {
		return txtFldYearPublish;
	}
	public JTextField getTxtFldPublisher() {
		return txtFldPublisher;
	}
	public JTextField getTxtFldIsbn() {
		return txtFldIsbn;
	}

	public JTextArea getTxtAreaDescription() {
		return txtAreaDescription;
	}
	public JLabel getLblErrorMsg() {
		return lblErrorMsg;
	}
	public void addBookActionListener(ActionListener add) {
		btnAdd.addActionListener(add);
	}
	public void addCancelActionListener(ActionListener cancel) {
		btnCancel.addActionListener(cancel);
	}
	
	public Book getBookInfo(){
		Book newBook = new Book();
		newBook.setAuthor(txtFldAuthor.getText());
		newBook.setCopies(Integer.parseInt(spinCopyVal.getModel().getValue().toString()));
		newBook.setDescription(txtAreaDescription.getText());
		newBook.setIsbn(txtFldIsbn.getText());
		newBook.setPublisher(txtFldPublisher.getText());
		newBook.setTitle(txtFldTitle.getText());
		newBook.setYearPublish(Integer.parseInt(txtFldYearPublish.getText()));
		return newBook;
	}
}
