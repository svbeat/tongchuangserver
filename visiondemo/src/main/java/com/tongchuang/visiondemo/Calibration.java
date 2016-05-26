package com.tongchuang.visiondemo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "Calibration.getLatestCalibration", query = "SELECT p FROM Calibration p WHERE enabled='Y' order by lastUpdateDate desc")
@Table(name = "calibration")
/*
 TABLE calibration (code  VARCHAR(10),
	creation_date TIMESTAMP NOT NULL, 
	last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	result text,
	enabled varchar(1),
	PRIMARY KEY (code));
 */
public class Calibration {

    @Id
    private String 	code;
    
    private Date	creationDate;
    private Date	lastUpdateDate;
    private String	result;
    private String	enabled;
    
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
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
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getEnabled() {
		return enabled;
	}
	
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public String toString() {
		return "Calibration [code=" + code + ", creationDate=" + creationDate + ", lastUpdateDate=" + lastUpdateDate
				+ ", result=" + result + ", enabled=" + enabled + "]";
	}
        
}