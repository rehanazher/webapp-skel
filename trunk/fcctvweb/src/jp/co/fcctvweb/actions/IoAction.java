package jp.co.fcctvweb.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.po.FakeFile;
import jp.co.fcctvweb.services.MyDocService;
import jp.co.fcctvweb.services.UploadInfoService;
import jp.co.fcctvweb.vo.MyFileVo;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class IoAction extends ActionSupport {

	private static final long serialVersionUID = 9050347875670852730L;

	private String type;
	private String fileId;
	private int index;

	private InputStream inputStream;
	
	private MyDocService myDocService;
	private UploadInfoService uploadInfoService;

	public String execute() {

		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		System.out.println(request);

		try {
			// FileInputStream is = null;
			
			File f = null;
			if ("tv".equals(type)) {
				f = getFile(type, Config.getHddMp4Dir() + fileId + ".mp4");
				inputStream = new FileInputStream(f);
				return "video";
			} else if ("pic".equals(type)) {
				// f = getFile(type, Config.getHddThumbsDir() + fileId +
				// ".jpg");
				inputStream = new FileInputStream(Config.getHddThumbsDir()
						+ fileId + ".jpg");
				return "jpeg";
			} else if ("video".equals(type)) {
				MyFileVo uploadFile = uploadInfoService.getFileById(fileId);
				f = getFile(type, Config.getUploadVideoDir() + uploadFile.getFileName());
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				response.setHeader("Accept-Ranges", "bytes");
				response.setHeader("Connection", "Keep-Alive");
				return "video";
			} else if ("doc".equals(type)) {
				FakeFile file = myDocService.getFileById(Integer.parseInt(fileId));
				f = getFile(type, Config.getUploadDocDir() + file.getFileName());
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				return "doc";
			} else if ("docx".equals(type)) {
				FakeFile file = myDocService.getFileById(Integer.parseInt(fileId));
				f = getFile(type, Config.getUploadDocDir() + file.getFileName());
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				return type;
			} else if ("pdf".equals(type)) {
				FakeFile file = myDocService.getFileById(Integer.parseInt(fileId));
				f = getFile(type, Config.getUploadDocDir() + file.getFileName());
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				return "pdf";
			} else if ("photo".equals(type)) {
				MyFileVo uploadFile = uploadInfoService.getPhotoByIndex(index);
				if (uploadFile == null){
//					String path = ServletActionContext.getRequest().getContextPath();
					String realPath = ServletActionContext.getRequest().getRealPath("/");
					f = new File(realPath + "/touch/images/photo.png");
				}else{
					f = getFile(type, Config.getUploadPhotoDir() + uploadFile.getFileName());
				}
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				return "jpeg";
			} else if ("music".equals(type)) {
				MyFileVo uploadFile = uploadInfoService.getFileById(fileId);
				f = getFile(type, Config.getUploadMusicDir() + uploadFile.getFileName());
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				return "mp3";
			}

			// OutputStream os = response.getOutputStream();
			// byte[] b = new byte[4096];
			// while (is.read(b) != -1) {
			// os.write(b);
			// }
			// os.flush();
			// os.close();
			// response.setStatus(HttpServletResponse.SC_OK);
			// response.flushBuffer();
			// response.resetBuffer();
			// response.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
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
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setMyDocService(MyDocService myDocService) {
		this.myDocService = myDocService;
	}

	public void setUploadInfoService(UploadInfoService uploadInfoService) {
		this.uploadInfoService = uploadInfoService;
	}
}
