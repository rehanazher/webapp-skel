package jp.co.fcctvweb.vo;

import java.util.Date;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.po.Channel;
import jp.co.fcctvweb.po.GtvId;
import jp.co.fcctvweb.utils.DateUtils;

public class GtvVo {
	// gtvid po info
	private int epgid;
	private String gtvid;
	private String cid;
	private String tsid;
	private int serviceId;
	private int orgNwId;
	// duplicate with channel info
	// private int ch;
	private Date bstartTime;
	private int stime;
	private int etime;
	private String bdate;
	private String btime;
	private String duration;
	private String contentname;
	private String contentdesc;
	private String genre;
	private int favorite;

	// channel po info
	private int chId;
	private int ch;
	private String chName;
	private int chNwId;
	private int chServiceId;
	
	// some combine info
	private String posterUrl;
	private String videoUrl;
	private String playTime;
	
	public void setGtvIdPo(GtvId po){
		this.setEpgid(po.getEpgid());
		this.setGtvid(po.getGtvid());
		this.setCid(po.getCid());
		this.setTsid(po.getTsid());
		this.setServiceId(po.getServiceId());
		this.setOrgNwId(po.getOrgNwId());
		this.setCh(po.getCh());
		this.setBstartTime(po.getBstartTime());
		this.setStime(po.getStime());
		this.setEtime(po.getEtime());
		this.setBdate(po.getBdate());
		this.setBtime(po.getBtime());
		this.setDuration(po.getDuration());
		this.setContentname(po.getContentname());
		this.setContentdesc(po.getContentdesc());
		this.setGenre(po.getGenre());
		this.setFavorite(po.getFavorite());
		this.setPosterUrl(Config.getHddThumbsDir() + po.getGtvid());
		this.setVideoUrl(Config.getHddMp4Dir() + po.getGtvid());
		this.setPlayTime(DateUtils.date2String(po.getBstartTime(), "hh:MM:ss"));
	}
	
	public void setChannelPo(Channel po){
		this.setChId(po.getChId());
		this.setCh(po.getCh());
		this.setChName(po.getChName());
		this.setChNwId(po.getChNwId());
		this.setChServiceId(po.getChServiceId());
	}

	public int getEpgid() {
		return epgid;
	}

	public void setEpgid(int epgid) {
		this.epgid = epgid;
	}

	public String getGtvid() {
		return gtvid;
	}

	public void setGtvid(String gtvid) {
		this.gtvid = gtvid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getTsid() {
		return tsid;
	}

	public void setTsid(String tsid) {
		this.tsid = tsid;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getOrgNwId() {
		return orgNwId;
	}

	public void setOrgNwId(int orgNwId) {
		this.orgNwId = orgNwId;
	}

	public Date getBstartTime() {
		return bstartTime;
	}

	public void setBstartTime(Date bstartTime) {
		this.bstartTime = bstartTime;
	}

	public int getStime() {
		return stime;
	}

	public void setStime(int stime) {
		this.stime = stime;
	}

	public int getEtime() {
		return etime;
	}

	public void setEtime(int etime) {
		this.etime = etime;
	}

	public String getBdate() {
		return bdate;
	}

	public void setBdate(String bdate) {
		this.bdate = bdate;
	}

	public String getBtime() {
		return btime;
	}

	public void setBtime(String btime) {
		this.btime = btime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getContentname() {
		return contentname;
	}

	public void setContentname(String contentname) {
		this.contentname = contentname;
	}

	public String getContentdesc() {
		return contentdesc;
	}

	public void setContentdesc(String contentdesc) {
		this.contentdesc = contentdesc;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	public int getChId() {
		return chId;
	}

	public void setChId(int chId) {
		this.chId = chId;
	}

	public int getCh() {
		return ch;
	}

	public void setCh(int ch) {
		this.ch = ch;
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
	}

	public int getChNwId() {
		return chNwId;
	}

	public void setChNwId(int chNwId) {
		this.chNwId = chNwId;
	}

	public int getChServiceId() {
		return chServiceId;
	}

	public void setChServiceId(int chServiceId) {
		this.chServiceId = chServiceId;
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

	public String getPlayTime() {
		return playTime;
	}

	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}
}
