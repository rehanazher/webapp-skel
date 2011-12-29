package jp.co.fcctvweb.actions;

import jp.co.fcctvweb.services.MyDocService;


public class MyDocAction extends BasicJsonAction {

	private static final long serialVersionUID = 7053026591244306076L;
	
	private MyDocService myDocService;

	public String retrieveDocTree(){
		setJsonObj(myDocService.retrieveDocTree());
		return jsonReturn();
	}

	public void setMyDocService(MyDocService myDocService) {
		this.myDocService = myDocService;
	}
}
