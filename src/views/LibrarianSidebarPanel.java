package views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import utilities.MyButton;

public class LibrarianSidebarPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyButton viewBooksButton;
	private MyButton viewUsersButton;
	private MyButton editProfileButton;
	private MyButton logoutButton;
	private MyButton bookTransactionsButton;
	private ButtonGroup buttonGroup;

	public LibrarianSidebarPanel() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();				
		setLayout(new MigLayout("wrap 1", "[grow]", "10px[]10px[]10px[]10px[]"+screenSize.height/2.3+"[]"));

		viewBooksButton = new MyButton("Search Books", new ImageIcon(
				"resources/images/Book.png"));
		viewUsersButton = new MyButton("Search Users", new ImageIcon(
				"resources/images/user32x32.png"));
		editProfileButton = new MyButton("Edit Profile", new ImageIcon(
				"resources/images/editProfile.png"));
		logoutButton = new MyButton("Logout", new ImageIcon(
				"resources/images/logout32x32.png"));
		bookTransactionsButton = new MyButton("Book Transactions",
				new ImageIcon("resources/images/checkInOut32x32.png"));		
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(viewBooksButton);
		buttonGroup.add(viewUsersButton);
		buttonGroup.add(editProfileButton);
		buttonGroup.add(logoutButton);
		buttonGroup.add(bookTransactionsButton);
		
		bookTransactionsButton.setSelected(true);
		
		add(bookTransactionsButton, "growx");
		add(viewBooksButton, "growx");
		add(viewUsersButton, "growx");
		add(editProfileButton, "growx");
		add(logoutButton, "growx, wrap push");		
	}

	public void addViewBooksListener(ActionListener viewBooks) {
		viewBooksButton.addActionListener(viewBooks);
	}

	public void addViewUsersListener(ActionListener viewUsers) {
		viewUsersButton.addActionListener(viewUsers);
	}

	public void addBookTransactionsListener(ActionListener bookTransactions) {
		bookTransactionsButton.addActionListener(bookTransactions);
	}

	public void addEditProfileListener(ActionListener editProfile) {
		editProfileButton.addActionListener(editProfile);
	}

	public void addLogoutListener(ActionListener logout) {
		logoutButton.addActionListener(logout);
	}

//	public static void main(String[] args) {
//		JFrame frame = new JFrame();
//
//		frame.setLayout(new MigLayout("", "[][grow]", "[grow]"));
//		frame.add(new LibrarianSidebarPanel(), "aligny top");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setMinimumSize(new Dimension(1024, 768));
//
//		frame.setVisible(true);
//	}

}
