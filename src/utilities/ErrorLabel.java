package utilities;

import java.awt.Color;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ErrorLabel extends JLabel {
	public ErrorLabel(){
		super();
		setForeground(Color.RED);
		setName("errorLabel");
		
	}
	
	public ErrorLabel(String text){
		super(text);
		setForeground(Color.RED);
		setName("errorLabel");
	}
	
	public void clear(){
		setText("");
	}
	
	public void makeSuccess(String text){
		setForeground(Color.GREEN);
		this.setText(text);
	}
	
	public void makeError(String text){
		setForeground(Color.RED);
		this.setText(text);
	}

}
