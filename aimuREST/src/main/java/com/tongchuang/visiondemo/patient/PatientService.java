package com.tongchuang.visiondemo.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tongchuang.visiondemo.doctor.DoctorRepository;
import com.tongchuang.visiondemo.patient.dto.PatientDTO;
import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.perimetry.PerimetryTestRepository;
import com.tongchuang.visiondemo.user.UserRepository;
import com.tongchuang.visiondemo.user.UserRoleRepository;
import com.tongchuang.visiondemo.user.dto.User;
import com.tongchuang.visiondemo.user.dto.UserRole;
import com.tongchuang.visiondemo.user.dto.UserInfo.Role;
import com.tongchuang.visiondemo.util.ApplicationUtil;

@Service
public class PatientService {

	private PatientRepository 		patientRepository;
	private UserRepository			userRepository;
	private UserRoleRepository		userRoleRepository;
	
	@Autowired
	public PatientService(PatientRepository patientRepository, UserRepository userRepository,
							UserRoleRepository userRoleRepository) {
		this.patientRepository = patientRepository;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
	}
	

	@Transactional(rollbackFor = Exception.class)
	public PatientDTO doCreatePatient(PatientDTO patient) {
		Patient pe = populatePatientEntity(patient);
		patientRepository.save(pe);
		
		User user = populateUser(patient);
		if (user != null) {
			user = userRepository.save(user);
			UserRole ur = new UserRole();
			ur.setRole(Role.PATIENT);
			ur.setSubjectId(pe.getPatientId());
			ur.setUserid(user.getUserid());
			userRoleRepository.save(ur);
		}
		PatientDTO newPatient = new PatientDTO(pe, user);
		return newPatient;
	}
	
	private User populateUser(PatientDTO patient) {
		if (patient.getUsername() != null && !patient.getUsername().isEmpty()
				&& patient.getPassword() != null && !patient.getPassword().isEmpty()) {
			
			User user = new User();
			user.setPassword(patient.getPassword());
			user.setUsername(patient.getUsername());
			if (patient.getUserid() != null) {
				user.setUserid(patient.getUserid());
			}
			return user;
		}
			
		return null;
	}


	private Patient populatePatientEntity(PatientDTO patient) {
		Patient pe = new Patient();
		pe.setAddress(patient.getAddress());
		pe.setBirthdate(patient.getBirthdate());
		pe.setEmail(patient.getEmail());
		pe.setGender(patient.getGender());
		pe.setName(patient.getName());
		if (patient.getPatientId() != null) {
			pe.setPatientId(patient.getPatientId());
		} else {
			pe.setPatientId(ApplicationUtil.getEntityID());
		}
		
		pe.setPhone(patient.getPhone());
		
		return pe;
	}

	
	@Transactional(rollbackFor = Exception.class)
	public PatientDTO doUpdatePatient(PatientDTO patientDTO) {
		
		Patient origPatient = patientRepository.findOne(patientDTO.getPatientId());
		if (origPatient == null) {
			throw new RuntimeException("invalid patientId"); 
		}

		Patient patient = patientRepository.save(this.populatePatientEntity(patientDTO));
		
		User user = populateUser(patientDTO);
		
		if (user != null) {
			User origUser = this.userRepository.getUserByPatientId(patientDTO.getPatientId());
			if (origUser!=null && origUser.getUserid() != patientDTO.getUserid()) {
				throw new RuntimeException("invalid userId"); 	
			}
			user = userRepository.save(user);;
		}
		
		PatientDTO newPatient = new PatientDTO(patient, user);
		return newPatient;
	}
}
