package jp.co.fcctvweb.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import jp.co.fcctvweb.actions.condition.GtvCondition;
import jp.co.fcctvweb.po.GtvId;
import jp.co.fcctvweb.utils.dao.BasicDao;
import jp.co.fcctvweb.utils.dao.MultiRowMapper;
import jp.co.fcctvweb.utils.dao.SingleRowMapper;

public class GtvIdDaoImpl extends BasicDao<GtvId> implements GtvIdDao {

	private static final String SQL_FIND_ALL = "SELECT * FROM gtvid_tbl";

	private static class GtvIdMultiRowMapper implements MultiRowMapper<GtvId> {
		public GtvId mapRow(ResultSet rs, int rowNum) throws SQLException {
			GtvId gtvId = new GtvId();
			gtvId.setEpgid(rs.getInt("epgid"));
			gtvId.setGtvid(rs.getString("gtvid"));
			gtvId.setCid(rs.getString("cid"));
			gtvId.setTsid(rs.getString("tsid"));
			gtvId.setServiceId(rs.getInt("service_id"));
			gtvId.setOrgNwId(rs.getInt("org_nw_id"));
			gtvId.setCh(rs.getInt("ch"));
			gtvId.setBstartTime(rs.getTimestamp("bstart_time"));
			gtvId.setStime(rs.getInt("stime"));
			gtvId.setEtime(rs.getInt("etime"));
			gtvId.setBdate(rs.getString("bdate"));
			gtvId.setBtime(rs.getString("btime"));
			gtvId.setDuration(rs.getTimestamp("duration"));
			gtvId.setContentname(rs.getString("contentname"));
			gtvId.setContentdesc(rs.getString("contentdesc"));
			gtvId.setGenre(rs.getString("genre"));
			gtvId.setFavorite(rs.getInt("favorite"));
			return gtvId;
		}
	}

	private static class GtvIdSingleRowMapper implements SingleRowMapper<GtvId> {
		public GtvId mapRow(ResultSet rs) throws SQLException {
			return new GtvIdMultiRowMapper().mapRow(rs, 1);
		}
	}

	public List<GtvId> findAll() {
		return query(SQL_FIND_ALL, new GtvIdMultiRowMapper());
	}

	public List<GtvId> findByCondition(GtvCondition condition) {

		return null;
	}
}
