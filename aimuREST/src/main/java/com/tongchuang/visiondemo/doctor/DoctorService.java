package com.tongchuang.visiondemo.doctor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tongchuang.visiondemo.common.ResponseList;
import com.tongchuang.visiondemo.doctor.DoctorRepository;
import com.tongchuang.visiondemo.doctor.dto.DoctorDTO;
import com.tongchuang.visiondemo.doctor.entity.Doctor;
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
public class DoctorService {

	private DoctorRepository 		doctorRepository;
	private UserRepository			userRepository;
	private UserRoleRepository		userRoleRepository;
	private NamedParameterJdbcTemplate visionDBTemplate;
	
	@Autowired
	public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository,
							UserRoleRepository userRoleRepository,
							NamedParameterJdbcTemplate visionDBTemplate) {
		this.doctorRepository = doctorRepository;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.visionDBTemplate = visionDBTemplate;
	}
	

	@Transactional(rollbackFor = Exception.class)
	public DoctorDTO doCreateDoctor(DoctorDTO doctor) {
		Doctor pe = populateDoctorEntity(doctor);
		pe = doctorRepository.save(pe);
		
		User user = populateUser(doctor);
		if (user != null) {
			user = userRepository.save(user);
			UserRole ur = new UserRole();
			ur.setRole(Role.DOCTOR);
			ur.setSubjectId(pe.getDoctorId().toString());
			ur.setUserid(user.getUserid());
			userRoleRepository.save(ur);
		}
		DoctorDTO newDoctor = new DoctorDTO(pe, user);
		return newDoctor;
	}
	
	private User populateUser(DoctorDTO doctor) {
		if (doctor.getUsername() != null && !doctor.getUsername().isEmpty()
				&& doctor.getPassword() != null && !doctor.getPassword().isEmpty()) {
			
			User user = new User();
			user.setPassword(doctor.getPassword());
			user.setUsername(doctor.getUsername());
			if (doctor.getUserid() != null) {
				user.setUserid(doctor.getUserid());
			}
			return user;
		}
			
		return null;
	}


	private Doctor populateDoctorEntity(DoctorDTO doctor) {
		Doctor de = new Doctor();
		de.setDescription(doctor.getDescription());

		de.setEmail(doctor.getEmail());
		de.setGender(doctor.getGender());
		de.setHospitalId(doctor.getHospitalId());
		de.setName(doctor.getName());
		de.setPhone(doctor.getPhone());
		if (doctor.getDoctorId() != null) {
			de.setDoctorId(doctor.getDoctorId());
		} 
		
		return de;
	}

	
	@Transactional(rollbackFor = Exception.class)
	public DoctorDTO doUpdateDoctor(DoctorDTO doctorDTO) {
		
		Doctor origDoctor = doctorRepository.findOne(doctorDTO.getDoctorId());
		if (origDoctor == null) {
			throw new RuntimeException("invalid doctorId"); 
		}

		Doctor doctor = doctorRepository.save(this.populateDoctorEntity(doctorDTO));
		
		User user = populateUser(doctorDTO);
		
		if (user != null) {
			User origUser = this.userRepository.getUserByDoctorId(doctorDTO.getDoctorId().toString());
			if (origUser!=null && origUser.getUserid() != doctorDTO.getUserid()) {
				throw new RuntimeException("invalid userId"); 	
			}
			user = userRepository.save(user);;
		}
		
		DoctorDTO newDoctor = new DoctorDTO(doctor, user);
		return newDoctor;
	}
	
	public List<PatientDTO>  getPatientsByDoctorId(Integer doctorId, Integer offset, Integer rowCount) {
		String SQL = " SELECT p.*, r.relationship_id FROM patient p, relationship r "
					+ "where r.relationship_type='DOCTOR_PATIENT' "
					+ "and IFNULL(r.deleted, 'N')<>'Y' and r.subject_id=:doctorId "
					+ "and r.object_id=p.patient_id and p.status<>'DELETED' order by p.name asc "
					+ "limit :offset, :rowCount";

		SqlParameterSource namedParameters = new MapSqlParameterSource("doctorId", doctorId)
												.addValue("offset", offset)
												.addValue("rowCount", rowCount);
		
		List<PatientDTO> patients = (List<PatientDTO>)visionDBTemplate.query(SQL, namedParameters, 
										new PatientDTOMapper());
		
		return patients;
	}
}
