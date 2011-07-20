package views;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class UserSidebarPanel extends JPanel {

	private JButton searchBooksButton;
	private JButton editProfileButton;
	private JButton logoutButton;
	private JButton transactionHistoryButton;
	
	public UserSidebarPanel(){
		
		setLayout(new MigLayout("wrap 1","[grow]","[grow]"));
		setMinimumSize(new Dimension(100, 700));
		
		searchBooksButton = new JButton("Search Books");
		editProfileButton = new JButton("Edit Profile");
		logoutButton = new JButton("Logout");
		transactionHistoryButton = new JButton("View E-Library Card");
		
		add(searchBooksButton, "growx");
		add(editProfileButton, "growx");
		add(transactionHistoryButton, "growx");
		add(logoutButton,"dock south");
	}
	
	public void addSearchBookListener(ActionListener searchBooks){
		searchBooksButton.addActionListener(searchBooks);
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
}
