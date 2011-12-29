package jp.co.fcctvweb.vo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.po.FakeFile;
import jp.co.fcctvweb.po.FakeFolder;

public class MyDocNode implements Comparator<MyDocNode> {

	private boolean root = false;
	private boolean leaf = true;

	private int key;
	private String name;
	private String fileName;
	private String folderName;
	private String position;
	private int parentId;
	private String type = "folder";

	public MyDocNode() {
	}

	public MyDocNode(FakeFolder folder) {
		this.type = "folder";
		this.key = folder.getId();
		this.name = folder.getFolderName();
		this.folderName = folder.getFolderName();
		this.position = folder.getPosition();
		this.parentId = folder.getParentId();
	}

	public MyDocNode(FakeFile file, FakeFolder parentFolder) {
		this.type = "file";
		this.key = file.getId();
		this.name = file.getFileName().replace(
				parentFolder.getPosition() + Config.DOC_NAME_SEP, "");
		this.fileName = file.getFileName();
		this.parentId = file.getFolderId();
		this.leaf = true;
	}

	private List<MyDocNode> children = new ArrayList<MyDocNode>();

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<MyDocNode> getChildren() {
		return children;
	}

	public void addChild(MyDocNode child) {
		children.add(child);
		leaf = false;
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Override
	public int compare(MyDocNode o1, MyDocNode o2) {
		if (o1.getType() == "folder" && o2.getType() == "file") {
			return -1;
		} else if (o1.getType() == "file" && o2.getType() == "folder") {
			return 1;
		} else {
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	}

}
