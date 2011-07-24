package views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import controllers.UserController;

import models.User;
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
	
	private JTable requestTable, reserveTable, onloanTable, historyTable;
	private DefaultTableCellRenderer trender = new DefaultTableCellRenderer();

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
	
	public int getSelectedTab(){
		return tabs.getSelectedIndex();
	}
	
	public void addListener(MouseListener tab){
		tabs.addMouseListener(tab);		
	}
	
	private void setTableSettings(JTable table){
		table.setRowHeight(28);		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		for(int i=0; i<table.getColumnCount(); i++){
			table.getColumn(i).setCellRenderer(trender);
		}
	}
	
	public void setTableModel(int tab, AbstractTableModel model){
		
		switch(tab){
			case 0:
				requestTable.setModel(model);
				setTableSettings(requestTable);
				break;
				
			case 1:
				reserveTable.setModel(model);
				setTableSettings(reserveTable);
				break;
			
			case 2:
				onloanTable.setModel(model);
				setTableSettings(onloanTable);
				break;
			
			case 3:
				historyTable.setModel(model);
				setTableSettings(historyTable);
				break;				
		}
		
		tabs.validate();
		tabs.repaint();
	}
	
	private void addHistoryPane(){
		history.setBorder(new EmptyBorder(5, 5, 5, 5)); 
		history.setLayout(new MigLayout("", "", ""));

		//TODO add table model
		historyTable = new JTable();
		historyTable.setName("historyTable");
		historyTable.setRowHeight(28);		
		historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		//histTable.getColumn("Username").setCellRenderer(trender);
		//histTable.getColumn("Name").setCellRenderer(trender);
		
		JScrollPane scrollPane = new JScrollPane(historyTable);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);			
		history.add(scrollPane);		
	}
	
	private void addOnLoanPane(){
		onloan.setBorder(new EmptyBorder(5, 5, 5, 5)); 
		onloan.setLayout(new MigLayout("", "", ""));

		//TODO add table model
		onloanTable = new JTable();
		onloanTable.setName("onloanTable");
		onloanTable.setRowHeight(28);		
		onloanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		//histTable.getColumn("Username").setCellRenderer(trender);
		//histTable.getColumn("Name").setCellRenderer(trender);
		
		JScrollPane scrollPane = new JScrollPane(onloanTable);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);			
		onloan.add(scrollPane);		
	}
	
	private void addRequestPane(){
		request.setBorder(new EmptyBorder(5, 5, 5, 5)); 
		request.setLayout(new MigLayout("", "", ""));

		//TODO add table model
		requestTable = new JTable();
		requestTable.setName("requestTable");
		requestTable.setRowHeight(28);		
		requestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		//histTable.getColumn("Username").setCellRenderer(trender);
		//histTable.getColumn("Name").setCellRenderer(trender);
		
		JScrollPane scrollPane = new JScrollPane(requestTable);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);			
		request.add(scrollPane);		
	}
	
	private void addReservePane(){
		reserve.setBorder(new EmptyBorder(5, 5, 5, 5)); 
		reserve.setLayout(new MigLayout("", "", ""));

		//TODO add table model
		reserveTable = new JTable();
		reserveTable.setName("reserveTable");
		reserveTable.setRowHeight(28);		
		reserveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		//histTable.getColumn("Username").setCellRenderer(trender);
		//histTable.getColumn("Name").setCellRenderer(trender);
		
		JScrollPane scrollPane = new JScrollPane(reserveTable);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);			
		reserve.add(scrollPane);		
	}		
}
