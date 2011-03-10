/**
 * Added by James
 * on 2011-3-9
 */
package com.aaut.skeleton.rbac.vo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.aaut.skeleton.commons.util.Validators;
import com.aaut.skeleton.commons.util.dao.Entity;
import com.aaut.skeleton.rbac.po.Catalog;

public class CatalogFacade implements Entity {

	private static final long serialVersionUID = -3132107626449248877L;

	private Catalog catalog;
	private CatalogFacade parent;

	private List<CatalogFacade> children = new ArrayList<CatalogFacade>();
	private List<OperativeFacade> operatives = new ArrayList<OperativeFacade>();

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

	public List<CatalogFacade> getChildren() {
		return children;
	}

	public void setChildren(List<CatalogFacade> children) {
		if (!Validators.isEmpty(children)) {
			this.children = children;
		} else {
			this.children = new ArrayList<CatalogFacade>();
		}
	}

	public void addChild(CatalogFacade child) {
		if (!children.contains(child)) {
			children.add(child);
			child.parent = this;
		}
	}

	public List<OperativeFacade> getOperatives() {
		return operatives;
	}

	public void setOperatives(List<OperativeFacade> operatives) {
		if (!Validators.isEmpty(operatives)) {
			this.operatives = operatives;
		} else {
			this.operatives = new ArrayList<OperativeFacade>();
		}
	}

	public void addOperative(OperativeFacade operative) {
		if (!operatives.contains(operative)) {
			operatives.add(operative);
		}
	}

	public CatalogFacade getParent() {
		return parent;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public boolean hasChildren() {
		return !Validators.isEmpty(children);
	}

	public boolean hasOperatives() {
		return !Validators.isEmpty(operatives);
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
