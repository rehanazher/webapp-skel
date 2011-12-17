package jp.co.fcctvweb.vo;

import java.util.Date;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.po.UploadInfo;

public class MyFileVo {

	private String id;
	private int type;
	private int favorite;
	private String name;
	private String fileName;
	private long length;
	private String extName;
	private Date creationTime;
	private String posterUrl;
	private String videoUrl;
	
	public void setFileInfo(UploadInfo info){
		this.setId(info.getId());
		this.setType(info.getType());
		this.setFavorite(info.getFavorite());
		this.setName(info.getName());
		this.setFileName(info.getFileName());
		this.setLength(info.getSize());
		this.setExtName(info.getExtName());
		this.setCreationTime(info.getCreationTime());
		switch(this.type){
		case Config.MY_FILE_TYPE_VIDEO:
			this.setPosterUrl("");
			this.setVideoUrl("");
			break;
		case Config.MY_FILE_TYPE_DOC:
			break;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

}
