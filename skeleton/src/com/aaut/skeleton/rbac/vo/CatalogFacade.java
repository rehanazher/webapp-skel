/**
 * Added by James
 * on 2011-3-9
 */
package com.aaut.skeleton.rbac.vo;

import java.util.LinkedHashSet;
import java.util.Set;

import com.aaut.skeleton.commons.util.Validators;
import com.aaut.skeleton.rbac.po.Catalog;

public class CatalogFacade {

	private Catalog catalog;
	private CatalogFacade parent;
	private Set<CatalogFacade> children = new LinkedHashSet<CatalogFacade>();
	private Set<OperativeFacade> operatives = new LinkedHashSet<OperativeFacade>();

	private static CatalogFacade root;

	public static CatalogFacade getRoot() {
		if (root == null) {
			root = new CatalogFacade(Catalog.getRoot());
		}

		return root;
	}

	public CatalogFacade(Catalog catalog) {
		this.catalog = catalog;
	}

	public Set<CatalogFacade> getChildren() {
		return children;
	}

	public void setChildren(Set<CatalogFacade> children) {
		if (!Validators.isEmpty(children)) {
			this.children = children;
		} else {
			this.children = new LinkedHashSet<CatalogFacade>();
		}
	}

	public void addChild(CatalogFacade child) {
		child.parent = this;
		if (this.children == null) {
			children = new LinkedHashSet<CatalogFacade>();
		}
		this.children.add(child);
	}

	public Set<OperativeFacade> getOperatives() {
		return operatives;
	}

	public void setOperatives(Set<OperativeFacade> operatives) {
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

	public void setParent(CatalogFacade parent) {
		this.parent = parent;
		this.parent.children.add(this);
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
