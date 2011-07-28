package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

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
		
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel pendingPane = new JPanel();
	private JPanel usersPane = new JPanel();

	private JTextField searchTextField = new JTextField(30);
	private JButton searchButton = new JButton("Search", new ImageIcon("resources/images/search32x32.png"));
	private JButton acceptButton = new JButton("Accept", new ImageIcon("resources/images/Apply.png"));
	private JButton denyButton = new JButton("Deny", new ImageIcon("resources/images/Delete.png"));
	private JCheckBox allCheckBox = new JCheckBox("Select All");

	private JTable pendingTable, usersTable;
	private TableModel modelPending, modelUsers;

	/*
	 * Getters - Setters
	 */	
	public JTextField getFieldSearch() { return searchTextField; }

	public int getSelectedTab() { return tabbedPane.getSelectedIndex(); }
	
	public JTable getUsersTable() { return usersTable; }

	public JTable getPendingTable() { return pendingTable; }
		
	public JCheckBox getCbAll() { return allCheckBox; }
	
	/**
	 * Create the panel.
	 */
	public UserSearchPanel(AbstractTableModel model1, AbstractTableModel model2) {
		modelUsers = model1;
		modelPending = model2;

		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new MigLayout("", "[60px][300px]10px[][grow]", "[]20px[][grow]"));

		add(searchTextField, "cell 1 0");
		add(searchButton, "cell 2 0");

		tabbedPane.addTab("User Accounts", new ImageIcon("resources/images/useraccounts.png"), usersPane);
		tabbedPane.addTab("Pending Applications", new ImageIcon("resources/images/pending.png"), pendingPane);

		add(tabbedPane, "cell 0 1, span 4 3,grow");
				
		usersPanel();
		pendingAppPanel();
	}
	
	private void usersPanel() {
		usersPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		usersPane.setLayout(new MigLayout("", "[grow]", "[grow]"));

		usersTable = new JTable(modelUsers);
		getUsersTable().setName("tableUsers");
		setTableSettings(getUsersTable(),0);

		JScrollPane scrollPane = new JScrollPane(getUsersTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		usersPane.add(scrollPane, "grow");
	}

	private void pendingAppPanel() {
		pendingPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		pendingPane.setLayout(new MigLayout("", "[grow]10px[40px]", "[40px]5px[40px]20px[10px][grow]"));

		pendingTable = new JTable(modelPending);
		getPendingTable().setName("tablePending");
		setTableSettings(getPendingTable(),PENDING);

		JScrollPane scrollPane = new JScrollPane(getPendingTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		pendingPane.add(scrollPane, "grow,cell 0 0, span 1 4");
		
		pendingPane.add(acceptButton, "cell 1 0, growx");
		pendingPane.add(denyButton, "cell 1 1, growx");
		pendingPane.add(allCheckBox, "cell 1 2, growx");						
	}

	/*
	 * Table-related 
	 */
	public void setTableModel(int tab, AbstractTableModel model) {
		if (tab == USER) {
			getUsersTable().setModel(model);
			setTableSettings(getUsersTable(), tab);

		} else if (tab == PENDING) {
			getPendingTable().setModel(model);
			setTableSettings(getPendingTable(),tab);
		}
		resetTabPane();
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
	
	public void resetTabPane(){
		tabbedPane.revalidate();
		tabbedPane.repaint();
	}
	
	/*
	 * Methods
	 */
	
	public void togglePendingButtons(boolean toggle){
		acceptButton.setEnabled(toggle);
		denyButton.setEnabled(toggle);	
	}
	
	public void toggleAllPendingComp(boolean toggle){
		togglePendingButtons(toggle);
		allCheckBox.setEnabled(toggle);
	}
	
	public void addUserSelectionLister(ListSelectionListener listener) {
		getUsersTable().getSelectionModel().addListSelectionListener(listener);
	}

	public void addListeners(ActionListener button, KeyListener text,
			ChangeListener tab, ListSelectionListener table, ActionListener cbox, 
			ActionListener accept, ActionListener deny) {
		
		searchButton.addActionListener(button);
		searchTextField.addKeyListener(text);
		tabbedPane.addChangeListener(tab);
		usersTable.getSelectionModel().addListSelectionListener(table);
		pendingTable.getSelectionModel().addListSelectionListener(table);
		allCheckBox.addActionListener(cbox);
		acceptButton.addActionListener(accept);
		denyButton.addActionListener(deny);
	}
}
