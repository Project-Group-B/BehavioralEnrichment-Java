package com.uno.zoo.dto;

/**
 * Info Incident Reports - Status
 * @author Mariah
 *
 */
public class SubmittedIncident {
	private String incidentID;
	private String incidentDate;
	private String enrichmentItem;
	private String department;
	private String futureUseDecision;
	
	public String getIncidentID() {
		return incidentID;
	}
	
	public void setIncidentID(String incidentID) {
		this.incidentID = incidentID;
	}
	
	public String getIncidentDate() {
		return incidentDate;
	}
	
	public void setIncidentDate(String incidentDate) {
		this.incidentDate = incidentDate;
	}
	
	public String getEnrichmentItem() {
		return enrichmentItem;
	}
	
	public void setEnrichmentItem(String enrichmentItem) {
		this.enrichmentItem = enrichmentItem;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getFutureUseDecision() {
		return futureUseDecision;
	}
	
	public void setFutureUseDecision(String futureUseDecision) {
		this.futureUseDecision = futureUseDecision;
	}
	

}
