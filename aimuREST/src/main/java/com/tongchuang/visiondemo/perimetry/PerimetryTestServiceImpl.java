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
import com.tongchuang.visiondemo.patient.PatientExamSettingsRepository;
import com.tongchuang.visiondemo.patient.dto.PatientSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettings;

@Service
public class PerimetryTestServiceImpl implements PerimetryTestService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private PatientExamSettingsRepository patientExamSettingsRepository;
	private PerimetryTestRepository perimetryTestRepository;
	
	
	
    @Autowired
    public PerimetryTestServiceImpl(PatientExamSettingsRepository patientExamSettingsRepository,
    					PerimetryTestRepository perimetryTestRepository) {
        this.patientExamSettingsRepository = patientExamSettingsRepository;
        this.perimetryTestRepository = perimetryTestRepository;
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
			successList.add(p.getJobPendingId());
		}

		for (String patientId : patientTests.keySet()) {
			PatientExamSettings settings = patientExamSettingsRepository.findSetting(ExamCode.PERIMETRY.name(), patientId);
			List<PerimetryTest> tests = perimetryTestRepository.getByPatientTestIds(patientId, patientTests.get(patientId));
			
			PatientSettings patientSettings = gson.fromJson(settings.getExamSettings(), PatientSettings.class);
			
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
			priorityLeftUpated = resetPriority(priorityLeft, latestResult.getExamResultLeft());
			priorityRightUpated = resetPriority(priorityRight, latestResult.getExamResultRight());
			
			if (initDBLeftUpated>0 || initDBRightUpated>0 || priorityLeftUpated>0 || priorityRightUpated>0) {
				patientSettings.setVersion(patientSettings.getVersion()+1);				
				settings.setExamSettings(gson.toJson(patientSettings));				
				patientExamSettingsRepository.save(settings);	
				logger.info("patient settings updated.");
			} else {
				logger.info("no change on patient settings.");
			}

		}
		
		Map<JobStatus, List<Integer>> result = new HashMap<JobStatus, List<Integer>>();
		result.put(JobStatus.DONE, successList);
		
		return result;
	}

	private int resetPriority(Map<String, Integer> priority, Map<String, String> examResult) {
		int updatedCount = 0;
		for (String pos : priority.keySet()) {
			if (priority.get(pos) != 1) {
				priority.put(pos, 1);	
				updatedCount++;
			}
			
		}
		
		// last rest is in-complete
		if (examResult.size() < priority.size()) {
			for (String pos : examResult.keySet()) {
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
