package utilities;

import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.uispec4j.*;
import org.uispec4j.interception.*;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.reflectionassert.ReflectionAssert;

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class TaskTest extends UISpecTestCase{

	Callable<Integer> toDo;
	Callable<Void> toDoAfter;
	
	Task<Integer, Object> task;
	
	
	@Before
	public void setUp() throws Exception {
		
		toDo = new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				
				return add(1, 5);
			}
		};
		
		toDoAfter = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				sayHello("Jeph");
				return null; 
			}
		};
		
		 task = new Task<Integer, Object>(toDo, toDoAfter);
	}

	@Test
	public void testDone() {
		WindowInterceptor.init(new Trigger() {
			
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				task.done();
			}
		}).process(new WindowHandler() {
			public Trigger process(Window dialog) {
				assertThat(dialog
						.containsLabel("Hello Jeph!"));
				return dialog.getButton("OK").triggerClick();
			}

		}).run();
	}
	
	@Test
	public void testDone2() {
		new Task<Integer, Object>(toDo).done();
	}
	
	@Test(expected=Error.class)
	public void testDone3() {
		PowerMock.mockStatic(System.class);
		PowerMock.mockStatic(JOptionPane.class);
		
		final Callable<Void> toDoAfter = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				throw new Exception();
			}
			
		};
		
		new Task<Integer, Object>(toDo, toDoAfter).done();		
	}

	@Test
	public void testTaskCallableOfT() {
		Task<Integer, Object> task = new Task<Integer, Object>(toDo);
		assertNotNull(task);
	}

	@Test
	public void testTaskCallableOfTCallableOfT() {
		Task<Integer, Object> task = new Task<Integer, Object>(toDo, toDoAfter);
		assertNotNull(task);
	}

	@Test
	public void testDoInBackground() {
		try {
			ReflectionAssert.assertReflectionEquals(toDo.call(), task.doInBackground());
		} catch (AssertionFailedError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDoInBackground2() {
		
		WindowInterceptor.init(new Trigger() {
			@Override
			public void run() throws Exception {
				// TODO Auto-generated method stub
				new Task<Void, Object>(toDoAfter).doInBackground();
			}
		}).process(new WindowHandler() {
			public Trigger process(Window dialog) {
				assertThat(dialog
						.containsLabel("Hello Jeph!"));
				return dialog.getButton("OK").triggerClick();
			}
		}).run();
	}
	
	private int add(int i, int j){
		return i+j;
	}
	
	private void sayHello(String name){
		JOptionPane.showMessageDialog(null, "Hello "+name+"!");
	}

}
