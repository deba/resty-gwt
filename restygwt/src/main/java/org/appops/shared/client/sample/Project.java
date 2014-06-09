package org.appops.shared.client.sample;

import org.appops.shared.client.core.Entity;

public interface Project extends Entity {

	Long getId();
	void setId(Long id);
	String getProjectName();
	void setProjectName(String projectName);
}
