package jp.co.fcctvweb.actions;

import jp.co.fcctvweb.services.GtvService;

public class FavoriteAction extends BasicJsonAction {

	private static final long serialVersionUID = 5427973813451695790L;

	private GtvService gtvService;

	private String gtvid;

	public String addFavorite() {
		gtvService.addFavorite(gtvid);
		return ajaxReturn();
	}

	public String removeFavorite() {
		gtvService.removeFavorite(gtvid);
		return ajaxReturn();
	}

	public String getGtvid() {
		return gtvid;
	}

	public void setGtvid(String gtvid) {
		this.gtvid = gtvid;
	}

	public void setGtvService(GtvService gtvService) {
		this.gtvService = gtvService;
	}
}
