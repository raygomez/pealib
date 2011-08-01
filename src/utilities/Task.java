package utilities;

import java.util.concurrent.Callable;

import javax.swing.SwingWorker;

public class Task<T, V> extends SwingWorker<T, V> {

	private Callable<T> toDo;
	private Callable<?> toDoAfter;
	
	public Task(Callable<T> toDo){
		this.toDo = toDo;
		this.toDoAfter = null;
	}
	
	public Task(Callable<T> toDo, Callable<?> toDoAfter){
		this.toDo = toDo;
		this.toDoAfter = toDoAfter;		
	}
	
	@Override
	protected T doInBackground() throws Exception {
		// TODO Auto-generated method stub
		return toDo.call();
	}

	@Override
	protected void done() {
		// TODO Auto-generated method stub
		if(toDoAfter != null){
			try {
				toDoAfter.call();
			} catch (Exception e) {
				CrashHandler.handle(e);
			}
		}
	}
}
