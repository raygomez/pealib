package views;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel sidebarPanel;
	private JPanel contentPanel;
	private JPanel welcomePanel;

	private JLabel welcomeLabel;

	public MainFrame(){

		setLayout(new MigLayout("","[][fill,grow]","[][fill,grow]"));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setUndecorated(true);
		setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);

		sidebarPanel = new JPanel();
		contentPanel = new JPanel(new MigLayout("","[fill,grow]","[fill,grow]"));
		welcomePanel = new JPanel();

		welcomeLabel = new JLabel();
		welcomePanel.add(welcomeLabel);

		add(welcomePanel, "wrap");
		add(sidebarPanel,"aligny top");
		add(contentPanel,"aligny top");

	}

	public void setSidebarPanel(JComponent sPanel)
	{
		sidebarPanel.setVisible(false);
		sidebarPanel.removeAll();
		sidebarPanel.add(sPanel);
		sidebarPanel.revalidate();
		sidebarPanel.setVisible(true);
	}

	public void setContentPanel(JComponent cPanel)
	{
		contentPanel.setVisible(false);
		contentPanel.removeAll();
		contentPanel.add(cPanel, "grow");
		contentPanel.revalidate();
		contentPanel.setVisible(true);
	}

	public void setWelcomeLabel(String name){
		welcomeLabel.setText("Welcome! "+name);
	}

	/*
	 * Overriden setVisible() and toFront() 
	 * retrieved from http://stackoverflow.com/questions/309023/howto-bring-a-java-window-to-the-front
	 * retrieval date: 7-27-2011 5:52 PM
	 * 
	 * all credits goes to 01es from stackoverflow.com
	 */

	@Override
	public void setVisible(final boolean visible) {
		// let's handle visibility...
		if (!visible || !isVisible()) { // have to check this condition simply because super.setVisible(true) invokes toFront if frame was already visible
			super.setVisible(visible);
		}
		// ...and bring frame to the front.. in a strange and weird way
		if (visible) {
			int state = super.getExtendedState();
			state &= ~JFrame.ICONIFIED;
			super.setExtendedState(state);
			super.setAlwaysOnTop(true);
			super.toFront();
			super.requestFocus();
			super.setAlwaysOnTop(false);
		}
	}

	@Override
	public void toFront() {
		super.setVisible(true);
		int state = super.getExtendedState();
		state &= ~JFrame.ICONIFIED;
		super.setExtendedState(state);
		super.setAlwaysOnTop(true);
		super.toFront();
		super.requestFocus();
		super.setAlwaysOnTop(false);
	}

}
