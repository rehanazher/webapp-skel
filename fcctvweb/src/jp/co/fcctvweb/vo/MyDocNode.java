package jp.co.fcctvweb.vo;

import java.util.ArrayList;
import java.util.List;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.po.FakeFile;
import jp.co.fcctvweb.po.FakeFolder;

public class MyDocNode {

	private boolean root = false;
	private boolean leaf = true;

	private int key;
	private String name;
	private String fileName;
	private String folderName;
	private String position;
	private String type = "folder";

	public MyDocNode() {
	}

	public MyDocNode(FakeFolder folder) {
		this.type = "folder";
		this.key = folder.getId();
		this.name = folder.getFolderName();
		this.folderName = folder.getFolderName();
		this.position = folder.getPosition();
	}

	public MyDocNode(FakeFile file, FakeFolder parentFolder) {
		this.type = "file";
		this.key = file.getId();
		this.name = file.getFileName().replaceFirst(
				parentFolder.getPosition() + Config.DOC_NAME_SEP, "");
		this.fileName = file.getFileName();
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
}
