package jp.co.fcctvweb.actions.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.co.fcctvweb.actions.BasicJsonAction;
import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.po.FakeFile;
import jp.co.fcctvweb.po.UploadInfo;
import jp.co.fcctvweb.services.MyDocService;
import jp.co.fcctvweb.services.UploadInfoService;
import jp.co.fcctvweb.utils.Validators;

public class UploadAction extends BasicJsonAction {

	private static final long serialVersionUID = -5125123775979436698L;

	private String name;
	private String filePath;
	private int type;
	private File uploadedFile;

	private UploadInfoService uploadInfoService;
	private MyDocService myDocService;

	public String execute() {

		if (uploadedFile == null) {
			setMsg("Empty file");
		}

		String fileName = "";
		if (filePath.indexOf("/") != -1) {
			fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		} else {
			fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
		}

		String extName = "";
		if (filePath.lastIndexOf(".") != -1) {
			extName = filePath.substring(filePath.lastIndexOf(".") + 1);
		}

		switch (type) {
		case 1: // video
			if (!"mp4".equalsIgnoreCase(extName)) {
				setMsg(getText("uploader.msg.type.not.support.video"));
			}
			break;
		case 2: // doc
			if (!("doc".equalsIgnoreCase(extName)
					|| "docx".equalsIgnoreCase(extName)
					|| "pdf".equalsIgnoreCase(extName)
					|| "xls".equalsIgnoreCase(extName)
					|| "xlsx".equalsIgnoreCase(extName) || "txt"
					.equalsIgnoreCase(extName))) {
				setMsg(getText("uploader.msg.type.not.support.doc"));
			}
			break;
		case 3: // music
			if (!"mp3".equalsIgnoreCase(extName)) {
				setMsg(getText("uploader.msg.type.not.support.music"));
			}
			break;
		case 4: // photo
			if (!("jpg".equalsIgnoreCase(extName))) {
				setMsg(getText("uploader.msg.type.not.support.photo"));
			}
			break;
		default:
			break;
		}

		if (!Validators.isEmpty(getMsg())) {
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

			String realName = fileName;
			switch (type) {
			case Config.MY_FILE_TYPE_DOC:
				realName = "1" + Config.DOC_NAME_SEP + realName;
				break;
			}
			FileOutputStream fos = new FileOutputStream(dir.getAbsolutePath()
					+ "/" + realName);
			byte[] b = new byte[65536];
			while (fis.read(b) != -1) {
				fos.write(b);
			}
			fos.flush();
			fis.close();
			fos.close();

			UploadInfo info = new UploadInfo();
			info.setName(name);
			info.setFileName(fileName);
			info.setExtName(extName);
			info.setSize(uploadedFile.length());
			info.setType(type);

			if (type != Config.MY_FILE_TYPE_DOC) {
				uploadInfoService.removeIfExists(type, fileName);
			}

			uploadInfoService.addUploadInfo(info);

			switch (type) {
			case Config.MY_FILE_TYPE_DOC:
				FakeFile ff = new FakeFile();
				ff.setUploadId(info.getId());
				ff.setFileName(realName);
				myDocService.addFileToRoot(ff);
				break;
			}

			setMsg(getText("uploader.msg.upload.success",
					new String[] { fileName }));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setMsg(getText("uploader.msg.upload.failed"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setMsg(getText("uploader.msg.upload.failed"));
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

	public void setUploadInfoService(UploadInfoService uploadInfoService) {
		this.uploadInfoService = uploadInfoService;
	}

	public void setMyDocService(MyDocService myDocService) {
		this.myDocService = myDocService;
	}
}
