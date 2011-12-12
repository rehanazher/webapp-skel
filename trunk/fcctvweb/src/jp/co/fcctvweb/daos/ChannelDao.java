package jp.co.fcctvweb.daos;

import java.util.List;

import jp.co.fcctvweb.po.Channel;

public interface ChannelDao {

	List<Channel> findAll();

}
