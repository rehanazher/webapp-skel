/**
 * Added by James
 * on 2011-2-21
 */
package com.aaut.skeleton.commons.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {

	protected static Map<String, CacheProvider<? extends Cache>> providerMap;

	public static synchronized void putCache(Cache c,
			CacheProvider<? extends Cache> provider) {
		if (providerMap == null) {
			providerMap = new HashMap<String, CacheProvider<? extends Cache>>();
		}
		providerMap.put(c.getKey(), provider);
	}

	public static Cache getCache(Cache c) {
		CacheProvider<? extends Cache> provider = null;
		Cache cache = null;
		if (providerMap.containsKey(c.getKey())) {
			provider = providerMap.get(c.getKey());
			if (provider.needRefresh()) {
				provider.refresh();
			}
			cache = provider.getCache();
		}

		return cache;
	}
}
