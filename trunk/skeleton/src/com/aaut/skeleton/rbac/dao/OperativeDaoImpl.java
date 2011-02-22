/**
 * Added by James
 * on 2011-2-22
 */
package com.aaut.skeleton.rbac.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.aaut.skeleton.commons.util.dao.BasicDaoSupport;
import com.aaut.skeleton.commons.util.dao.MultiRowMapper;
import com.aaut.skeleton.commons.util.dao.SingleRowMapper;
import com.aaut.skeleton.rbac.po.Operative;

public class OperativeDaoImpl extends BasicDaoSupport<Operative> implements
		OperativeDao<Operative> {

	private static class OperativeMultiRowMapper implements
			MultiRowMapper<Operative> {
		public Operative mapRow(ResultSet rs, int rowNum) throws SQLException {
			Operative operative = new Operative();
			operative.setId(rs.getString("id"));
			operative.setName(rs.getString("name"));
			operative.setWeight(rs.getInt("weight"));
			operative.setDescription(rs.getString("description"));
			operative.setParentId(rs.getString("parent_id"));
			return operative;
		}
	}

	private static class OperativeSingleRowMapper implements
			SingleRowMapper<Operative> {
		public Operative mapRow(ResultSet rs) throws SQLException {
			return new OperativeMultiRowMapper().mapRow(rs, 1);
		}
	}

	private static final String SQL_INSERT_OPERATIVE = "INSERT INTO rbac_operatives(id,name,weight,"
			+ "description,parent_id) " + "VALUES(?,?,?,?,?)";

	private static final String SQL_DELETE_OPERATIVE = "DELETE FROM rbac_operatives WHERE id=?";

	private static final String SQL_UPDATE_OPERATIVE = "UPDATE rbac_operatives SET name=?,weight=?,"
			+ "description=?,parent_id=? WHERE id=?";

	private static final String SQL_FIND_OPERATIVE_BY_ID = "SELECT * FROM rbac_operatives WHERE id=?";

	public String insert(Operative operative) {
		operative.setId(createId());
		if (update(SQL_INSERT_OPERATIVE, new Object[] { operative.getId(),
				operative.getName(), operative.getWeight(),
				operative.getDescription(), operative.getParentId() },
				new int[] { Types.CHAR, Types.VARCHAR, Types.INTEGER,
						Types.VARCHAR, Types.CHAR }) > 0) {
			return operative.getId();
		} else {
			return null;
		}
	}

	public String delete(String operativeId) {
		if (update(SQL_DELETE_OPERATIVE, operativeId) > 0) {
			return operativeId;
		} else {
			return null;
		}
	}

	public int update(Operative operative) {
		return update(SQL_UPDATE_OPERATIVE, new Object[] { operative.getName(),
				operative.getWeight(), operative.getDescription(),
				operative.getParentId(), operative.getId() }, new int[] {
				Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.CHAR,
				Types.CHAR });
	}

	public Operative findById(String operativeId) {
		return (Operative) query(SQL_FIND_OPERATIVE_BY_ID, operativeId,
				new OperativeSingleRowMapper());
	}

}
