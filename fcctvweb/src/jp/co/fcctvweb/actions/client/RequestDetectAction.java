package jp.co.fcctvweb.actions.client;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import jp.co.fcctvweb.actions.BasicJsonAction;
import jp.co.fcctvweb.actions.UserAction;
import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.utils.ajax.Reply;

public class RequestDetectAction extends BasicJsonAction {

	private static final long serialVersionUID = -4392581565711080868L;

	public String execute(){
		HttpSession session = ServletActionContext.getRequest().getSession();
		
		if (session.getAttribute(UserAction.STORAGE_KEY) == null){
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setIntHeader("Expires", -1);
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("application/json; charset=utf-8");
			// OutputStream os = response.getOutputStream();
			PrintWriter outer;
			try {
				outer = response.getWriter();
				Reply reply = new Reply();
				reply.setMsg("login");
				outer.print(JSONObject.fromObject(reply, Config.JSON_CONFIG)
						.toString());
				outer.flush();
				outer.close();
				response.setStatus(HttpServletResponse.SC_OK);
				response.flushBuffer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setIntHeader("Expires", -1);
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("application/json; charset=utf-8");
			// OutputStream os = response.getOutputStream();
			PrintWriter outer;
			try {
				outer = response.getWriter();
				Reply reply = new Reply();
				reply.setMsg("success");
				outer.print(JSONObject.fromObject(reply, Config.JSON_CONFIG)
						.toString());
				outer.flush();
				outer.close();
				response.setStatus(HttpServletResponse.SC_OK);
				response.flushBuffer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ajaxReturn();
	}
}
