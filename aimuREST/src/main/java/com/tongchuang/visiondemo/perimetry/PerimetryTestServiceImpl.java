package com.tongchuang.visiondemo.perimetry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tongchuang.visiondemo.ApplicationConstants.ExamCode;
import com.tongchuang.visiondemo.job.JobConstants.JobStatus;
import com.tongchuang.visiondemo.job.JobPendingRepository;
import com.tongchuang.visiondemo.job.PerimetryPendingDetails;
import com.tongchuang.visiondemo.job.entity.JobPending;
import com.tongchuang.visiondemo.patient.PatientExamSettingsLogRepository;
import com.tongchuang.visiondemo.patient.PatientExamSettingsRepository;
import com.tongchuang.visiondemo.patient.dto.PatientSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettingsLog;
import com.tongchuang.visiondemo.perimetry.entity.PerimetryTest;

@Service
public class PerimetryTestServiceImpl implements PerimetryTestService {
	private static final String DEFAULT_PATIENT_ID = "0";

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private PatientExamSettingsRepository patientExamSettingsRepository;
	private PatientExamSettingsLogRepository patientExamSettingsLogRepository;
	private PerimetryTestRepository perimetryTestRepository;
	
	
	
    @Autowired
    public PerimetryTestServiceImpl(PatientExamSettingsRepository patientExamSettingsRepository,
    					PerimetryTestRepository perimetryTestRepository,
    					PatientExamSettingsLogRepository patientExamSettingsLogRepository) {
        this.patientExamSettingsRepository = patientExamSettingsRepository;
        this.perimetryTestRepository = perimetryTestRepository;
        this.patientExamSettingsLogRepository = patientExamSettingsLogRepository;
    }
    
	/* (non-Javadoc)
	 * @see com.tongchuang.visiondemo.perimetry.PerimetryService#processTests(java.util.List)
	 */
	@Override
	public Map<JobStatus, List<Integer>> processTests(List<JobPending> pendingJobs) {
		if (pendingJobs == null || pendingJobs.isEmpty()) {
			return Collections.EMPTY_MAP;
		}
		
		List<Integer> successList = new ArrayList<Integer>();
		List<Integer> failedList = new ArrayList<Integer>();
		Map<Long, Integer> testId2JobId = new HashMap<Long, Integer>();
		Gson gson = new Gson();
		Map<String, List<Long>> patientTests = new HashMap<String, List<Long>>();
		for (JobPending p : pendingJobs) {
			PerimetryPendingDetails d = gson.fromJson(p.getDetails(), PerimetryPendingDetails.class);
			String patientId = d.getPatientId();
			List<Long> testIds = patientTests.get(patientId);
			if (testIds == null) {
				testIds = new ArrayList<Long>();
				patientTests.put(patientId, testIds);
			}
			testIds.add(d.getTestId());
			testId2JobId.put(d.getTestId(), p.getJobPendingId());
		}

		for (String patientId : patientTests.keySet()) {
			PatientExamSettings settings = patientExamSettingsRepository.findSetting(ExamCode.PERIMETRY.name(), patientId);
			
			boolean firstVersion = false;
			if (settings == null) { 
				// no setting for this patient, get the default one.
				settings = patientExamSettingsRepository.findSetting(ExamCode.PERIMETRY.name(), DEFAULT_PATIENT_ID);
				firstVersion = true;
			}
			
			List<PerimetryTest> tests = perimetryTestRepository.getByPatientTestIds(patientId, patientTests.get(patientId));
			
			try {
				PatientSettings patientSettings = gson.fromJson(settings.getExamSettings(), PatientSettings.class);
				
				PatientExamSettingsLog settingLog = new PatientExamSettingsLog();
				settingLog.setExamCode(ExamCode.PERIMETRY.name());
				settingLog.setExamSettings(settings.getExamSettings());
				settingLog.setPatientId(patientId);
				settingLog.setVersion(patientSettings.getVersion());
				
				Map<String, Integer> initDBLeft = patientSettings.getInitStimulusDBLeft();
				Map<String, Integer> initDBRight = patientSettings.getInitStimulusDBRight();
				
				ExamResult latestResult = null;
				int initDBLeftUpated = 0;
				int initDBRightUpated = 0;
				int priorityLeftUpated = 0;
				int priorityRightUpated = 0;
				for (PerimetryTest t : tests) {
					ExamResult r = gson.fromJson(t.getResult(), ExamResult.class);
					initDBLeftUpated = updateInitDB(initDBLeft, r.getExamResultLeft());
					initDBRightUpated = updateInitDB(initDBRight, r.getExamResultRight());
					latestResult = r;
				}
				
				Map<String, Integer> priorityLeft = patientSettings.getStimulusPrioritiesLeft();
				Map<String, Integer> priorityRight = patientSettings.getStimulusPrioritiesRight();
				priorityLeftUpated = resetPriority(priorityLeft, latestResult.getExamResultLeft(), latestResult.getAllResponsesLeft());
				priorityRightUpated = resetPriority(priorityRight, latestResult.getExamResultRight(), latestResult.getAllResponsesRight());
				
				if (firstVersion) {
					patientSettings.setVersion(1);				
					settings.setExamSettings(gson.toJson(patientSettings));				
					patientExamSettingsRepository.save(settings);						
				} else {
					if (initDBLeftUpated>0 || initDBRightUpated>0 || priorityLeftUpated>0 || priorityRightUpated>0) {
						patientSettings.setVersion(patientSettings.getVersion()+1);				
						settings.setExamSettings(gson.toJson(patientSettings));				
						patientExamSettingsRepository.save(settings);	
						patientExamSettingsLogRepository.save(settingLog);
						
						logger.info("patient settings updated. initDBLeftUpated="+initDBLeftUpated+"; initDBRightUpated="+initDBRightUpated+
								"; priorityLeftUpated"+priorityLeftUpated+"; priorityRightUpated"+priorityRightUpated);
					} else {
						logger.info("no change on patient settings.");
					}						
				}
	
				
				for (PerimetryTest t : tests) {
					successList.add(testId2JobId.get(t.getTestId()));
				}
			} catch(Exception e) {
				e.printStackTrace();
				for (PerimetryTest t : tests) {
					failedList.add(testId2JobId.get(t.getTestId()));
				}
			}


		}
		
		Map<JobStatus, List<Integer>> result = new HashMap<JobStatus, List<Integer>>();
		result.put(JobStatus.DONE, successList);
		result.put(JobStatus.FAILED, failedList);
		return result;
	}

	private int resetPriority(Map<String, Integer> priority, Map<String, String> examResult, Map<String, String> allResponses) {
		int updatedCount = 0;
		for (String pos : examResult.keySet()) {
			if (examResult.get(pos).endsWith("?") || allResponses.get(pos)==null||allResponses.get(pos).isEmpty()) {
				//test is not complete on this position
				if (priority.get(pos) != 1) {
					priority.put(pos, 1);	
					updatedCount++;
				} 				
			} else {
				if (priority.get(pos) != 2) {
					priority.put(pos, 2);	
					updatedCount++;
				}
			}
		}
		
		return updatedCount;
	}

	private int updateInitDB(Map<String, Integer> initDB, Map<String, String> examResult) {
		int updatedCount = 0;
		if (examResult == null) {
			return updatedCount;
		}
		for (Map.Entry<String, String> me :examResult.entrySet()) {
			String r = me.getValue();
			int db = 0;
			try {
				db = Integer.parseInt(r);
			} catch (Exception e) {
				db = Integer.parseInt(r.substring(0, r.length()-1));
			}
			if (db > 0 && initDB.get(me.getKey()) != db) {
				initDB.put(me.getKey(), db);
				updatedCount++;
			}
		}	
		return updatedCount;
	}
}
