package com.tongchuang.visiondemo.patient.dto;

import java.util.Date;

import com.tongchuang.visiondemo.patient.PatientConstants.Gender;
import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.user.dto.User;


public class PatientDTO {
	private Patient	patientEntity;
	private User			user;
	private Integer	dpRelationshipId;
	
	
	public PatientDTO() {
		patientEntity = new Patient();
		user = new User();
	}

	public PatientDTO(Patient patientEntity, User user) {
		this.patientEntity = patientEntity;
		this.user = user;
	}
	
	
	public void setPatientId(String patientId) {
		patientEntity.setPatientId(patientId);
	}

	public void setPhone(String phone) {
		patientEntity.setPhone(phone);
	}

	public String getPatientId() {
		return patientEntity.getPatientId();
	}
	public void setCreationDate(Date creationDate) {
		patientEntity.setCreationDate(creationDate);
	}
	public void setCreatedBy(Integer createdBy) {
		patientEntity.setCreatedBy(createdBy);
	}
	public String getName() {
		return patientEntity.getName();
	}
	public void setName(String name) {
		patientEntity.setName(name);
	}
	public Gender getGender() {
		return patientEntity.getGender();
	}
	public void setGender(Gender gender) {
		patientEntity.setGender(gender);
	}
	public Date getBirthdate() {
		return patientEntity.getBirthdate();
	}
	public void setBirthdate(Date birthdate) {
		patientEntity.setBirthdate(birthdate);
	}
	public String getAddress() {
		return patientEntity.getAddress();
	}
	public void setAddress(String address) {
		patientEntity.setAddress(address);
	}
	
	
	public String getPhone() {
		return patientEntity.getPhone();
	}
	public String getEmail() {
		return patientEntity.getEmail();
	}
	public void setEmail(String email) {
		patientEntity.setEmail(email);
	}
	public Long getUserid() {
		if (user != null) {
			return user.getUserid();	
		}
		return null;
	}
	public void setUserid(long userid) {
		user.setUserid(userid);
	}
	public String getUsername() {
		if (user != null) {
			return user.getUsername();			
		}
		return null;
	}
	public void setUsername(String username) {
		user.setUsername(username);
	}
	public String getPassword() {
		if (user != null) {
			return user.getPassword();	
		}
		return null;
		
	}
	public void setPassword(String password) {
		user.setPassword(password);
	}

	public Integer getDpRelationshipId() {
		return dpRelationshipId;
	}

	public void setDpRelationshipId(Integer dpRelationshipId) {
		this.dpRelationshipId = dpRelationshipId;
	}
	
	

}
