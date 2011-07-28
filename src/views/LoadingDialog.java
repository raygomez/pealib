package views;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoadingDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoadingDialog(){
		super();		
		initialize();
	}
	
	public LoadingDialog(Dialog parent) {
		super(parent);
		initialize();
	}
	
	public LoadingDialog(Frame parent) {
		super(parent);
		initialize();
	}
	
	public void initialize(){
		
		setPreferredSize(new Dimension(200, 50));
		setUndecorated(true);
		setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
				
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width/3), (screenSize.height/3), 200, 50);
		
		JPanel contentPanel = new JPanel();
		contentPanel.add(new JLabel("Loading",new ImageIcon("resources/images/ajax-loader.gif"), JLabel.HORIZONTAL));
		contentPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		
		getContentPane().add(contentPanel);
		setLocationRelativeTo(null);
		pack();	
	}
	
	public static void main(String[] args){
		
		new LoadingDialog().setVisible(true);
		
	}
}
