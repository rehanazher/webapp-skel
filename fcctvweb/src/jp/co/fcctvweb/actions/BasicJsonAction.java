package jp.co.fcctvweb.actions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.servlet.http.HttpServletResponse;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.utils.Validators;
import jp.co.fcctvweb.utils.ajax.Reply;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class BasicJsonAction extends ActionSupport {

	private static final long serialVersionUID = -5098565305331510249L;

	public static final String JSON = "json"; // return json;
	public static final String AJAX = "ajax"; // return reply object;

	public static Logger logger = Logger.getLogger(BasicJsonAction.class);

	private Reply reply = new Reply();
	private Object jsonObj;

	public String execute() {
		return SUCCESS;
	}
	
	public String emptyJson(){
		jsonObj = Collections.emptyList(); 
		return jsonReturn();
	}

	public String ajaxReturn() {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setIntHeader("Expires", -1);
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("application/json; charset=utf-8");
		// OutputStream os = response.getOutputStream();
		PrintWriter outer;
		try {
			outer = response.getWriter();
			if (logger.isDebugEnabled()) {
				logger.debug(JSONObject.fromObject(reply, Config.JSON_CONFIG)
						.toString());
			}
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
		return AJAX;
	}

	public String jsonReturn() {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setIntHeader("Expires", -1);
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("application/json; charset=utf-8");
		// OutputStream os = response.getOutputStream();
		PrintWriter outer;
		try {
			outer = response.getWriter();
			if (jsonObj instanceof List) {
				if (logger.isDebugEnabled()) {
					logger.debug(JSONArray.fromObject(jsonObj,
							Config.JSON_CONFIG).toString());
				}
				outer.print(JSONArray.fromObject(jsonObj, Config.JSON_CONFIG)
						.toString());
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug(JSONObject.fromObject(jsonObj,
							Config.JSON_CONFIG).toString());
				}
				outer.print(JSONObject.fromObject(jsonObj, Config.JSON_CONFIG)
						.toString());
			}
			outer.flush();
			outer.close();
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSON;
	}

	protected void makeSuccess() {
		getReply().setSuccess(true);
	}

	protected void makeFailure() {
		getReply().setSuccess(false);
	}

	public Reply getReply() {
		return reply;
	}

	public void setReply(Reply reply) {
		this.reply = reply;
	}

	public void addActionError(String anErrorMessage) {
		super.addActionError(anErrorMessage);
		reply.addErrMessage(anErrorMessage);
	}

	public void addActionError(List<String> errMessages) {
		reply.getErrMessage().addAll(errMessages);
	}

	public List<String> getActionError() {
		return reply.getErrMessage();
	}

	public void addActionMessage(String message) {
		super.addActionMessage(message);
		reply.addMessage(message);
	}

	public List<String> getActionMessages() {
		return reply.getMessage();
	}

	public void addActionMessage(List<String> messages) {
		reply.getMessage().addAll(messages);
	}

	public boolean hasActionErrors() {
		return !Validators.isEmpty(reply.getErrMessage());
	}

	public boolean hasActionMessages() {
		return !Validators.isEmpty(reply.getMessage());
	}

	public void setMsg(String msg) {
		reply.setMsg(msg);
	}

	public Object getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(Object jsonObj) {
		this.jsonObj = jsonObj;
	}

	public class I18N {
		public String getI18nText(String key) {
			return getText(key);
		}

		public String getI18nText(String key, String... args) {
			return getText(key, args);
		}

		public PropertyResourceBundle getBundle() {
			return (PropertyResourceBundle) getTexts("messages");
		}
	}

	public I18N getI18n() {
		return i18n;
	}

	private I18N i18n = new I18N();

	public static void main(String[] args) {
		Runtime run = Runtime.getRuntime();
		try {
			Process p = run.exec("ping www.baidu.com");
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null)
				System.out.println(lineStr);// ���杈��淇℃�
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1)// p.exitValue()==0琛ㄧず姝ｅ父缁��锛�锛��姝ｅ父缁��
					System.err.println("�戒护�ц�澶辫触!");
			}
			inBr.close();
			in.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
