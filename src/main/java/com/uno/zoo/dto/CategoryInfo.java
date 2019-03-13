package com.uno.zoo.dto;

/**
 * Info about the item categories, including their name and description.
 * @author Donovan
 *
 */
public class CategoryInfo {
	private int categoryId;
	private String categoryName;
	private String categoryDescription;
	
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDescription() {
		return categoryDescription;
	}
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}
}
