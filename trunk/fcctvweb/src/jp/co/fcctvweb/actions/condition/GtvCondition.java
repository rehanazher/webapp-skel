package jp.co.fcctvweb.actions.condition;

public class GtvCondition extends Pagination {
	private String date;
	private String type;
	private int favorite;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
}
