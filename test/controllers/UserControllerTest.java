package controllers;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.UISpecTestCase;

public class UserControllerTest extends UISpecTestCase {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetUserPanel() {
		JPanel panel = new UserController(null).getUserPanel();
		assertNotNull(panel);
	}

}
