package jp.co.fcctvweb.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import jp.co.fcctvweb.utils.dao.BasicDao;
import jp.co.fcctvweb.utils.dao.MultiRowMapper;
import jp.co.fcctvweb.vo.ChannelStatistic;

public class StatisticDaoImpl extends BasicDao<ChannelStatistic> implements StatisticDao {

	private static final String SQL_COUNT_GROUP_BY_CH = "SELECT ch, count(1) AS amount FROM gtvid_tbl GROUP BY ch";
	
	private static class ChannelStatisticMultiRowMapper implements MultiRowMapper<ChannelStatistic> {
		public ChannelStatistic mapRow(ResultSet rs, int rowNum) throws SQLException {
			ChannelStatistic vo = new ChannelStatistic();
			vo.setId(rs.getInt("ch"));
			vo.setAmount(rs.getInt("amount"));
			return vo;
		}
	}
	
	public List<ChannelStatistic> findAmountGroupByChannel(){
		return query(SQL_COUNT_GROUP_BY_CH, new ChannelStatisticMultiRowMapper());
	}
}
