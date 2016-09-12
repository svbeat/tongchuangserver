package com.tongchuang.visiondemo.doctor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.tongchuang.visiondemo.ApiError;
import com.tongchuang.visiondemo.ApplicationConstants;
import com.tongchuang.visiondemo.ApplicationConstants.EntityDeleted;
import com.tongchuang.visiondemo.ApplicationConstants.EntityStatus;
import com.tongchuang.visiondemo.ApplicationConstants.RelationshipType;
import com.tongchuang.visiondemo.common.ResponseList;
import com.tongchuang.visiondemo.device.Calibration;
import com.tongchuang.visiondemo.doctor.dto.DoctorDTO;
import com.tongchuang.visiondemo.doctor.entity.Doctor;
import com.tongchuang.visiondemo.hospital.Hospital;
import com.tongchuang.visiondemo.hospital.HospitalRepository;
import com.tongchuang.visiondemo.patient.PatientRepository;
import com.tongchuang.visiondemo.patient.PatientService;
import com.tongchuang.visiondemo.patient.dto.PatientDTO;
import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.perimetry.PerimetryTest;
import com.tongchuang.visiondemo.relationship.RelationshipRepository;
import com.tongchuang.visiondemo.relationship.entity.Relationship;
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
	private PatientService			patientService;
	private RelationshipRepository 	relationshipRepository;
	private HospitalRepository 	hospitalRepository;
	
	private Map<Integer, String>	hospitalMap;
	
	@Autowired
	public DoctorController(DoctorRepository doctorRepository, PatientRepository patientRepository,
								DoctorService doctorService, UserRepository	userRepository,
								PatientService patientService,
								RelationshipRepository relationshipRepository,
								HospitalRepository hospitalRepository) {
		this.doctorRepository = doctorRepository;
		this.patientRepository = patientRepository;
		this.doctorService = doctorService;
		this.userRepository = userRepository;
		this.patientService = patientService;
		this.relationshipRepository = relationshipRepository;
		this.hospitalRepository = hospitalRepository;
		
		List<Hospital> hospitals = (List<Hospital>)hospitalRepository.findAll();
		hospitalMap = new HashMap<Integer, String>();
		for (Hospital h : hospitals) {
			hospitalMap.put(h.getHospitalId(), h.getName());
		}
	}


	@RequestMapping(value = "/doctors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseList<DoctorDTO>> getDoctors( 
				@RequestParam("apiKey") String apiKey, 
				@RequestParam(value = "filter", required=false) String filter,
				@RequestParam(value = "returnTotal", required=false, defaultValue = "false") boolean returnTotal,
				@RequestParam(value = "orderBy", required=false) String orderBy,
				@RequestParam(value = "pageno", required=false, defaultValue = "0") int pageno, 
				@RequestParam(value = "pagesize", required=false, defaultValue = "10") int pagesize) {
		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<ResponseList<DoctorDTO>>(HttpStatus.UNAUTHORIZED);
		}
		logger.info("getDoctors: filter="+filter+"; pageno="+pageno+"; pagesize="+pagesize);
		
		String f = "%";
		if (filter != null) {
			f = f+filter+f;
		}
		List<Doctor> doctors = doctorRepository.getDoctors(f, new PageRequest(pageno, pagesize));
		
		List<DoctorDTO> doctorsDTO = new ArrayList<DoctorDTO>();
		for (Doctor d : doctors) {
			DoctorDTO dt = new DoctorDTO(d, null); 
			if (d.getHospitalId() != null) {
				dt.setHospitalName(hospitalMap.get(d.getHospitalId()));
			}
			doctorsDTO.add(dt);
		}
		ResponseList<DoctorDTO> result = new ResponseList<DoctorDTO>(doctorsDTO);
		Integer total = null;
		if (returnTotal) {
			total = doctorRepository.getTotalCount(f);
			result.setTotalCounts(total);
		}
		return new ResponseEntity<ResponseList<DoctorDTO>>(result, HttpStatus.OK);
	}
	

	@RequestMapping(value = "/doctors", method = RequestMethod.POST)
	public ResponseEntity createDoctor(@RequestParam("apiKey") String apiKey, @RequestBody DoctorDTO doctor) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<DoctorDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		logger.info("doctor name;"+doctor.getName());
		
		DoctorDTO newDoctor = null;
		
		try {
			newDoctor = doctorService.doCreateDoctor(doctor);
		} catch (Exception e) {			
			ApiError apiError = new ApiError(Integer.toString(HttpStatus.BAD_REQUEST.value()), 
					Integer.toString(HttpStatus.BAD_REQUEST.value()), 
					ApplicationConstants.API_ERROR, e.getMessage());
			return new ResponseEntity<ApiError>(apiError, HttpStatus.OK);
		}
		
		return new ResponseEntity<DoctorDTO>(newDoctor, HttpStatus.CREATED);
	}
		
	@RequestMapping(value = "/doctors/{doctorId}", method = RequestMethod.POST)
	public ResponseEntity updateDoctor(@PathVariable("doctorId")Integer doctorId,
							@RequestParam("apiKey") String apiKey, 
							@RequestBody DoctorDTO doctorDTO) {
		
		logger.info("updateDoctor: doctorId="+doctorId);

		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			ApiError apiError = new ApiError(Integer.toString(HttpStatus.UNAUTHORIZED.value()), 
					Integer.toString(HttpStatus.UNAUTHORIZED.value()), 
					ApplicationConstants.API_KEY_MISSING, ApplicationConstants.API_KEY_MISSING);
			return new ResponseEntity<ApiError>(apiError, HttpStatus.OK);
		}
		
		DoctorDTO newDoctor = null;
		
		doctorDTO.setDoctorId(doctorId);
		try {
			newDoctor = doctorService.doUpdateDoctor(doctorDTO);
		} catch (Exception e) {
			ApiError apiError = new ApiError(Integer.toString(HttpStatus.BAD_REQUEST.value()), 
					Integer.toString(HttpStatus.BAD_REQUEST.value()), 
					ApplicationConstants.API_ERROR, e.getMessage());
			return new ResponseEntity<ApiError>(apiError, HttpStatus.OK);
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
		
		if (doctor.getHospitalId() != null) {
			doctorDTO.setHospitalName(hospitalMap.get(doctor.getHospitalId()));
		}
		return new ResponseEntity<DoctorDTO>(doctorDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/doctors/{doctorId}/patients", method = RequestMethod.GET)
	public ResponseEntity<ResponseList<PatientDTO>> getPatientsByDoctor(@PathVariable("doctorId")Integer doctorId, 
			@RequestParam(value = "filter", required=false) String filter,
			@RequestParam(value = "returnTotal", required=false, defaultValue = "false") boolean returnTotal,
			@RequestParam(value = "orderBy", required=false) String orderBy,
			@RequestParam(value = "pageno", required=false, defaultValue = "0") int pageno, 
			@RequestParam(value = "pagesize", required=false, defaultValue = "10") int pagesize,
			@RequestParam("apiKey") String apiKey) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<ResponseList<PatientDTO>>(HttpStatus.UNAUTHORIZED);
		}
		List<PatientDTO> patients = doctorService.getPatientsByDoctorId(doctorId, 
									pageno*pagesize, pagesize);

		ResponseList<PatientDTO> result = new ResponseList<PatientDTO>(patients);
		Integer total = null;
		if (returnTotal) {
			total = patientRepository.getTotalCountByDoctorId(doctorId.toString());
			result.setTotalCounts(total);
		}
		
		return new ResponseEntity<ResponseList<PatientDTO>>(result, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/doctors/{doctorId}/patients", method = RequestMethod.POST)
	public ResponseEntity createPatient(@RequestParam("apiKey") String apiKey, @PathVariable("doctorId")Integer doctorId,
								@RequestBody PatientDTO patient) {
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
		
		Relationship relationship = new Relationship();
		relationship.setRelationshipType(RelationshipType.DOCTOR_PATIENT);
		relationship.setSubjectId(doctorId.toString());
		relationship.setObjectId(newPatient.getPatientId());
		relationshipRepository.save(relationship);
		return new ResponseEntity<PatientDTO>(newPatient, HttpStatus.CREATED);
	}
}
