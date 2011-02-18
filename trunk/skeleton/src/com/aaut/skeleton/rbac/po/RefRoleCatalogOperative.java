/**
 * Added by James
 * on 2011-2-18
 */
package com.aaut.skeleton.rbac.po;

import java.io.Serializable;

public class RefRoleCatalogOperative implements Serializable {

	private static final long serialVersionUID = -8883133207684363105L;

	private String roleId;
	private String catalogId;
	private String operativeId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

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
}
