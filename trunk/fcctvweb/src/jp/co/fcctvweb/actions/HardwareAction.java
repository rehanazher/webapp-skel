package jp.co.fcctvweb.actions;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.services.SysStatausServiceImpl;

public class HardwareAction extends BasicJsonAction {

	private static final long serialVersionUID = 4971651946041852701L;

	private SysStatausServiceImpl sysStatausServiceImpl;

	private int expiredDate;
	private String key;

	public String getHddInfo() {
		long g = 1000000000l;
		DecimalFormat df = new DecimalFormat("0.00");
		File f = new File(Config.getUploadDocDir());

		double fsd = (double) f.getFreeSpace() / g;
		double tsd = (double) f.getTotalSpace() / g;
		double percent = (tsd - fsd) / tsd * 100;
		String freeSpace = df.format(fsd) + " G";
		String fullSpace = df.format(tsd) + " G";
		String usage = df.format(percent) + " %";

		HddInfo hddInfo = new HddInfo();
		hddInfo.setFreeSpace(freeSpace);
		hddInfo.setFullSpace(fullSpace);
		hddInfo.setUsage(usage);

		getReply().setValue(hddInfo);

		return ajaxReturn();
	}

	public String getTunners() {
		getReply().setValue(sysStatausServiceImpl.getSysStatus());
		return ajaxReturn();
	}

	public String getTvTerminalId() {
		getReply().setValue("tv id");
		return ajaxReturn();
	}

	public String getSoftwareId() {
		getReply().setValue("software id");
		return ajaxReturn();
	}

	public String getMovieExpire() {
		getReply().setValue("7");
		return ajaxReturn();
	}

	public String changeMovieExpire() {
		System.out.println("expiredDate: " + expiredDate);
		return ajaxReturn();
	}

	public String hddList() {
		List<HddInfo> hddList = new ArrayList<HddInfo>();
		HddInfo hi = new HddInfo();
		hi.setName("test_hdd");
		hi.setKey("1");
		hddList.add(hi);
		setJsonObj(hddList);
		return jsonReturn();
	}

	public String hddFormat() {
		System.out.println("key: " + key);
		System.out.println("hddFormat");
		return ajaxReturn();
	}

	public String deviceReset() {
		System.out.println("deviceReset");
		return ajaxReturn();
	}

	public String deviceShutdown() {
		System.out.println("deviceShutdown");
		return ajaxReturn();
	}

	public void setSysStatausServiceImpl(
			SysStatausServiceImpl sysStatausServiceImpl) {
		this.sysStatausServiceImpl = sysStatausServiceImpl;
	}

	public int getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(int expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public static class HddInfo {
		private String key;
		private String name;
		private String freeSpace;
		private String fullSpace;
		private String usage;

		public String getFreeSpace() {
			return freeSpace;
		}

		public void setFreeSpace(String freeSpace) {
			this.freeSpace = freeSpace;
		}

		public String getFullSpace() {
			return fullSpace;
		}

		public void setFullSpace(String fullSpace) {
			this.fullSpace = fullSpace;
		}

		public String getUsage() {
			return usage;
		}

		public void setUsage(String usage) {
			this.usage = usage;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
