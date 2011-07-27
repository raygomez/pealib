package utilities;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class MyTextArea extends JTextArea {

	public MyTextArea() {
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}

	public MyTextArea(String arg) {
		super(arg);
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}

	public void hasError(boolean error){
		if(error)
			this.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, Color.getHSBColor((float)0.0,(float) 0.6, (float)1)));
		else
			this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}
}
