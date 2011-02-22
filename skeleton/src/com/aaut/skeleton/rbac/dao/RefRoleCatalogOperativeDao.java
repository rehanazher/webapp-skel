/**
 * Added by James
 * on 2011-2-22
 */
package com.aaut.skeleton.rbac.dao;

import java.util.List;

import com.aaut.skeleton.rbac.po.RefRoleCatalogOperative;

public interface RefRoleCatalogOperativeDao {
	
	List<RefRoleCatalogOperative> findByRoleId(String roleId);
	
	List<RefRoleCatalogOperative> findByCatalogIdOperative(String roleId);
}
