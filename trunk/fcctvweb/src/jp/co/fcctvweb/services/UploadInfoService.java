package jp.co.fcctvweb.services;

import java.util.List;

import jp.co.fcctvweb.actions.condition.MyFileCondition;
import jp.co.fcctvweb.po.UploadInfo;
import jp.co.fcctvweb.vo.MyFileVo;

public interface UploadInfoService {

	String addUploadInfo(UploadInfo info);

	boolean removeIfExists(int type, String fileName);

	List<MyFileVo> getUploadFilesByCondition(MyFileCondition condition);

}
