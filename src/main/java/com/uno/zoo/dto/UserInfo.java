package com.uno.zoo.dto;

public class UserInfo {
	private boolean loggedIn;
	private String username;
	private String sessionId;
	private UserPermissions permissions;
	private boolean isAdmin;
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public UserPermissions getPermissions() {
		return permissions;
	}
	public void setPermissions(UserPermissions permissions) {
		this.permissions = permissions;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
