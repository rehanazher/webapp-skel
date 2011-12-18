package jp.co.fcctvweb.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.fcctvweb.config.Config;
import jp.co.fcctvweb.utils.Validators;

import org.apache.struts2.ServletActionContext;

import sun.misc.BASE64Encoder;

import com.opensymphony.xwork2.ActionSupport;

public class IoAction extends ActionSupport {

	private static final long serialVersionUID = 9050347875670852730L;

	private String type;
	private String fileId;

	private InputStream inputStream;

	public String execute() {

		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		System.out.println(request);

		boolean byteRange = !Validators.isEmpty(request.getHeader("range"));
		try {
			// FileInputStream is = null;
			File f = null;
			if ("tv".equals(type)) {
				f = getFile(type, Config.getHddMp4Dir() + fileId + ".mp4");
				inputStream = new FileInputStream(f);
				 response.setHeader("Content-Length", "" + f.length());
				 response.setHeader("Accept-Ranges", "bytes");
				 response.setHeader("Connection", "Keep-Alive");

				response.setHeader("Last-Modified",
						new Date(f.lastModified()).toGMTString());
				try {
					MessageDigest md5 = MessageDigest.getInstance("MD5");
					BASE64Encoder base64en = new BASE64Encoder();
					String md5String = base64en.encode(md5.digest(request
							.getRequestURI().getBytes()));
					response.setHeader("Etag", "\\W" + md5String + f.length());
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// response.setHeader("buffer", "")
				System.out.println(request.getHeader("range"));
				if (byteRange) {
					response.setContentType("video/mp4");
//					response.setContentType("multipart/byteranges; boundary=THIS_STRING_SEPARATES");
//
//					long start = 0;
//					long end = 0;
					response.setHeader("Content-Range", "bytes 0-" + (f.length() - 1)  + "/" + f.length());
//					response.flushBuffer();
//					OutputStream os = response.getOutputStream();
//					byte[] b = new byte[2];
//					if (inputStream.read(b) != -1) {
//						os.write(b);
//						os.flush();
//					}
//					inputStream.close();
//					os.close();
					response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
//					response.flushBuffer();
					return "httpheader";
				}
				// header('HTTP/1.1 416 Requested Range Not Satisfiable');
				// header("Content-Range: bytes $start-$end/$size");
				return "video";
			} else if ("pic".equals(type)) {
				// f = getFile(type, Config.getHddThumbsDir() + fileId +
				// ".jpg");
				inputStream = new FileInputStream(Config.getHddThumbsDir()
						+ fileId + ".jpg");
				System.out.println(request.getHeader("range"));
				return "jpeg";
			} else if ("video".equals(type)) {
				f = getFile(type, Config.getUploadVideoDir() + fileId);
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				response.setHeader("Accept-Ranges", "bytes");
				response.setHeader("Connection", "Keep-Alive");
				return "video";
			} else if ("doc".equals(type)) {
				f = getFile(type, Config.getUploadDocDir() + fileId);
				inputStream = new FileInputStream(f);
				response.setHeader("Content-Length", "" + f.length());
				return "doc";
			} else if ("docx".equals(type)) {
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

	public InputStream getInputStream() {
		return inputStream;
	}
}
