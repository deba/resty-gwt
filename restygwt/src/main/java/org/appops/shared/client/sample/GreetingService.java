package org.appops.shared.client.sample;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * The client-side stub for the RPC service.
 */
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
}
