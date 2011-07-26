package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

public class UserSearchPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final static int USER = 0;
	private final static int PENDING = 1;
	
	private JTextField fieldSearch = new JTextField(30);
	private JButton btnSearch = new JButton("Search", new ImageIcon("resources/images/search32x32.png"));
	
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel pendingPane = new JPanel();
	private JPanel usersPane = new JPanel();
	
	private JButton btnAccept = new JButton("Accept", new ImageIcon("resources/images/Apply.png"));
	private JButton btnDeny = new JButton("Deny", new ImageIcon("resources/images/Delete.png"));
	private JCheckBox cbAll = new JCheckBox("Select All");

	private JTable pendingTable, usersTable;
	private TableModel modelPending, modelUsers;

	/*
	 * Getters - Setters
	 */	
	public JTextField getFieldSearch() { return fieldSearch; }
	
	public JButton getBtnSearch(){ return btnSearch; }
	
	public JTabbedPane getTabbedPane(){ return tabbedPane; }

	public int getSelectedTab() { return tabbedPane.getSelectedIndex(); }
	
	//USERS
	public JPanel getUsersPane() { return usersPane; }
	
	public JTable getUsersTable() { return usersTable; }

	public void setUsersTable(JTable usersTable) { this.usersTable = usersTable; }
	
	public TableModel getUsersTableModel(){ return modelUsers;}
	
	public void setUsersTableModel(TableModel model) { this.modelUsers = model; }
	
	//PENDING
	public JPanel getPendingPane() { return pendingPane; }

	public void setPendingPane(JPanel pendingPane) { this.pendingPane = pendingPane; }

	public JTable getPendingTable() { return pendingTable; }

	public void setPendingTable(JTable pendingTable) { this.pendingTable = pendingTable; }
	
	public TableModel getPendingTableModel(){ return modelPending;}
	
	public void setPendingTableModel(TableModel model) { this.modelPending = model; }
		
	public JCheckBox getCbAll() { return cbAll; }
	
	public JButton getBtnAccept(){ return btnAccept; }
	
	public JButton getBtnDeny(){ return btnDeny; }
	
	/*
	 * Methods
	 */
	public void togglePendingButtons(boolean toggle){
		btnAccept.setEnabled(toggle);
		btnDeny.setEnabled(toggle);	
	}
	
	public void togglePending(boolean toggle){
		togglePendingButtons(toggle);
		cbAll.setEnabled(toggle);
	}

	private void setTableSettings(JTable table, int tab) {
		DefaultTableCellRenderer trender = new DefaultTableCellRenderer();
		trender.setHorizontalAlignment(SwingConstants.CENTER);
		
		table.setRowHeight(28);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumn("Username").setCellRenderer(trender);
		table.getColumn("Name").setCellRenderer(trender);
		
		if(tab==PENDING){
			table.getColumn("Accept").setPreferredWidth(5);
		}
		
		table.getTableHeader().setReorderingAllowed(false);	
		table.getTableHeader().setResizingAllowed(false);
	}
	
	/**
	 * Create the panel.
	 */
	public UserSearchPanel(AbstractTableModel model1, AbstractTableModel model2) {
		setUsersTableModel(model1);
		setPendingTableModel(model2);

		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new MigLayout("", "[60px][300px]10px[][grow]",
				"[]20px[][grow]"));

		add(fieldSearch, "cell 1 0");
		add(btnSearch, "cell 2 0");

		getTabbedPane().addTab("User Accounts", new ImageIcon(
				"resources/images/useraccounts.png"), getUsersPane());
		getTabbedPane().addTab("Pending Applications", new ImageIcon(
				"resources/images/pending.png"), getPendingPane());

		add(tabbedPane, "cell 0 1, span 4 3,grow");
				
		usersPanel();
		pendingAppPanel();
	}
	
	private void usersPanel() {
		getUsersPane().setBorder(new EmptyBorder(5, 5, 5, 5));
		getUsersPane().setLayout(new MigLayout("", "", ""));

		setUsersTable(new JTable(getUsersTableModel()));
		getUsersTable().setName("tablePending");
		setTableSettings(getUsersTable(),0);

		JScrollPane scrollPane = new JScrollPane(getUsersTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		getUsersPane().add(scrollPane);
	}

	private void pendingAppPanel() {
		getPendingPane().setBorder(new EmptyBorder(5, 5, 5, 5));
		getPendingPane().setLayout(new MigLayout("", "[]10px[40px]", "[40px]5px[40px]20px[10px][grow]"));

		setPendingTable(new JTable(getPendingTableModel()));
		getPendingTable().setName("tablePending");
		setTableSettings(getPendingTable(),PENDING);

		JScrollPane scrollPane = new JScrollPane(getPendingTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		getPendingPane().add(scrollPane, "cell 0 0, span 1 4");
		
		getPendingPane().add(btnAccept, "cell 1 0, growx");
		getPendingPane().add(btnDeny, "cell 1 1, growx");
		getPendingPane().add(cbAll, "cell 1 2, growx");						
	}

	public void setTableModel(int tab, AbstractTableModel model) {
		if (tab == USER) {
			getUsersTable().setModel(model);
			setTableSettings(getUsersTable(), tab);

		} else if (tab == PENDING) {
			getPendingTable().setModel(model);
			setTableSettings(getPendingTable(),tab);
		}
		resetTable();
	}
	
	public void resetTable(){
		getTabbedPane().revalidate();
		getTabbedPane().repaint();
	}

	public void addUserSelectionLister(ListSelectionListener listener) {
		getUsersTable().getSelectionModel().addListSelectionListener(listener);
	}

	public void addListeners(ActionListener button, KeyListener text,
			ChangeListener tab, ListSelectionListener table, ActionListener cbox, 
			ActionListener accept, ActionListener deny) {
		
		getBtnSearch().addActionListener(button);
		getFieldSearch().addKeyListener(text);
		getTabbedPane().addChangeListener(tab);
		getUsersTable().getSelectionModel().addListSelectionListener(table);
		getPendingTable().getSelectionModel().addListSelectionListener(table);
		getCbAll().addActionListener(cbox);
		getBtnAccept().addActionListener(accept);
		getBtnDeny().addActionListener(deny);
	}
	
	
	public void removeListener(ListSelectionListener table){
		getPendingTable().getSelectionModel().removeListSelectionListener(table);
	}
	
	public void addListener(ListSelectionListener table){
		getPendingTable().getSelectionModel().addListSelectionListener(table);
	}
}
