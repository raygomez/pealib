package controllers;

import java.awt.Dialog;
import java.awt.Frame;


import utilities.Task;
import views.LoadingDialog;

public class LoadingControl {
	
	private static Task<?, ?> task;
	private static LoadingDialog loadingDialog;
	
	private LoadingControl(Task<?, ?> task, Frame parent) {
		
		this.task = task;
		loadingDialog = new LoadingDialog(parent);
		task.addPropertyChangeListener(loadingDialog);
		
	}
	
	private LoadingControl(Task<?, ?> task, Dialog parent) {
		
		this.task = task;
		loadingDialog = new LoadingDialog(parent);
		task.addPropertyChangeListener(loadingDialog);
		
	}

	public static LoadingControl init(Task<?, ?> task, Frame parent){
		return new LoadingControl(task, parent);
	}
	
	public static LoadingControl init(Task<?, ?> task, Dialog parent){
		return new LoadingControl(task, parent);
	}
	
	public void executeLoading(){
		task.execute();
	}
}
