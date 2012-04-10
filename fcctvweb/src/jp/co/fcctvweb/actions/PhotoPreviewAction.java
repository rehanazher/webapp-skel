package jp.co.fcctvweb.actions;

public class PhotoPreviewAction extends BasicJsonAction {
	
	private static final long serialVersionUID = -7135760664374129773L;

	private String imageLink;
	
	public String execute() {
		return SUCCESS;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
}
