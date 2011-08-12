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
		
	private JTabbedPane tabbedPane;
	private JPanel pendingPane;
	private JPanel usersPane;

	private JTextField searchTextField;
	private JButton searchButton;
	private JButton clearButton;
	private JButton acceptButton;
	private JButton denyButton;
	private JCheckBox allCheckBox;

	private JTable pendingTable, usersTable;
	private TableModel modelUsers; //, modelPending;

	/*
	 * Getters - Setters
	 */	
	public String getSearchWord() { return searchTextField.getText(); }
	
	public int getSelectedTab() { return tabbedPane.getSelectedIndex(); }
	
	public JTable getUsersTable() { return usersTable; }

	public JTable getPendingTable() { return pendingTable; }
		
	public JCheckBox getCbAll() { return allCheckBox; }

	public void setModelUsers(TableModel modelUsers) { this.modelUsers = modelUsers; }
	
	public void clearSearchField() { searchTextField.setText("");}
	/**
	 * Create the panel.
	 */
	public UserSearchPanel() {
		tabbedPane = new JTabbedPane();
		searchTextField = new JTextField(30);
		searchButton = new JButton("Search", new ImageIcon("resources/images/search32x32.png"));
		clearButton = new JButton("Clear", new ImageIcon("resources/images/edit_clear.png"));
		
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new MigLayout("", "[60px][300px]10px[40px][40px][grow]", "[]20px[][grow]"));

		add(searchTextField, "cell 1 0");
		add(searchButton, "cell 2 0");
		add(clearButton, "cell 3 0");
		
		usersPane = new JPanel();
		pendingPane = new JPanel();
		
		tabbedPane.addTab("User Accounts", new ImageIcon("resources/images/useraccounts.png"), usersPane);
		tabbedPane.addTab("Pending Applications", new ImageIcon("resources/images/pending.png"), pendingPane);

		add(tabbedPane, "cell 0 1, span 4 4,grow");
	}
	
	public void usersPanel() {
		
		usersPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		usersPane.setLayout(new MigLayout("", "[grow]", "[grow]"));
		usersPane.setName("usersPane");

		usersTable = new JTable(modelUsers);
		getUsersTable().setName("tableUsers");
		setTableSettings(getUsersTable(),0);

		JScrollPane scrollPane = new JScrollPane(getUsersTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		usersPane.add(scrollPane, "grow");
	}

	public void pendingAppPanel() {
		
		pendingPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		pendingPane.setLayout(new MigLayout("", "[grow]10px[40px]", "[40px]5px[40px]20px[10px][grow]"));
		pendingPane.setName("pendingPane");
		
		pendingTable = new JTable();
		getPendingTable().setName("tablePending");		

		JScrollPane scrollPane = new JScrollPane(getPendingTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		pendingPane.add(scrollPane, "grow,cell 0 0, span 1 4");
		
		acceptButton = new JButton("Accept", new ImageIcon("resources/images/Apply.png"));
		denyButton = new JButton("Deny", new ImageIcon("resources/images/Delete.png"));
		allCheckBox = new JCheckBox("Select All");
		pendingPane.add(acceptButton, "cell 1 0, growx");
		pendingPane.add(denyButton, "cell 1 1, growx");
		pendingPane.add(allCheckBox, "cell 1 2, growx");		
		toggleAllPendingComp(false);
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
		table.getColumn("Username").setCellRenderer(trender);
		table.getColumn("Name").setCellRenderer(trender);
		
		if(tab==USER){
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

	public void addListeners(ActionListener button, ActionListener clear, KeyListener text,
			ChangeListener tab, ListSelectionListener table, ActionListener cbox, 
			ActionListener accept, ActionListener deny) {
		
		searchButton.addActionListener(button);
		clearButton.addActionListener(clear);
		searchTextField.addKeyListener(text);
		tabbedPane.addChangeListener(tab);
		usersTable.getSelectionModel().addListSelectionListener(table);
		pendingTable.getSelectionModel().addListSelectionListener(table);
		allCheckBox.addActionListener(cbox);
		acceptButton.addActionListener(accept);
		denyButton.addActionListener(deny);
	}
}
