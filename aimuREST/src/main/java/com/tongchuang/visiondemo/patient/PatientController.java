package com.tongchuang.visiondemo.patient;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.tongchuang.visiondemo.ApplicationConstants;
import com.tongchuang.visiondemo.ApplicationConstants.EntityStatus;
import com.tongchuang.visiondemo.common.ResponseList;
import com.tongchuang.visiondemo.device.Calibration;
import com.tongchuang.visiondemo.doctor.DoctorRepository;
import com.tongchuang.visiondemo.doctor.entity.Doctor;
import com.tongchuang.visiondemo.patient.dto.PatientDTO;
import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.perimetry.PerimetryTest;
import com.tongchuang.visiondemo.perimetry.PerimetryTestRepository;
import com.tongchuang.visiondemo.user.UserRepository;
import com.tongchuang.visiondemo.user.UserRoleRepository;
import com.tongchuang.visiondemo.user.dto.User;
import com.tongchuang.visiondemo.user.dto.UserInfo.Role;
import com.tongchuang.visiondemo.user.dto.UserRole;
import com.tongchuang.visiondemo.util.ApplicationUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin
public class PatientController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private PatientRepository 		patientRepository;
	private	DoctorRepository		doctorRespository;
	private UserRepository			userRepository;
	private UserRoleRepository		userRoleRepository;
	private final PerimetryTestRepository perimetryTestRepository;
	private PatientService			patientService;
	
	
	
	@Autowired
	public PatientController(PatientRepository patientRepository, PerimetryTestRepository perimetryTestRepository,
							DoctorRepository doctorRespository, UserRepository userRepository,
							UserRoleRepository userRoleRepository,
							PatientService patientService) {
		this.patientRepository = patientRepository;
		this.perimetryTestRepository = perimetryTestRepository;
		this.doctorRespository = doctorRespository;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.patientService = patientService;
	}


	@RequestMapping(value = "/patients", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseList<Patient>> getPatients( 
				@RequestParam("apiKey") String apiKey, 
				@RequestParam(value = "filter", required=false) String filter,
				@RequestParam(value = "returnTotal", required=false, defaultValue = "false") boolean returnTotal,
				@RequestParam(value = "orderBy", required=false) String orderBy,
				@RequestParam(value = "pageno", required=false, defaultValue = "0") int pageno, 
				@RequestParam(value = "pagesize", required=false, defaultValue = "10") int pagesize) {
		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<ResponseList<Patient>>(HttpStatus.UNAUTHORIZED);
		}
		logger.info("getPatients: filter="+filter+"; pageno="+pageno+"; pagesize="+pagesize);
		List<Patient> patients = patientRepository.getPatients(new PageRequest(pageno, pagesize));
		ResponseList<Patient> result = new ResponseList<Patient>(patients);
		Integer total = null;
		if (returnTotal) {
			total = patientRepository.getTotalCount();
			result.setTotalCounts(total);
		}
		return new ResponseEntity<ResponseList<Patient>>(result, HttpStatus.OK);
	}
	

	@RequestMapping(value = "/patients", method = RequestMethod.POST)
	public ResponseEntity createPatient(@RequestParam("apiKey") String apiKey, @RequestBody PatientDTO patient) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<PatientDTO>(HttpStatus.UNAUTHORIZED);
		}
		

		PatientDTO newPatient = null;
		
		try {
			newPatient = patientService.doCreatePatient(patient);
		} catch (Exception e) {
			return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
		}
		
		return new ResponseEntity<PatientDTO>(newPatient, HttpStatus.CREATED);
	}
		

	@RequestMapping(value = "/patients/{patientId}", method = RequestMethod.POST)
	public ResponseEntity<?> updatePatientDTO(@PathVariable("patientId")String patientId,
							@RequestParam("apiKey") String apiKey, 
							@RequestBody PatientDTO patientDTO) {
		
		logger.info("updatePatient: patientId="+patientId);

		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)
				||!patientId.equals(patientDTO.getPatientId())) {
			return new ResponseEntity<PatientDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		PatientDTO newPatient = null;
		
		try {
			newPatient = patientService.doUpdatePatient(patientDTO);
		} catch (Exception e) {
			return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
		}
		
		return new ResponseEntity<PatientDTO>(newPatient, HttpStatus.OK);
	}

	@RequestMapping(value = "/patients/{patientId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deletePatient(@PathVariable("patientId") String patientId, @RequestParam("apiKey") String apiKey) {
		
		logger.info("deletePatient: patientId="+patientId);

		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}

		Patient patient = patientRepository.findOne(patientId);
		if (patient == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); 
		}
		
		patient.setStatus(EntityStatus.DELETED);
		patientRepository.save(patient);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/patients/{patientId}", method = RequestMethod.GET)
	public ResponseEntity<PatientDTO> getPatient(@PathVariable("patientId")String patientId, @RequestParam("apiKey") String apiKey) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<PatientDTO>(HttpStatus.UNAUTHORIZED);
		}
		Patient patient = patientRepository.findOne(patientId);
		if (patient == null) {
			return new ResponseEntity<PatientDTO>(HttpStatus.NOT_FOUND); 
		}
		
		User user = userRepository.getUserByPatientId(patientId);
		PatientDTO patientDTO= new PatientDTO(patient, user);
		return new ResponseEntity<PatientDTO>(patientDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/patients/{patientId}/doctors", method = RequestMethod.GET)
	public ResponseEntity<ResponseList<Doctor>> getDoctorsByPatient(@PathVariable("patientId")String patientId,
			@RequestParam("apiKey") String apiKey) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<ResponseList<Doctor>>(HttpStatus.UNAUTHORIZED);
		}
		List<Doctor> doctors = doctorRespository.getDoctorsByPatientId(patientId);

		ResponseList<Doctor> result = new ResponseList<Doctor>(doctors);
		
		return new ResponseEntity<ResponseList<Doctor>>(result, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/patients/{patientId}/tests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseList<PerimetryTest>> getPerimetryTests(@PathVariable("patientId") String patientId, 
				@RequestParam("apiKey") String apiKey, 
				@RequestParam(value = "filter", required=false) String filter,
				@RequestParam(value = "returnTotal", required=false, defaultValue = "false") boolean returnTotal,
				@RequestParam(value = "orderBy", required=false) String orderBy,
				@RequestParam(value = "pageno", required=false, defaultValue = "0") int pageno, 
				@RequestParam(value = "limit", required=false, defaultValue = "10") int limit) {
		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<ResponseList<PerimetryTest>>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Fetching PerimetryTest by patientId " + patientId+", pageno="+pageno+", limit="+limit);
		List<PerimetryTest> perimetryExams = perimetryTestRepository.findByPatientId(patientId, new PageRequest(pageno, limit));
		int total = perimetryTestRepository.getTotalByPatientId(patientId);
		
		ResponseList<PerimetryTest> response = new ResponseList<PerimetryTest>(perimetryExams);
		response.setTotalCounts(total);
		return new ResponseEntity<ResponseList<PerimetryTest>>(response, HttpStatus.OK);
	}
	
}
