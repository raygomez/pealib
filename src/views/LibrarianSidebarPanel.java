package views;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class LibrarianSidebarPanel extends JPanel {

	private JButton viewBooksButton;
	private JButton viewUsersButton;
	private JButton editProfileButton;
	private JButton logoutButton;
	private JButton bookTransactionsButton;

	public LibrarianSidebarPanel(){
		
		setLayout(new MigLayout("wrap 1","[grow]","[]5[]"));

		viewBooksButton = new JButton("Search Books");
		viewUsersButton = new JButton("Search Users");
		editProfileButton = new JButton("Edit Profile");
		logoutButton = new JButton("Logout");
		bookTransactionsButton = new JButton("Book Transactions");
		
		add(viewBooksButton, "growx");
		add(viewUsersButton, "growx");
		add(editProfileButton, "growx");
		add(bookTransactionsButton, "growx");
		add(logoutButton, "growx, wrap push");
		
	}
	
	public void addViewBooksListener(ActionListener viewBooks){
		viewBooksButton.addActionListener(viewBooks);
	}
	
	public void addViewUsersListener(ActionListener viewUsers){
		viewUsersButton.addActionListener(viewUsers);
	}
	
	public void addBookTransactionsListener(ActionListener bookTransactions){
		bookTransactionsButton.addActionListener(bookTransactions);
	}
	
	public void addEditProfileListener(ActionListener editProfile){
		editProfileButton.addActionListener(editProfile);
	}
	
	public void addLogoutListener(ActionListener logout){
		logoutButton.addActionListener(logout);
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		
		frame.setLayout(new MigLayout("","[][grow]","[grow]"));
		frame.add(new LibrarianSidebarPanel(),"aligny top");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(1024,768));
		
		frame.setVisible(true);
	}
	
	
}
