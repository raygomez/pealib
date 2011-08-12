package pealib;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Button;
import org.uispec4j.PasswordField;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Constants;

@DataSet({ "../models/user.xml", "../models/book.xml",
		"../models/reserves.xml", "../models/borrows.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class PeaLibraryTest extends UISpecTestCase {

	@Test
	public void testLogin() {
		WindowInterceptor.init(new Trigger() {

			@Override
			public void run() throws Exception {
				PeaLibrary.main(new String[] { Constants.TEST_CONFIG });

			}
		}).process(new WindowHandler() {

			@Override
			public Trigger process(final Window window) throws Exception {
				TextBox username = window.getInputTextBox("username");
				PasswordField password = window.getPasswordField("password");
				username.setText("jvillar");
				password.setPassword("123456");

				return new Trigger() {

					@Override
					public void run() throws Exception {
						final Button login = window.getButton("Log In");
						login.click();
					}
				};
			}
		}).processTransientWindow().processTransientWindow()
				.process(new WindowHandler() {

					@Override
					public Trigger process(final Window window)
							throws Exception {

						return new Trigger() {

							@Override
							public void run() throws Exception {
								window.dispose();

							}
						};

					}
				}).run();

	}

}
