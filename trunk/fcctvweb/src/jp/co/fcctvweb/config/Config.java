package jp.co.fcctvweb.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.fcctvweb.vo.MenuItem;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class Config {

	public static MenuItem TREE_STORE;
	public static JsonConfig JSON_CONFIG;

	static {
		TREE_STORE = retriveMenuItemStore();
		JSON_CONFIG = defaultJsonConfig();
	}

	private static JsonConfig defaultJsonConfig() {
		JsonConfig cfg = new JsonConfig();
		cfg.registerJsonValueProcessor(java.util.Date.class,
				new JsonValueProcessor() {
					private final String format = "yyyy-MM-dd";

					public Object processObjectValue(String key, Object value,
							JsonConfig arg2) {
						if (value == null)
							return "";
						if (value instanceof Date) {
							String str = new SimpleDateFormat(format)
									.format((Date) value);
							return str;
						}
						return value.toString();
					}

					public Object processArrayValue(Object value,
							JsonConfig arg1) {
						return null;
					}
				});
		return cfg;
	}

	private static MenuItem retriveMenuItemStore() {
		MenuItem root = new MenuItem();
		root.setRoot(true);
		root.setId("root");

		MenuItem node1 = new MenuItem("menu.accounting", "accounting/home",
				"/app/test.action");
		node1.setExpanded(true);
		node1.addChild(new MenuItem("menu.accounting.input",
				"accounting/input", "app/test.action"));
		node1.addChild(new MenuItem("menu.accounting.query",
				"accounting/query", "app/test.action"));
		node1.addChild(new MenuItem("menu.accounting.report.realtime",
				"accounting/realtimereport", "app/test.action"));
		node1.addChild(new MenuItem("menu.accounting.report.history",
				"accounting/historyreport", "app/test.action"));

		root.addChild(node1);

		return root;
	}
}
