package com.tongchuang.visiondemo.perimetry.dto;

import java.util.Date;

import com.tongchuang.visiondemo.perimetry.entity.PerimetryTest;

public class PerimetryTestDTO {
	private PerimetryTest 	perimetryTest;
	private String			blindSpotCheckedLeft;
	private String			blindSpotCheckedRight;
	
	
	public PerimetryTestDTO(PerimetryTest perimetryTest) {
		this.perimetryTest = perimetryTest;
	}
	
	public long getTestId() {
		return perimetryTest.getTestId();
	}
	public int hashCode() {
		return perimetryTest.hashCode();
	}
	public void setTestId(long testId) {
		perimetryTest.setTestId(testId);
	}
	public Date getCreationDate() {
		return perimetryTest.getCreationDate();
	}
	public void setCreationDate(Date creationDate) {
		perimetryTest.setCreationDate(creationDate);
	}
	public Date getLastUpdateDate() {
		return perimetryTest.getLastUpdateDate();
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		perimetryTest.setLastUpdateDate(lastUpdateDate);
	}
	public String getPatientId() {
		return perimetryTest.getPatientId();
	}
	public void setPatientId(String patientId) {
		perimetryTest.setPatientId(patientId);
	}
	public long getTestDate() {
		return perimetryTest.getTestDate();
	}
	public void setTestDate(long testDate) {
		perimetryTest.setTestDate(testDate);
	}
	public String getResult() {
		return perimetryTest.getResult();
	}
	public void setResult(String result) {
		perimetryTest.setResult(result);
	}
	public String getTestDeviceId() {
		return perimetryTest.getTestDeviceId();
	}
	public void setTestDeviceId(String testDeviceId) {
		perimetryTest.setTestDeviceId(testDeviceId);
	}
	public String getOrigClientTestId() {
		return perimetryTest.getOrigClientTestId();
	}
	public void setOrigClientTestId(String origClientTestId) {
		perimetryTest.setOrigClientTestId(origClientTestId);
	}
	public String getDeleted() {
		return perimetryTest.getDeleted();
	}
	public void setDeleted(String deleted) {
		perimetryTest.setDeleted(deleted);
	}
	public String getType() {
		return perimetryTest.getType();
	}
	public String getTestDateDisplay() {
		return perimetryTest.getTestDateDisplay();
	}
	public String getStatus() {
		return perimetryTest.getStatus();
	}
	public String getBlindSpotCheckedLeft() {
		return blindSpotCheckedLeft;
	}
	public void setBlindSpotCheckedLeft(String blindSpotCheckedLeft) {
		this.blindSpotCheckedLeft = blindSpotCheckedLeft;
	}
	public String getBlindSpotCheckedRight() {
		return blindSpotCheckedRight;
	}
	public void setBlindSpotCheckedRight(String blindSpotCheckedRight) {
		this.blindSpotCheckedRight = blindSpotCheckedRight;
	}

	
}
