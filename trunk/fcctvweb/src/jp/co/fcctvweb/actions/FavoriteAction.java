package jp.co.fcctvweb.actions;

import jp.co.fcctvweb.services.GtvService;
import jp.co.fcctvweb.services.UploadInfoService;

public class FavoriteAction extends BasicJsonAction {

	private static final long serialVersionUID = 5427973813451695790L;

	private GtvService gtvService;
	private UploadInfoService uploadInfoService;

	private String id;
	private String gtvid;

	public String addFavorite() {
		gtvService.addFavorite(gtvid);
		return ajaxReturn();
	}

	public String removeFavorite() {
		gtvService.removeFavorite(gtvid);
		return ajaxReturn();
	}
	
	public String addMyFileFavorite(){
		uploadInfoService.addFavorite(id);
		return ajaxReturn();
	}
	
	public String removeMyFileFavorite(){
		uploadInfoService.removeFavorite(id);
		return ajaxReturn();
	}

	public String getGtvid() {
		return gtvid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setGtvid(String gtvid) {
		this.gtvid = gtvid;
	}

	public void setGtvService(GtvService gtvService) {
		this.gtvService = gtvService;
	}

	public void setUploadInfoService(UploadInfoService uploadInfoService) {
		this.uploadInfoService = uploadInfoService;
	}

}
