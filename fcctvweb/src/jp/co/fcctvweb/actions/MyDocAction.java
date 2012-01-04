package jp.co.fcctvweb.actions;

import jp.co.fcctvweb.services.MyDocService;
import jp.co.fcctvweb.vo.MyDocNode;


public class MyDocAction extends BasicJsonAction {

	private static final long serialVersionUID = 7053026591244306076L;
	
	private int fileId;
	private int toDirId;
	
	private MyDocService myDocService;

	public String retrieveDocTree(){
		MyDocNode docTree = myDocService.retrieveDocTree();
		MyDocNode fakeRoot = new MyDocNode();
		fakeRoot.addChild(docTree);
		setJsonObj(fakeRoot);
		
		return jsonReturn();
	}
	
	public String retrieveDocFlatDir(){
		setJsonObj(myDocService.retrieveDocFlatDir());
		return jsonReturn();
	}
	
	public String changeDocDir(){
		if (myDocService.moveFileToFolder(fileId, toDirId)){
			
		}
		return ajaxReturn();
	}

	public void setMyDocService(MyDocService myDocService) {
		this.myDocService = myDocService;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getToDirId() {
		return toDirId;
	}

	public void setToDirId(int toDirId) {
		this.toDirId = toDirId;
	}
}
