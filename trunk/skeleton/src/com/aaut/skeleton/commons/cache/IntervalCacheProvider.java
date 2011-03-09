/**
 * Added by James
 * on 2011-2-21
 */
package com.aaut.skeleton.commons.cache;

public class IntervalCacheProvider<E extends Cache> implements CacheProvider<E> {

	// interval refresh time(s)
	private long interval = 3600L;
	private E cache;
	private long lastUpdate;
	private boolean forceUpdate = false;

	public IntervalCacheProvider() {
	}

	public IntervalCacheProvider(E cache) {
		this.cache = cache;
	}

	public IntervalCacheProvider(E cache, long interval) {
		this.cache = cache;
		this.interval = interval;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	@Override
	public boolean needRefresh() {
		return forceUpdate
				|| lastUpdate < (System.currentTimeMillis() - interval * 1000);
	}

	@Override
	public void refresh() {
		cache.refresh();
		lastUpdate = System.currentTimeMillis();
		forceUpdate = false;
	}

	@Override
	public void destroy() {
		cache = null;
	}

	@Override
	public E getCache() {
		return this.cache;
	}

	@Override
	public void setCache(E cache) {
		this.cache = cache;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
}
