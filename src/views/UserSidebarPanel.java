package views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import utilities.MyButton;

public class UserSidebarPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyButton viewBooksButton;
	private MyButton editProfileButton;
	private MyButton logoutButton;
	private MyButton transactionHistoryButton;
	private ButtonGroup buttonGroup;
	
	public UserSidebarPanel() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();				
		setLayout(new MigLayout("wrap 1", "[grow]", "10px[]10px[]10px[]"+screenSize.height/1.9+"[]"));

		viewBooksButton = new MyButton("Search Books", new ImageIcon(
				"resources/images/Book.png"));
		editProfileButton = new MyButton("Edit Profile", new ImageIcon(
				"resources/images/editProfile.png"));
		logoutButton = new MyButton("Logout", new ImageIcon(
				"resources/images/logout32x32.png"));
		transactionHistoryButton = new MyButton("View E-Library Card",
				new ImageIcon("resources/images/elib32x32.png"));

		buttonGroup = new ButtonGroup();
		buttonGroup.add(viewBooksButton);
		buttonGroup.add(editProfileButton);
		buttonGroup.add(logoutButton);
		buttonGroup.add(transactionHistoryButton);
		
		add(viewBooksButton, "growx");
		add(editProfileButton, "growx");
		add(transactionHistoryButton, "growx");
		add(logoutButton, "growx");

		viewBooksButton.setSelected(true);
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

//	public static void main(String[] args) {
//		JFrame frame = new JFrame();
//
//		frame.setLayout(new MigLayout("", "[][grow]", "[grow]"));
//		frame.add(new UserSidebarPanel(), "aligny top");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setMinimumSize(new Dimension(1024, 768));
//
//		frame.setVisible(true);
//	}
}
