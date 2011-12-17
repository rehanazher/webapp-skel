package jp.co.fcctvweb.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class Config {

	public static JsonConfig JSON_CONFIG;
	private static ResourceBundle configuration = ResourceBundle
			.getBundle("config");
	
	public static final int MY_FILE_TYPE_VIDEO = 1;
	public static final int MY_FILE_TYPE_DOC = 2;
	public static final int MY_FILE_TYPE_MUSIC = 3;
	public static final int MY_FILE_TYPE_PHOTO = 4;

	static {
		JSON_CONFIG = defaultJsonConfig();
	}

	public static String getUserCookieTime() {
		return configuration.getString("user.cookie.remain");
	}
	
	public static int getPageSize(){
		return Integer.parseInt(configuration.getString("page.size"));
	}
	
	public static String getHddMp4Dir(){
		return configuration.getString("hdd.mp4.dir");
	}
	
	public static String getHddThumbsDir(){
		return configuration.getString("hdd.thumbs.dir");
	}
	
	public static String getUploadVideoDir(){
		return configuration.getString("upload.video.dir");
	}
	
	public static String getUploadMusicDir(){
		return configuration.getString("upload.music.dir");
	}
	
	public static String getUploadDocDir(){
		return configuration.getString("upload.doc.dir");
	}
	
	public static String getUploadPhotoDir(){
		return configuration.getString("upload.photo.dir");
	}
	
	public static String getHddThumbsDefault(){
		return configuration.getString("hdd.thumbs.default");
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

}
