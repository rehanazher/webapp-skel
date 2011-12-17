package jp.co.fcctvweb.daos;

import java.util.List;

import jp.co.fcctvweb.actions.condition.MyFileCondition;
import jp.co.fcctvweb.po.UploadInfo;

public interface UploadInfoDao {

	List<UploadInfo> findAll();

	String insert(UploadInfo uploadInfo);

	String delete(String uploadInfoId);

	UploadInfo findById(String uploadInfoId);
	
	boolean deleteByTypeAndFileName(int type, String fileName);

	List<UploadInfo> findByCondition(MyFileCondition condition);

}
