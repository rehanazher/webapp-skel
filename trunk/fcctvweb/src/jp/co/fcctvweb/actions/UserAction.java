package jp.co.fcctvweb.actions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.po.Channel;
import jp.co.fcctvweb.po.User;
import jp.co.fcctvweb.services.GtvService;
import jp.co.fcctvweb.services.UserService;

import org.apache.struts2.ServletActionContext;

public class UserAction extends BasicJsonAction {

	private static final long serialVersionUID = -8054682999648658581L;
	
	public static final String STORAGE_KEY = "jp.co.fcctvweb.User";

	private UserService userService;

	private String username;
	private String password;

	private boolean loginFlag;
	private Calendar serverTime = Calendar.getInstance();
	private Config configurations;

	private List<String> chNames = new ArrayList<String>();

	private GtvService gtvService;

	public String retrieveLogin() {
		String remoteIp = ServletActionContext.getRequest().getRemoteAddr();
		User user = userService.userLogin("", "", remoteIp);
		if (user != null) {
			loginFlag = true;
			HttpSession session = ServletActionContext.getRequest().getSession();
			session.setAttribute(STORAGE_KEY, user);
		}
		

		List<Channel> chList = gtvService.getAllChannels();
		for (Channel ch : chList) {
			chNames.add(ch.getChName());
		}
		return SUCCESS;
	}

	public String login() {
		String remoteIp = ServletActionContext.getRequest().getRemoteAddr();
		User user = userService.userLogin(username, password, remoteIp);
		if (user == null) {
			makeFailure();
			setMsg(getText("app.login.msg.invalid"));
			HttpSession session = ServletActionContext.getRequest().getSession();
			session.setAttribute(STORAGE_KEY, user);
		}
		return ajaxReturn();
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(boolean loginFlag) {
		this.loginFlag = loginFlag;
	}

	public Calendar getServerTime() {
		return serverTime;
	}

	public Config getConfigurations() {
		return configurations;
	}

	public List<String> getChNames() {
		return chNames;
	}

	public void setGtvService(GtvService gtvService) {
		this.gtvService = gtvService;
	}
}
