package ca.twodee.backend.manager;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.google.common.util.concurrent.FutureCallback;

abstract public class BackendTask<T> implements Callable<T> {
	private final ArrayList<FutureCallback<T>> callbacks = new ArrayList<>();
	
	public void addCallback(FutureCallback<T> callback) {
		callbacks.add(callback);
	}
	
	public ArrayList<FutureCallback<T>> getCallbacks() {
		return callbacks;
	}
}
