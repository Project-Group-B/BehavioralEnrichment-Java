package com.uno.zoo.dto;

public class SpeciesInfo {
	private int speciesId;
	private String speciesName;
	private String speciesDescription;
	private int speciesIsisNumber;
	
	public int getSpeciesId() {
		return speciesId;
	}
	public void setSpeciesId(int speciesId) {
		this.speciesId = speciesId;
	}
	public String getSpeciesName() {
		return speciesName;
	}
	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}
	public String getSpeciesDescription() {
		return speciesDescription;
	}
	public void setSpeciesDescription(String speciesDescription) {
		this.speciesDescription = speciesDescription;
	}
	public int getSpeciesIsisNumber() {
		return speciesIsisNumber;
	}
	public void setSpeciesIsisNumber(int speciesIsisNumber) {
		this.speciesIsisNumber = speciesIsisNumber;
	}
}
