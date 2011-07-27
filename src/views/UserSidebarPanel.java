package views;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import utilities.MyButton;

import net.miginfocom.swing.MigLayout;

public class UserSidebarPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton viewBooksButton;
	private JButton editProfileButton;
	private JButton logoutButton;
	private JButton transactionHistoryButton;

	public UserSidebarPanel() {

		setLayout(new MigLayout("wrap 1", "[grow]", "[]5[]"));

		viewBooksButton = new MyButton("Search Books", new ImageIcon(
				"resources/images/Book.png"));
		editProfileButton = new MyButton("Edit Profile", new ImageIcon(
				"resources/images/editProfile.png"));
		logoutButton = new MyButton("Logout", new ImageIcon(
				"resources/images/logout32x32.png"));
		transactionHistoryButton = new MyButton("View E-Library Card",
				new ImageIcon("resources/images/elib32x32.png"));

		add(viewBooksButton, "growx");
		add(editProfileButton, "growx");
		add(transactionHistoryButton, "growx");
		add(logoutButton, "growx");

	}

	public void addViewBooksListener(ActionListener searchBooks) {
		viewBooksButton.addActionListener(searchBooks);
	}

	public void addEditProfileListener(ActionListener editProfile) {
		editProfileButton.addActionListener(editProfile);
	}

	public void addShowTransactionHistoryListener(ActionListener showHistory) {
		transactionHistoryButton.addActionListener(showHistory);
	}

	public void addLogoutListener(ActionListener logout) {
		logoutButton.addActionListener(logout);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();

		frame.setLayout(new MigLayout("", "[][grow]", "[grow]"));
		frame.add(new UserSidebarPanel(), "aligny top");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(1024, 768));

		frame.setVisible(true);
	}
}
