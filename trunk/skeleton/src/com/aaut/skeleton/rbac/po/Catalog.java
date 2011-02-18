/**
 * Added by James
 * on 2011-2-18
 */
package com.aaut.skeleton.rbac.po;

import java.io.Serializable;

import com.aaut.skeleton.SkelConstants;

public class Catalog implements Serializable {

	private static final long serialVersionUID = 551051807135811120L;

	public static final String ROOT_ID = SkelConstants.RbacConstants.ROOT_ID;

	private String id;
	private String name;
	private String parentId;
	private int weight;
	private int disabled;

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getDisabled() {
		return disabled;
	}

	public void setDisabled(int disabled) {
		this.disabled = disabled;
	}
}
