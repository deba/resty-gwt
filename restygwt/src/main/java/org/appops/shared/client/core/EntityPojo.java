package org.appops.shared.client.core;

import java.io.Serializable;
import java.util.HashMap;

public class EntityPojo implements Entity {

	private Boolean dirty = false;
	private EntityType type;
	private Boolean partial = false;
	protected final String ENTITYPOJO = "entityPojo";

	private HashMap<String, Serializable> properties = new HashMap<String, Serializable>();

	public EntityPojo(Property<Serializable>... propertyArr) {
		for(int i = 0; i < propertyArr.length; i++) {
			Property<Serializable> property = propertyArr[i];
			setPropertyValue(property.getName(), property.getValue());
		}
	}
	
	@Override
	public <T extends Serializable> T getPropertyValue(String name) {
		return (T) this.properties.get(name);
	}

	@Override
	public <T extends Serializable> void setPropertyValue(String name, T t) {
		this.properties.put(name, t);
	}

	@Override
	public boolean isPartial() {
		return false;
	}

	@Override
	public void setPartial(boolean partial) {
		this.partial = partial;
	}

	@Override
	public EntityType getType() {
		return type;
	}

	@Override
	public void setType(EntityType t) {
		this.type = t;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

}
