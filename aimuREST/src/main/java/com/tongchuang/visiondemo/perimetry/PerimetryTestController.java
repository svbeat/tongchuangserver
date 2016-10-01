package com.tongchuang.visiondemo.perimetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.google.gson.Gson;
import com.tongchuang.visiondemo.ApplicationConstants;
import com.tongchuang.visiondemo.ApplicationConstants.EntityDeleted;
import com.tongchuang.visiondemo.ApplicationConstants.ExamCode;
import com.tongchuang.visiondemo.device.Calibration;
import com.tongchuang.visiondemo.device.CalibrationRepository;
import com.tongchuang.visiondemo.doctor.entity.Doctor;
import com.tongchuang.visiondemo.job.PerimetryPendingDetails;
import com.tongchuang.visiondemo.job.JobConstants.JobCode;
import com.tongchuang.visiondemo.job.JobPendingRepository;
import com.tongchuang.visiondemo.job.entity.JobPending;
import com.tongchuang.visiondemo.notification.Notification;
import com.tongchuang.visiondemo.notification.NotificationRepository;
import com.tongchuang.visiondemo.patient.PatientExamSettingsLogRepository;
import com.tongchuang.visiondemo.patient.PatientExamSettingsRepository;
import com.tongchuang.visiondemo.patient.dto.PatientSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettingsLog;
import com.tongchuang.visiondemo.perimetry.PerimetryTest;
import com.tongchuang.visiondemo.perimetry.PerimetryTestRepository;

@RestController
@CrossOrigin
public class PerimetryTestController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public final static String SUPER_API_KEY="rock2016";

	private final PerimetryTestRepository perimetryTestRepository;
	private final PatientExamSettingsRepository patientExamSettingsRepository;
	private final PatientExamSettingsLogRepository patientExamSettingsLogRepository;
	private final JobPendingRepository jobPendingRepository;
	private Gson gson = new Gson();


	@Autowired
	public PerimetryTestController(PerimetryTestRepository perimetryExamRepository, 
								 CalibrationRepository calibrationRepository,
								 NotificationRepository notificationRepository,
								 JobPendingRepository jobPendingRepository,
								 PatientExamSettingsRepository patientExamSettingsRepository,
			    				 PatientExamSettingsLogRepository patientExamSettingsLogRepository) {
		this.perimetryTestRepository = perimetryExamRepository;
        this.patientExamSettingsRepository = patientExamSettingsRepository;
        this.patientExamSettingsLogRepository = patientExamSettingsLogRepository;
		this.jobPendingRepository = jobPendingRepository;
	}

	
	@RequestMapping(value = "/{patientId}/perimetrytests", method = RequestMethod.POST)
	public ResponseEntity<Void> addPerimetryTest(@PathVariable String patientId, @RequestParam("apiKey") String apiKey, @RequestBody PerimetryTest exam, UriComponentsBuilder ucBuilder) {
		return createPerimetryTest(patientId, apiKey, exam, ucBuilder);
	}

	@RequestMapping(value = "/patients/{patientId}/tests", method = RequestMethod.POST)
	public ResponseEntity<Void> postPerimetryTest(@PathVariable String patientId, @RequestParam("apiKey") String apiKey, @RequestBody PerimetryTest exam, UriComponentsBuilder ucBuilder) {
		return createPerimetryTest(patientId, apiKey, exam, ucBuilder);
	}

	public ResponseEntity<Void> createPerimetryTest(String patientId, String apiKey, PerimetryTest exam, UriComponentsBuilder ucBuilder) {
		if (!SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}

		ExamResult eResult = gson.fromJson(exam.getResult(), ExamResult.class);
		PatientSettings pSettings = getPatientSettings(patientId, Integer.parseInt(eResult.getPatientSettingsVersion()));
		if (pSettings != null && pSettings.getAggrAnalysis() != null) {
			List<ExamResult> pastResults = getPastResults(patientId, pSettings.getAggrAnalysis());
			logger.info("patientId="+patientId+"; AggrAnalysis="+pSettings.getAggrAnalysis()
							+"; pastResults.size="+(pastResults==null?0:pastResults.size()));
			logger.info("orig_result_left="+gson.toJson(eResult.getExamResultLeft()));
			logger.info("orig_result_right="+gson.toJson(eResult.getExamResultRight()));
			aggrAnalysis(eResult, pastResults);
			logger.info("new_result_left="+gson.toJson(eResult.getExamResultLeft()));
			logger.info("new_result_right="+gson.toJson(eResult.getExamResultRight()));
			exam.setResult(gson.toJson(eResult));
		}
		
		exam = perimetryTestRepository.save(exam);
		
		PerimetryPendingDetails pendingDetails =  new PerimetryPendingDetails();
		pendingDetails.setPatientId(patientId);
		pendingDetails.setTestId(exam.getTestId());
		JobPending jobPending  = new JobPending();

		jobPending.setDetails(gson.toJson(pendingDetails));
		jobPending.setJobCode(JobCode.PERIMETRY_POSTPROCESS);
		jobPendingRepository.save(jobPending);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/perimetrytests/{id}").buildAndExpand(exam.getTestId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	private void aggrAnalysis(ExamResult eResult, List<ExamResult> pastResults) {
		if (pastResults == null || pastResults.isEmpty()) {
			return;
		}
		
		List<Map<String, String>> pastLeft = new ArrayList<Map<String, String>>();
		List<Map<String, String>> pastRight = new ArrayList<Map<String, String>>();
		for (ExamResult r : pastResults) {
			pastLeft.add(r.getAllResponsesLeft());
			pastRight.add(r.getAllResponsesRight());
		}
		pastLeft.add(eResult.getAllResponsesLeft());
		pastRight.add(eResult.getAllResponsesRight());
		
		processAggr(eResult.getExamResultLeft(), pastLeft);
		processAggr(eResult.getExamResultRight(), pastRight);

	}


	private void processAggr(Map<String, String> currResponse, List<Map<String, String>> pastResponses) {

		
		for (Map.Entry<String, String> me : currResponse.entrySet()) {
			int maxSeen = -1;
			int maxUnSeen = -1;
			for (Map<String, String> p : pastResponses) {
				String pr = p.get(me.getKey());
				if (pr != null) {
					String[] dvList = pr.split(";");
					for (String dv : dvList) {
						String[] d = dv.split(":");
						int v = Integer.parseInt(d[0]);
						if ("0".equals(d[1]) && v > maxUnSeen) {
							maxUnSeen = v;
						} else if ("1".equals(d[1]) && v > maxSeen) {
							maxSeen = v;
						}
					}
				}				
			}
			
			if (maxSeen > 0 && maxUnSeen > 0) {
				currResponse.put(me.getKey(), Integer.toString((maxSeen+maxUnSeen)/2));
			}
		}
	}


	private List<ExamResult> getPastResults(String patientId, String aggrAnalysis) {
		
		List<ExamResult> results = null;
		
		List<PerimetryTest> tests  = null;
		try {
			if (aggrAnalysis.endsWith("d")) {
				int days = Integer.parseInt(aggrAnalysis.substring(0, aggrAnalysis.length()-1));
				tests = perimetryTestRepository.getPastTests(patientId, days);
			} else {
				int numRecords = Integer.parseInt(aggrAnalysis);
				PageRequest pageRequest = new PageRequest(0, numRecords);
				tests = perimetryTestRepository.findByPatientId(patientId, pageRequest);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (tests != null) {
			results = new ArrayList<ExamResult>();
			for (PerimetryTest t : tests) {
				results.add(gson.fromJson(t.getResult(), ExamResult.class));
			}
		}

		return results;
	}


	private PatientSettings getPatientSettings(String patientId, int version) {
		PatientExamSettings settings = patientExamSettingsRepository.findSetting(ExamCode.PERIMETRY.name(), patientId);
		
		PatientSettings patientSettings = gson.fromJson(settings.getExamSettings(), PatientSettings.class);
		
		if (patientSettings.getVersion() != null && patientSettings.getVersion()==version) {
			return patientSettings;
		}
		
		PatientExamSettingsLog oldSettings = patientExamSettingsLogRepository.findSetting(ExamCode.PERIMETRY.name(), 
												patientId, version);
		if (oldSettings != null) {
			patientSettings = gson.fromJson(oldSettings.getExamSettings(), PatientSettings.class);
			
			return patientSettings;
		}
		return null;
	}

	@RequestMapping(value = "/perimetrytests/{testId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deletePerimetryTest(@PathVariable("testId") Long testId, @RequestParam("apiKey") String apiKey) {
		
		logger.info("deletePerimetryTest: testId="+testId);

		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}

		PerimetryTest test = perimetryTestRepository.findOne(testId);
		if (test == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); 
		}
		
		test.setLastUpdateDate(null);
		test.setDeleted(EntityDeleted.Y.name());
		perimetryTestRepository.save(test);
		
		// check if the deleted test was the latest one
		PageRequest pageRequest = new PageRequest(0, 1);
		List<PerimetryTest> tests = perimetryTestRepository.findByPatientId(test.getPatientId(), pageRequest);
		if (tests != null && tests.size()==1 && tests.get(0).getTestId() < test.getTestId()) {
			logger.info("reset setting for patientId="+test.getPatientId()+" with testId="+tests.get(0).getTestId());
			PerimetryPendingDetails pendingDetails =  new PerimetryPendingDetails();
			pendingDetails.setPatientId(test.getPatientId());
			pendingDetails.setTestId(tests.get(0).getTestId());
			JobPending jobPending  = new JobPending();

			jobPending.setDetails(gson.toJson(pendingDetails));
			jobPending.setJobCode(JobCode.PERIMETRY_POSTPROCESS);
			jobPendingRepository.save(jobPending);
		}
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}