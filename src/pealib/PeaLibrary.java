package pealib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import models.User;

import controllers.AuthenticationController;
import controllers.BookController;
import controllers.ELibController;
import controllers.TransactionController;
import controllers.UserController;

import utilities.Connector;
import views.LibrarianSidebarPanel;
import views.MainFrame;
import views.UserSidebarPanel;

public class PeaLibrary {

	private MainFrame frame;
	private AuthenticationController authControl;
	private UserController userControl;
	private BookController bookControl;
	private ELibController elibControl;
	private TransactionController transactionControl;
	
	private UserSidebarPanel userSidebarPanel;
	private LibrarianSidebarPanel librarianSidebarPanel;
	
	private User currentUser;

	public PeaLibrary(){
		new Connector();
	}

	/**
	 * @return the frame
	 */
	public MainFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(MainFrame frame) {
		this.frame = frame;
	}

	/**
	 * @return the currentUser
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser the currentUser to set
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * @return the transactionControl
	 */
	public TransactionController getTransactionControl() {
		return transactionControl;
	}

	/**
	 * @param transactionControl the transactionControl to set
	 */
	public void setTransactionControl(TransactionController transactionControl) {
		this.transactionControl = transactionControl;
	}
	public void authenticate(){
		authControl = new AuthenticationController();
		AuthenticationController.getLogin().setVisible(true);
		setCurrentUser(authControl.getUser());		
		initialize();
	}

	public void initialize() {
		
		setFrame(new MainFrame());
		
		if(getCurrentUser() == null){
			System.exit(0);
		}
		
		initializedLoggedUser();
	}
	
	private void initializedLoggedUser(){
		//currentUser = user;
		//currentUser = new User(101123,"jajalim","1234567","Jaja","Lim","jjlim@gmail.com","UP Ayala Technohub", "09171234567",1,"Librarian");
		
		getFrame().setWelcomeLabel(getCurrentUser().getFirstName()+" "+getCurrentUser().getLastName());
		
		try {
			bookControl = new BookController(getCurrentUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			userControl = new UserController(getCurrentUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		userControl.getUserInfoPanel().addSaveListener(updateCurrentUser);
		
		if(getCurrentUser().getType().equals("Librarian")){
			initializeLibrarian();
		}
		else if(getCurrentUser().getType().equals("User")){
			initializeUser();
		}
	}
	
	private void initializeLibrarian(){
		
		try {
			setTransactionControl(new TransactionController());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		librarianSidebarPanel = new LibrarianSidebarPanel();
		initializeSidebarPanel(librarianSidebarPanel);
		
		getFrame().setSidebarPanel(librarianSidebarPanel);
		frame.setContentPanel(transactionControl.getTabbedPane());
		getFrame().validate();
		getFrame().repaint();
	}
	
	private void initializeUser(){
		
		elibControl = new ELibController(getCurrentUser());
		userSidebarPanel = new UserSidebarPanel();
		initializeSidebarPanel(userSidebarPanel);
		
		getFrame().setSidebarPanel(userSidebarPanel);
		try {
			getFrame().setContentPanel(bookControl.getBookLayoutPanel());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getFrame().validate();
		getFrame().repaint();
		
	}
	
	private void initializeSidebarPanel(UserSidebarPanel userSidebarPanel) {
		userSidebarPanel.addViewBooksListener(viewBooks);
		userSidebarPanel.addEditProfileListener(showEditProfile);
		userSidebarPanel.addShowTransactionHistoryListener(showBookTransactions);
		userSidebarPanel.addLogoutListener(logout);
	}

	private void initializeSidebarPanel(LibrarianSidebarPanel librarianSidebarPanel) {
		librarianSidebarPanel.addViewBooksListener(viewBooks);
		librarianSidebarPanel.addViewUsersListener(viewUsers);
		librarianSidebarPanel.addEditProfileListener(showEditProfile);
		librarianSidebarPanel.addBookTransactionsListener(showBookTransactions);
		librarianSidebarPanel.addLogoutListener(logout);
	}
	
	public MainFrame getMainFrame(){
		return getFrame();
	}
	
	private ActionListener viewBooks = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				getFrame().setContentPanel(bookControl.getBookLayoutPanel());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	private ActionListener viewUsers = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			getFrame().setContentPanel(userControl.getLayoutPanel());
		}
	};
	private ActionListener showEditProfile = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {

			userControl.getUserInfoPanel().setFields(currentUser);
			
			userControl.getUserInfoPanel().setFirstNameEnabled(false);
			userControl.getUserInfoPanel().setLastNameEnabled(false);
			
			getFrame().setContentPanel(userControl.getUserInfoPanel());
		}
	};
	
	private ActionListener logout = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {			
			getFrame().setVisible(false);
			getFrame().dispose();
			setCurrentUser(null);
			authenticate();
			getFrame().setVisible(true);
		}
	};
	
	private ActionListener showBookTransactions = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(getCurrentUser().getType().equals("Librarian")){
				frame.setContentPanel(transactionControl.getTabbedPane());
			}
			else if(getCurrentUser().getType().equals("User")){
				getFrame().setContentPanel(elibControl.getView());
			}
		}
	};
	
	private ActionListener updateCurrentUser = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			setCurrentUser(userControl.getCurrentUser());			
		}
	};
	
	public static void main(String[] args){
		
		PeaLibrary app = new PeaLibrary();
		app.authenticate();
		app.getFrame().setVisible(true);
	}
}
