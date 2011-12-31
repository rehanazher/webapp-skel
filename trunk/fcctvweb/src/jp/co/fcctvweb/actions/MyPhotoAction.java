package jp.co.fcctvweb.actions;

import jp.co.fcctvweb.services.UploadInfoService;

public class MyPhotoAction extends BasicJsonAction {

	private static final long serialVersionUID = 5338140541780710271L;

	private UploadInfoService uploadInfoService;
	
	public String photoAmount(){
		getReply().setValue(uploadInfoService.getPhotoAmount());
		return ajaxReturn();
	}

	public void setUploadInfoService(UploadInfoService uploadInfoService) {
		this.uploadInfoService = uploadInfoService;
	}
}
