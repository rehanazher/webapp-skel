/**
 * Added by James
 * on 2011-2-21
 */
package com.aaut.skeleton.commons.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {

	protected static Map<String, CacheProvider<Cache>> providerMap;

	public static synchronized void putCache(String key,
			CacheProvider<Cache> provider) {
		if (providerMap == null) {
			providerMap = new HashMap<String, CacheProvider<Cache>>();
		}
		providerMap.put(key, provider);
	}

	public static Cache getCache(Cache c) {
		CacheProvider<Cache> provider = null;
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
