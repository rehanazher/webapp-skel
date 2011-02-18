/**
 * Added by James
 * on 2011-2-18
 */
package com.aaut.skeleton.commons.util.dao;

public interface BasicDao<PO> {

	PO findById(String id);

	String deleteById(String id);

	String add(PO ele);

	int update(PO ele);

}
