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
		
		setLayout(new MigLayout("","[][grow]","[][grow]"));
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setUndecorated(true);
		setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);
		
		sidebarPanel = new JPanel();
		contentPanel = new JPanel();
		welcomePanel = new JPanel();
		
		welcomeLabel = new JLabel();
		welcomePanel.add(welcomeLabel);
		
		add(welcomePanel, "wrap");
		add(sidebarPanel,"aligny top");
		add(contentPanel,"aligny top");
		
	}
	
	public void setSidebarPanel(JPanel sPanel)
	{
		sidebarPanel.setVisible(false);
		sidebarPanel.removeAll();
		sidebarPanel.add(sPanel);
		sidebarPanel.revalidate();
		sidebarPanel.setVisible(true);
	}
	
	public void setContentPanel(JPanel cPanel)
	{
		contentPanel.setVisible(false);
		contentPanel.removeAll();
		contentPanel.add(cPanel);
		contentPanel.revalidate();
		contentPanel.setVisible(true);
	}
	
	public void setWelcomeLabel(String name){
		welcomeLabel.setText("Welcome! "+name);
	}
}
