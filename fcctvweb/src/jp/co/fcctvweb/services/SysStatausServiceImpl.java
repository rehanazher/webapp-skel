package jp.co.fcctvweb.services;

import jp.co.fcctvweb.daos.SysStatusDao;
import jp.co.fcctvweb.po.SysStatus;

public class SysStatausServiceImpl implements SysStatusService {

	private SysStatusDao sysStatusDao;
	
	public SysStatus getSysStatus(){
		return sysStatusDao.findOne();
	}

	public void setSysStatusDao(SysStatusDao sysStatusDao) {
		this.sysStatusDao = sysStatusDao;
	}
}
