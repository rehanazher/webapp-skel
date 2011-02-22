/**
 * Added by James
 * on 2011-2-22
 */
package com.aaut.skeleton.rbac.dao;

import java.util.List;

import com.aaut.skeleton.rbac.po.RefRoleGroup;

public interface RefRoleGroupDao {

	List<RefRoleGroup> findByRoleId(String roleId);

	List<RefRoleGroup> findByGroupId(String groupId);

	RefRoleGroup findByRefId(String roleId, String groupId);

	int deleteByRoleId(String roleId);

	int deleteByGroupId(String groupId);

	int deleteByRefId(String roleId, String groupId);

	int insert(RefRoleGroup... refs);
}
