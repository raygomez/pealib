package utilities;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class MyTextArea extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyTextArea() {
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}
	
	public MyTextArea(int length){
		this.setDocument(new TextLimit(length));
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}

	public MyTextArea(String arg, int col, int row) {
		super(arg, row, col);
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}

	public void hasError(boolean error) {
		if (error)
			this.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1,
					Color.getHSBColor((float) 0.0, (float) 0.6, (float) 1)));
		else
			this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
					Color.gray));
	}
}
