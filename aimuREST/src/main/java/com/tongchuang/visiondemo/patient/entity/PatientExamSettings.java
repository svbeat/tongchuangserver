package com.tongchuang.visiondemo.patient.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Entity
@Table(name = "patient_exam_settings")
@IdClass(PatientExamSettingsCompositePK.class)
public class PatientExamSettings {
    @Id
    private String examCode;
    @Id
    private Integer patientId;
	
	private String	examSettings;
	
	

	
	public String getExamCode() {
		return examCode;
	}
	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public String getExamSettings() {
		return examSettings;
	}
	public void setExamSettings(String examSettings) {
		this.examSettings = examSettings;
	}
	
	
}
