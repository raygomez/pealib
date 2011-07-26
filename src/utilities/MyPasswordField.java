package utilities;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;

public class MyPasswordField extends JPasswordField {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyPasswordField(){
		super(20);
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}
	
	public MyPasswordField(int length){
		super(length);
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}
	
	public void hasError(boolean error){
		if(error)
			this.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, Color.getHSBColor((float)0.0,(float) 0.6, (float)1)));
		else
			this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}
}
