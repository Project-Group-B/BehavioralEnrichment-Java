package com.uno.zoo.dto;

public class ApprovedEntry {
	private String enrichmentItem;
	private String behaviorsEncouraged;
	private String dateApproved;
	private String safetyConcerns;
	private String exceptions;
	private String comments;
	private String species;
	private String category;
	
	public String getEnrichmentItem() {
		return enrichmentItem;
	}
	public void setEnrichmentItem(String enrichmentItem) {
		this.enrichmentItem = enrichmentItem;
	}

  public String getBehaviorsEncouraged() {
		return behaviorsEncouraged;
	}
	public void setBehaviorsEncouraged(String behaviorsEncouraged) {
		this.behaviorsEncouraged = behaviorsEncouraged;
	}

	public String getDateApproved() {
		return dateApproved;
	}
	public void setDateApproved(String dateApproved) {
		this.dateApproved = dateApproved;
	}

	public String getSafetyConcerns() {
		return safetyConcerns;
	}
	public void setSafetyConcerns(String safetyConcerns) {
		this.safetyConcerns = safetyConcerns;
	}

	public String getExceptions() {
		return exceptions;
	}
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

}