package pealib;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingWorker;

import models.User;
import utilities.Connector;
import utilities.Constants;
import utilities.CrashHandler;
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
	private LoadingDialog loadingDialog;

	public PeaLibrary() {
		new Connector();
	}

	public PeaLibrary(String config){
		new Connector(config);
	}
	
	/**
	 * @return the frame
	 */
	public MainFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame
	 *            the frame to set
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
	 * @param currentUser
	 *            the currentUser to set
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
	 * @param transactionControl
	 *            the transactionControl to set
	 */
	public void setTransactionControl(TransactionController transactionControl) {
		this.transactionControl = transactionControl;
	}

	public void authenticate() {
		authControl = new AuthenticationController();
		AuthenticationController.getLogin().setVisible(true);
		setCurrentUser(authControl.getUser());
		
		SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>(){
		

			@Override
			protected Void doInBackground() {
				try{
					initialize();
				}catch (Exception e) {
					cancel(true);
					CrashHandler.handle(e);
				}
				return null;
			}
			
			@Override
			protected void done() {
				getFrame().toFront();
				getFrame().setVisible(true);
			}
		};
		
		sw.addPropertyChangeListener(new PropertyChangeListener() {
						
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				
				if(arg0.getPropertyName().equalsIgnoreCase("state")){
					if(arg0.getNewValue().toString().equals("STARTED")){
						loadingDialog = new LoadingDialog();
						loadingDialog.setVisible(true);
					}
					else if(arg0.getNewValue().toString().equals("DONE")){
						loadingDialog.dispose();
					}
				}
			}
		});
		sw.execute();
	}

	public void initialize() {

		setFrame(new MainFrame());

		if (getCurrentUser() == null) {
			System.exit(0);
		}

		initializedLoggedUser();
	}

	private void initializedLoggedUser() {
		// currentUser = user;
		// currentUser = new
		// User(101123,"jajalim","1234567","Jaja","Lim","jjlim@gmail.com","UP Ayala Technohub",
		// "09171234567",1,"Librarian");

		getFrame().setWelcomeLabel(
				getCurrentUser().getFirstName() + " "
						+ getCurrentUser().getLastName());

		try {
			bookControl = new BookController(getCurrentUser());
		} catch (Exception e) {
			CrashHandler.handle(e);

		}
		try {
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
			setTransactionControl(new TransactionController());
		} catch (Exception e) {
			CrashHandler.handle(e);
		}

		librarianSidebarPanel = new LibrarianSidebarPanel();
		initializeSidebarPanel(librarianSidebarPanel);

		getFrame().setSidebarPanel(librarianSidebarPanel);
		frame.setContentPanel(transactionControl.getTabbedPane());
		getFrame().validate();
		getFrame().repaint();
	}

	private void initializeUser() {

		try {
			elibControl = new ELibController(getCurrentUser());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CrashHandler.handle(e);
		}
		userSidebarPanel = new UserSidebarPanel();
		initializeSidebarPanel(userSidebarPanel);

		getFrame().setSidebarPanel(userSidebarPanel);
		try {
			getFrame().setContentPanel(bookControl.getBookLayoutPanel());
		} catch (Exception e) {
			CrashHandler.handle(e);

		}
		getFrame().validate();
		getFrame().repaint();

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

	public MainFrame getMainFrame() {
		return getFrame();
	}

	private ActionListener viewBooks = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				getFrame().setContentPanel(bookControl.getBookLayoutPanel());
			} catch (Exception e) {
				CrashHandler.handle(e);

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
			userControl.getUserInfoPanel().resetErrorMessages();
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
			//getFrame().setVisible(true);
		}
	};

	private ActionListener showBookTransactions = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (getCurrentUser().getType().equals("Librarian")) {
				frame.setContentPanel(transactionControl.getTabbedPane());
			} else if (getCurrentUser().getType().equals("User")) {
				try {
					getFrame().setContentPanel(elibControl.getView());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					PeaLibrary app = new PeaLibrary(Constants.TEST_CONFIG);
					app.authenticate();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setLoadingDialog(LoadingDialog loadingDialog) {
		this.loadingDialog = loadingDialog;
	}

	public LoadingDialog getLoadingDialog() {
		return loadingDialog;
	}
}
