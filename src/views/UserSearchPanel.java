package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

public class UserSearchPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField fieldSearch = new JTextField(30);
	private JButton btnSearch = new JButton("Search", new ImageIcon(
			"resources/images/search32x32.png"));
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel pendingPane = new JPanel();
	private JPanel usersPane = new JPanel();

	private DefaultTableCellRenderer trender = new DefaultTableCellRenderer();
	private JTable pendingTable, usersTable;
	private TableModel modelPending, modelUsers;

	public JTextField getFieldSearch() {
		return fieldSearch;
	}

	public int getSelectedTab() {
		return tabbedPane.getSelectedIndex();
	}

	/**
	 * Create the panel.
	 */
	public UserSearchPanel(AbstractTableModel model1, AbstractTableModel model2) {
		this.modelPending = model2;
		this.modelUsers = model1;

		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new MigLayout("", "[60px][300px]10px[][grow]",
				"[]20px[][grow]"));

		add(fieldSearch, "cell 1 0");
		add(btnSearch, "cell 2 0");

		tabbedPane.addTab("User Accounts", new ImageIcon(
				"resources/images/useraccounts.png"), usersPane);
		tabbedPane.addTab("Pending Applications", new ImageIcon(
				"resources/images/pending.png"), pendingPane);

		add(tabbedPane, "cell 0 1, span 4 3,grow");

		trender.setHorizontalAlignment(SwingConstants.CENTER);
		usersPanel();
		pendingAppPanel();
	}

	/**
	 * @return the usersTable
	 */
	public JTable getUsersTable() {
		return usersTable;
	}

	/**
	 * @param usersTable the usersTable to set
	 */
	public void setUsersTable(JTable usersTable) {
		this.usersTable = usersTable;
	}

	private void setTableSettings(JTable table) {
		table.setRowHeight(28);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumn("Username").setCellRenderer(trender);
		table.getColumn("Name").setCellRenderer(trender);
	}

	private void pendingAppPanel() {
		pendingPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		pendingPane.setLayout(new MigLayout("", "", ""));

		// table pane
		pendingTable = new JTable(modelPending);
		pendingTable.setName("tablePending");
		setTableSettings(pendingTable);

		JScrollPane scrollPane = new JScrollPane(pendingTable);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		pendingPane.add(scrollPane);
	}

	private void usersPanel() {
		usersPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		usersPane.setLayout(new MigLayout("", "", ""));

		// table pane
		setUsersTable(new JTable(modelUsers));
		getUsersTable().setName("tablePending");
		setTableSettings(getUsersTable());

		JScrollPane scrollPane = new JScrollPane(getUsersTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		usersPane.add(scrollPane);
	}

	public void setTableModel(int tab, AbstractTableModel model) {
		if (tab == 0) {
			getUsersTable().setModel(model);
			setTableSettings(getUsersTable());
			
		} else {
			pendingTable.setModel(model);
			setTableSettings(pendingTable);
		}
		tabbedPane.validate();
		tabbedPane.repaint();
	}

	public void addUserSelectionLister(ListSelectionListener listener) {
		getUsersTable().getSelectionModel().addListSelectionListener(listener);
	}

	public void addListeners(ActionListener button, KeyListener text,
			MouseListener tab, ListSelectionListener table) {
		btnSearch.addActionListener(button);
		fieldSearch.addKeyListener(text);
		tabbedPane.addMouseListener(tab);
		usersTable.getSelectionModel().addListSelectionListener(table);
	}

}
