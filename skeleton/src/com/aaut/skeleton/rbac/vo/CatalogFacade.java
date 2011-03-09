/**
 * Added by James
 * on 2011-3-9
 */
package com.aaut.skeleton.rbac.vo;

import java.util.ArrayList;
import java.util.List;

import com.aaut.skeleton.commons.util.Validators;
import com.aaut.skeleton.rbac.po.Catalog;

public class CatalogFacade {

	private Catalog catalog;
	private CatalogFacade parent;
	private List<CatalogFacade> children = new ArrayList<CatalogFacade>();
	private List<OperativeFacade> operatives;

	public CatalogFacade(Catalog catalog) {
		this.catalog = catalog;
	}

	public List<OperativeFacade> getOperatives() {
		return operatives;
	}



	public void setOperatives(List<OperativeFacade> operatives) {
		this.operatives = operatives;
	}



	public CatalogFacade getParent() {
		return parent;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public boolean hasChildren() {
		return Validators.isEmpty(children);
	}

	public List<CatalogFacade> getChildren() {
		return this.children;
	}

	public void setParent(CatalogFacade parent) {
		this.parent = parent;
	}

	public void setChildren(List<CatalogFacade> children) {
		this.children = children;
	}

	public String getId() {
		return catalog.getId();
	}

	public void setId(String id) {
		catalog.setId(id);
	}

	public String getName() {
		return catalog.getName();
	}

	public void setName(String name) {
		catalog.setName(name);
	}

	public String getParentId() {
		return catalog.getParentId();
	}

	public void setParentId(String parentId) {
		catalog.setParentId(parentId);
	}

	public int getWeight() {
		return catalog.getWeight();
	}

	public void setWeight(int weight) {
		catalog.setWeight(weight);
	}

	public int getDisabled() {
		return catalog.getDisabled();
	}

	public void setDisabled(int disabled) {
		catalog.setDisabled(disabled);
	}
}
