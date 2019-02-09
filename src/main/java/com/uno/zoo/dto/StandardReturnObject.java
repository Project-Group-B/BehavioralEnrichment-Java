package com.uno.zoo.dto;

public class StandardReturnObject {
	private String message;
	private boolean error;
	private String errorMsg;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public void setError(boolean error, String errorMsg) {
		this.error = error;
		this.errorMsg = errorMsg;
	}
}
