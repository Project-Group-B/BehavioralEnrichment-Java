package com.uno.zoo.dto;

/**
 * Database id and name of each department.
 * @author Donovan
 *
 */
public class DepartmentInfo {
	private int departmentId;
	private String departmentName;
	
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	@Override
	public String toString() {
		return "{departmentId='" + departmentId + "', departmentName='" + departmentName + "'}";
	}
}
