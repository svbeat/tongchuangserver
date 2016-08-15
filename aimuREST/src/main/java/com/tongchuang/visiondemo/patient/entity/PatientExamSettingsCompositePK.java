package com.tongchuang.visiondemo.patient.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PatientExamSettingsCompositePK implements Serializable{
    @Column
    private String examCode;
    @Column
    private Integer patientId;
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
    
	 @Override
	    public boolean equals(Object obj) {
	        if(obj instanceof PatientExamSettingsCompositePK){
	        	PatientExamSettingsCompositePK oPK = (PatientExamSettingsCompositePK) obj;
	 
	            if(!oPK.getExamCode().equals(examCode)){
	                return false;
	            }
	 
	            if(oPK.getPatientId().compareTo(patientId)!=0){
	                return false;
	            }
	 
	            return true;
	        }
	 
	        return false;
	    }
	 
	    @Override
	    public int hashCode() {
	        return examCode.hashCode() + patientId.hashCode();
	    }
	 

}
