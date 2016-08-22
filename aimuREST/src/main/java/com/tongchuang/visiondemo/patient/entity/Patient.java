package com.tongchuang.visiondemo.patient.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tongchuang.visiondemo.ApplicationConstants.EntityStatus;
import com.tongchuang.visiondemo.patient.PatientConstants.Gender;

@Entity
@Table(name = "patient")
/*
CREATE TABLE patient (patient_id varchar(30) not null,
	creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	created_by int(11),
	last_updated_by int(11),	
	name varchar(30),
    gender varchar(4),
	birthdate Date,
	address varchar(300),
	phone varchar(30),
	email varchar(30),
	status varchar(8),
	primary key (patient_id));
 */
public class Patient {
	
    @Id
    private String 			patientId;    
    private Date			creationDate;    
    private Date			lastUpdateDate;
    private Integer			createdBy;
    private Integer			lastUpdatedBy;
    private String			name;
    private Gender			gender;
    private Date			birthdate;
    private String			address;
    private String			phone;
    private String			email;
    
    @Enumerated(EnumType.STRING)
    private EntityStatus	status;
    
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
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
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public EntityStatus getStatus() {
		return status;
	}
	public void setStatus(EntityStatus status) {
		this.status = status;
	}
    
    
    
}