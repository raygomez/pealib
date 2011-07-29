package views;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import net.miginfocom.swing.MigLayout;
import controllers.ELibController.CancelButtonRenderer;

public class ELibTabbedPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTabbedPane tabs = new JTabbedPane();
	private JPanel historyPanel = new JPanel();
	private JPanel onloanPanel = new JPanel();
	private JPanel requestPanel = new JPanel();
	private JPanel reservePanel = new JPanel();

	private JTable requestTable;
	private JTable reserveTable;
	private JTable onloanTable;
	private JTable historyTable;

	private DefaultTableCellRenderer trender = new DefaultTableCellRenderer();

	public ELibTabbedPanel() {
		setLayout(new MigLayout("", "[fill,grow]", "[fill,grow]"));

		tabs.addTab("Requests", new ImageIcon("resources/images/request.png"),
				requestPanel);
		tabs.addTab("Reservations", new ImageIcon(
				"resources/images/reserve.png"), reservePanel);
		tabs.addTab("On Loan", new ImageIcon("resources/images/onloan.png"),
				onloanPanel);
		tabs.addTab("History", new ImageIcon("resources/images/history.png"),
				historyPanel);

		add(tabs, "");
		trender.setHorizontalAlignment(SwingConstants.CENTER);

		addHistoryPane();
		addOnLoanPane();
		addRequestPane();
		addReservePane();
	}

	/**
	 * @return the historyTable
	 */
	public JTable getHistoryTable() {
		return historyTable;
	}

	/**
	 * @param historyTable
	 *            the historyTable to set
	 */
	public void setHistoryTable(JTable historyTable) {
		this.historyTable = historyTable;
	}

	/**
	 * @return the onloanTable
	 */
	public JTable getOnloanTable() {
		return onloanTable;
	}

	/**
	 * @param onloanTable
	 *            the onloanTable to set
	 */
	public void setOnloanTable(JTable onloanTable) {
		this.onloanTable = onloanTable;
	}

	/**
	 * @return the reserveTable
	 */
	public JTable getReserveTable() {
		return reserveTable;
	}

	/**
	 * @param reserveTable
	 *            the reserveTable to set
	 */
	public void setReserveTable(JTable reserveTable) {
		this.reserveTable = reserveTable;
	}

	/**
	 * @return the requestTable
	 */
	public JTable getRequestTable() {
		return requestTable;
	}

	/**
	 * @param requestTable
	 *            the requestTable to set
	 */
	public void setRequestTable(JTable requestTable) {
		this.requestTable = requestTable;
	}

	public int getSelectedTab() {
		return tabs.getSelectedIndex();
	}

	public void addChangeTabListener(ChangeListener listener) {
		tabs.addChangeListener(listener);
	}

	private void setTableSettings(JTable table) {
		table.setRowHeight(28);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(table.getColumnName(i)).setCellRenderer(trender);
		}
		
		table.getTableHeader().setReorderingAllowed(false);	
		table.getTableHeader().setResizingAllowed(false);
	}

	public void setCellEditor(int tab, TableCellEditor renderer) {

		switch (tab) {
		case 0:
			getRequestTable().getColumn("Cancel").setCellEditor(renderer);
			break;
		case 1:
			getReserveTable().getColumn("Cancel").setCellEditor(renderer);
			break;
		}
	}

	public void setCellRenderer(int tab, CancelButtonRenderer renderer) {

		switch (tab) {
		case 0:
			getRequestTable().getColumn("Cancel").setCellRenderer(renderer);
			break;
		case 1:
			getReserveTable().getColumn("Cancel").setCellRenderer(renderer);
			break;
		}
	}

	public void setTableModel(int tab, AbstractTableModel model) {

		switch (tab) {
		case 0:
			getRequestTable().setModel(model);
			setTableSettings(getRequestTable());
			break;

		case 1:
			getReserveTable().setModel(model);
			setTableSettings(getReserveTable());
			break;

		case 2:
			getOnloanTable().setModel(model);
			setTableSettings(getOnloanTable());
			break;

		case 3:
			getHistoryTable().setModel(model);
			setTableSettings(getHistoryTable());
			break;
		}

		tabs.validate();
		tabs.repaint();
	}

	private void addHistoryPane() {
		historyPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		historyPanel.setLayout(new MigLayout("", "[fill,grow]", "[fill,grow]"));

		setHistoryTable(new JTable());
		getHistoryTable().setName("historyTable");
		getHistoryTable().setRowHeight(28);
		getHistoryTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(getHistoryTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		historyPanel.add(scrollPane);
	}

	private void addOnLoanPane() {
		onloanPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		onloanPanel.setLayout(new MigLayout("", "[fill,grow]", "[fill,grow]"));

		setOnloanTable(new JTable());
		getOnloanTable().setName("onloanTable");
		getOnloanTable().setRowHeight(28);
		getOnloanTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(getOnloanTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		onloanPanel.add(scrollPane);
	}

	private void addRequestPane() {
		requestPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		requestPanel.setLayout(new MigLayout("", "[fill,grow]", "[fill,grow]"));

		setRequestTable(new JTable());
		getRequestTable().setName("requestTable");
		getRequestTable().setRowHeight(28);
		getRequestTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(getRequestTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		requestPanel.add(scrollPane);
	}

	private void addReservePane() {
		reservePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		reservePanel.setLayout(new MigLayout("", "[fill,grow]", "[fill,grow]"));

		setReserveTable(new JTable());
		getReserveTable().setName("reserveTable");
		getReserveTable().setRowHeight(28);
		getReserveTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(getReserveTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		reservePanel.add(scrollPane);
	}

	public JTable getTableByTab(int tab) {
		// TODO Auto-generated method stub
		switch(tab){
			case 0:
				return requestTable;
			case 1:
				return reserveTable;
			case 2:
				return onloanTable;
			case 3:
				return historyTable;
		}
		return historyTable;
	}
}
