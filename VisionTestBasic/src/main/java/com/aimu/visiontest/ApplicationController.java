package com.aimu.visiontest;

import java.util.List;

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

import com.aimu.visiontest.database.entity.VisionTestRun;
import com.aimu.visiontest.dto.TestRunDto;
import com.aimu.visiontest.response.TestResultResponse;
import com.aimu.visiontest.service.VisionTestService;

@RestController
@CrossOrigin
@RequestMapping("/visiontest/basic")
public class ApplicationController {
	private final static String SUPER_API_KEY="aimu2016";
	private final static int	BASIC_TEST_ID=1;
	
	private final VisionTestService		visionTestService;
	

	@RequestMapping(value = "/testrun", method = RequestMethod.POST)
	public ResponseEntity<TestResultResponse> createBasicTestRun(@RequestParam("apiKey") String apiKey, 
								@RequestBody TestRunDto testRunDto) {
		if (!SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<TestResultResponse>(HttpStatus.UNAUTHORIZED);
		}
		
		TestResultResponse response = visionTestService.createTestRun(generateBasicTestRun(testRunDto));
		return new ResponseEntity<TestResultResponse>(response, HttpStatus.CREATED);
	}

	private VisionTestRun generateBasicTestRun(TestRunDto testRunDto) {
		VisionTestRun testRun = new VisionTestRun();
	    
		testRun.setDeviceId(testRunDto.getDeviceId());
		testRun.setSubjectType("WECHAT");
		testRun.setSubjectId(testRunDto.getWechatSubjectId());
		testRun.setData(testRunDto.getData());
		testRun.setTestId(BASIC_TEST_ID);
		testRun.setSubjectAge(testRunDto.getSubjectAge());
		return testRun;
	}
	
	@RequestMapping(value = "/testrun/{testRunId}/data", method = RequestMethod.POST)
	public ResponseEntity<TestResultResponse> updateTestRunData(@RequestParam("apiKey") String apiKey, 
							@PathVariable("testRunId") String testRunId,
							@RequestBody String payload) {
		if (!SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<TestResultResponse>(HttpStatus.UNAUTHORIZED);
		}
		
		TestResultResponse response = visionTestService.updateTestRunData(Long.parseLong(testRunId), payload);
		return new ResponseEntity<TestResultResponse>(response, HttpStatus.OK);
	}
	
	

	@RequestMapping(value = "/testrun/{testRunId}/age", method = RequestMethod.POST)
	public ResponseEntity<TestResultResponse> updateTestRunAge(@RequestParam("apiKey") String apiKey, 
							@PathVariable("testRunId") String testRunId,
							@RequestBody String payload) {
		if (!SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<TestResultResponse>(HttpStatus.UNAUTHORIZED);
		}
		
		TestResultResponse response = visionTestService.updateTestRunAge(Long.parseLong(testRunId), Integer.parseInt(payload));
		return new ResponseEntity<TestResultResponse>(response, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/testrun/{testRunId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestResultResponse> getTestRun(@PathVariable("testRunId") String testRunId, @RequestParam("apiKey") String apiKey) {
		
		if (!SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<TestResultResponse>(HttpStatus.UNAUTHORIZED);
		}
		
		TestResultResponse response = visionTestService.getTestRun(Long.parseLong(testRunId), false);
		return new ResponseEntity<TestResultResponse>(response, HttpStatus.OK);
	}
	
	

	@Autowired
	public ApplicationController(VisionTestService visionTestService) {
		this.visionTestService = visionTestService;
	}

}