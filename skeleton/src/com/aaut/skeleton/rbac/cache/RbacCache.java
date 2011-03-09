/**
 * Added by James
 * on 2011-3-9
 */
package com.aaut.skeleton.rbac.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aaut.skeleton.commons.cache.Cache;
import com.aaut.skeleton.rbac.vo.CatalogFacade;

public class RbacCache implements Cache {

	private static RbacCache defaultInstance = new RbacCache();

	public static RbacCache getDefaultInstance() {
		return defaultInstance;
	}
	
	List<CatalogFacade> catalogs = new ArrayList<CatalogFacade>();
	Map<String, CatalogFacade> catalogMap = new HashMap<String, CatalogFacade>();

	@Override
	public String getKey() {
		return "com.aaut.skeleton.rbac.cache.RbacCache";
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
