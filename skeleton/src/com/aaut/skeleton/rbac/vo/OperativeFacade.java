/**
 * Added by James
 * on 2011-3-9
 */
package com.aaut.skeleton.rbac.vo;

import java.util.ArrayList;
import java.util.List;

import com.aaut.skeleton.commons.util.Validators;
import com.aaut.skeleton.commons.util.dao.Entity;
import com.aaut.skeleton.rbac.po.Operative;

public class OperativeFacade implements Entity {

	private static final long serialVersionUID = 5432054772425496297L;

	private Operative operative;
	private OperativeFacade parent;
	private List<OperativeFacade> children = new ArrayList<OperativeFacade>();
	private List<ActionFacade> actions = new ArrayList<ActionFacade>();

	public List<OperativeFacade> getChildren() {
		return children;
	}

	public void setChildren(List<OperativeFacade> children) {
		if (!Validators.isEmpty(children)) {
			this.children = children;
		} else {
			this.children = new ArrayList<OperativeFacade>();
		}
	}

	public void addChild(OperativeFacade child) {
		if (!children.contains(child)) {
			children.add(child);
			child.parent = this;
		}
	}

	public List<ActionFacade> getActions() {
		return actions;
	}

	public void setActions(List<ActionFacade> actions) {
		if (!Validators.isEmpty(actions)) {
			this.actions = actions;
		} else {
			this.actions = new ArrayList<ActionFacade>();
		}
	}

	public void addAction(ActionFacade action) {
		if (!actions.contains(action)) {
			actions.add(action);
		}
	}

	public OperativeFacade(Operative operative) {
		this.operative = operative;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public boolean hasChildren() {
		return !Validators.isEmpty(children);
	}

	public boolean hasActions() {
		return !Validators.isEmpty(actions);
	}

	public OperativeFacade getParent() {
		return parent;
	}

	public void setParent(OperativeFacade parent) {
		this.parent = parent;
		this.parent.children.add(this);
	}

	public String getId() {
		return operative.getId();
	}

	public void setId(String id) {
		operative.setId(id);
	}

	public String getName() {
		return operative.getName();
	}

	public void setName(String name) {
		operative.setName(name);
	}

	public String getParentId() {
		return operative.getParentId();
	}

	public void setParentId(String parentId) {
		operative.setParentId(parentId);
	}

	public String getDescription() {
		return operative.getDescription();
	}

	public void setDescription(String description) {
		operative.setDescription(description);
	}

	public int getWeight() {
		return operative.getWeight();
	}

	public void setWeight(int weight) {
		operative.setWeight(weight);
	}
}
