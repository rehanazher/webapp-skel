/**
 * Added by James
 * on 2011-3-8
 */
package com.aaut.skeleton.rbac.dao;

import java.util.List;

import com.aaut.skeleton.rbac.po.RefCatalogOperativeAction;

public interface RefCatalogOperativeActionDao {

	List<RefCatalogOperativeAction> findByCatalogId(String catalogId);

	List<RefCatalogOperativeAction> findByCatalogIdOperativeId(
			String catalogId, String operativeId);

	List<RefCatalogOperativeAction> findByActionId(String actionId);

	RefCatalogOperativeAction findByRefId(String catalogId, String operativeId,
			String actionId);

	int deleteByCatalogId(String catalogId);

	int deleteByCatalogIdOperativeId(String catalogId, String operativeId);

	int deleteByActionId(String actionId);

	int deleteByRefId(String catalogId, String operativeId, String actionId);

	int insert(RefCatalogOperativeAction... refs);
}
