package pealib;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import models.User;

import controllers.AuthenticationController;
import controllers.BookController;
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
	
	private UserSidebarPanel userSidebarPanel;
	private LibrarianSidebarPanel librarianSidebarPanel;
	
	private User currentUser;

	public PeaLibrary(){
		new Connector();
		initialize();	
	}

	private void initialize() {
		// TODO Auto-generated method stub
		frame = new MainFrame();
		frame.setVisible(false);
		
		authControl = new AuthenticationController(this);
	}

	public void setUser(User user){
		currentUser = user;
		initializedLoggedUser();
	}
	
	private void initializedLoggedUser(){
		//currentUser = user;
		//currentUser = new User(101123,"jajalim","1234567","Jaja","Lim","jjlim@gmail.com","UP Ayala Technohub", "09171234567",1,"Librarian");
		
		frame.setWelcomeLabel(currentUser.getFirstName()+" "+currentUser.getLastName());
		
		bookControl = new BookController(currentUser);
		userControl = new UserController(currentUser);
		
		if(currentUser.getType().equals("Librarian")){
			initializeLibrarian();
		}
		else if(currentUser.getType().equals("User")){
			initializeUser();
		}
	}
	
	private void initializeLibrarian(){
		bookControl = new BookController(currentUser);
		userControl = new UserController(currentUser);
		
		librarianSidebarPanel = new LibrarianSidebarPanel();
		initializeSidebarPanel(librarianSidebarPanel);
		
		frame.setSidebarPanel(librarianSidebarPanel);
		frame.validate();
		frame.setVisible(true);
		frame.repaint();
	}
	
	private void initializeUser(){
		
	}
	
	private void initializeSidebarPanel(LibrarianSidebarPanel librarianSidebarPanel) {
		// TODO Auto-generated method stub
		librarianSidebarPanel.addViewBooksListener(viewBooks);
		librarianSidebarPanel.addViewUsersListener(viewUsers);
		librarianSidebarPanel.addEditProfileListener(showEditProfile);
		librarianSidebarPanel.addBookTransactionsListener(showBookTransactions);
		librarianSidebarPanel.addLogoutListener(logout);
	}
	
	private ActionListener viewBooks = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			frame.setContentPanel(bookControl.getBookLayoutPanel());
		}
	};
	
	private ActionListener viewUsers = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			frame.setContentPanel(userControl.getLayoutPanel());
		}
	};
	private ActionListener showEditProfile;
	
	private ActionListener logout = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			frame.setVisible(false);
			frame = null;
			currentUser = null;
			
			initialize();
		}
	};
	
	private ActionListener showBookTransactions;
	
	public static void main(String[] args){
		
		PeaLibrary main = new PeaLibrary();
	}
}
