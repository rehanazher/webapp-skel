/**
 * Added by James
 * on 2011-3-9
 */
package com.aaut.skeleton.rbac.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

import com.aaut.skeleton.commons.cache.Cache;
import com.aaut.skeleton.commons.cache.CacheManager;
import com.aaut.skeleton.commons.cache.IntervalCacheProvider;
import com.aaut.skeleton.commons.util.EntityUtils;
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
import com.aaut.skeleton.rbac.po.RefCatalogOperativeAction;
import com.aaut.skeleton.rbac.po.RefRoleGroup;
import com.aaut.skeleton.rbac.po.RefUserGroup;
import com.aaut.skeleton.rbac.po.Role;
import com.aaut.skeleton.rbac.po.User;
import com.aaut.skeleton.rbac.vo.ActionFacade;
import com.aaut.skeleton.rbac.vo.CatalogFacade;
import com.aaut.skeleton.rbac.vo.OperativeFacade;

public class RbacServiceImpl implements RbacService {

	private static Logger logger = Logger.getLogger(RbacServiceImpl.class);

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
		return RbacCacheHandler.getRbacCache().getCatalogs();
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

	protected List<RefCatalogOperativeAction> getAllCoaOrigin() {
		return refCoaDao.findAll();
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

		private List<CatalogFacade> catalogs = new ArrayList<CatalogFacade>();

		private Map<String, CatalogFacade> catalogMap = new HashMap<String, CatalogFacade>();

		@Override
		public String getKey() {
			return "com.aaut.skeleton.rbac.cache.RbacCache";
		}

		@SuppressWarnings("unchecked")
		@Override
		public void refresh() {
			catalogs = new ArrayList<CatalogFacade>();
			catalogs.add(CatalogFacade.getRoot());

			// prepare data;
			List<Catalog> cList = outter.getAllCatalogOrigin();
			List<Operative> oList = outter.getAllOperativeOrigin();
			List<Action> aList = outter.getAllActionOrigin();

			List<RefCatalogOperativeAction> coaList = outter.getAllCoaOrigin();

			MultiKeyMap coaMap = new MultiKeyMap();
			Map<String, List<String>> coMap = new HashMap<String, List<String>>();

			for (RefCatalogOperativeAction coa : coaList) {
				if (coaMap
						.containsKey(coa.getCatalogId(), coa.getOperativeId())) {
					List<String> actionIdList = (List<String>) coaMap.get(coa
							.getCatalogId(), coa.getOperativeId());
					actionIdList.add(coa.getActionId());
				} else {
					List<String> actionIdList = new ArrayList<String>();
					actionIdList.add(coa.getActionId());
					coaMap.put(coa.getCatalogId(), coa.getOperativeId(),
							actionIdList);
				}

				if (coMap.containsKey(coa.getCatalogId())) {
					List<String> optIdList = coMap.get(coa.getCatalogId());
					optIdList.add(coa.getOperativeId());
				} else {
					List<String> optIdList = new ArrayList<String>();
					optIdList.add(coa.getOperativeId());
					coMap.put(coa.getCatalogId(), optIdList);
				}
			}

			List<CatalogFacade> cfList = new ArrayList<CatalogFacade>();
			List<OperativeFacade> ofList = new ArrayList<OperativeFacade>();
			List<ActionFacade> afList = new ArrayList<ActionFacade>();

			// transform to facade mode
			cfList.add(CatalogFacade.getRoot());
			for (Catalog c : cList) {
				cfList.add(new CatalogFacade(c));
			}

			for (Operative o : oList) {
				ofList.add(new OperativeFacade(o));
			}

			for (Action a : aList) {
				afList.add(new ActionFacade(a));
			}

			Map<String, CatalogFacade> cfMap = EntityUtils.list2Map(cfList);
			Map<String, OperativeFacade> ofMap = EntityUtils.list2Map(ofList);
			Map<String, ActionFacade> afMap = EntityUtils.list2Map(afList);

			// set relationship
			// set operative cascade relationship
			for (OperativeFacade of : ofList) {
				if (ofMap.containsKey(of.getParentId())) {
					of.setParent(ofMap.get(of.getParentId()));
				}
			}
			// set catalog cascade relationship
			for (CatalogFacade cf : cfList) {
				if (cfMap.containsKey(cf.getParentId())) {
					cf.setParent(cfMap.get(cf.getParentId()));
				}
			}

			// set catalog - operative relationship
			for (Entry<String, List<String>> entry : coMap.entrySet()) {
				String catalogId = entry.getKey();
				List<String> operativeIdList = entry.getValue();

				if (cfMap.containsKey(catalogId)) {
					CatalogFacade cf = cfMap.get(catalogId);

					for (String optId : operativeIdList) {
						if (ofMap.containsKey(optId)) {
							cf.addOperative(ofMap.get(optId));
						}
					}
				}
			}

			// set catalog - operative - action relationship
			for (CatalogFacade cf : cfList) {
				for (OperativeFacade of : cf.getOperatives()) {
					if (coaMap.containsKey(cf.getId(), of.getId())) {
						List<String> actionIdList = (List<String>) coaMap.get(
								cf.getId(), of.getId());

						for (String actionId : actionIdList) {
							of.addAction(afMap.get(actionId));
						}
					}
				}

				catalogs.add(cf);
			}

		}

		public List<CatalogFacade> getCatalogs() {
			return catalogs;
		}
	}

	private static class RbacCacheHandler {

		public static RbacCache getRbacCache() {

			RbacCache cache = CacheManager.getCache(RbacCache
					.getDefaultInstance());

			if (cache == null) {

				CacheManager.putCache(RbacCache.getDefaultInstance(),
						new IntervalCacheProvider<RbacCache>(new RbacCache(),
								3600));

				cache = CacheManager.getCache(cache);
			}

			return cache;
		}
	}

}
