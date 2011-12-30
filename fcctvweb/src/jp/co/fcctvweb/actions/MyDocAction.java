package jp.co.fcctvweb.actions;

import jp.co.fcctvweb.services.MyDocService;
import jp.co.fcctvweb.vo.MyDocNode;


public class MyDocAction extends BasicJsonAction {

	private static final long serialVersionUID = 7053026591244306076L;
	
	private MyDocService myDocService;

	public String retrieveDocTree(){
		MyDocNode docTree = myDocService.retrieveDocTree();
		MyDocNode fakeRoot = new MyDocNode();
		fakeRoot.addChild(docTree);
		setJsonObj(fakeRoot);
		
		return jsonReturn();
	}

	public void setMyDocService(MyDocService myDocService) {
		this.myDocService = myDocService;
	}
}
