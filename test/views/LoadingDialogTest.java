package views;

import static org.junit.Assert.*;

import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import org.unitils.UnitilsJUnit4TestClassRunner;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class LoadingDialogTest extends UISpecTestCase {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testLoadingDialog() {
		LoadingDialog ld = new LoadingDialog();
		assertNotNull(ld);
	}

	@Test
	public void testLoadingDialogDialog() {
		LoadingDialog ld = new LoadingDialog(new JDialog());
		assertNotNull(ld);
	}

	@Test
	public void testLoadingDialogFrame() {
		LoadingDialog ld = new LoadingDialog(new JFrame());
		assertNotNull(ld);
	}

	@Test
	public void testInitialize() {
		LoadingDialog ld = new LoadingDialog();
		Window loadingDialog = new Window(ld);
		
		assertThat(loadingDialog.isModal());
		assertNotNull(loadingDialog.getTextBox("Loading"));
	}

}
