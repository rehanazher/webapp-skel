package jp.co.fcctvweb.actions;

import jp.co.fcctvweb.actions.condition.MyFileCondition;
import jp.co.fcctvweb.services.UploadInfoService;

public class QueryMyFileAction extends BasicJsonAction {

	private static final long serialVersionUID = -1028370357390772078L;
	private UploadInfoService uploadInfoService;

	private int favorite = -1;
	private String key;
	private int type;
	private static final int TYPE_VIDEO = 1;
	private static final int TYPE_DOC = 2;
	private static final int TYPE_MUSIC = 3;
	private static final int TYPE_PHOTO = 4;
	

	// pagination
	private int start;
	private int page;
	private int limit;

	public String query() {
		MyFileCondition condition = new MyFileCondition();
		condition.setStart(start);
		condition.setPage(page);
		condition.setLimit(limit);
		condition.setFavorite(favorite);
		condition.setType(type);
		condition.setKey(key);
		setJsonObj(uploadInfoService.getUploadFilesByCondition(condition));
		return jsonReturn();
	}

	public void setUploadInfoService(UploadInfoService uploadInfoService) {
		this.uploadInfoService = uploadInfoService;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
