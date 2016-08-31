package com.tongchuang.visiondemo.patient.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Entity
@Table(name = "patient_exam_settings_log")
/*
 * CREATE TABLE patient_exam_settings_log (
	settings_log_id int(11) not null auto_increment,
	exam_code varchar(100) not null,
	patient_id varchar(60),
	creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	created_by int(11),
	version int(11),
	exam_settings text,
	primary key (settings_log_id));
 */
public class PatientExamSettingsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer	settingsLogId;
    
    private String	examCode;
    private String 	patientId;
	private Integer	version;
	private String	examSettings;
	
	public Integer getSettingsLogId() {
		return settingsLogId;
	}
	public void setSettingsLogId(Integer settingsLogId) {
		this.settingsLogId = settingsLogId;
	}
	
	public String getExamCode() {
		return examCode;
	}
	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}
	
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public String getExamSettings() {
		return examSettings;
	}
	public void setExamSettings(String examSettings) {
		this.examSettings = examSettings;
	}
		
}
