package jp.co.fcctvweb.actions;

import java.util.ArrayList;
import java.util.List;

import jp.co.fcctvweb.vo.QueryItem;

public class TestAction extends BasicJsonAction {

	private static final long serialVersionUID = 4515695325555926180L;

	private int limit;
	private int page;
	private int start;

	public String execute() {
		System.out.println(limit);
		System.out.println(start);
		System.out.println(page);

		List<QueryItem> itemList = new ArrayList<QueryItem>();

		if (start < 50) {
			for (int i = 0; i < 15; i++) {
				QueryItem item = new QueryItem();
				int n = i + start;
				item.setPosterUrl("posterUrl " + n);
				item.setUrl("url " + n);
				item.setTitle("title " + n);
				item.setDesc("desc " + n);
				itemList.add(item);
			}
		}

		setJsonObj(itemList);

		return jsonReturn();
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
}
