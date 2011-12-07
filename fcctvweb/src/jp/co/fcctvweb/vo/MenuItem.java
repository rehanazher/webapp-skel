package jp.co.fcctvweb.vo;

import jp.co.fcctvweb.actions.client.TreeNode;

public class MenuItem extends TreeNode {

	private String url;
	private String text;
	private String key;

	public MenuItem() {
	}

	public MenuItem(String text, String key, String url) {
		this.text = text;
		this.key = key;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
