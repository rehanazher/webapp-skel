package jp.co.fcctvweb.actions;

import java.util.List;

import jp.co.fcctvweb.po.Channel;
import jp.co.fcctvweb.services.GtvService;

public class ChannelAction extends BasicJsonAction {

	private static final long serialVersionUID = 1351957451971483593L;

	private GtvService gtvService;
	
	public String execute(){
		List<Channel> channelList = gtvService.getAllChannels();
		setJsonObj(channelList);
		return jsonReturn();
	}
	
	public String getChannelInfo(){
		getReply().setValue(gtvService.getChannelStatistic());
		return ajaxReturn();
	}

	public void setGtvService(GtvService gtvService) {
		this.gtvService = gtvService;
	}
}
