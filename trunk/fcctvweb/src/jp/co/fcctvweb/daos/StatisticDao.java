package jp.co.fcctvweb.daos;

import java.util.List;

import jp.co.fcctvweb.vo.ChannelStatistic;

public interface StatisticDao {

	List<ChannelStatistic> findAmountGroupByChannel();

}
