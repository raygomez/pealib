package pealib;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;

import models.User;
import utilities.Connector;
import utilities.CrashHandler;
import utilities.Task;
import utilities.TaskUpdateListener;
import views.LibrarianSidebarPanel;
import views.LoadingDialog;
import views.MainFrame;
import views.UserSidebarPanel;
import controllers.AuthenticationController;
import controllers.BookController;
import controllers.ELibController;
import controllers.TransactionController;
import controllers.UserController;

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

	public PeaLibrary() {
		new Connector();
	}

	public PeaLibrary(String config){
		new Connector(config);
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

	public MainFrame getMainFrame() {
		return frame;
	}

	public void authenticate() {
		authControl = new AuthenticationController();
		AuthenticationController.getLogin().setVisible(true);
		setCurrentUser(authControl.getUser());
		
		Callable<Void> toDo = new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				initialize();
				return null;
			}
		};
		
		Callable<Void> toDoAfter = new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				frame.toFront();
				frame.setVisible(true);
				return null;
			}
		};
		
		Task<Void, Void> task = new Task<Void, Void>(toDo, toDoAfter);
		task.addPropertyChangeListener(new TaskUpdateListener(new LoadingDialog(frame)));
		
		task.execute();
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

		try {
			bookControl = new BookController(getCurrentUser());
			userControl = new UserController(getCurrentUser());
		} catch (Exception e) {
			CrashHandler.handle(e);
		}

		userControl.getUserInfoPanel().addSaveListener(updateCurrentUser);

		if (getCurrentUser().getType().equals("Librarian")) {
			initializeLibrarian();
		} else if (getCurrentUser().getType().equals("User")) {
			initializeUser();
		}
	}

	private void initializeLibrarian() {

		try {
			transactionControl = new TransactionController();
		} catch (Exception e) {
			CrashHandler.handle(e);
		}

		librarianSidebarPanel = new LibrarianSidebarPanel();
		initializeSidebarPanel(librarianSidebarPanel);

		frame.setSidebarPanel(librarianSidebarPanel);
		frame.setContentPanel(transactionControl.getTabbedPane());
		frame.validate();
		frame.repaint();
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
		} catch (Exception e) {
			CrashHandler.handle(e);

		}
		frame.validate();
		frame.repaint();

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

			userControl.getUserInfoPanel().setFields(currentUser);
			userControl.getUserInfoPanel().resetErrorMessages();
			userControl.getUserInfoPanel().setFirstNameEnabled(false);
			userControl.getUserInfoPanel().setLastNameEnabled(false);

			frame.setContentPanel(userControl.getUserInfoPanel());
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
			if (getCurrentUser().getType().equals("Librarian")) {
				frame.setContentPanel(transactionControl.getTabbedPane());
			} else if (getCurrentUser().getType().equals("User")) {
				try {
					frame.setContentPanel(elibControl.getView());
				} catch (Exception e) {
					CrashHandler.handle(e);
				}
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
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PeaLibrary app = new PeaLibrary();
					app.authenticate();
					
				} catch (Exception e) {
					CrashHandler.handle(e);
				}
			}
		});
	}
}
