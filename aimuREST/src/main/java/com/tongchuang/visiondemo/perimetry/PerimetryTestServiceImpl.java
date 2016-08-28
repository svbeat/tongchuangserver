package com.tongchuang.visiondemo.perimetry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			for (PerimetryTest t : tests) {
				ExamResult r = gson.fromJson(t.getResult(), ExamResult.class);
				updateInitDB(initDBLeft, r.getExamResultLeft());
				updateInitDB(initDBRight, r.getExamResultRight());
				latestResult = r;
			}
			
			Map<String, Integer> priorityLeft = patientSettings.getStimulusPrioritiesLeft();
			Map<String, Integer> priorityRight = patientSettings.getStimulusPrioritiesRight();
			resetPriority(priorityLeft, latestResult.getExamResultLeft());
			resetPriority(priorityRight, latestResult.getExamResultRight());
			
			patientSettings.setVersion(patientSettings.getVersion()+1);
			
			settings.setExamSettings(gson.toJson(patientSettings));
			
			patientExamSettingsRepository.save(settings);
		}
		
		Map<JobStatus, List<Integer>> result = new HashMap<JobStatus, List<Integer>>();
		result.put(JobStatus.DONE, successList);
		
		return result;
	}

	private void resetPriority(Map<String, Integer> priority, Map<String, String> examResult) {
		for (String pos : priority.keySet()) {
			priority.put(pos, 1);
		}
		
		// last rest is in-complete
		if (examResult.size() < priority.size()) {
			for (String pos : examResult.keySet()) {
				priority.put(pos, 2);
			}
		}
	}

	private void updateInitDB(Map<String, Integer> initDB, Map<String, String> examResult) {
		if (examResult == null) {
			return;
		}
		for (Map.Entry<String, String> me :examResult.entrySet()) {
			String r = me.getValue();
			int db = 0;
			try {
				db = Integer.parseInt(r);
			} catch (Exception e) {
				db = Integer.parseInt(r.substring(0, r.length()-1));
			}
			if (db > 0) {
				initDB.put(me.getKey(), db);
			}
		}		
	}
}
