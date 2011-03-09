/**
 * Added by James
 * on 2011-2-21
 */
package com.aaut.skeleton.commons.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {

	protected static Map<String, CacheProvider<? extends Cache>> providerMap;

	public static synchronized <E extends Cache> void putCache(Cache c,
			CacheProvider<E> provider) {
		if (providerMap == null) {
			providerMap = new HashMap<String, CacheProvider<? extends Cache>>();
		}
		providerMap.put(c.getKey(), provider);
	}

	@SuppressWarnings("unchecked")
	public static <E extends Cache> E getCache(E c) {
		CacheProvider<E> provider = null;
		E cache = null;
		if (providerMap.containsKey(c.getKey())) {
			provider = (CacheProvider<E>) providerMap.get(c.getKey());
			if (provider.needRefresh()) {
				provider.refresh();
			}
			cache = provider.getCache();
		}

		return cache;
	}
}
