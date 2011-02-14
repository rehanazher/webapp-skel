package com.aaut.skeleton.commons.util;

import java.util.ArrayList;
import java.util.List;

public class Reply {

	private boolean success = true;
	private String status;
	private Object value;
	private Object collections;

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
}
