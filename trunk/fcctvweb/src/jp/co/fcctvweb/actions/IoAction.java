package jp.co.fcctvweb.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.fcctvweb.config.Config;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class IoAction extends ActionSupport {

	private static final long serialVersionUID = 9050347875670852730L;

	private String type;
	private String fileId;

	public String execute() {

		HttpServletResponse response = ServletActionContext.getResponse();
		// HttpServletRequest request = ServletActionContext.getRequest();
		try {
			FileInputStream is = null;
			File f = null;
			if ("tv".equals(type)) {
				f = getFile(type, Config.getHddMp4Dir() + fileId + ".mp4");
				is = new FileInputStream(f);
				response.setContentType("video/mp4");
			} else if ("pic".equals(type)) {
				f = getFile(type, Config.getHddThumbsDir() + fileId + ".jpg");
				is = new FileInputStream(f);
				response.setContentType("image/jpeg");
			} else if ("video".equals(type)) {
				response.setContentType("video/mp4");
			} else if ("doc".equals(type)) {

			} else if ("pdf".equals(type)) {

			} else if ("photo".equals(type)) {

			} else if ("music".equals(type)) {

			}

			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Connection", "Keep-Alive");
			response.setHeader("Content-Length", "" + f.length());

			OutputStream os = response.getOutputStream();
			byte[] b = new byte[4096];
			while (is.read(b) != -1) {
				os.write(b);
			}
			os.flush();
			os.close();
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
			response.resetBuffer();
			response.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			 e.printStackTrace();
		}
		return "ajax";
	}

	private File getFile(String type, String fileName) {
		File f = new File(fileName);
		return f;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
}
