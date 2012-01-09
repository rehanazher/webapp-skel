package jp.co.fcctvweb.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.fcctvweb.actions.condition.GtvCondition;
import jp.co.fcctvweb.daos.ChannelDao;
import jp.co.fcctvweb.daos.GtvIdDao;
import jp.co.fcctvweb.daos.StatisticDao;
import jp.co.fcctvweb.po.Channel;
import jp.co.fcctvweb.po.GtvId;
import jp.co.fcctvweb.vo.ChannelStatistic;
import jp.co.fcctvweb.vo.GtvVo;

public class GtvServiceImpl implements GtvService {

	private GtvIdDao gtvIdDao;
	private ChannelDao channelDao;
	private StatisticDao statisticDao;

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

	public List<Channel> getAllChannels() {
		return channelDao.findAll();
	}

	@Override
	public boolean addFavorite(String gtvid) {
		return gtvIdDao.updateFavoriteByGtvid(gtvid, 1);
	}

	@Override
	public boolean removeFavorite(String gtvid) {
		return gtvIdDao.updateFavoriteByGtvid(gtvid, 0);
	}
	
	@Override
	public List<ChannelStatistic> getChannelStatistic() {
		List<ChannelStatistic> amountList = statisticDao.findAmountGroupByChannel();
		if (!amountList.isEmpty()){
			List<Channel> chList = getAllChannels();
			Map<Integer, Channel> chMap = new HashMap<Integer, Channel>();
			for (Channel ch: chList){
				chMap.put(ch.getCh(), ch);
			}
			
			for (ChannelStatistic st : amountList){
				st.setChName(chMap.get(st.getId()).getChName());
			}
		}
		
		return amountList;
	}

	public void setGtvIdDao(GtvIdDao gtvIdDao) {
		this.gtvIdDao = gtvIdDao;
	}

	public void setChannelDao(ChannelDao channelDao) {
		this.channelDao = channelDao;
	}

	public void setStatisticDao(StatisticDao statisticDao) {
		this.statisticDao = statisticDao;
	}
}
