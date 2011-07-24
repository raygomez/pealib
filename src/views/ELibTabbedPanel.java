package views;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class ELibTabbedPanel extends JPanel{
	/**
	 *  //TODO: NEED GENERIC TABLE
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabs = new JTabbedPane();
	private JPanel history = new JPanel();
	private JPanel onloan = new JPanel();	
	private JPanel request = new JPanel();
	private JPanel reserve = new JPanel();
	
	private JTable dataTable;
	//private DefaultTableCellRenderer trender = new DefaultTableCellRenderer();
	
	/*
	 * For visual testing purposes only
	 */
	public static void main(String[] args) {
		
		ELibTabbedPanel tabpane = new ELibTabbedPanel();
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setResizable(false);
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	      
	    frame.setBounds(0,0,screenSize.width, screenSize.height);
	      
	    frame.setContentPane(tabpane);
		
	}
	public ELibTabbedPanel(){		
		setBorder(new EmptyBorder(5, 5, 5, 5)); 
		setLayout(new MigLayout("", "[600px]", "[]20px[500px]"));
		
		tabs.addTab("Requests", new ImageIcon("resources/images/request.png"), request );
		tabs.addTab("Reservations",new ImageIcon("resources/images/reserve.png"), reserve );
		tabs.addTab("On-Loans",new ImageIcon("resources/images/onloan.png"), onloan );
		tabs.addTab("History",new ImageIcon("resources/images/history.png"), history );
		
		add(tabs, "cell 0 1, grow");
		
		addHistoryPane();
		addOnLoanPane();
		addRequestPane();
		addReservePane();
	}
	
	private void addHistoryPane(){
		history.setBorder(new EmptyBorder(5, 5, 5, 5)); 
		history.setLayout(new MigLayout("", "", ""));

		//TODO add table model
		dataTable = new JTable();
		dataTable.setName("historyTable");
		dataTable.setRowHeight(28);		
		dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		//histTable.getColumn("Username").setCellRenderer(trender);
		//histTable.getColumn("Name").setCellRenderer(trender);
		
		JScrollPane scrollPane = new JScrollPane(dataTable);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);			
		history.add(scrollPane);		
	}
	
	private void addOnLoanPane(){
		onloan.setBorder(new EmptyBorder(5, 5, 5, 5)); 
		onloan.setLayout(new MigLayout("", "", ""));

		//TODO add table model
		dataTable = new JTable();
		dataTable.setName("onloanTable");
		dataTable.setRowHeight(28);		
		dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		//histTable.getColumn("Username").setCellRenderer(trender);
		//histTable.getColumn("Name").setCellRenderer(trender);
		
		JScrollPane scrollPane = new JScrollPane(dataTable);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);			
		onloan.add(scrollPane);		
	}
	
	private void addRequestPane(){
		request.setBorder(new EmptyBorder(5, 5, 5, 5)); 
		request.setLayout(new MigLayout("", "", ""));

		//TODO add table model
		dataTable = new JTable();
		dataTable.setName("requestTable");
		dataTable.setRowHeight(28);		
		dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		//histTable.getColumn("Username").setCellRenderer(trender);
		//histTable.getColumn("Name").setCellRenderer(trender);
		
		JScrollPane scrollPane = new JScrollPane(dataTable);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);			
		request.add(scrollPane);		
	}
	
	private void addReservePane(){
		reserve.setBorder(new EmptyBorder(5, 5, 5, 5)); 
		reserve.setLayout(new MigLayout("", "", ""));

		//TODO add table model
		dataTable = new JTable();
		dataTable.setName("reserveTable");
		dataTable.setRowHeight(28);		
		dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		//histTable.getColumn("Username").setCellRenderer(trender);
		//histTable.getColumn("Name").setCellRenderer(trender);
		
		JScrollPane scrollPane = new JScrollPane(dataTable);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);			
		reserve.add(scrollPane);		
	}
	
	
}
