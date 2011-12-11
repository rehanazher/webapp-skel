package jp.co.fcctvweb.po;

import java.util.Date;

public class GtvId {
	private int epgid;
	private String gtvid;
	private String cid;
	private String tsid;
	private int serviceId;
	private int orgNwId;
	private int ch;
	private Date bstartTime;
	private int stime;
	private int etime;
	private String bdate;
	private String btime;
	private Date duration;
	private String contentname;
	private String contentdesc;
	private String genre;
	private int favorite;

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

	public int getCh() {
		return ch;
	}

	public void setCh(int ch) {
		this.ch = ch;
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

	public Date getDuration() {
		return duration;
	}

	public void setDuration(Date duration) {
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
}
