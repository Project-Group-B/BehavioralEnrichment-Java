package com.uno.zoo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User's sign up information. All fields must be provided.
 * @author Donovan
 *
 */
public class UserSignUp {
	@Size(min=1, max=25)
	@NotNull
	private String username;
	
	@NotNull
	private int status;
	
	@NotNull
	private DepartmentInfo department;
	
	@Size(min=1, max=50)
	@NotNull
	private String firstName;
	
	@Size(min=1, max=45)
	@NotNull
	private String lastName;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public DepartmentInfo getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentInfo department) {
		this.department = department;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
