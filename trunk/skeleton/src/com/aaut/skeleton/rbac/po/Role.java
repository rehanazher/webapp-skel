/**
 * Added by James
 * on 2011-2-18
 */
package com.aaut.skeleton.rbac.po;

import com.aaut.skeleton.commons.util.dao.Entity;

public class Role implements Entity {

	private static final long serialVersionUID = -237207996550051932L;

	private String id;
	private String name;
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
