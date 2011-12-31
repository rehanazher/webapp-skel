package jp.co.fcctvweb.services;

import java.util.ArrayList;
import java.util.List;

import jp.co.fcctvweb.actions.condition.MyFileCondition;
import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.daos.UploadInfoDao;
import jp.co.fcctvweb.po.UploadInfo;
import jp.co.fcctvweb.vo.MyFileVo;

public class UploadInfoServiceImpl implements UploadInfoService {

	private UploadInfoDao uploadInfoDao;

	public String addUploadInfo(UploadInfo info) {
		return uploadInfoDao.insert(info);
	}

	public boolean removeIfExists(int type, String fileName) {
		return uploadInfoDao.deleteByTypeAndFileName(type, fileName);
	}

	public List<MyFileVo> getUploadFilesByCondition(MyFileCondition condition) {
		List<MyFileVo> resultList = new ArrayList<MyFileVo>();
		List<UploadInfo> infoList = uploadInfoDao.findByCondition(condition);
		for (UploadInfo info : infoList) {
			MyFileVo vo = new MyFileVo();
			vo.setFileInfo(info);
			resultList.add(vo);
		}
		return resultList;
	}

	public boolean addFavorite(String id) {
		return uploadInfoDao.updateFavoriteById(id, 1);
	}

	public boolean removeFavorite(String id) {
		return uploadInfoDao.updateFavoriteById(id, 0);
	}

	@Override
	public MyFileVo getFileById(String id) {
		UploadInfo upInfo = uploadInfoDao.findById(id);
		if (upInfo != null) {
			MyFileVo vo = new MyFileVo();
			vo.setFileInfo(upInfo);
			return vo;
		}
		return null;
	}

	@Override
	public int getMusicAmount() {
		return uploadInfoDao.findAmountByType(Config.MY_FILE_TYPE_MUSIC);
	}

	@Override
	public int getPhotoAmount() {
		return uploadInfoDao.findAmountByType(Config.MY_FILE_TYPE_PHOTO);
	}

	@Override
	public int getVideoAmount() {
		return uploadInfoDao.findAmountByType(Config.MY_FILE_TYPE_VIDEO);
	}

	public void setUploadInfoDao(UploadInfoDao uploadInfoDao) {
		this.uploadInfoDao = uploadInfoDao;
	}

	@Override
	public MyFileVo getPhotoByIndex(int index) {
		UploadInfo upInfo = uploadInfoDao.findByTypeAndIndex(
				Config.MY_FILE_TYPE_PHOTO, index);
		if (upInfo != null) {
			MyFileVo vo = new MyFileVo();
			vo.setFileInfo(upInfo);
			return vo;
		}
		return null;
	}
}
