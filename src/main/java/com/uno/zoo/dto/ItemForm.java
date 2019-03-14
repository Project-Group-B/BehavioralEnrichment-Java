package com.uno.zoo.dto;

public class ItemForm {
	private String itemName;
	private String photo;
	private String comments;
	private String safetyNotes;
	private String exceptions;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
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
}
