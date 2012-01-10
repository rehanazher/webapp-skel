package jp.co.fcctvweb.interceptors;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.co.fcctvweb.actions.UserAction;
import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.utils.ajax.Reply;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class AuthInterceptor implements Interceptor {

	private static final long serialVersionUID = -7303540806491305L;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
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
				System.out.println("login");
				outer.flush();
				outer.close();
				response.setStatus(HttpServletResponse.SC_OK);
				response.flushBuffer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "login";
		}
			
		return invocation.invoke();
	}

}
