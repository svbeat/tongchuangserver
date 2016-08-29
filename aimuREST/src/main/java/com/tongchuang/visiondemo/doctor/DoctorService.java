package com.tongchuang.visiondemo.doctor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tongchuang.visiondemo.doctor.DoctorRepository;
import com.tongchuang.visiondemo.doctor.dto.DoctorDTO;
import com.tongchuang.visiondemo.doctor.entity.Doctor;
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
	
	@Autowired
	public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository,
							UserRoleRepository userRoleRepository) {
		this.doctorRepository = doctorRepository;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
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
}
