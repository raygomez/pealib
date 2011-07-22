package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

public class UserSearch extends JPanel {
	
	private JTextField fieldSearch = new JTextField(30);
	private JButton btnSearch = new JButton("Search",new ImageIcon("resources/images/search32x32.png"));
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel pendingPane = new JPanel();
	private JPanel usersPane = new JPanel();
	
	private DefaultTableCellRenderer trender = new DefaultTableCellRenderer();
	private JTable tablePending, tableUsers;
	private TableModel modelPending, modelUsers;
	
	/**
	 * Create the panel.
	 */
	public UserSearch(AbstractTableModel model1) {
		this.modelPending = model1;
		
		setBorder(new EmptyBorder(5, 5, 5, 5)); 
		setLayout(new MigLayout("", "[60px][300px]10px[][grow]", "[]20px[][grow]"));

		add(fieldSearch, "cell 1 0");
		add(btnSearch, "cell 2 0");
		
		tabbedPane.addTab("User Accounts", new ImageIcon("resources/images/useraccounts.png"), usersPane );
		tabbedPane.addTab("Pending Applications",new ImageIcon("resources/images/pending.png"), pendingPane );
		
		add(tabbedPane, "cell 0 1, span 4 3,grow");
		
		trender.setHorizontalAlignment(SwingConstants.CENTER);
		usersPanel();
		pendingAppPanel();
	}
	
	private void pendingAppPanel(){
		pendingPane.setBorder(new EmptyBorder(5, 5, 5, 5)); 
		pendingPane.setLayout(new MigLayout("", "", ""));
		
		//table pane
		tablePending = new JTable(modelPending);
		tablePending.setName("tablePending");
		tablePending.setRowHeight(28);		
		tablePending.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		tablePending.getColumn("Username").setCellRenderer(trender);
		tablePending.getColumn("Name").setCellRenderer(trender);
		
		JScrollPane scrollPane = new JScrollPane(tablePending);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);			
		pendingPane.add(scrollPane);		
	}
	
	private void usersPanel(){
		usersPane.setBorder(new EmptyBorder(5, 5, 5, 5)); 
		usersPane.setLayout(new MigLayout("", "", ""));
		
		//table pane
		tablePending = new JTable(modelPending);
		tablePending.setName("tablePending");
		tablePending.setRowHeight(28);		
		tablePending.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		tablePending.getColumn("Username").setCellRenderer(trender);
		tablePending.getColumn("Name").setCellRenderer(trender);
		
		JScrollPane scrollPane = new JScrollPane(tablePending);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);			
		usersPane.add(scrollPane);		
	}

}
