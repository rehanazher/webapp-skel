package jp.co.fcctvweb.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import jp.co.fcctvweb.config.Config;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class IoAction extends ActionSupport {

	private static final long serialVersionUID = 9050347875670852730L;

	private String type;
	private String fileId;
	
	private InputStream inputStream;

	public String execute() {

		HttpServletResponse response = ServletActionContext.getResponse();
		// HttpServletRequest request = ServletActionContext.getRequest();
		try {
//			FileInputStream is = null;
			File f = null;
			if ("tv".equals(type)) {
				f = getFile(type, Config.getHddMp4Dir() + fileId + ".mp4");
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				return "video";
			} else if ("pic".equals(type)) {
//				f = getFile(type, Config.getHddThumbsDir() + fileId + ".jpg");
				inputStream = new FileInputStream(Config.getHddThumbsDir() + fileId + ".jpg");
				return "jpeg";
			} else if ("video".equals(type)) {
				f = getFile(type, Config.getUploadVideoDir() + fileId);
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				return "video";
			} else if ("doc".equals(type)) {
				f = getFile(type, Config.getUploadDocDir() + fileId);
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				return "doc";
			}else if ("docx".equals(type)){
				f = getFile(type, Config.getUploadDocDir() + fileId);
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				return type;
			} else if ("pdf".equals(type)) {
				f = getFile(type, Config.getUploadDocDir() + fileId);
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				return "pdf";
			} else if ("photo".equals(type)) {

			} else if ("music".equals(type)) {

			}

			
//			OutputStream os = response.getOutputStream();
//			byte[] b = new byte[4096];
//			while (is.read(b) != -1) {
//				os.write(b);
//			}
//			os.flush();
//			os.close();
//			response.setStatus(HttpServletResponse.SC_OK);
//			response.flushBuffer();
//			response.resetBuffer();
//			response.reset();
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

	public InputStream getInputStream() {
		return inputStream;
	}
}
