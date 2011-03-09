/**
 * Added by James
 * on 2011-2-18
 */
package com.aaut.skeleton.rbac.po;

import static com.aaut.skeleton.SkelConstants.RbacConstants.ROOT_ID;
import static com.aaut.skeleton.SkelConstants.RbacConstants.ROOT_NAME;

import com.aaut.skeleton.commons.util.dao.Entity;

public class Catalog implements Entity {

	private static final long serialVersionUID = 551051807135811120L;

	private static Catalog root;

	public static Catalog getRoot() {
		if (root == null) {
			root = new Catalog();
			root.setId(ROOT_ID);
			root.setName(ROOT_NAME);
			root.setParentId(null);
			root.setWeight(0);
			root.setDisabled(0);
		}

		return root;
	}

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
