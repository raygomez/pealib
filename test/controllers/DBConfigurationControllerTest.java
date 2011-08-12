package controllers;


import java.io.File;
import java.lang.reflect.Field;
import java.util.Properties;

import javassist.tools.reflect.Reflection;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.PasswordField;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import org.unitils.mock.Mock;

import utilities.Constants;
import utilities.PropertyLoader;

public class DBConfigurationControllerTest extends UISpecTestCase {

	private Properties databaseProperties;
	private DBConfigurationController testController;
	
	private static Window configDialog;
	private Field field;
	
	
	@Before
	public void setUp() throws Exception {
	
		databaseProperties = new PropertyLoader(Constants.TEST_CONFIG).getProperties();
		testController = new DBConfigurationController();
		
		field = DBConfigurationController.class.getDeclaredField("configFile");
		field.setAccessible(true);
		field.set(testController, new File("dummy.config"));
		
		
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				testController.showConfigurationDialog();
			}
		}).process(new WindowHandler() {
			
			@Override
			public Trigger process(Window configDialog) throws Exception {
				DBConfigurationControllerTest.configDialog = configDialog;
				return configDialog.getButton("Cancel").triggerClick();
			}
		}).run();
	}

	@Test
	public void testShowDialog(){
		
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				testController.showConfigurationDialog();
			}
		}).process(new WindowHandler() {
			
			@Override
			public Trigger process(Window configDialog) throws Exception {
				
				assertNotNull(configDialog);
				
				String[] textFields = {"databaseHostField","databaseNameField","databaseUsernameField"};
				
				for(String s : textFields){
					assertNotNull(configDialog.getInputTextBox(s));
					assertThat(configDialog.getInputTextBox(s).isEnabled());
					assertThat(configDialog.getInputTextBox(s).isVisible());
					assertThat(configDialog.getInputTextBox(s).isEditable());
				}
				
				PasswordField passwordField = configDialog.getPasswordField();
				
				assertNotNull(passwordField);
				assertThat(passwordField.isEnabled());
				assertThat(passwordField.isVisible());
				
				String[] buttons = {"Cancel","Save Configuration","Test Connection"};
				
				for(String s : buttons){
					assertNotNull(configDialog.getButton(s));
					assertThat(configDialog.getButton(s).isEnabled());
					assertThat(configDialog.getButton(s).isVisible());
				}
				
				return configDialog.getButton("Cancel").triggerClick();
			}
		}).run();
		
	}
	
	@Test
	public void testTestConnection(){
		
		configDialog.getInputTextBox("databaseHostField").setText(databaseProperties.getProperty("app.hostname"));
		configDialog.getInputTextBox("databaseNameField").setText(databaseProperties.getProperty("app.db"));
		configDialog.getInputTextBox("databaseUsernameField").setText(databaseProperties.getProperty("app.username"));
		configDialog.getPasswordField("databasePasswordField").setPassword(databaseProperties.getProperty("app.password"));
		
		WindowInterceptor.init(configDialog.getButton("Test Connection").triggerClick()).process(new WindowHandler() {
			
		@Override
			public Trigger process(Window dialog) throws Exception {
				assertThat(dialog.containsLabel("The database settings are working"));
				return dialog.getButton("OK").triggerClick();
			}
		}).run();
	}
	
	@Test
	public void testTestConnection2(){
		
		configDialog.getInputTextBox("databaseHostField").clear();
		configDialog.getInputTextBox("databaseNameField").clear();
		configDialog.getInputTextBox("databaseUsernameField").clear();
		configDialog.getPasswordField("databasePasswordField").setPassword("");
		
		WindowInterceptor.init(configDialog.getButton("Test Connection").triggerClick()).process(new WindowHandler() {
			
			@Override
			public Trigger process(Window dialog) throws Exception {
				assertThat(dialog.containsLabel("Can't connect to the database with the settings"));
				return dialog.getButton("OK").triggerClick();
			}
		}).run();
	}
	
	@Test
	public void testSaveConfiguration(){
		
		configDialog.getInputTextBox("databaseHostField").setText(databaseProperties.getProperty("app.hostname"));
		configDialog.getInputTextBox("databaseNameField").setText(databaseProperties.getProperty("app.db"));
		configDialog.getInputTextBox("databaseUsernameField").setText(databaseProperties.getProperty("app.username"));
		configDialog.getPasswordField("databasePasswordField").setPassword(databaseProperties.getProperty("app.password"));
		
		WindowInterceptor.init(configDialog.getButton("Save Configuration").triggerClick()).process(new WindowHandler() {
			
		@Override
			public Trigger process(Window dialog) throws Exception {
				assertThat(dialog.containsLabel("Database configuration file was successfully updated"));
				return dialog.getButton("OK").triggerClick();
			}
		}).run();
	}
}
