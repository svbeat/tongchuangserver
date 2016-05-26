package com.aimu.visiontest.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


public class TestRunDto {
    private int 	testId;
    private String	deviceId;
    private String	wechatSubjectId;
    private int		subjectAge;
    private String	data;
    
	public int getTestId() {
		return testId;
	}
	public void setTestId(int testId) {
		this.testId = testId;
	}

	public String getDeviceId() {
		return deviceId;
	}	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getWechatSubjectId() {
		return wechatSubjectId;
	}
	public void setWechatSubjectId(String wechatSubjectId) {
		this.wechatSubjectId = wechatSubjectId;
	}
	
	public int getSubjectAge() {
		return subjectAge;
	}
	public void setSubjectAge(int subjectAge) {
		this.subjectAge = subjectAge;
	}
	
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "TestRunDto [testId=" + testId + ", deviceId=" + deviceId + ", wechatSubjectId=" + wechatSubjectId
				+ ", subjectAge=" + subjectAge + ", data=" + data + "]";
	}
    
}