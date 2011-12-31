package jp.co.fcctvweb.actions;

import jp.co.fcctvweb.po.FakeFile;
import jp.co.fcctvweb.services.MyDocService;

import com.opensymphony.xwork2.ActionSupport;

public class DocPreviewAction extends BasicJsonAction {

	private static final long serialVersionUID = 5851935697419961709L;

	private String type;
	private int fileId;
	private int height;
	private int width;
	private String title;
	
	private MyDocService myDocService;

	public String execute() {
		FakeFile file = myDocService.getFileById(fileId);
		this.title = file.getFileName().substring(file.getFileName().indexOf("$") + 1);
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMyDocService(MyDocService myDocService) {
		this.myDocService = myDocService;
	}

}
