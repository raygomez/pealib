package controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Panel;
import org.uispec4j.UISpecTestCase;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.dbunit.annotation.DataSet;

import utilities.Connector;
import utilities.Constants;

@DataSet({ "../models/user.xml", "../models/book.xml",
		"../models/reserves.xml", "../models/borrows.xml" })
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class TransactionControllerTest extends UISpecTestCase {

	Panel panel;

	@Before
	public void setUp() throws Exception {
		new Connector(Constants.TEST_CONFIG);
		panel = new Panel(new TransactionController().getTabbedPane());
	}

	@Test
	public void test() {
	}

}
