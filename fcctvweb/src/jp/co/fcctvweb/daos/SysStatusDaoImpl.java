package jp.co.fcctvweb.daos;

import java.sql.ResultSet;
import java.sql.SQLException;

import jp.co.fcctvweb.po.SysStatus;
import jp.co.fcctvweb.utils.dao.BasicDao;
import jp.co.fcctvweb.utils.dao.MultiRowMapper;
import jp.co.fcctvweb.utils.dao.SingleRowMapper;

public class SysStatusDaoImpl extends BasicDao<SysStatus> implements
		SysStatusDao {
	
	private static final String SQL_FIND_ALL = "SELECT * FROM sys_status";

	private static class SysStatusMultiRowMapper implements
			MultiRowMapper<SysStatus> {
		public SysStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
			SysStatus sysStatus = new SysStatus();
			sysStatus.setSkey(rs.getInt("skey"));
			sysStatus.setLogtime(rs.getString("logtime"));
			sysStatus.setAnt0Level(rs.getInt("ant0_level"));
			sysStatus.setAnt1Level(rs.getInt("ant1_level"));
			sysStatus.setAnt2Level(rs.getInt("ant2_level"));
			sysStatus.setAnt3Level(rs.getInt("ant3_level"));
			sysStatus.setAnt4Level(rs.getInt("ant4_level"));
			sysStatus.setAnt5Level(rs.getInt("ant5_level"));
			sysStatus.setAnt6Level(rs.getInt("ant6_level"));
			sysStatus.setpIn0(rs.getDouble("p_in0"));
			sysStatus.setpIn1(rs.getDouble("p_in1"));
			sysStatus.setpIn2(rs.getDouble("p_in2"));
			sysStatus.setpIn3(rs.getDouble("p_in3"));
			sysStatus.setpIn4(rs.getDouble("p_in4"));
			sysStatus.setpIn5(rs.getDouble("p_in5"));
			sysStatus.setpIn6(rs.getDouble("p_in6"));
			sysStatus.setpIn7(rs.getDouble("p_in7"));
			sysStatus.setpIn8(rs.getDouble("p_in8"));
			sysStatus.setFan1(rs.getInt("fan1"));
			sysStatus.setFan2(rs.getInt("fan2"));
			sysStatus.setFan3(rs.getInt("fan3"));
			sysStatus.setTemp1(rs.getDouble("temp1"));
			sysStatus.setTemp2(rs.getDouble("temp2"));
			sysStatus.setTemp3(rs.getDouble("temp3"));
			sysStatus.setLoadavg1(rs.getDouble("loadavg1"));
			sysStatus.setLoadavg5(rs.getDouble("loadavg5"));
			sysStatus.setLoadavg15(rs.getDouble("loadavg15"));
			return sysStatus;
		}
	}

	private static class SysStatusSingleRowMapper implements
			SingleRowMapper<SysStatus> {
		public SysStatus mapRow(ResultSet rs) throws SQLException {
			return new SysStatusMultiRowMapper().mapRow(rs, 1);
		}
	}

	public SysStatus findOne() {
		return query(SQL_FIND_ALL + " LIMIT 0,1", new SysStatusSingleRowMapper());
	}
}
