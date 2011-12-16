package jp.co.fcctvweb.actions.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.co.fcctvweb.actions.BasicJsonAction;
import jp.co.fcctvweb.config.Config;

public class UploadAction extends BasicJsonAction {

	private static final long serialVersionUID = -5125123775979436698L;

	private String name;
	private String filePath;
	private int type;
	private File uploadedFile;

	public String execute() {

		if (uploadedFile == null) {
			addActionError("Empty file");
		}

		String fileName = "";
		if (filePath.indexOf("/") != -1) {
			fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		} else {
			fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
		}
		String extName = filePath.substring(filePath.lastIndexOf(".") + 1);

		switch (type) {
		case 1: // video
			if (!"mp4".equalsIgnoreCase(extName)) {
				addActionError(getText("uploader.msg.type.not.support.video"));
			}
			break;
		case 2: // doc
			if (!("doc".equalsIgnoreCase(extName)
					|| "docx".equalsIgnoreCase(extName) || "pdf"
					.equalsIgnoreCase(extName))) {
				addActionError(getText("uploader.msg.type.not.support.doc"));
			}
			break;
		case 3: // music
			if (!("mp3".equalsIgnoreCase(extName) || "ogg"
					.equalsIgnoreCase(extName))) {
				addActionError(getText("uploader.msg.type.not.support.music"));
			}
			break;
		case 4: // photo
			if (!("jpg".equalsIgnoreCase(extName))) {
				addActionError(getText("uploader.msg.type.not.support.photo"));
			}
			break;
		default:
			break;
		}

		if (hasActionErrors()) {
			makeFailure();
			return ajaxReturn();
		}

		File dir = null;
		try {
			switch (type) {
			case 1: // video
				dir = new File(Config.getUploadVideoDir());
				break;
			case 2: // doc
				dir = new File(Config.getUploadDocDir());
				break;
			case 3: // music
				dir = new File(Config.getUploadMusicDir());
				break;
			case 4: // photo
				dir = new File(Config.getUploadPhotoDir());
				break;
			default:
				break;
			}
			
			if (!dir.exists()) {
				dir.mkdirs();
			}

			FileInputStream fis = new FileInputStream(uploadedFile);
			FileOutputStream fos = new FileOutputStream(dir.getAbsolutePath() + "/"
					+ fileName);
			byte[] b = new byte[4096];
			while (fis.read(b) != -1) {
				fos.write(b);
			}
			fos.flush();
			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ajaxReturn();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public File getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(File uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
}
