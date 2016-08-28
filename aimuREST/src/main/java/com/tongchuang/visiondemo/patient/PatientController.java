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
import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.perimetry.PerimetryTest;
import com.tongchuang.visiondemo.perimetry.PerimetryTestRepository;
import com.tongchuang.visiondemo.user.UserRoleRepository;
import com.tongchuang.visiondemo.util.ApplicationUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin
public class PatientController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private PatientRepository 	patientRepository;
	private	DoctorRepository	doctorRespository;
	private final PerimetryTestRepository perimetryTestRepository;
	
	
	
	@Autowired
	public PatientController(PatientRepository patientRepository, PerimetryTestRepository perimetryTestRepository,
							DoctorRepository doctorRespository) {
		this.patientRepository = patientRepository;
		this.perimetryTestRepository = perimetryTestRepository;
		this.doctorRespository = doctorRespository;
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
	public ResponseEntity<Patient> createPatient(@RequestParam("apiKey") String apiKey, @RequestBody Patient patient) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Patient>(HttpStatus.UNAUTHORIZED);
		}
		patient.setPatientId(ApplicationUtil.getEntityID());
		patientRepository.save(patient);
		patient = patientRepository.findOne(patient.getPatientId());
		return new ResponseEntity<Patient>(patient, HttpStatus.CREATED);
	}
		
	@RequestMapping(value = "/patients/{patientId}", method = RequestMethod.POST)
	public ResponseEntity<Patient> updatePatient(@PathVariable("patientId")String patientId,
							@RequestParam("apiKey") String apiKey, 
							@RequestBody Patient patient) {
		
		logger.info("updatePatient: patientId="+patientId);

		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)
				||!patientId.equals(patient.getPatientId())) {
			return new ResponseEntity<Patient>(HttpStatus.UNAUTHORIZED);
		}
		
		Patient origPatient = patientRepository.findOne(patientId);
		if (origPatient == null) {
			return new ResponseEntity<Patient>(HttpStatus.NOT_FOUND); 
		}
		patientRepository.save(patient);
		patient = patientRepository.findOne(patientId);
		return new ResponseEntity<Patient>(patient, HttpStatus.OK);
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
	public ResponseEntity<Patient> getPatient(@PathVariable("patientId")String patientId, @RequestParam("apiKey") String apiKey) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Patient>(HttpStatus.UNAUTHORIZED);
		}
		Patient patient = patientRepository.findOne(patientId);
		if (patient == null) {
			return new ResponseEntity<Patient>(HttpStatus.NOT_FOUND); 
		}
		return new ResponseEntity<Patient>(patient, HttpStatus.CREATED);
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
