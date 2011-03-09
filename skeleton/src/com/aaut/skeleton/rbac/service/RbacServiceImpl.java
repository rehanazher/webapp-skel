/**
 * Added by James
 * on 2011-3-9
 */
package com.aaut.skeleton.rbac.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aaut.skeleton.commons.cache.Cache;
import com.aaut.skeleton.commons.cache.CacheManager;
import com.aaut.skeleton.commons.cache.CacheProvider;
import com.aaut.skeleton.commons.cache.IntervalCacheProvider;
import com.aaut.skeleton.commons.util.EntityUtils;
import com.aaut.skeleton.rbac.cache.RbacCache;
import com.aaut.skeleton.rbac.dao.ActionDao;
import com.aaut.skeleton.rbac.dao.CatalogDao;
import com.aaut.skeleton.rbac.dao.GroupDao;
import com.aaut.skeleton.rbac.dao.OperativeDao;
import com.aaut.skeleton.rbac.dao.RefCatalogOperativeActionDao;
import com.aaut.skeleton.rbac.dao.RefRoleCatalogOperativeDao;
import com.aaut.skeleton.rbac.dao.RoleDao;
import com.aaut.skeleton.rbac.dao.UserDao;
import com.aaut.skeleton.rbac.po.Action;
import com.aaut.skeleton.rbac.po.Catalog;
import com.aaut.skeleton.rbac.po.Group;
import com.aaut.skeleton.rbac.po.Operative;
import com.aaut.skeleton.rbac.po.RefRoleGroup;
import com.aaut.skeleton.rbac.po.RefUserGroup;
import com.aaut.skeleton.rbac.po.Role;
import com.aaut.skeleton.rbac.po.User;
import com.aaut.skeleton.rbac.vo.CatalogFacade;

public class RbacServiceImpl implements RbacService {

	private ActionDao<Action> actionDao;
	private CatalogDao<Catalog> catalogDao;
	private GroupDao<Group> groupDao;
	private OperativeDao<Operative> operativeDao;
	private RoleDao<Role> roleDao;
	private UserDao<User> userDao;

	private RefCatalogOperativeActionDao refCoaDao;
	private RefRoleCatalogOperativeDao refRcoDao;
	private RefRoleGroup refRoleGroupDao;
	private RefUserGroup refUserGroupDao;

	public List<CatalogFacade> getAllCatalog() {

		RbacCache rbac = RbacCacheHandler.getRbacCache();

		return null;
	}

	protected List<Catalog> getAllCatalogOrigin() {
		return catalogDao.findAll();
	}

	protected List<Operative> getAllOperativeOrigin() {
		return operativeDao.findAll();
	}

	protected List<Action> getAllActionOrigin() {
		return actionDao.findAll();
	}

	public void setActionDao(ActionDao<Action> actionDao) {
		this.actionDao = actionDao;
	}

	public void setCatalogDao(CatalogDao<Catalog> catalogDao) {
		this.catalogDao = catalogDao;
	}

	public void setGroupDao(GroupDao<Group> groupDao) {
		this.groupDao = groupDao;
	}

	public void setOperativeDao(OperativeDao<Operative> operativeDao) {
		this.operativeDao = operativeDao;
	}

	public void setRefCoaDao(RefCatalogOperativeActionDao refCoaDao) {
		this.refCoaDao = refCoaDao;
	}

	public void setRefRcoDao(RefRoleCatalogOperativeDao refRcoDao) {
		this.refRcoDao = refRcoDao;
	}

	public void setRefRoleGroupDao(RefRoleGroup refRoleGroupDao) {
		this.refRoleGroupDao = refRoleGroupDao;
	}

	public void setRefUserGroupDao(RefUserGroup refUserGroupDao) {
		this.refUserGroupDao = refUserGroupDao;
	}

	public void setRoleDao(RoleDao<Role> roleDao) {
		this.roleDao = roleDao;
	}

	public void setUserDao(UserDao<User> userDao) {
		this.userDao = userDao;
	}

	public static class RbacCache implements Cache {

		private static RbacCache defaultInstance = new RbacCache();
		private static RbacServiceImpl outter = new RbacServiceImpl();

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
			List<Catalog> cList = outter.getAllCatalogOrigin();
			List<Operative> oList = outter.getAllOperativeOrigin();
			List<Action> aList = outter.getAllActionOrigin();

			Map<String, Catalog> cMap = EntityUtils.list2Map(cList);
			Map<String, Operative> oMap = EntityUtils.list2Map(oList);
			Map<String, Action> aMap = EntityUtils.list2Map(aList);
		}
	}

	private static class RbacCacheHandler {

		public static RbacCache getRbacCache() {

			RbacCache cache = (RbacCache) CacheManager.getCache(RbacCache
					.getDefaultInstance());

			if (cache == null) {

				CacheManager.putCache(RbacCache.getDefaultInstance(),
						new IntervalCacheProvider<RbacCache>(new RbacCache(),
								3600));

				cache = (RbacCache) CacheManager.getCache(cache);
			}

			return cache;
		}
	}
}
