package jp.co.fcctvweb.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import jp.co.fcctvweb.po.UploadInfo;
import jp.co.fcctvweb.utils.dao.BasicDao;
import jp.co.fcctvweb.utils.dao.MultiRowMapper;
import jp.co.fcctvweb.utils.dao.SingleRowMapper;

public class UploadInfoDaoImpl extends BasicDao<UploadInfo> implements
		UploadInfoDao {
	private static final String SQL_FIND_ALL = "SELECT * FROM upload_info";

	private static final String SQL_INSERT_UPLOADINFO = "INSERT INTO upload_info(id,name,file_name,"
			+ "type,creation_time,favorite,size,ext_name) "
			+ "VALUES(?,?,?,?,?,?,?,?)";

	private static final String SQL_DELETE_UPLOADINFO = "DELETE FROM upload_info WHERE id=?";

	private static final String SQL_FIND_UPLOADINFO_BY_ID = "SELECT * FROM upload_info WHERE id=?";

	private static class UploadInfoMultiRowMapper implements
			MultiRowMapper<UploadInfo> {
		public UploadInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UploadInfo uploadInfo = new UploadInfo();
			uploadInfo.setId(rs.getString("id"));
			uploadInfo.setName(rs.getString("name"));
			uploadInfo.setFileName(rs.getString("file_name"));
			uploadInfo.setType(rs.getInt("type"));
			uploadInfo.setCreationTime(rs.getTimestamp("creation_time"));
			uploadInfo.setFavorite(rs.getInt("favorite"));
			uploadInfo.setSize(rs.getInt("size"));
			uploadInfo.setExtName(rs.getString("ext_name"));
			return uploadInfo;
		}
	}

	private static class UploadInfoSingleRowMapper implements
			SingleRowMapper<UploadInfo> {
		public UploadInfo mapRow(ResultSet rs) throws SQLException {
			return new UploadInfoMultiRowMapper().mapRow(rs, 1);
		}
	}

	public List<UploadInfo> findAll() {
		return query(SQL_FIND_ALL, new UploadInfoMultiRowMapper());
	}

	public String insert(UploadInfo uploadInfo) {
		uploadInfo.setId(createId());
		if (update(
				SQL_INSERT_UPLOADINFO,
				new Object[] { uploadInfo.getId(), uploadInfo.getName(),
						uploadInfo.getFileName(),
						new Integer(uploadInfo.getType()),
						uploadInfo.getCreationTime(),
						new Integer(uploadInfo.getFavorite()),
						new Integer(uploadInfo.getSize()),
						uploadInfo.getExtName() }, new int[] { Types.CHAR,
						Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
						Types.TIMESTAMP, Types.INTEGER, Types.INTEGER,
						Types.VARCHAR }) > 0) {
			return uploadInfo.getId();
		} else {
			return null;
		}
	}

	public String delete(String uploadInfoId) {
		if (update(SQL_DELETE_UPLOADINFO, uploadInfoId) > 0) {
			return uploadInfoId;
		} else {
			return null;
		}
	}

	public UploadInfo findById(String uploadInfoId) {
		return (UploadInfo) query(SQL_FIND_UPLOADINFO_BY_ID, uploadInfoId,
				new UploadInfoSingleRowMapper());
	}

}
