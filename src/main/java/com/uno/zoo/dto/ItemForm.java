package com.uno.zoo.dto;

public class ItemForm {
	private String itemName;
	private String base64EncodedPhoto;
	private String comments;
	private String safetyNotes;
	private String exceptions;
	private String submittor;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getBase64EncodedPhoto() {
		return base64EncodedPhoto;
	}
	public void setBase64EncodedPhoto(String photo) {
		this.base64EncodedPhoto = photo;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getSafetyNotes() {
		return safetyNotes;
	}
	public void setSafetyNotes(String safetyNotes) {
		this.safetyNotes = safetyNotes;
	}
	public String getExceptions() {
		return exceptions;
	}
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}
	public String getSubmittor() {
		return submittor;
	}
	public void setSubmittor(String submittor) {
		this.submittor = submittor;
	}
}
