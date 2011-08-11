package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import utilities.Connector;
import utilities.CrashHandler;
import views.ConfigureDBConnectionDialog;

public class DBConfigurationController {

	private ConfigureDBConnectionDialog configureDialog;
	private Properties databaseProperties;
	
	public DBConfigurationController(){
		
		databaseProperties = new Properties();
		
		configureDialog = ConfigureDBConnectionDialog.getConfigureDBConnectionDialog();
		configureDialog.addSaveButtonListener(saveConfiguration);
		configureDialog.addTestConnectionButtonListener(testConnection);
	}
	
	private void saveConfiguration(){
		
		setDatabaseProperties();
		
		File configFile = new File("app.config");
		
		try{
		if(!configFile.exists()){
			configFile.createNewFile();
		}
		
		FileOutputStream out = new FileOutputStream(configFile);
		
		databaseProperties.store(out, null);
		}catch (Exception e) {
			// TODO: handle exception
			CrashHandler.handle(e);
		}
	}

	private void setDatabaseProperties() {
		databaseProperties.setProperty("app.hostname", configureDialog.getDatabaseHost());
		databaseProperties.setProperty("app.db", configureDialog.getDatabaseName());
		databaseProperties.setProperty("app.username", configureDialog.getDatabaseUsername());
		databaseProperties.setProperty("app.password", configureDialog.getDatabasePassword());
	}
	
	private void testConnection() {
				
		setDatabaseProperties();
		
		if(Connector.testConnection(databaseProperties)){
			JOptionPane.showMessageDialog(configureDialog, "The database settings are working");
		}
		else{
			JOptionPane.showMessageDialog(configureDialog, "Can't connect to the database with the settings");
		}
	}
	
	private ActionListener saveConfiguration = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			saveConfiguration();
		}
	};
	
	private ActionListener testConnection = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			testConnection();
		}
	};
	
}
