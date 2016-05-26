package com.aimu.visiontest.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.aimu.visiontest.dto.TestRunDto;

@Entity
@Table(name = "vision_test_run")
/*
	CREATE TABLE vision_test_run (test_run_id  int NOT NULL AUTO_INCREMENT,
		creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
		last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		test_id int NOT NULL,
		testee_type VARCHAR(240) NOT NULL,
		testee_id VARCHAR(240) NOT NULL,
		testee_age int,
		result TEXT,
		score int,
		device_id VARCHAR(240),
		PRIMARY KEY (test_run_id));
*/

public class VisionTestRun {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long 	testRunId;
    
    private int		testId;
    private Date	creationDate;
    private Date	lastUpdateDate;
    private String	deviceId;
    private String	subjectType;
    private String	subjectId;
    private int		subjectAge;
    private String	data;
    private int		score;
	
	public long getTestRunId() {
		return testRunId;
	}
	public void setTestRunId(long testRunId) {
		this.testRunId = testRunId;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getTestId() {
		return testId;
	}
	public void setTestId(int testId) {
		this.testId = testId;
	}
	
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
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
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "VisionTestRun [testRunId=" + testRunId + ", testId=" + testId + ", creationDate=" + creationDate
				+ ", lastUpdateDate=" + lastUpdateDate + ", deviceId=" + deviceId + ", subjectType=" + subjectType
				+ ", subjectId=" + subjectId + ", subjectAge=" + subjectAge + ", data=" + data + ", score=" + score
				+ "]";
	}

    
    
}