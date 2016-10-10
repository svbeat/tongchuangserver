package com.tongchuang.visiondemo.patient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.google.gson.Gson;
import com.tongchuang.visiondemo.ApiError;
import com.tongchuang.visiondemo.ApplicationConstants;
import com.tongchuang.visiondemo.ApplicationController;
import com.tongchuang.visiondemo.ApplicationConstants.EntityStatus;
import com.tongchuang.visiondemo.ApplicationConstants.ExamCode;
import com.tongchuang.visiondemo.common.ResponseList;
import com.tongchuang.visiondemo.device.Calibration;
import com.tongchuang.visiondemo.doctor.DoctorRepository;
import com.tongchuang.visiondemo.doctor.dto.DoctorDTO;
import com.tongchuang.visiondemo.doctor.entity.Doctor;
import com.tongchuang.visiondemo.patient.dto.PatientDTO;
import com.tongchuang.visiondemo.patient.dto.PatientSettings;
import com.tongchuang.visiondemo.patient.dto.TestsTimeline;
import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettingsLog;
import com.tongchuang.visiondemo.perimetry.ExamResult;
import com.tongchuang.visiondemo.perimetry.PerimetryTestRepository;
import com.tongchuang.visiondemo.perimetry.dto.PerimetryTestDTO;
import com.tongchuang.visiondemo.perimetry.entity.PerimetryTest;
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
	
	private PatientExamSettingsRepository patientExamSettingsRepository;
	private PatientExamSettingsLogRepository patientExamSettingsLogRepository;
	
	private Gson				gson = new Gson();
    private static Pattern POS_CODE_PATTERN = Pattern.compile("q([0-9]+)r([0-9]+)c([0-9]+)");
	
	
	@Autowired
	public PatientController(PatientRepository patientRepository, PerimetryTestRepository perimetryTestRepository,
							DoctorRepository doctorRespository, UserRepository userRepository,
							UserRoleRepository userRoleRepository,
							PatientService patientService,
							PatientExamSettingsRepository patientExamSettingsRepository,
							PatientExamSettingsLogRepository patientExamSettingsLogRepository) {
		this.patientRepository = patientRepository;
		this.perimetryTestRepository = perimetryTestRepository;
		this.doctorRespository = doctorRespository;
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.patientService = patientService;
		this.patientExamSettingsRepository = patientExamSettingsRepository;
		this.patientExamSettingsLogRepository = patientExamSettingsLogRepository;
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
		String f = "%";
		if (filter != null) {
			f = f+filter+f;
		}
		List<Patient> patients = patientRepository.getPatients(f, new PageRequest(pageno, pagesize));
		ResponseList<Patient> result = new ResponseList<Patient>(patients);
		Integer total = null;
		if (returnTotal) {
			total = patientRepository.getTotalCount(f);
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
			ApiError apiError = new ApiError(Integer.toString(HttpStatus.BAD_REQUEST.value()), 
					Integer.toString(HttpStatus.BAD_REQUEST.value()), 
					ApplicationConstants.API_ERROR, e.getMessage());
			return new ResponseEntity<ApiError>(apiError, HttpStatus.OK);
		}
		
		return new ResponseEntity<PatientDTO>(newPatient, HttpStatus.CREATED);
	}
		

	@RequestMapping(value = "/patients/{patientId}", method = RequestMethod.POST)
	public ResponseEntity<?> updatePatientDTO(@PathVariable("patientId")String patientId,
							@RequestParam("apiKey") String apiKey, 
							@RequestBody PatientDTO patientDTO) {
		
		logger.info("updatePatient: patientId="+patientId);

		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<PatientDTO>(HttpStatus.UNAUTHORIZED);
		}
		
		PatientDTO newPatient = null;
		patientDTO.setPatientId(patientId);
		try {
			newPatient = patientService.doUpdatePatient(patientDTO);
		} catch (Exception e) {
			ApiError apiError = new ApiError(Integer.toString(HttpStatus.BAD_REQUEST.value()), 
					Integer.toString(HttpStatus.BAD_REQUEST.value()), 
					ApplicationConstants.API_ERROR, e.getMessage());
			return new ResponseEntity<ApiError>(apiError, HttpStatus.OK);
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
	public ResponseEntity<ResponseList<DoctorDTO>> getDoctorsByPatient(@PathVariable("patientId")String patientId,
			@RequestParam("apiKey") String apiKey) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<ResponseList<DoctorDTO>>(HttpStatus.UNAUTHORIZED);
		}
		List<DoctorDTO> doctors = patientService.getDoctorsByPatientId(patientId);
		ResponseList<DoctorDTO> result = new ResponseList<DoctorDTO>(doctors);
		
		return new ResponseEntity<ResponseList<DoctorDTO>>(result, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/patients/{patientId}/tests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseList<PerimetryTestDTO>> getPerimetryTests(@PathVariable("patientId") String patientId, 
				@RequestParam("apiKey") String apiKey, 
				@RequestParam(value = "filter", required=false) String filter,
				@RequestParam(value = "returnTotal", required=false, defaultValue = "false") boolean returnTotal,
				@RequestParam(value = "orderBy", required=false) String orderBy,
				@RequestParam(value = "pageno", required=false, defaultValue = "0") int pageno, 
				@RequestParam(value = "pagesize", required=false, defaultValue = "10") int pagesize) {
		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<ResponseList<PerimetryTestDTO>>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Fetching PerimetryTest by patientId " + patientId+", pageno="+pageno+", pagesize="+pagesize);
		List<PerimetryTest> perimetryExams = perimetryTestRepository.findByPatientId(patientId, new PageRequest(pageno, pagesize));

		int total = perimetryTestRepository.getTotalByPatientId(patientId);
		
		List<PerimetryTestDTO> perimetryExamsDTO = postPoulateResult(perimetryExams);

		
		ResponseList<PerimetryTestDTO> response = new ResponseList<PerimetryTestDTO>(perimetryExamsDTO);
		response.setTotalCounts(total);
		return new ResponseEntity<ResponseList<PerimetryTestDTO>>(response, HttpStatus.OK);
	}
	
	private List<PerimetryTestDTO> postPoulateResult(List<PerimetryTest> perimetryExams) {
		if (perimetryExams == null) {
			return null;
		}
		
		List<PerimetryTestDTO> results = new ArrayList<PerimetryTestDTO>();
		for (PerimetryTest test : perimetryExams) {
			PerimetryTestDTO dto = new PerimetryTestDTO(test);
			ExamResult r = gson.fromJson(test.getResult(), ExamResult.class);
			dto.setBlindSpotCheckedLeft(r.getBlindSpotCheckedLeft());
			dto.setBlindSpotCheckedRight(r.getBlindSpotCheckedRight());
			results.add(dto);
		}
		// TODO Auto-generated method stub
		return results;
	}


	@RequestMapping(value = "/patients/{patientid}/examsettings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PatientSettings> getPatientSettings(@PathVariable("patientid") String patientId,
			@RequestParam("examCode") String examCode, @RequestParam("apiKey") String apiKey) {
		
		if (!ApplicationController.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<PatientSettings>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Fetching patient settings for patient:"+patientId);
		PatientExamSettings settings = patientExamSettingsRepository.findSetting(examCode, patientId);
		if (settings==null) {
			settings = patientExamSettingsRepository.findSetting(examCode, "0");
			if (settings == null)
				return new ResponseEntity<PatientSettings>(HttpStatus.NOT_FOUND);
		}
		
		PatientSettings patientSettings = gson.fromJson(settings.getExamSettings(),  PatientSettings.class);
		
		patientSettings.setInitStimulusDBLeft(sortByPosCode(patientSettings.getInitStimulusDBLeft()));
		patientSettings.setInitStimulusDBRight(sortByPosCode(patientSettings.getInitStimulusDBRight()));
		patientSettings.setStimulusPrioritiesLeft(sortByPosCode(patientSettings.getStimulusPrioritiesLeft()));
		patientSettings.setStimulusPrioritiesRight(sortByPosCode(patientSettings.getStimulusPrioritiesRight()));
		return new ResponseEntity<PatientSettings>(patientSettings, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/patients/{patientid}/examsettings/default", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PatientSettings> resetPatientSettings(@PathVariable("patientid") String patientId,
			@RequestParam("examCode") String examCode, @RequestParam("apiKey") String apiKey) {
		
		if (!ApplicationController.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<PatientSettings>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("reset patient settings for patient:"+patientId);
		PatientExamSettings defaultSettings =  patientExamSettingsRepository.findSetting(examCode, "0");
		if (defaultSettings == null) {
			return new ResponseEntity<PatientSettings>(HttpStatus.NOT_FOUND);
		}
		
		PatientSettings defaultPatientSettings = gson.fromJson(defaultSettings.getExamSettings(),  PatientSettings.class);
		
		PatientExamSettings settings = patientExamSettingsRepository.findSetting(examCode, patientId);

		PatientSettings patientSettings = null;
		if (settings==null) {
			settings = new PatientExamSettings();
			settings.setExamCode(examCode);
			settings.setPatientId(patientId);
			patientSettings = new PatientSettings(defaultPatientSettings);
		} else {
			patientSettings = gson.fromJson(settings.getExamSettings(), PatientSettings.class);
			
			PatientExamSettingsLog settingLog = new PatientExamSettingsLog();
			settingLog.setExamCode(ExamCode.PERIMETRY.name());
			settingLog.setExamSettings(settings.getExamSettings());
			settingLog.setPatientId(patientId);
			settingLog.setVersion(patientSettings.getVersion());
			
			patientExamSettingsLogRepository.save(settingLog);
			
			patientSettings.setVersion(patientSettings.getVersion()+1);	
			patientSettings.setInitStimulusDBLeft(defaultPatientSettings.getInitStimulusDBLeft());
			patientSettings.setInitStimulusDBRight(defaultPatientSettings.getInitStimulusDBRight());
			patientSettings.setStimulusPrioritiesLeft(defaultPatientSettings.getStimulusPrioritiesLeft());
			patientSettings.setStimulusPrioritiesRight(defaultPatientSettings.getInitStimulusDBRight());
		}
		
			
		settings.setExamSettings(gson.toJson(patientSettings));				
		patientExamSettingsRepository.save(settings);	
		
		return new ResponseEntity<PatientSettings>(patientSettings, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/patients/{patientid}/examsettings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PatientSettings> updatePatientSettings(@PathVariable("patientid") String patientId,
			@RequestParam("examCode") String examCode, @RequestParam("apiKey") String apiKey,
			@RequestBody PatientSettings patientSettings) {
		
		if (!ApplicationController.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<PatientSettings>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("update patient settings for patient:"+patientId);
		PatientExamSettings dbPatientExamSettings =  patientExamSettingsRepository.findSetting(examCode, patientId);
		int version=1;
		if (dbPatientExamSettings != null) {
			PatientSettings origSettings = gson.fromJson(dbPatientExamSettings.getExamSettings(), PatientSettings.class);
			version = origSettings.getVersion()==null?1:origSettings.getVersion()+1;
			
			PatientExamSettingsLog settingLog = new PatientExamSettingsLog();
			settingLog.setExamCode(ExamCode.PERIMETRY.name());
			settingLog.setExamSettings(dbPatientExamSettings.getExamSettings());
			settingLog.setPatientId(patientId);
			settingLog.setVersion(origSettings.getVersion());			
			patientExamSettingsLogRepository.save(settingLog);			
		} else {
			dbPatientExamSettings = new PatientExamSettings();
			dbPatientExamSettings.setExamCode(examCode);
			dbPatientExamSettings.setPatientId(patientId);
		}
		patientSettings.setVersion(version);
		dbPatientExamSettings.setExamSettings(gson.toJson(patientSettings));
		patientExamSettingsRepository.save(dbPatientExamSettings);
		
		return new ResponseEntity<PatientSettings>(patientSettings, HttpStatus.OK);
	}	
	
	@RequestMapping(value = "/patients/{patientId}/tests/timeline", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestsTimeline> getPerimetryTests(@PathVariable("patientId") String patientId, 
				@RequestParam("apiKey") String apiKey, 
				@RequestParam(value = "pageno", required=false, defaultValue = "0") int pageno, 
				@RequestParam(value = "pagesize", required=false, defaultValue = "10") int pagesize) {
		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<TestsTimeline>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Fetching PerimetryTest by patientId " + patientId+", pageno="+pageno+", pagesize="+pagesize);
		List<PerimetryTest> perimetryExams = perimetryTestRepository.findByPatientId(patientId, new PageRequest(pageno, pagesize));
		
		List<String> testDates = new ArrayList<String>();
		Map<String, String> resultsLeft = new HashMap<String, String>();
		Map<String, String> resultsRight = new HashMap<String, String>();
		for (PerimetryTest test : perimetryExams) {
			testDates.add(test.getTestDateDisplay());
			ExamResult r = gson.fromJson(test.getResult(), ExamResult.class);
			appendResult(resultsLeft, r.getExamResultLeft());
			appendResult(resultsRight, r.getExamResultRight());
		}
		
		LinkedHashMap sortedResultsLeft = sortByPosCode(resultsLeft);
		LinkedHashMap sortedResultRight = sortByPosCode(resultsRight)
				;		
		TestsTimeline testsTimeline = new TestsTimeline(patientId, testDates, sortedResultsLeft, sortedResultRight);

		return new ResponseEntity<TestsTimeline>(testsTimeline, HttpStatus.OK);
	}

	private void appendResult(Map<String, String> results, Map<String, String> r) {
		for (Map.Entry<String, String> me: r.entrySet()) {
			String s = results.get(me.getKey());
			if (s == null) {
				s = me.getValue();
			} else {
				s = s+", "+me.getValue();
			}
			results.put(me.getKey(), s);
		}
	}
	

	private LinkedHashMap sortByPosCode(Map<String, ?> results) {
		
		List<String> posCodes = new ArrayList<String>(results.keySet());
		Collections.sort(posCodes, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {	        
				Matcher m1 = POS_CODE_PATTERN.matcher(o1);
				Matcher m2 = POS_CODE_PATTERN.matcher(o2);		
				
			    if (!m1.find()) {
			          new RuntimeException("matcher1 not found!");
			        }

			    if (!m2.find()) {
			          new RuntimeException("matcher2 not found!");
			        }
			    
				int q1 = Integer.parseInt(m1.group(1));
				int r1 = Integer.parseInt(m1.group(2));
				int c1 = Integer.parseInt(m1.group(3));
				
				int q2 = Integer.parseInt(m2.group(1));
				int r2 = Integer.parseInt(m2.group(2));
				int c2 = Integer.parseInt(m2.group(3));
				
				if (q1 == q2) {
					if (r1 == r2) {
						return c1-c2;
					} else {
						return r1-r2;
					}
				} else {
					return q1-q2;
				}
			}
			
		});
		
		LinkedHashMap sortedResults = new LinkedHashMap();
		for (String s : posCodes) {
			sortedResults.put(s, results.get(s));
		}

		return sortedResults;
	}

}
