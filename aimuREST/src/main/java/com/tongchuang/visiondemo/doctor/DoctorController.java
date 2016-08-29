package com.tongchuang.visiondemo.doctor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.tongchuang.visiondemo.ApplicationConstants;
import com.tongchuang.visiondemo.ApplicationConstants.EntityDeleted;
import com.tongchuang.visiondemo.ApplicationConstants.EntityStatus;
import com.tongchuang.visiondemo.common.ResponseList;
import com.tongchuang.visiondemo.device.Calibration;
import com.tongchuang.visiondemo.doctor.dto.DoctorDTO;
import com.tongchuang.visiondemo.doctor.entity.Doctor;
import com.tongchuang.visiondemo.patient.PatientRepository;
import com.tongchuang.visiondemo.patient.dto.PatientDTO;
import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.perimetry.PerimetryTest;
import com.tongchuang.visiondemo.user.UserRepository;
import com.tongchuang.visiondemo.user.UserRoleRepository;
import com.tongchuang.visiondemo.user.dto.User;
import com.tongchuang.visiondemo.util.ApplicationUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin
public class DoctorController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private DoctorRepository 	doctorRepository;
	private PatientRepository 	patientRepository;
	private DoctorService		doctorService;
	private UserRepository			userRepository;
	
	@Autowired
	public DoctorController(DoctorRepository doctorRepository, PatientRepository patientRepository,
								DoctorService doctorService, UserRepository	userRepository) {
		this.doctorRepository = doctorRepository;
		this.patientRepository = patientRepository;
		this.doctorService = doctorService;
		this.userRepository = userRepository;
	}


	@RequestMapping(value = "/doctors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseList<Doctor>> getDoctors( 
				@RequestParam("apiKey") String apiKey, 
				@RequestParam(value = "filter", required=false) String filter,
				@RequestParam(value = "returnTotal", required=false, defaultValue = "false") boolean returnTotal,
				@RequestParam(value = "orderBy", required=false) String orderBy,
				@RequestParam(value = "pageno", required=false, defaultValue = "0") int pageno, 
				@RequestParam(value = "pagesize", required=false, defaultValue = "10") int pagesize) {
		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<ResponseList<Doctor>>(HttpStatus.UNAUTHORIZED);
		}
		logger.info("getDoctors: filter="+filter+"; pageno="+pageno+"; pagesize="+pagesize);
		List<Doctor> doctors = doctorRepository.getDoctors(new PageRequest(pageno, pagesize));
		ResponseList<Doctor> result = new ResponseList<Doctor>(doctors);
		Integer total = null;
		if (returnTotal) {
			total = doctorRepository.getTotalCount();
			result.setTotalCounts(total);
		}
		return new ResponseEntity<ResponseList<Doctor>>(result, HttpStatus.OK);
	}
	

	@RequestMapping(value = "/doctors", method = RequestMethod.POST)
	public ResponseEntity createDoctor(@RequestParam("apiKey") String apiKey, @RequestBody DoctorDTO doctor) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<DoctorDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		DoctorDTO newDoctor = null;
		
		try {
			newDoctor = doctorService.doCreateDoctor(doctor);
		} catch (Exception e) {
			return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
		}
		
		return new ResponseEntity<DoctorDTO>(newDoctor, HttpStatus.CREATED);
	}
		
	@RequestMapping(value = "/doctors/{doctorId}", method = RequestMethod.POST)
	public ResponseEntity updateDoctor(@PathVariable("doctorId")Integer doctorId,
							@RequestParam("apiKey") String apiKey, 
							@RequestBody DoctorDTO doctorDTO) {
		
		logger.info("updateDoctor: doctorId="+doctorId);

		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)
				||!doctorId.equals(doctorDTO.getDoctorId())) {
			return new ResponseEntity<DoctorDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		DoctorDTO newDoctor = null;
		
		try {
			newDoctor = doctorService.doUpdateDoctor(doctorDTO);
		} catch (Exception e) {
			return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
		}
		
		return new ResponseEntity<DoctorDTO>(newDoctor, HttpStatus.OK);
	}

	@RequestMapping(value = "/doctors/{doctorId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteDoctor(@PathVariable("doctorId") Integer doctorId, @RequestParam("apiKey") String apiKey) {
		
		logger.info("deleteDoctor: doctorId="+doctorId);

		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}

		Doctor doctor = doctorRepository.findOne(doctorId);
		if (doctor == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); 
		}
		
		doctor.setDeleted(EntityDeleted.Y);
		doctorRepository.save(doctor);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/doctors/{doctorId}", method = RequestMethod.GET)
	public ResponseEntity<DoctorDTO> getDoctor(@PathVariable("doctorId")Integer doctorId, @RequestParam("apiKey") String apiKey) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<DoctorDTO>(HttpStatus.UNAUTHORIZED);
		}

		Doctor doctor = doctorRepository.findOne(doctorId);
		if (doctor == null) {
			return new ResponseEntity<DoctorDTO>(HttpStatus.NOT_FOUND); 
		}
		
		User user = userRepository.getUserByDoctorId(doctorId.toString());
		DoctorDTO doctorDTO= new DoctorDTO(doctor, user);
		
		return new ResponseEntity<DoctorDTO>(doctorDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/doctors/{doctorId}/patients", method = RequestMethod.GET)
	public ResponseEntity<ResponseList<Patient>> getPatientsByDoctor(@PathVariable("doctorId")Integer doctorId, 
			@RequestParam(value = "filter", required=false) String filter,
			@RequestParam(value = "returnTotal", required=false, defaultValue = "false") boolean returnTotal,
			@RequestParam(value = "orderBy", required=false) String orderBy,
			@RequestParam(value = "pageno", required=false, defaultValue = "0") int pageno, 
			@RequestParam(value = "pagesize", required=false, defaultValue = "10") int pagesize,
			@RequestParam("apiKey") String apiKey) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<ResponseList<Patient>>(HttpStatus.UNAUTHORIZED);
		}
		List<Patient> patients = patientRepository.getPatientsByDoctorId(doctorId.toString(), new PageRequest(pageno, pagesize));

		ResponseList<Patient> result = new ResponseList<Patient>(patients);
		Integer total = null;
		if (returnTotal) {
			total = patientRepository.getTotalCountByDoctorId(doctorId.toString());
			result.setTotalCounts(total);
		}
		
		return new ResponseEntity<ResponseList<Patient>>(result, HttpStatus.OK);
	}
}
