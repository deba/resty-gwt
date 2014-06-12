package org.appops.shared.rebind.callback;


public interface MethodAsyncCallback<T> {

	 public void onFailure(Throwable exception);

	 public void onSuccess(T response);
}
