package com.tongchuang.visiondemo.patient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tongchuang.visiondemo.doctor.DoctorRepository;
import com.tongchuang.visiondemo.doctor.PatientDTOMapper;
import com.tongchuang.visiondemo.doctor.dto.DoctorDTO;
import com.tongchuang.visiondemo.hospital.Hospital;
import com.tongchuang.visiondemo.hospital.HospitalRepository;
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
	private NamedParameterJdbcTemplate visionDBTemplate;
	private HospitalRepository 	hospitalRepository;
	
	private Map<Integer, String>	hospitalMap;
	
	@Autowired
	public PatientService(PatientRepository patientRepository, UserRepository userRepository,
							UserRoleRepository userRoleRepository,
							NamedParameterJdbcTemplate visionDBTemplate,
							HospitalRepository 	hospitalRepository) {
		this.patientRepository = patientRepository;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.visionDBTemplate = visionDBTemplate;
		
		List<Hospital> hospitals = (List<Hospital>)hospitalRepository.findAll();
		hospitalMap = new HashMap<Integer, String>();
		for (Hospital h : hospitals) {
			hospitalMap.put(h.getHospitalId(), h.getName());
		}
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
	
	
	public List<DoctorDTO>  getDoctorsByPatientId(String patientId) {
		String SQL =  "SELECT d.*, r.relationship_id FROM Doctor d, Relationship r "
					+ "where r.relationship_type='DOCTOR_PATIENT' "
					+ "and IFNULL(r.deleted, 'N')<>'Y' and r.subject_id=d.doctor_id "
					+ "and r.object_id=:patientid and IFNULL(d.deleted, 'N')<>'Y' ";

		SqlParameterSource namedParameters = new MapSqlParameterSource("patientid", patientId);
		
		List<DoctorDTO> doctors = (List<DoctorDTO>)visionDBTemplate.query(SQL, namedParameters, 
										new DoctorDTOMapper());
		if (doctors != null) {
			for (DoctorDTO d : doctors) {
				d.setHospitalName(hospitalMap.get(d.getHospitalId()));
			}
		}
		return doctors;
	}
}
