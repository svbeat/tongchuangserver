package com.tongchuang.visiondemo.patient.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestsTimeline {
	private String					patientId;
	private List<String>			testDates;
	private Map<String, String>		posResultsLeft;
	private Map<String, String>		posResultsRight;
	

	
	public TestsTimeline(String patientId, List<String> testDates, Map<String, String> posResultsLeft,
			Map<String, String> posResultsRight) {
		this.patientId = patientId;
		this.testDates = testDates;
		this.posResultsLeft = posResultsLeft;
		this.posResultsRight = posResultsRight;
	}
	
	
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public List<String> getTestDates() {
		return testDates;
	}
	public void setTestDates(List<String> testDates) {
		this.testDates = testDates;
	}
	public Map<String, String> getPosResultsLeft() {
		return posResultsLeft;
	}
	public void setPosResultsLeft(Map<String, String> posResultsLeft) {
		this.posResultsLeft = posResultsLeft;
	}
	public Map<String, String> getPosResultsRight() {
		return posResultsRight;
	}
	public void setPosResultsRight(Map<String, String> posResultsRight) {
		this.posResultsRight = posResultsRight;
	}

	
}
