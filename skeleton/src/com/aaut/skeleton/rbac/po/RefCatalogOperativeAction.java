/**
 * Added by James
 * on 2011-2-18
 */
package com.aaut.skeleton.rbac.po;

import java.io.Serializable;

public class RefCatalogOperativeAction implements Serializable {

	private static final long serialVersionUID = -5641763662609250556L;

	private String catalogId;
	private String operativeId;
	private String actionId;

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getOperativeId() {
		return operativeId;
	}

	public void setOperativeId(String operativeId) {
		this.operativeId = operativeId;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
}
