package utilities;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class MyButton extends JButton {

	private static final long serialVersionUID = 1L;

	public MyButton(String text, Icon icon) {
		super(text, icon);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setHorizontalTextPosition(SwingConstants.CENTER);
	}

	
}