/**
 * Added by James
 * on 2011-2-22
 */
package com.aaut.skeleton.rbac.dao;

import java.util.List;

import com.aaut.skeleton.commons.util.dao.BasicDao;

public interface CatalogDao<PO> extends BasicDao<PO> {

	List<PO> findAll();
}
