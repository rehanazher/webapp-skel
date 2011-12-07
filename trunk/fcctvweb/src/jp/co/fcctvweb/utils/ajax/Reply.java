package jp.co.fcctvweb.utils.ajax;

import java.util.ArrayList;
import java.util.List;

public class Reply {

	public static class ErrorInfo {

		private String clientCode;
		private String portOfLoading;

		public ErrorInfo(String clientCode, String portOfLoading) {
			this.clientCode = clientCode;
			this.portOfLoading = portOfLoading;
		}

		public String getClientCode() {
			return clientCode;
		}

		public void setClientCode(String clientCode) {
			this.clientCode = clientCode;
		}

		public String getPortOfLoading() {
			return portOfLoading;
		}

		public void setPortOfLoading(String portOfLoading) {
			this.portOfLoading = portOfLoading;
		}
	}

	private boolean success = true;
	
	private ErrorInfo errors;
	private String status;
	private Object value;
	private Object collections;

	private String msg;
	
	private List<String> errMessage = new ArrayList<String>();
	private List<String> message = new ArrayList<String>();

	public void addErrMessage(String msg) {
		errMessage.add(msg);
	}

	public void addMessage(String msg) {
		message.add(msg);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public List<String> getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(List<String> errMessage) {
		this.errMessage = errMessage;
	}

	public List<String> getMessage() {
		return message;
	}

	public void setMessage(List<String> message) {
		this.message = message;
	}

	public Object getCollections() {
		return collections;
	}

	public void setCollections(Object collections) {
		this.collections = collections;
	}

	public ErrorInfo getErrors() {
		return errors;
	}

	public void setErrors(ErrorInfo errors) {
		this.errors = errors;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
