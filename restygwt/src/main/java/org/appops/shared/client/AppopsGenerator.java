package org.appops.shared.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class AppopsGenerator implements EntryPoint {
	@Override
	public void onModuleLoad() {
		GWT.create(Dummy.class);
		/*GreetingServiceAsync async = com.google.gwt.core.client.GWT.create(GreetingService.class);
		async.greetServer(null, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});*/
	}
}
