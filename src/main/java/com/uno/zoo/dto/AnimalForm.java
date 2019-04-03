package com.uno.zoo.dto;

public class AnimalForm {
	private String isisNumber;
    private int species;
    private int location;
    private String housed;
    private String activityCycle;
    private String age;
    
	public String getIsisNumber() {
		return isisNumber;
	}
	public void setIsisNumber(String isisNumber) {
		this.isisNumber = isisNumber;
	}
	public int getSpecies() {
		return species;
	}
	public void setSpecies(int species) {
		this.species = species;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public String getHoused() {
		return housed;
	}
	public void setHoused(String housed) {
		this.housed = housed;
	}
	public String getActivityCycle() {
		return activityCycle;
	}
	public void setActivityCycle(String activityCycle) {
		this.activityCycle = activityCycle;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
}
