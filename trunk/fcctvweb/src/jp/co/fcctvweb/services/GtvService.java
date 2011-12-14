package jp.co.fcctvweb.services;

import java.util.List;

import jp.co.fcctvweb.actions.condition.GtvCondition;
import jp.co.fcctvweb.vo.GtvVo;

public interface GtvService {

	List<GtvVo> getGtvIdByCondition(GtvCondition condition);

	boolean addFavorite(String gtvid);
	
	boolean removeFavorite(String gtvid);
}
