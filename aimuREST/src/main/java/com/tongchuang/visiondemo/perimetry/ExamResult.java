package com.tongchuang.visiondemo.perimetry;

import java.util.Map;

public class ExamResult {
	public static enum EXAM_FIELD_OPTION {LEFT, RIGHT, BOTH};
	
    private int     id;
    private String  patientId;
    private long    examDate;
    private String  uploaded;
    private Integer     serverId;
    private String  testDeviceId;

    private String  deviceSettingsVersion;
    private String  patientSettingsVersion;

    private EXAM_FIELD_OPTION examFieldOption;

    private Map<String, String> examResultLeft;
    private Map<String, String> allResponsesLeft;

    private Map<String, String> examResultRight;
    private Map<String, String> allResponsesRight;
    
    private String     blindSpotCheckedLeft;
    private String     blindSpotCheckedRight;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public long getExamDate() {
		return examDate;
	}
	public void setExamDate(long examDate) {
		this.examDate = examDate;
	}
	public String getUploaded() {
		return uploaded;
	}
	public void setUploaded(String uploaded) {
		this.uploaded = uploaded;
	}
	public Integer getServerId() {
		return serverId;
	}
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	public String getTestDeviceId() {
		return testDeviceId;
	}
	public void setTestDeviceId(String testDeviceId) {
		this.testDeviceId = testDeviceId;
	}
	public String getDeviceSettingsVersion() {
		return deviceSettingsVersion;
	}
	public void setDeviceSettingsVersion(String deviceSettingsVersion) {
		this.deviceSettingsVersion = deviceSettingsVersion;
	}
	public String getPatientSettingsVersion() {
		return patientSettingsVersion;
	}
	public void setPatientSettingsVersion(String patientSettingsVersion) {
		this.patientSettingsVersion = patientSettingsVersion;
	}
	public EXAM_FIELD_OPTION getExamFieldOption() {
		return examFieldOption;
	}
	public void setExamFieldOption(EXAM_FIELD_OPTION examFieldOption) {
		this.examFieldOption = examFieldOption;
	}
	public Map<String, String> getExamResultLeft() {
		return examResultLeft;
	}
	public void setExamResultLeft(Map<String, String> examResultLeft) {
		this.examResultLeft = examResultLeft;
	}
	public Map<String, String> getAllResponsesLeft() {
		return allResponsesLeft;
	}
	public void setAllResponsesLeft(Map<String, String> allResponsesLeft) {
		this.allResponsesLeft = allResponsesLeft;
	}
	public Map<String, String> getExamResultRight() {
		return examResultRight;
	}
	public void setExamResultRight(Map<String, String> examResultRight) {
		this.examResultRight = examResultRight;
	}
	public Map<String, String> getAllResponsesRight() {
		return allResponsesRight;
	}
	public void setAllResponsesRight(Map<String, String> allResponsesRight) {
		this.allResponsesRight = allResponsesRight;
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
