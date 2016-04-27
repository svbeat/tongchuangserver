package com.tongchuang.visiondemo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class ApplicationController {
	private final static String SUPER_API_KEY="rock2016";

	private final PerimetryTestRepository perimetryTestRepository;

	@RequestMapping(value = "/{patientId}/perimetrytests", method = RequestMethod.POST)
	public ResponseEntity<Void> addPerimetryTest(@PathVariable String patientId, @RequestParam("apiKey") String apiKey, @RequestBody PerimetryTest exam, UriComponentsBuilder ucBuilder) {
		if (!SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		
		perimetryTestRepository.save(exam);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/perimetrytests/{id}").buildAndExpand(exam.getTestId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}


	@RequestMapping(value = "/{patientId}/perimetrytests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PerimetryTest>> getPerimetryTest(@PathVariable("patientId") String patientId, @RequestParam("apiKey") String apiKey) {
		
		if (!SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<List<PerimetryTest>>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Fetching PerimetryTest by patientId " + patientId);
		List<PerimetryTest> perimetryExams = perimetryTestRepository.findByPatientId(patientId);
		return new ResponseEntity<List<PerimetryTest>>(perimetryExams, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/perimetrytests/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PerimetryTest> getPerimetryTest(@PathVariable("id") long id, @RequestParam("apiKey") String apiKey) {
		
		if (!SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<PerimetryTest>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Fetching PerimetryTest with id " + id);
		PerimetryTest perimetryExam = perimetryTestRepository.findOne(id);
		return new ResponseEntity<PerimetryTest>(perimetryExam, HttpStatus.OK);
	}


	@RequestMapping(value = "/perimetrytests/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deletePerimetryTest(@PathVariable("id") long id, @RequestParam("apiKey") String apiKey) {
		
		if (!SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Fetching PerimetryTest with id " + id);
		PerimetryTest perimetryTest = perimetryTestRepository.findOne(id);
		perimetryTest.setLastUpdateDate(null);
		perimetryTest.setDeleted("Y");
		perimetryTestRepository.save(perimetryTest);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@Autowired
	public ApplicationController(PerimetryTestRepository perimetryExamRepository) {
		this.perimetryTestRepository = perimetryExamRepository;
	}

}