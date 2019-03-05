package com.uno.zoo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserLogIn {
	@Size(min=1, max=25)
	@NotNull
	private String username;
	
	@Size(min=1)
	@NotNull
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
