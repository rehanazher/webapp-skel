package jp.co.fcctvweb.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import jp.co.fcctvweb.po.FakeFolder;
import jp.co.fcctvweb.utils.dao.BasicDao;
import jp.co.fcctvweb.utils.dao.MultiRowMapper;
import jp.co.fcctvweb.utils.dao.SingleRowMapper;

public class FolderDaoImpl extends BasicDao<FakeFolder> implements FolderDao {
	private static final String SQL_FIND_ALL = "SELECT * FROM folder_tbl";

	private static final String SQL_FIND_BY_PARENT_ID = "SELECT * FROM folder_tbl WHERE parent_id=?";

	private static final String SQL_INSERT_FAKEFOLDER = "INSERT INTO folder_tbl(id,folder_name,position,"
			+ "parent_id) " + "VALUES(?,?,?,?)";

	private static final String SQL_DELETE_FAKEFOLDER = "DELETE FROM folder_tbl WHERE id=?";

	private static final String SQL_UPDATE_FAKEFOLDER = "UPDATE folder_tbl SET folder_name=?,position=?,"
			+ "parent_id=? WHERE id=?";

	private static final String SQL_FIND_FAKEFOLDER_BY_ID = "SELECT * FROM folder_tbl WHERE id=?";

	private static class FakeFolderMultiRowMapper implements
			MultiRowMapper<FakeFolder> {
		public FakeFolder mapRow(ResultSet rs, int rowNum) throws SQLException {
			FakeFolder fakeFolder = new FakeFolder();
			fakeFolder.setId(rs.getInt("id"));
			fakeFolder.setFolderName(rs.getString("folder_name"));
			fakeFolder.setPosition(rs.getString("position"));
			fakeFolder.setParentId(rs.getInt("parent_id"));
			return fakeFolder;
		}
	}

	private static class FakeFolderSingleRowMapper implements
			SingleRowMapper<FakeFolder> {
		public FakeFolder mapRow(ResultSet rs) throws SQLException {
			return new FakeFolderMultiRowMapper().mapRow(rs, 1);
		}
	}

	public List<FakeFolder> findAll() {
		return query(SQL_FIND_ALL, new FakeFolderMultiRowMapper());
	}

	public boolean insert(FakeFolder fakeFolder) {
		return update(
				SQL_INSERT_FAKEFOLDER,
				new Object[] { new Integer(fakeFolder.getId()),
						fakeFolder.getFolderName(), fakeFolder.getPosition(),
						new Integer(fakeFolder.getParentId()) }, new int[] {
						Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
						Types.INTEGER }) > 0;
	}

	public boolean delete(int fakeFolderId) {
		return update(SQL_DELETE_FAKEFOLDER, new Object[] { fakeFolderId }) > 0;
	}

	public boolean update(FakeFolder fakeFolder) {
		return update(
				SQL_UPDATE_FAKEFOLDER,
				new Object[] { fakeFolder.getFolderName(),
						fakeFolder.getPosition(),
						new Integer(fakeFolder.getParentId()),
						fakeFolder.getId() }, new int[] { Types.VARCHAR,
						Types.VARCHAR, Types.INTEGER, Types.INTEGER }) > 0;
	}

	public FakeFolder findById(int fakeFolderId) {
		return (FakeFolder) query(SQL_FIND_FAKEFOLDER_BY_ID,
				new Object[] { fakeFolderId }, new FakeFolderSingleRowMapper());
	}

	@Override
	public List<FakeFolder> findByParentId(int parentId) {
		return query(SQL_FIND_BY_PARENT_ID, new Object[] { parentId },
				new FakeFolderMultiRowMapper());
	}
}
