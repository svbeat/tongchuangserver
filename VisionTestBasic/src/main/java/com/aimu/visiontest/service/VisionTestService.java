package com.aimu.visiontest.service;

import com.aimu.visiontest.database.entity.VisionTestRun;
import com.aimu.visiontest.dto.TestRunDto;
import com.aimu.visiontest.response.TestResultResponse;

public interface VisionTestService {

	TestResultResponse createTestRun(VisionTestRun visionTestRun);

	TestResultResponse updateTestRunData(long testRunId, String data);

	TestResultResponse updateTestRunAge(long testRunId, int age);

	TestResultResponse getTestRun(long testRunId, boolean resultOnly);

}