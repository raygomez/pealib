package views;

import javax.swing.CellEditor;
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
import controllers.ELibController.CancelRequestButtonEditor;

;

public class ELibTabbedPanel extends JPanel {
	/**
	 * //TODO: NEED GENERIC TABLE
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabs = new JTabbedPane();
	private JPanel historyPanel = new JPanel();
	private JPanel onloanPanel = new JPanel();
	private JPanel requestPanel = new JPanel();
	private JPanel reservePanel = new JPanel();

	private JTable requestTable, reserveTable, onloanTable, historyTable;
	private DefaultTableCellRenderer trender = new DefaultTableCellRenderer();

	public ELibTabbedPanel() {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new MigLayout("", "[600px]", "[]20px[500px]"));

		tabs.addTab("Requests", new ImageIcon("resources/images/request.png"),
				requestPanel);
		tabs.addTab("Reservations", new ImageIcon(
				"resources/images/reserve.png"), reservePanel);
		tabs.addTab("On-Loans", new ImageIcon("resources/images/onloan.png"),
				onloanPanel);
		tabs.addTab("History", new ImageIcon("resources/images/history.png"),
				historyPanel);

		add(tabs, "cell 0 1, grow");
		trender.setHorizontalAlignment(SwingConstants.CENTER);

		addHistoryPane();
		addOnLoanPane();
		addRequestPane();
		addReservePane();
	}

	/**
	 * @return the reserveTable
	 */
	public JTable getReserveTable() {
		return reserveTable;
	}

	/**
	 * @param reserveTable the reserveTable to set
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

	}

	public void setCellEditor(int tab, TableCellEditor renderer) {

		switch (tab) {
		case 0:
			getRequestTable().getColumn("Cancel").setCellEditor(renderer);
			break;
			
		case 1:
			getReserveTable().getColumn("Cancel").setCellEditor(renderer);

			break;

		case 2:
			break;

		case 3:
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

		case 2:
			break;

		case 3:
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

	private void addHistoryPane() {
		historyPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		historyPanel.setLayout(new MigLayout("", "", ""));

		historyTable = new JTable();
		historyTable.setName("historyTable");
		historyTable.setRowHeight(28);
		historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(historyTable);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		historyPanel.add(scrollPane);
	}

	private void addOnLoanPane() {
		onloanPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		onloanPanel.setLayout(new MigLayout("", "", ""));

		onloanTable = new JTable();
		onloanTable.setName("onloanTable");
		onloanTable.setRowHeight(28);
		onloanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(onloanTable);
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		onloanPanel.add(scrollPane);
	}

	private void addRequestPane() {
		requestPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		requestPanel.setLayout(new MigLayout("", "", ""));

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
		reservePanel.setLayout(new MigLayout("", "", ""));

		setReserveTable(new JTable());
		getReserveTable().setName("reserveTable");
		getReserveTable().setRowHeight(28);
		getReserveTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(getReserveTable());
		scrollPane.setName("scrollPane");
		scrollPane.setSize(10, 10);
		reservePanel.add(scrollPane);
	}
}
