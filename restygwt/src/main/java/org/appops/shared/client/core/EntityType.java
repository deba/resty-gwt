package org.appops.shared.client.core;

import java.io.Serializable;


public class EntityType implements Serializable {

	private String name;
	private String signature;
	
	public EntityType() {
	}
	
	public EntityType(String name, String signature) {
		this.setName(name);
		this.setSignature(signature);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getSignature() {
		return signature;
	}
}
