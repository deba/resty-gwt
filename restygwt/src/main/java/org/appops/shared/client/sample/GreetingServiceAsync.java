package org.appops.shared.client.sample;

import org.appops.shared.client.core.annotate.Interface;

import com.google.gwt.user.client.rpc.AsyncCallback;

@Interface
public interface GreetingServiceAsync {

	String greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	
	int greetServerId(String input, AsyncCallback<Integer> callback) throws IllegalArgumentException;
}
