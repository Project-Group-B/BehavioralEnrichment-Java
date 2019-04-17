package com.uno.zoo.dto;

/**
 * Info about each species, including their name, description, and ISIS number.
 * @author Donovan
 *
 */
public class SpeciesInfo {
	private int speciesId;
	private String speciesName;
	private String speciesDescription;
	
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
}
