package utilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import views.LoadingDialog;

public class TaskUpdateListener implements PropertyChangeListener {

	private LoadingDialog loadingDialog;
	
	public TaskUpdateListener(LoadingDialog loadingDialog) {
		this.loadingDialog = loadingDialog;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getPropertyName().equalsIgnoreCase("state")){
			System.out.println(evt.getNewValue());
			if(evt.getNewValue().toString().equals("STARTED")){
				loadingDialog.setVisible(true);
			}
			else if(evt.getNewValue().toString().equals("DONE")){
				loadingDialog.dispose();
			}
		}
	}

}
