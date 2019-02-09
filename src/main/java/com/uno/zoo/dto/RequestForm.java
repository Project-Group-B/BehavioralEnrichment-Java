package com.uno.zoo.dto;

import java.sql.Date;

public class RequestForm {
	private String department;
	private String species;
	private String housed;
	private String activityCycle;
	private String age;
	private String enrichmentName;
	private String enrichmentDayNightTime;
	private String enrichmentDescription;
	private String enrichmentFrequency;
	private String enrichmentPresentation;
	private boolean anotherDeptZoo;
	private boolean anotherDeptZooMoreInfo;
	private boolean lifeStrategiesWksht;
	private String safetyComment;
	private boolean safetyQuestion;
	private String naturalBehaviors;
	private String[] enrichmentCategory;
	private String nameOfSubmitter;
	private String otherSource;
	private String source;
	private String timeRequired;
	private boolean volunteerDocentUtilized;
	private String whoConstructs;
	private Date dateOfSubmission;
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
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
	public String getEnrichmentName() {
		return enrichmentName;
	}
	public void setEnrichmentName(String enrichmentName) {
		this.enrichmentName = enrichmentName;
	}
	public String getEnrichmentDayNightTime() {
		return enrichmentDayNightTime;
	}
	public void setEnrichmentDayNightTime(String enrichmentDayNightTime) {
		this.enrichmentDayNightTime = enrichmentDayNightTime;
	}
	public String getEnrichmentDescription() {
		return enrichmentDescription;
	}
	public void setEnrichmentDescription(String enrichmentDescription) {
		this.enrichmentDescription = enrichmentDescription;
	}
	public String getEnrichmentFrequency() {
		return enrichmentFrequency;
	}
	public void setEnrichmentFrequency(String enrichmentFrequency) {
		this.enrichmentFrequency = enrichmentFrequency;
	}
	public String getEnrichmentPresentation() {
		return enrichmentPresentation;
	}
	public void setEnrichmentPresentation(String enrichmentPresentation) {
		this.enrichmentPresentation = enrichmentPresentation;
	}
	public boolean isAnotherDeptZoo() {
		return anotherDeptZoo;
	}
	public void setAnotherDeptZoo(boolean anotherDeptZoo) {
		this.anotherDeptZoo = anotherDeptZoo;
	}
	public boolean isAnotherDeptZooMoreInfo() {
		return anotherDeptZooMoreInfo;
	}
	public void setAnotherDeptZooMoreInfo(boolean anotherDeptZooMoreInfo) {
		this.anotherDeptZooMoreInfo = anotherDeptZooMoreInfo;
	}
	public boolean isLifeStrategiesWksht() {
		return lifeStrategiesWksht;
	}
	public void setLifeStrategiesWksht(boolean lifeStrategiesWksht) {
		this.lifeStrategiesWksht = lifeStrategiesWksht;
	}
	public String getSafetyComment() {
		return safetyComment;
	}
	public void setSafetyComment(String safetyComment) {
		this.safetyComment = safetyComment;
	}
	public boolean isSafetyQuestion() {
		return safetyQuestion;
	}
	public void setSafetyQuestion(boolean safetyQuestion) {
		this.safetyQuestion = safetyQuestion;
	}
	public String getNaturalBehaviors() {
		return naturalBehaviors;
	}
	public void setNaturalBehaviors(String naturalBehaviors) {
		this.naturalBehaviors = naturalBehaviors;
	}
	public String[] getEnrichmentCategory() {
		return enrichmentCategory;
	}
	public void setEnrichmentCategory(String[] enrichmentCategory) {
		this.enrichmentCategory = enrichmentCategory;
	}
	public String getNameOfSubmitter() {
		return nameOfSubmitter;
	}
	public void setNameOfSubmitter(String nameOfSubmitter) {
		this.nameOfSubmitter = nameOfSubmitter;
	}
	public String getOtherSource() {
		return otherSource;
	}
	public void setOtherSource(String otherSource) {
		this.otherSource = otherSource;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTimeRequired() {
		return timeRequired;
	}
	public void setTimeRequired(String timeRequired) {
		this.timeRequired = timeRequired;
	}
	public boolean isVolunteerDocentUtilized() {
		return volunteerDocentUtilized;
	}
	public void setVolunteerDocentUtilized(boolean volunteerDocentUtilized) {
		this.volunteerDocentUtilized = volunteerDocentUtilized;
	}
	public String getWhoConstructs() {
		return whoConstructs;
	}
	public void setWhoConstructs(String whoConstructs) {
		this.whoConstructs = whoConstructs;
	}
	public Date getDateOfSubmission() {
		return dateOfSubmission;
	}
	public void setDateOfSubmission(Date dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}
}
