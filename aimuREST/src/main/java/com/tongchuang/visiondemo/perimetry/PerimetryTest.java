package com.tongchuang.visiondemo.perimetry;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@NamedQuery(name = "PerimetryTest.findByPatientId", query = "SELECT p FROM PerimetryTest p WHERE patientId = ? order by testDate desc ")
@Table(name = "perimetry_test")
/*
 TABLE perimetry_test (test_id  bigint NOT NULL AUTO_INCREMENT,
	creation_date TIMESTAMP NOT NULL, 
	last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	patient_id VARCHAR(255), 
	test_date long, 
	result text,
	test_device_id VARCHAR(255),
	orig_client_test_id varchar(255),
	deleted varchar(1),
	PRIMARY KEY (test_id));
 */
public class PerimetryTest {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long 	testId;
    
    private Date	creationDate;
    
    private Date	lastUpdateDate;
    private String	patientId;
    private long	testDate;
    private String	result;
    private String	testDeviceId;
    private String	origClientTestId;
    private String	deleted;
    
    @Transient
    private String 	type="视野检测";
    @Transient
    private String	status="正常";
    
	public long getTestId() {
		return testId;
	}
	public void setTestId(long testId) {
		this.testId = testId;
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
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public long getTestDate() {
		return testDate;
	}
	public void setTestDate(long testDate) {
		this.testDate = testDate;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTestDeviceId() {
		return testDeviceId;
	}
	public void setTestDeviceId(String testDeviceId) {
		this.testDeviceId = testDeviceId;
	}
	public String getOrigClientTestId() {
		return origClientTestId;
	}
	public void setOrigClientTestId(String origClientTestId) {
		this.origClientTestId = origClientTestId;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
	@Transient
	public String getType() {
		return type;
	}
	
	@Transient
	public String getTestDateDisplay() {
	    Date date=new Date(testDate);
	    return date.toString();
	}
	
	@Transient
	public String getStatus() {
		return status;
	}
	
	@Override
	public String toString() {
		return "PerimetryTest [testId=" + testId + ", creationDate=" + creationDate + ", lastUpdateDate="
				+ lastUpdateDate + ", patientId=" + patientId + ", testDate=" + testDate + ", result=" + result
				+ ", testDeviceId=" + testDeviceId + ", origClientTestId=" + origClientTestId + ", deleted=" + deleted
				+ "]";
	}
 
	
    
}