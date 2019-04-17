package com.uno.zoo.dto;

import java.sql.Date;

/**
 * Complete item enrichment request form.
 * @author Donovan
 *
 */
public class CompleteRequestForm {
	private DepartmentInfo department;
	private SpeciesInfo species;
	private int animalId;

	private int itemId;
	private String enrichmentName;
	private String enrichmentDescription;
	private int enrichmentLocation;
	private String enrichmentPresentationMethod;
	private String enrichmentDayNightTime;
	private String enrichmentFrequency;

	private boolean lifeStrategiesWksht;
	private boolean anotherDeptZoo;
	private boolean anotherDeptZooMoreInfo;
	private boolean safetyQuestion;
	private boolean risksQuestion;
	private String safetyComment;

	private String naturalBehaviors;

	private String source;
	private String otherSource;
	private int timeRequired;
	private String whoConstructs;
	private boolean volunteerDocentUtilized;
	private String enrichmentCategory;
	private PartialUserInfo nameOfSubmitter;
	private Date dateOfSubmission;

	public DepartmentInfo getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentInfo department) {
		this.department = department;
	}
	public SpeciesInfo getSpecies() {
		return species;
	}
	public void setSpecies(SpeciesInfo species) {
		this.species = species;
	}
	public int getAnimalId() {
		return animalId;
	}
	public void setAnimalId(int animalId) {
		this.animalId = animalId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getEnrichmentName() {
		return enrichmentName;
	}
	public void setEnrichmentName(String enrichmentName) {
		this.enrichmentName = enrichmentName;
	}
	public String getEnrichmentDescription() {
		return enrichmentDescription;
	}
	public void setEnrichmentDescription(String enrichmentDescription) {
		this.enrichmentDescription = enrichmentDescription;
	}
	public int getEnrichmentLocation() {
		return enrichmentLocation;
	}
	public void setEnrichmentLocation(int enrichmentLocation) {
		this.enrichmentLocation = enrichmentLocation;
	}
	public String getEnrichmentPresentationMethod() {
		return enrichmentPresentationMethod;
	}
	public void setEnrichmentPresentationMethod(String enrichmentPresentationMethod) {
		this.enrichmentPresentationMethod = enrichmentPresentationMethod;
	}
	public String getEnrichmentDayNightTime() {
		return enrichmentDayNightTime;
	}
	public void setEnrichmentDayNightTime(String enrichmentDayNightTime) {
		this.enrichmentDayNightTime = enrichmentDayNightTime;
	}
	public String getEnrichmentFrequency() {
		return enrichmentFrequency;
	}
	public void setEnrichmentFrequency(String enrichmentFrequency) {
		this.enrichmentFrequency = enrichmentFrequency;
	}
	public boolean isLifeStrategiesWksht() {
		return lifeStrategiesWksht;
	}
	public void setLifeStrategiesWksht(boolean lifeStrategiesWksht) {
		this.lifeStrategiesWksht = lifeStrategiesWksht;
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
	public boolean isSafetyQuestion() {
		return safetyQuestion;
	}
	public void setSafetyQuestion(boolean safetyQuestion) {
		this.safetyQuestion = safetyQuestion;
	}
	public boolean isRisksQuestion() {
		return risksQuestion;
	}
	public void setRisksQuestion(boolean risksQuestion) {
		this.risksQuestion = risksQuestion;
	}
	public String getSafetyComment() {
		return safetyComment;
	}
	public void setSafetyComment(String safetyComment) {
		this.safetyComment = safetyComment;
	}
	public String getNaturalBehaviors() {
		return naturalBehaviors;
	}
	public void setNaturalBehaviors(String naturalBehaviors) {
		this.naturalBehaviors = naturalBehaviors;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getOtherSource() {
		return otherSource;
	}
	public void setOtherSource(String otherSource) {
		this.otherSource = otherSource;
	}
	public int getTimeRequired() {
		return timeRequired;
	}
	public void setTimeRequired(int timeRequired) {
		this.timeRequired = timeRequired;
	}
	public String getWhoConstructs() {
		return whoConstructs;
	}
	public void setWhoConstructs(String whoConstructs) {
		this.whoConstructs = whoConstructs;
	}
	public boolean isVolunteerDocentUtilized() {
		return volunteerDocentUtilized;
	}
	public void setVolunteerDocentUtilized(boolean volunteerDocentUtilized) {
		this.volunteerDocentUtilized = volunteerDocentUtilized;
	}
	public String getEnrichmentCategory() {
		return enrichmentCategory;
	}
	public void setEnrichmentCategory(String enrichmentCategory) {
		this.enrichmentCategory = enrichmentCategory;
	}
	public PartialUserInfo getNameOfSubmitter() {
		return nameOfSubmitter;
	}
	public void setNameOfSubmitter(PartialUserInfo nameOfSubmitter) {
		this.nameOfSubmitter = nameOfSubmitter;
	}
	public Date getDateOfSubmission() {
		return dateOfSubmission;
	}
	public void setDateOfSubmission(Date dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}
}
