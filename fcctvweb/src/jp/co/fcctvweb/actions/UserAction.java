package jp.co.fcctvweb.actions;

import jp.co.fcctvweb.po.User;
import jp.co.fcctvweb.services.UserService;

import org.apache.struts2.ServletActionContext;

public class UserAction extends BasicJsonAction {

	private static final long serialVersionUID = -8054682999648658581L;

	private UserService userService;

	private String username;
	private String password;

	private boolean loginFlag;

	public String retrieveLogin() {
		String remoteIp = ServletActionContext.getRequest().getRemoteAddr();
		User user = userService.userLogin("", "", remoteIp);
		if (user != null) {
			loginFlag = true;
		}
		return SUCCESS;
	}

	public String login() {
		String remoteIp = ServletActionContext.getRequest().getRemoteAddr();
		User user = userService.userLogin(username, password, remoteIp);
		if (user == null) {
			makeFailure();
			setMsg(getText("app.login.msg.invalid"));
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
}