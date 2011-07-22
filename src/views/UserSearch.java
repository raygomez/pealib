package views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

public class UserSearch extends JPanel {
	
	private JTextField fieldSearch = new JTextField(30);
	private JButton btnSearch = new JButton("Search",new ImageIcon("resources/images/search32x32.png"));
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel pendingApp = new JPanel();
	private JPanel userAccts = new JPanel();
	
	private JTable tablePending;
	private TableModel model;
	
	/**
	 * Create the panel.
	 */
	public UserSearch(AbstractTableModel model) {
		this.model = model;
		
		setBorder(new EmptyBorder(5, 5, 5, 5)); 
		setLayout(new MigLayout("", "[60px][300px]10px[][grow]", "[]20px[][grow]"));

		add(fieldSearch, "cell 1 0");
		add(btnSearch, "cell 2 0");
		
		tabbedPane.addTab("User Accounts", new ImageIcon("resources/images/useraccounts.png"), userAccts );
		tabbedPane.addTab("Pending Applications",new ImageIcon("resources/images/pending.png"), pendingApp );
		
		add(tabbedPane, "cell 0 1, span 4 3,grow");
	
		pendingAppPanel();

	}
	
	private void pendingAppPanel(){
		pendingApp.setBorder(new EmptyBorder(5, 5, 5, 5)); 
		pendingApp.setLayout(new MigLayout("", "", ""));
		//table pane
		tablePending = new JTable(model);
		tablePending.setName("tablePending");
		
		JScrollPane scrollPane = new JScrollPane(tablePending);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);	
		
		pendingApp.add(scrollPane);		
	}
	
	public void setTableModel(AbstractTableModel model){
		this.model = model;
	}

}
