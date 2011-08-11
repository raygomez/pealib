package controllers;

import java.util.concurrent.Callable;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.interception.WindowInterceptor;

import utilities.Task;

public class LoadingControlTest extends UISpecTestCase {

	Task<?,?> task1;
	Task<?,?> task2;
	
	@Before
	public void setUp() throws Exception {
		
		Callable<Void> toDo = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				Thread.sleep(1000);
				return null;
			}
		};
		
		task1 = new Task<Void, Object>(toDo);		
	}

	@Test
	public void testInitTaskOfQQFrame() {
		
		LoadingControl lc = LoadingControl.init(task1, new JFrame());
		assertNotNull(lc);
		
	}

	@Test
	public void testInitTaskOfQQDialog() {
		LoadingControl lc = LoadingControl.init(task1, new JDialog());
		assertNotNull(lc);
	}

	@Test
	public void testExecuteLoading() {
		
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				LoadingControl.init(task1, new JDialog()).executeLoading();
			}
		}).processTransientWindow().run();
	}

}
