package jp.co.fcctvweb.daos;

import java.util.List;

import jp.co.fcctvweb.actions.condition.GtvCondition;
import jp.co.fcctvweb.po.GtvId;

public interface GtvIdDao {

	List<GtvId> findAll();

	List<GtvId> findByCondition(GtvCondition condition);
	
	boolean updateFavoriteByGtvid(String gtvid, int favoriteFlag);
	
}
