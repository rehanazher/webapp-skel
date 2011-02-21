/**
 * Added by James
 * on 2011-2-21
 */
package com.aaut.skeleton.commons.cache;

public class IntervalCacheProvider<E extends Cache> implements CacheProvider<E> {

	// interval refresh time(s)
	private long interval = 3600L;
	private E value;
	private long lastUpdate;
	private boolean forceUpdate = false;

	public IntervalCacheProvider() {
	}

	public IntervalCacheProvider(E value) {
		this.value = value;
	}

	public IntervalCacheProvider(E value, long interval) {
		this.value = value;
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
				|| lastUpdate < (System.currentTimeMillis() - 3600 * 1000);
	}

	@Override
	public void refresh() {
		value.refresh();
		lastUpdate = System.currentTimeMillis();
		forceUpdate = false;
	}

	@Override
	public void destroy() {
		value = null;
	}

	@Override
	public E getValue() {
		return this.value;
	}

	@Override
	public void setValue(E value) {
		this.value = value;
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
