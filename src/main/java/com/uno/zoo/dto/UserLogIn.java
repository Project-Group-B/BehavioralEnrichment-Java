package com.uno.zoo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User log in information. Username must be between 1 {@literal &} 25 characters and user password must be at least 1
 * character.
 * @author Donovan
 *
 */
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
