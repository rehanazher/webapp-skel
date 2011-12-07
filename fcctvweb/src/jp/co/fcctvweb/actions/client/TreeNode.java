package jp.co.fcctvweb.actions.client;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

	public static final String ROOT_ID = "00000000000000000000000000000000";

	private boolean root;
	private boolean leaf = true;
	private boolean expanded;
	private boolean expandable = true;

	private String href;
	private String hrefTarget;

	private String id;
	private String parentId;

	private String cls = "";

	private List<TreeNode> children = new ArrayList<TreeNode>();

	public TreeNode() {
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isExpandable() {
		return expandable;
	}

	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getHrefTarget() {
		return hrefTarget;
	}

	public void setHrefTarget(String hrefTarget) {
		this.hrefTarget = hrefTarget;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public boolean hasChild() {
		return !children.isEmpty();
	}

	public TreeNode addChild(TreeNode child) {
		leaf = false;
		child.setParentId(this.id);
		this.children.add(child);
		return this;
	}

}
