package jp.co.fcctvweb.actions.condition;

public class MyFileCondition extends Pagination {

	private int type;
	private int favorite;
	private String key;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
