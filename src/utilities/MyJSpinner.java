package utilities;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

public class MyJSpinner extends JSpinner {

	public MyJSpinner() {
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}

	public MyJSpinner(SpinnerModel arg0) {
		super(arg0);
		this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}
	
	public void hasError(boolean error){
		if(error)
			this.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, Color.getHSBColor((float)0.0,(float) 0.6, (float)1)));
		else
			this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
	}

}
