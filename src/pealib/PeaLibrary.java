package pealib;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;

import models.User;
import models.UserDAO;
import utilities.Connector;
import utilities.CrashHandler;
import utilities.Emailer;
import utilities.Task;
import views.LibrarianSidebarPanel;
import views.MainFrame;
import views.UserSidebarPanel;
import controllers.AuthenticationController;
import controllers.BookController;
import controllers.ELibController;
import controllers.LoadingControl;
import controllers.TransactionController;
import controllers.UserController;

public class PeaLibrary {

	private static MainFrame frame;
	private AuthenticationController authControl;
	private UserController userControl;
	private BookController bookControl;
	private ELibController elibControl;
	private TransactionController transactionControl;

	private UserSidebarPanel userSidebarPanel;
	private LibrarianSidebarPanel librarianSidebarPanel;

	private User currentUser;

	public PeaLibrary() {
		Connector.init();
		Emailer.setOn(true);
	}

	public PeaLibrary(String config){
		Connector.init(config);
		Emailer.setOn(false);
	}
	
	/**
	 * @return the currentUser
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser
	 *            the currentUser to set
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public static MainFrame getMainFrame() {
		return frame;
	}

	public void authenticate() {
		authControl = new AuthenticationController();
		AuthenticationController.getLogin().setVisible(true);
		setCurrentUser(authControl.getUser());
		
		initialize();
	}

	public void initialize() {

		frame = new MainFrame();

		if (getCurrentUser() == null) {
			System.exit(0);
		}

		initializedLoggedUser();
	}

	private void initializedLoggedUser() {

		frame.setWelcomeLabel(
				getCurrentUser().getFirstName() + " "
						+ getCurrentUser().getLastName());


		Callable<Void> toDo = new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				try {
					bookControl = new BookController(getCurrentUser());
					userControl = new UserController(getCurrentUser());
				} catch (Exception e) {
					CrashHandler.handle(e);
				}
				return null;
			}
		};
		
		Callable<Void> toDoAfter = new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				userControl.getUserInfoPanel().addSaveListener(updateCurrentUser);

				if (getCurrentUser().getType().equals("Librarian")) {
					initializeLibrarian();
				} else if (getCurrentUser().getType().equals("User")) {
					initializeUser();
				}
				return null;
			}
		};
		
		Task<Void, Void> task = new Task<Void, Void>(toDo, toDoAfter);
		LoadingControl.init(task, frame).executeLoading();
		
		
	}

	private void initializeLibrarian() {

		try {
			transactionControl = new TransactionController();
			
			librarianSidebarPanel = new LibrarianSidebarPanel();
			initializeSidebarPanel(librarianSidebarPanel);
	
			frame.setSidebarPanel(librarianSidebarPanel);
			frame.setContentPanel(transactionControl.getTabbedPane());
			frame.validate();
			frame.repaint();
			frame.toFront();
			frame.setVisible(true);
			
		} catch (Exception e) {
			CrashHandler.handle(e);
		}
	}

	private void initializeUser() {

		try {
			elibControl = new ELibController(getCurrentUser());
		} catch (Exception e) {
			CrashHandler.handle(e);
		}
		userSidebarPanel = new UserSidebarPanel();
		initializeSidebarPanel(userSidebarPanel);

		frame.setSidebarPanel(userSidebarPanel);
		try {
			frame.setContentPanel(bookControl.getBookLayoutPanel());
			bookControl.initializePanelContent();
		} catch (Exception e) {
			CrashHandler.handle(e);

		}
		frame.validate();
		frame.repaint();
		frame.toFront();
		frame.setVisible(true);

	}

	private void initializeSidebarPanel(UserSidebarPanel userSidebarPanel) {
		userSidebarPanel.addViewBooksListener(viewBooks);
		userSidebarPanel.addEditProfileListener(showEditProfile);
		userSidebarPanel
				.addShowTransactionHistoryListener(showBookTransactions);
		userSidebarPanel.addLogoutListener(logout);
	}

	private void initializeSidebarPanel(
			LibrarianSidebarPanel librarianSidebarPanel) {
		librarianSidebarPanel.addViewBooksListener(viewBooks);
		librarianSidebarPanel.addViewUsersListener(viewUsers);
		librarianSidebarPanel.addEditProfileListener(showEditProfile);
		librarianSidebarPanel.addBookTransactionsListener(showBookTransactions);
		librarianSidebarPanel.addLogoutListener(logout);
	}

	private ActionListener viewBooks = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				frame.setContentPanel(bookControl.getBookLayoutPanel());
				
				Task<Void, Object> task = new Task<Void, Object>(new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						bookControl.initializePanelContent();
						return null;
					}
				});
				
				LoadingControl.init(task, frame).executeLoading();
				
			} catch (Exception e) {
				CrashHandler.handle(e);

			}
		}
	};

	private ActionListener viewUsers = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			frame.setContentPanel(userControl.getLayoutPanel());
		}
	};
	private ActionListener showEditProfile = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				User userProfile = UserDAO.getUserById(currentUser.getUserId());
				userControl.getUserInfoPanel().setFields(userProfile);
				userControl.getUserInfoPanel().resetErrorMessages();
				userControl.getUserInfoPanel().setEnableFields(true);
				userControl.getUserInfoPanel().setFirstNameEnabled(false);
				userControl.getUserInfoPanel().setLastNameEnabled(false);
				userControl.getUserInfoPanel().toggleButton(true);

				frame.setContentPanel(userControl.getUserInfoPanel());
			} catch (Exception e) {
				CrashHandler.handle(e);
			}
		}
	};

	private ActionListener logout = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setVisible(false);
			frame.dispose();
			setCurrentUser(null);
			authenticate();
		}
	};

	private ActionListener showBookTransactions = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (getCurrentUser().getType().equals("Librarian")) {
					frame.setContentPanel(transactionControl.getTabbedPane());
				} else if (getCurrentUser().getType().equals("User")) {
					
						frame.setContentPanel(elibControl.getTabPane());
						elibControl.update();	
				}
			} catch (Exception e) {
				CrashHandler.handle(e);
			}
		}
	};

	private ActionListener updateCurrentUser = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			setCurrentUser(userControl.getCurrentUser());
		}
	};

	public static void main(final String[] args){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(args.length == 0){
						PeaLibrary app = new PeaLibrary();
						app.authenticate();
						
					} else if(args.length == 1){
						PeaLibrary app = new PeaLibrary(args[0]);
						app.authenticate();
					}
					
				} catch (Exception e) {
					CrashHandler.handle(e);
				}
			}
		});
	}
}
