package com.tongchuang.visiondemo.doctor.entity;

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

import com.tongchuang.visiondemo.ApplicationConstants.EntityDeleted;
import com.tongchuang.visiondemo.patient.PatientConstants.Gender;

@Entity
@Table(name = "doctor")
/*
CREATE TABLE doctor (doctor_id int(11) not null auto_increment,
	creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	created_by int(11),
	last_updated_by int(11),
	hospital_id int(11),	
	name varchar(30),
	title varchar(300),
	description text,
	phone varchar(30),
	email varchar(30),
	deleted char(1),
	primary key (doctor_id)) DEFAULT CHARSET=utf8;
 */
public class Doctor {
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer 		doctorId;
    
    private Date		creationDate;    
    private Date		lastUpdateDate;
    private Integer		createdBy;
    private Integer		lastUpdatedBy;
    
    private Integer		hospitalId;
    private String		name;
    @Enumerated(EnumType.STRING)
    private Gender		gender;
    private String		phone;
    private String		email;
    private String		description;
    
    @Enumerated(EnumType.STRING)
    private EntityDeleted	deleted;

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EntityDeleted getDeleted() {
		return deleted;
	}

	public void setDeleted(EntityDeleted deleted) {
		this.deleted = deleted;
	}
    
}