/**
 * Added by James
 * on 2011-2-18
 */
package com.aaut.skeleton.commons.util.dao;

public interface BasicDao<PO> {

	public abstract PO findById(String id);

	public abstract String deleteById(String id);

	public abstract String add(PO ele);

	public abstract int update(PO ele);

}
