package com.tongchuang.visiondemo.doctor.dto;

import com.tongchuang.visiondemo.doctor.entity.Doctor;
import com.tongchuang.visiondemo.patient.PatientConstants.Gender;
import com.tongchuang.visiondemo.user.dto.User;

public class DoctorDTO {
	private Doctor		doctorEntity;
	private User		user;
	private String		hospitalName;
	private Integer		relationshipId;
	
	public DoctorDTO() {
		doctorEntity = new Doctor();
		user = new User();
	}
	

	public DoctorDTO(Doctor doctorEntity, User user) {
		this.doctorEntity = doctorEntity;
		this.user = user;
	}


	public Integer getDoctorId() {
		return doctorEntity.getDoctorId();
	}


	public void setDoctorId(Integer doctorId) {
		doctorEntity.setDoctorId(doctorId);
	}


	public Integer getHospitalId() {
		return doctorEntity.getHospitalId();
	}


	public void setHospitalId(Integer hospitalId) {
		doctorEntity.setHospitalId(hospitalId);
	}


	public String getHospitalName() {
		return hospitalName;
	}


	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}


	public String getName() {
		return doctorEntity.getName();
	}


	public void setName(String name) {
		doctorEntity.setName(name);
	}


	public Gender getGender() {
		return doctorEntity.getGender();
	}


	public void setGender(Gender gender) {
		doctorEntity.setGender(gender);
	}


	public String getPhone() {
		return doctorEntity.getPhone();
	}


	public void setPhone(String phone) {
		doctorEntity.setPhone(phone);
	}


	public String getEmail() {
		return doctorEntity.getEmail();
	}


	public void setEmail(String email) {
		doctorEntity.setEmail(email);
	}


	public String getDescription() {
		return doctorEntity.getDescription();
	}


	public void setDescription(String description) {
		doctorEntity.setDescription(description);
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

	public Integer getRelationshipId() {
		return relationshipId;
	}


	public void setRelationshipId(Integer relationshipId) {
		this.relationshipId = relationshipId;
	}

}
