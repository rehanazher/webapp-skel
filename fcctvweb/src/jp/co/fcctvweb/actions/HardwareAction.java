package jp.co.fcctvweb.actions;

import java.io.File;
import java.text.DecimalFormat;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.services.SysStatausServiceImpl;

public class HardwareAction extends BasicJsonAction {

	private static final long serialVersionUID = 4971651946041852701L;
	
	private SysStatausServiceImpl sysStatausServiceImpl;

	public String getHddInfo(){
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
	
	public String getTunners(){
		getReply().setValue(sysStatausServiceImpl.getSysStatus());
		return ajaxReturn();
	}
	
	public String getTvTerminalId(){
		getReply().setValue("tv id");
		return ajaxReturn();
	}
	
	public String getSoftwareId(){
		getReply().setValue("software id");
		return ajaxReturn();
	}

	public void setSysStatausServiceImpl(SysStatausServiceImpl sysStatausServiceImpl) {
		this.sysStatausServiceImpl = sysStatausServiceImpl;
	}

	public static class HddInfo {
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
	}
}
