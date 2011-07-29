package utilities;

import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;

import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import views.LoadingDialog;

public class TaskUpdateListenerTest extends UISpecTestCase {

	TaskUpdateListener taskUpdate;
	
	@Before
	public void setUp() throws Exception {
		taskUpdate = new TaskUpdateListener(new LoadingDialog());
	}

	@Test
	public void testTaskUpdateListener() {
		taskUpdate = new TaskUpdateListener(new LoadingDialog());
		assertNotNull(taskUpdate);
	}

	@Test
	public void testPropertyChange() {
		
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				taskUpdate.propertyChange(new PropertyChangeEvent(new JFrame(), "state", "PENDING", "STARTED"));
			}
		}).process(new WindowHandler() {
			public Trigger process(Window dialog) {
				return new Trigger() {
					
					@Override
					public void run() throws Exception {
						// TODO Auto-generated method stub
						taskUpdate.propertyChange(new PropertyChangeEvent(new JFrame(), "state", "STARTED", "DONE"));
					}
				};
			}

		}).run();
		
	}
	
	@Test
	public void testPropertyChange2() {
		
		taskUpdate.propertyChange(new PropertyChangeEvent(new JFrame(), "progress", "PENDING", "STARTED"));
		
	}

}
