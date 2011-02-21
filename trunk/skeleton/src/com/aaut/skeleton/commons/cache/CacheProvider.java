/**
 * Added by James
 * on 2011-2-21
 */
package com.aaut.skeleton.commons.cache;

public interface CacheProvider<E> {
	
	boolean needRefresh();
	
	void refresh();

	void destroy();

	E getValue();

	void setValue(E value);
	
	boolean isForceUpdate();
	
	void setForceUpdate(boolean force);
}
