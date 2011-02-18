/**
 * Added by James
 * on 2011-2-18
 */
package com.aaut.skeleton.rbac.po;

import java.io.Serializable;

public class RefUserGroup implements Serializable {

	private static final long serialVersionUID = 3977382180707772723L;

	private String userId;
	private String groupId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
