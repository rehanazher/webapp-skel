package jp.co.fcctvweb;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport {

	private static final long serialVersionUID = -8136972939199368948L;

	public String execute() {
		System.out.println("test action");
		return SUCCESS;
	}
}
