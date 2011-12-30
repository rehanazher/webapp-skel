package jp.co.fcctvweb.actions;

import com.opensymphony.xwork2.ActionSupport;

public class DocPreviewAction extends ActionSupport {

	private static final long serialVersionUID = 5851935697419961709L;

	private String type;
	private int fileId;
	private int height;
	private int width;

	public String execute() {
		return SUCCESS;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
