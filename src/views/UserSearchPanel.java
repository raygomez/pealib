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
	private JTextField fieldSearch = new JTextField(30);
	private JButton btnSearch = new JButton("Search", new ImageIcon("resources/images/search32x32.png"));
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel pendingPane = new JPanel();
	private JPanel usersPane = new JPanel();
	
	private JButton btnAccept = new JButton("Accept", new ImageIcon("resources/images/Apply.png"));
	private JButton btnDeny = new JButton("Deny", new ImageIcon("resources/images/Delete.png"));
	private JCheckBox cbAll = new JCheckBox("Select All");

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
				"resources/images/pending.png"), getPendingPane());

		add(tabbedPane, "cell 0 1, span 4 3,grow");

		trender.setHorizontalAlignment(SwingConstants.CENTER);
		usersPanel();
		pendingAppPanel();
	}

	/**
	 * @return the pendingTable
	 */
	public JTable getPendingTable() {
		return pendingTable;
	}

	/**
	 * @param pendingTable
	 *            the pendingTable to set
	 */
	public void setPendingTable(JTable pendingTable) {
		this.pendingTable = pendingTable;
	}

	/**
	 * @return the pendingPane
	 */
	public JPanel getPendingPane() {
		return pendingPane;
	}

	/**
	 * @param pendingPane
	 *            the pendingPane to set
	 */
	public void setPendingPane(JPanel pendingPane) {
		this.pendingPane = pendingPane;
	}

	/**
	 * @return the usersTable
	 */
	public JTable getUsersTable() {
		return usersTable;
	}

	/**
	 * @param usersTable
	 *            the usersTable to set
	 */
	public void setUsersTable(JTable usersTable) {
		this.usersTable = usersTable;
	}

	private void setTableSettings(JTable table, int tab) {
		table.setRowHeight(28);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumn("Username").setCellRenderer(trender);
		table.getColumn("Name").setCellRenderer(trender);
		
		if(tab==1){
			table.getColumn("Accept").setPreferredWidth(5);
		}
	}

	private void pendingAppPanel() {
		getPendingPane().setBorder(new EmptyBorder(5, 5, 5, 5));
		getPendingPane().setLayout(new MigLayout("", "[]10px[40px]", "[40px]5px[40px]20px[10px][grow]"));

		// table pane
		setPendingTable(new JTable(modelPending));
		getPendingTable().setName("tablePending");
		setTableSettings(getPendingTable(),1);

		JScrollPane scrollPane = new JScrollPane(getPendingTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		getPendingPane().add(scrollPane, "cell 0 0, span 1 4");
		
		getPendingPane().add(btnAccept, "cell 1 0, growx");
		getPendingPane().add(btnDeny, "cell 1 1, growx");
		getPendingPane().add(cbAll, "cell 1 2, growx");
		
	}

	private void usersPanel() {
		usersPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		usersPane.setLayout(new MigLayout("", "", ""));

		// table pane
		setUsersTable(new JTable(modelUsers));
		getUsersTable().setName("tablePending");
		setTableSettings(getUsersTable(),0);

		JScrollPane scrollPane = new JScrollPane(getUsersTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		usersPane.add(scrollPane);
	}

	public void setTableModel(int tab, AbstractTableModel model) {
		if (tab == 0) {
			getUsersTable().setModel(model);
			setTableSettings(getUsersTable(), tab);

		} else {
			getPendingTable().setModel(model);
			setTableSettings(getPendingTable(),tab);
		}
		tabbedPane.validate();
		tabbedPane.repaint();
	}

	public void addUserSelectionLister(ListSelectionListener listener) {
		getUsersTable().getSelectionModel().addListSelectionListener(listener);
	}

	public void addListeners(ActionListener button, KeyListener text,
			ChangeListener tab, ListSelectionListener table) {
		btnSearch.addActionListener(button);
		fieldSearch.addKeyListener(text);
		tabbedPane.addChangeListener(tab);
		getUsersTable().getSelectionModel().addListSelectionListener(table);
		getPendingTable().getSelectionModel().addListSelectionListener(table);
	}

}
