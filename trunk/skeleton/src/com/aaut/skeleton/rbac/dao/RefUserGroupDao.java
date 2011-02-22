/**
 * Added by James
 * on 2011-2-22
 */
package com.aaut.skeleton.rbac.dao;

import java.util.List;

import com.aaut.skeleton.rbac.po.RefUserGroup;

public interface RefUserGroupDao {
	
	List<RefUserGroup> findByUserId(String userId);
	
	List<RefUserGroup> findByGroupId(String groupId);
	
	RefUserGroup findByRefId(String userId, String groupId);
	
	int deleteByUserId(String userId);
	
	int deleteByGroupId(String groupId);
	
	int deleteByRefId(String userId, String groupId);
	
	int insert(RefUserGroup... refs);
	
}
