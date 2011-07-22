package views;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class UserSidebarPanel extends JPanel {

	private JButton viewBooksButton;
	private JButton editProfileButton;
	private JButton logoutButton;
	private JButton transactionHistoryButton;
	
	public UserSidebarPanel(){
		
		setLayout(new MigLayout("wrap 1","[grow]","[]5[]"));
		
		viewBooksButton = new JButton("Search Books");
		editProfileButton = new JButton("Edit Profile");
		logoutButton = new JButton("Logout");
		transactionHistoryButton = new JButton("View E-Library Card");
		
		add(viewBooksButton);
		add(editProfileButton);
		add(transactionHistoryButton);
		add(logoutButton);
		
	}
	
	public void addViewBooksListener(ActionListener searchBooks){
		viewBooksButton.addActionListener(searchBooks);
	}
	
	public void addEditProfileListener(ActionListener editProfile){
		editProfileButton.addActionListener(editProfile);
	}
	
	public void addShowTransactionHistoryListener(ActionListener showHistory){
		transactionHistoryButton.addActionListener(showHistory);
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
