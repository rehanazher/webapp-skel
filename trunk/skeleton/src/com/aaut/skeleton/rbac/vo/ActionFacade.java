/**
 * Added by James
 * on 2011-3-9
 */
package com.aaut.skeleton.rbac.vo;

import com.aaut.skeleton.commons.util.dao.Entity;
import com.aaut.skeleton.rbac.po.Action;

public class ActionFacade implements Entity {
	
	private static final long serialVersionUID = 4467025456427377198L;
	
	private Action action;

	public ActionFacade(Action action) {
		this.action = action;
	}

	public String getId() {
		return action.getId();
	}

	public void setId(String id) {
		action.setId(id);
	}

	public String getName() {
		return action.getName();
	}

	public void setName(String name) {
		action.setName(name);
	}

	public String getAction() {
		return action.getAction();
	}

	public void setAction(String action) {
		this.action.setAction(action);
	}

	public String getNamespace() {
		return action.getNamespace();
	}

	public void setNamespace(String namespace) {
		action.setNamespace(namespace);
	}
}
