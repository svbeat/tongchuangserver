package com.aimu.visiontest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimu.visiontest.database.VisionTestOutcomeRepository;
import com.aimu.visiontest.database.VisionTestRunRepository;
import com.aimu.visiontest.database.entity.VisionTestOutcome;
import com.aimu.visiontest.database.entity.VisionTestRun;
import com.aimu.visiontest.dto.TestRunDto;
import com.aimu.visiontest.response.TestResultResponse;
import com.aimu.visiontest.service.VisionTestService;

@Service
public class VisionTestServiceImpl implements VisionTestService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final int COUNT_BY_AGE_THRESHOLD = 5;
	private final VisionTestRunRepository visionTestRunRepository;
	private final VisionTestOutcomeRepository visionTestOutcomeRepository;

	private Map<Integer, List<VisionTestOutcome>> testOutcomes;

	private String[] standardVision={"3.7- (0.05-)", "3.7 (0.05)", "3.8 (0.06)", "3.9 (0.08)", "4.0 (0.1)",
			"4.1 (0.12)", "4.2 (0.15)", "4.3 (0.2)", "4.4 (0.25)", "4.5 (0.3)", "4.6 (0.4)", "4.7 (0.5)", "4.8 (0.6)",
			"4.9 (0.8)", "5.0 (1.0)", "5.1 (1.2)", "5.1+ (1.2+)"};
	@Autowired
	public VisionTestServiceImpl(VisionTestRunRepository visionTestRunRepository, VisionTestOutcomeRepository visionTestOutcomeRepository) {
		this.visionTestRunRepository = visionTestRunRepository;
		this.visionTestOutcomeRepository = visionTestOutcomeRepository;
	}

	/* (non-Javadoc)
	 * @see com.aimu.visiontest.service.impl.VisionTestService#createTestRun(com.aimu.visiontest.dto.TestRunDto)
	 */
	@Override
	public TestResultResponse createTestRun(VisionTestRun testRun) {
		if (testRun.getData() != null) {
			calcTestScore(testRun, testRun.getData());
		}
		visionTestRunRepository.save(testRun);
		TestResultResponse response = new TestResultResponse(testRun.getTestRunId());
		if (testRun.getData() != null) {
			populateOutcomeMessage(testRun, response);	
		}
		return response;
	}
	
	private void populateOutcomeMessage(VisionTestRun testRun, TestResultResponse response) {
		
		int score = testRun.getScore();
		response.setScore(score);
		response.setVision(testRun.getVision());
		List<VisionTestOutcome> outcomes = getTestOutcomes(testRun.getTestId());
		for (VisionTestOutcome o : outcomes) {
			if (o.getScoreRangeMax() > score && o.getScoreRangeMin() <= score) {
				response.setOutcome(o.getOutcome());
				response.setMessage(o.getMessage());
				break;
			}
		}
	}

	private void ensureTestOutcomes() {
		if (testOutcomes != null) {
			return;
		}
		
		List<VisionTestOutcome> outcomes = (List<VisionTestOutcome>) visionTestOutcomeRepository.findAll();
		Map<Integer, List<VisionTestOutcome>> testOutcomesHolder = new HashMap<Integer, List<VisionTestOutcome>>();
		for (VisionTestOutcome o : outcomes) {
			List<VisionTestOutcome> lo = testOutcomesHolder.get(o.getTestId());
			if (lo == null) {
				lo = new ArrayList<VisionTestOutcome>();
				testOutcomesHolder.put(o.getTestId(), lo);
			}
			lo.add(o);
		}
		testOutcomes = testOutcomesHolder;
	}

	private void calcTestScore(VisionTestRun testRun, String data) {
		int visionIndex = 0;
		
		if (data != null) {
			String[] levelValues = data.split(";");
			for (int i = levelValues.length; i>0; i--) {
				String[] values = levelValues[i-1].split(",");
				if (values.length == 3 && Integer.parseInt(values[2])==1) {
					visionIndex = Integer.parseInt(values[0]);
					break;
				}
			}
		}
		
		int score = visionIndex*5;
		if (visionIndex < 0) {
			visionIndex = 0;
		} else if (visionIndex > standardVision.length) {
			visionIndex = standardVision.length-1;
		}
		
		testRun.setScore(score);
		testRun.setVision(standardVision[visionIndex]);
	}

	/* (non-Javadoc)
	 * @see com.aimu.visiontest.service.impl.VisionTestService#updateTestRunData(long, java.lang.String)
	 */
	@Override
	public TestResultResponse updateTestRunData(long testRunId, String data) {
		log.info("updateTestRunData: testRunId="+testRunId);
		VisionTestRun testRun = visionTestRunRepository.findOne(testRunId);
		testRun.setData(data);
		calcTestScore(testRun, data);
		testRun.setLastUpdateDate(null);
		visionTestRunRepository.save(testRun);		

		TestResultResponse response = new TestResultResponse(testRun.getTestRunId());
		populateOutcomeMessage(testRun, response);
		
		return response;
	}	
	
	/* (non-Javadoc)
	 * @see com.aimu.visiontest.service.impl.VisionTestService#updateTestRunAge(long, int)
	 */
	@Override
	public TestResultResponse updateTestRunAge(long testRunId, int age) {
		log.info("updateTestRunData: testRunId="+testRunId);
		VisionTestRun testRun = visionTestRunRepository.findOne(testRunId);
		testRun.setSubjectAge(age);
		testRun.setLastUpdateDate(null);
		visionTestRunRepository.save(testRun);		
		TestResultResponse response = new TestResultResponse(testRun.getTestRunId());	
		
		int runCountByAge = visionTestRunRepository.findRunCountByAge(age);
		int runCountByAgeScore = visionTestRunRepository.findRunCountByAgeScore(testRun.getScore(), age);
		if (runCountByAgeScore>=runCountByAge) {
			runCountByAgeScore = runCountByAge-1;
		}
		int score = runCountByAgeScore*100/runCountByAge;
		List<VisionTestOutcome> outcomes = getTestOutcomes(0);
		for (VisionTestOutcome o : outcomes) {
			if (runCountByAge <= COUNT_BY_AGE_THRESHOLD) {
				if (o.getScoreRangeMax() < 0) {
					response.setOutcome(o.getOutcome());
					response.setMessage(String.format(o.getMessage(), runCountByAge, runCountByAgeScore));
					break;					
				}
			} else if (o.getScoreRangeMax() > score && o.getScoreRangeMin() <= score) {
				response.setOutcome(o.getOutcome());
				response.setMessage(String.format(o.getMessage(), score));
				break;
			}
		}
		return response;		
	}
	
	private List<VisionTestOutcome> getTestOutcomes(int testId) {
		ensureTestOutcomes();
		return testOutcomes.get(testId);
	}

	/* (non-Javadoc)
	 * @see com.aimu.visiontest.service.impl.VisionTestService#getTestRun(long, boolean)
	 */
	@Override
	public TestResultResponse getTestRun(long testRunId, boolean resultOnly) {
		VisionTestRun testRun = visionTestRunRepository.findOne(testRunId);
		
		TestResultResponse response = new TestResultResponse(testRun.getTestId());
		if (testRun.getData() != null) {
			populateOutcomeMessage(testRun, response);
		}
		return response;
	}
}
