package jp.co.fcctvweb.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import jp.co.fcctvweb.po.Channel;
import jp.co.fcctvweb.utils.dao.BasicDao;
import jp.co.fcctvweb.utils.dao.MultiRowMapper;

public class ChannelDaoImpl extends BasicDao<Channel> implements ChannelDao {

	private static final String SQL_FIND_ALL = "SELECT * FROM ch_tbl";

	private static class ChannelMultiRowMapper implements
			MultiRowMapper<Channel> {
		public Channel mapRow(ResultSet rs, int rowNum) throws SQLException {
			Channel channel = new Channel();
			channel.setChId(rs.getInt("ch_id"));
			channel.setCh(rs.getInt("ch"));
			channel.setChName(rs.getString("ch_name"));
			channel.setChNwId(rs.getInt("ch_nw_id"));
			channel.setChServiceId(rs.getInt("ch_service_id"));
			return channel;
		}
	}

	public List<Channel> findAll() {
		return query(SQL_FIND_ALL, new ChannelMultiRowMapper());
	}
}
