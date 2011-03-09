/**
 * Added by James
 * on 2011-3-9
 */
package com.aaut.skeleton.rbac.vo;

import java.util.List;

import com.aaut.skeleton.commons.util.Validators;
import com.aaut.skeleton.rbac.po.Operative;

public class OperativeFacade {

	private Operative operative;
	private OperativeFacade parent;
	private List<OperativeFacade> children;
	private List<ActionFacade> actions;

	public OperativeFacade(Operative operative) {
		this.operative = operative;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public boolean hasChildren() {
		return Validators.isEmpty(children);
	}

	public boolean hasActions() {
		return Validators.isEmpty(actions);
	}

	public List<ActionFacade> getActions() {
		return actions;
	}

	public void setActions(List<ActionFacade> actions) {
		this.actions = actions;
	}

	public OperativeFacade getParent() {
		return parent;
	}

	public void setParent(OperativeFacade parent) {
		this.parent = parent;
	}

	public List<OperativeFacade> getChildren() {
		return children;
	}

	public void setChildren(List<OperativeFacade> children) {
		this.children = children;
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
