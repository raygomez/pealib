package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import views.LogInDialog;

public class AuthenticationController {
	private LogInDialog login = new LogInDialog(); 
	
	public AuthenticationController(LogInDialog login){
		this.login = login;
		
		login.setActionListeners(new SignUpListener(), new SubmitListener(), new SubmitKeyListener());
	}
	
	//SignUp Listener
	class SignUpListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	submit();
        }
	}
	
	//Submit Listener
	class SubmitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	submit();
        }
	}
	
	//Submit when pressing Enter
	class SubmitKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent arg0) {
			 int keyCode = arg0.getKeyCode();
			 
			 if(keyCode==10){
				 submit();
			 }
		}
		@Override
		public void keyReleased(KeyEvent e) {}
		@Override
		public void keyTyped(KeyEvent e) {}
	}
	
	private void submit(){
		String username = login.getFieldUsername().getText();
    	String password = login.getFieldPassword().getText();
  
    	String regex = "[a-zA-Z_0-9.]{4,20}";
    	boolean validUsername = (username.matches(regex));
    	boolean validPassword = (password.matches(regex));
    	    	    	
    	if(username.equals("") || password.equals("")){
    		login.setLabelError("Incomplete fields");
    		
    		if (username.equals("")) login.setUsernameColor(true); 
    		else login.setUsernameColor(false);
    		
    		if(password.equals("")) login.setPasswordColor(true);
    		else login.setPasswordColor(false);
    	}  
    	else if(!validUsername || !validPassword){
    		login.setLabelError("Invalid input");  
    		if(!validUsername) login.setUsernameColor(true);   else  login.setUsernameColor(false); 
    		if(!validPassword) login.setPasswordColor(true);   else  login.setPasswordColor(false);
    	}
    	
    	else if(validUsername && validPassword){
    		//Add again for DB validation
    		System.out.println("LOGIN");
    		login.setUsernameColor(false);
    		login.setPasswordColor(false);
    		login.getFieldPassword().setText("");
    		login.getFieldUsername().setText("");
    		login.setLabelError("");
    	}
    	
    	else{
    		login.setLabelError("Username/Password Mismatch");  
    		login.getFieldPassword().setText("");
    	}
	}	
}
