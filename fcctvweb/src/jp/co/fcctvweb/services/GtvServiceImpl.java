package jp.co.fcctvweb.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.fcctvweb.actions.condition.GtvCondition;
import jp.co.fcctvweb.daos.ChannelDao;
import jp.co.fcctvweb.daos.GtvIdDao;
import jp.co.fcctvweb.po.Channel;
import jp.co.fcctvweb.po.GtvId;
import jp.co.fcctvweb.vo.GtvVo;

public class GtvServiceImpl implements GtvService {

	private GtvIdDao gtvIdDao;
	private ChannelDao channelDao;

	@Override
	public List<GtvVo> getGtvIdByCondition(GtvCondition condition) {
		List<GtvVo> resultList = new ArrayList<GtvVo>();

		List<GtvId> gtvIdList = gtvIdDao.findByCondition(condition);
		if (!gtvIdList.isEmpty()) {
			List<Channel> chList = channelDao.findAll();
			Map<Integer, Channel> chMap = new HashMap<Integer, Channel>();
			for (Channel ch : chList) {
				chMap.put(ch.getCh(), ch);
			}

			for (GtvId po : gtvIdList) {
				GtvVo vo = new GtvVo();
				vo.setGtvIdPo(po);
				vo.setChannelPo(chMap.get(po.getCh()));
				resultList.add(vo);
			}
		}
		return resultList;
	}

	@Override
	public boolean addFavorite(String gtvid) {
		return gtvIdDao.updateFavoriteByGtvid(gtvid, 1);
	}

	@Override
	public boolean removeFavorite(String gtvid) {
		return gtvIdDao.updateFavoriteByGtvid(gtvid, 0);
	}

	public void setGtvIdDao(GtvIdDao gtvIdDao) {
		this.gtvIdDao = gtvIdDao;
	}

	public void setChannelDao(ChannelDao channelDao) {
		this.channelDao = channelDao;
	}
}
