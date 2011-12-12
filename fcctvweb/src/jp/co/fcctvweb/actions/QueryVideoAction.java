package jp.co.fcctvweb.actions;

import jp.co.fcctvweb.actions.condition.GtvCondition;
import jp.co.fcctvweb.services.GtvService;

public class QueryVideoAction extends BasicJsonAction {

	private static final long serialVersionUID = -48581266386898766L;

	private GtvService gtvService;
	
	private String date;
	private String type;
	
	private int start;
	private int page;
	private int limit;
	
	public String query(){
		GtvCondition condition = new GtvCondition();
		condition.setDate(date);
		condition.setStart(start);
		condition.setPage(page);
		condition.setLimit(limit);
		setJsonObj(gtvService.getGtvIdByCondition(condition));
		return jsonReturn();
	}
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String date){
		this.date = date;
	}

	private String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}

	public void setGtvService(GtvService gtvService) {
		this.gtvService = gtvService;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}
