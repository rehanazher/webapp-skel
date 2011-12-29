package jp.co.fcctvweb.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import jp.co.fcctvweb.po.FakeFile;
import jp.co.fcctvweb.utils.dao.BasicDao;
import jp.co.fcctvweb.utils.dao.MultiRowMapper;
import jp.co.fcctvweb.utils.dao.SingleRowMapper;

public class FileDaoImpl extends BasicDao<FakeFile> implements FileDao {
	private static final String SQL_FIND_ALL = "SELECT * FROM file_tbl";

	private static final String SQL_INSERT_FAKEFILE = "INSERT INTO file_tbl(id,file_name,upload_id,"
			+ "folder_id) " + "VALUES(?,?,?,?)";

	private static final String SQL_DELETE_FAKEFILE = "DELETE FROM file_tbl WHERE id=?";

	private static final String SQL_UPDATE_FAKEFILE = "UPDATE file_tbl SET file_name=?,upload_id=?,"
			+ "folder_id=? WHERE id=?";

	private static final String SQL_FIND_FAKEFILE_BY_ID = "SELECT * FROM file_tbl WHERE id=?";

	private static class FakeFileMultiRowMapper implements
			MultiRowMapper<FakeFile> {
		public FakeFile mapRow(ResultSet rs, int rowNum) throws SQLException {
			FakeFile fakeFile = new FakeFile();
			fakeFile.setId(rs.getInt("id"));
			fakeFile.setFileName(rs.getString("file_name"));
			fakeFile.setUploadId(rs.getString("upload_id"));
			fakeFile.setFolderId(rs.getInt("folder_id"));
			return fakeFile;
		}
	}

	private static class FakeFileSingleRowMapper implements
			SingleRowMapper<FakeFile> {
		public FakeFile mapRow(ResultSet rs) throws SQLException {
			return new FakeFileMultiRowMapper().mapRow(rs, 1);
		}
	}

	public List<FakeFile> findAll() {
		return query(SQL_FIND_ALL, new FakeFileMultiRowMapper());
	}

	public boolean insert(FakeFile fakeFile) {
		return update(
				SQL_INSERT_FAKEFILE,
				new Object[] { new Integer(fakeFile.getId()),
						fakeFile.getFileName(), fakeFile.getUploadId(),
						new Integer(fakeFile.getFolderId()) },
				new int[] { Types.INTEGER, Types.VARCHAR, Types.CHAR,
						Types.INTEGER }) > 0;
	}

	public boolean delete(int fakeFileId) {
		return update(SQL_DELETE_FAKEFILE, new Object[] { fakeFileId }) > 0;
	}

	public boolean update(FakeFile fakeFile) {
		return update(
				SQL_UPDATE_FAKEFILE,
				new Object[] { fakeFile.getFileName(), fakeFile.getUploadId(),
						new Integer(fakeFile.getFolderId()), fakeFile.getId() },
				new int[] { Types.VARCHAR, Types.CHAR, Types.INTEGER,
						Types.INTEGER }) > 0;
	}

	public FakeFile findById(int fakeFileId) {
		return query(SQL_FIND_FAKEFILE_BY_ID, new Object[] { fakeFileId },
				new FakeFileSingleRowMapper());
	}
}
