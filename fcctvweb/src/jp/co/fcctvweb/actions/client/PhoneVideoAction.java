package jp.co.fcctvweb.actions.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.co.fcctvweb.actions.BasicJsonAction;
import jp.co.fcctvweb.config.Config;

import org.apache.struts2.ServletActionContext;

public class PhoneVideoAction extends BasicJsonAction {

	private static final long serialVersionUID = -3994495719026632083L;

	private String fileId;
	private String type;

	public String execute() {
		String path = ServletActionContext.getRequest().getContextPath();
		String realPath = ServletActionContext.getRequest().getRealPath("/");

		File f = new File(realPath + Config.TMP_VIDEO_DIR);
		if (!f.exists()) {
			f.mkdirs();
		}

		File video = new File(realPath + Config.TMP_VIDEO_DIR + fileId);
		if (!video.exists()) {
			try {
				if ("tv".equals(type)) {
					OutputStream os = new FileOutputStream(video);
					InputStream is = new FileInputStream(Config.getHddMp4Dir() + fileId);
					byte[] b = new byte[65536];
					while(is.read(b) != -1){
						os.write(b);
					}
				} else if ("video".equals(type)) {
					OutputStream os = new FileOutputStream(video);
					InputStream is = new FileInputStream(Config.getUploadVideoDir() + fileId);
					byte[] b = new byte[65536];
					while(is.read(b) != -1){
						os.write(b);
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		setMsg(path + Config.TMP_VIDEO_DIR + fileId);
		return ajaxReturn();
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
