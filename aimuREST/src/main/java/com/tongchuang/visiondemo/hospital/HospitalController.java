package com.tongchuang.visiondemo.hospital;

import java.util.List;

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

import com.tongchuang.visiondemo.ApplicationConstants;
import com.tongchuang.visiondemo.ApplicationConstants.EntityDeleted;
import com.tongchuang.visiondemo.ApplicationConstants.EntityStatus;
import com.tongchuang.visiondemo.common.ResponseList;
import com.tongchuang.visiondemo.device.Calibration;
import com.tongchuang.visiondemo.doctor.entity.Doctor;
import com.tongchuang.visiondemo.patient.PatientRepository;
import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.perimetry.PerimetryTest;
import com.tongchuang.visiondemo.user.UserRoleRepository;
import com.tongchuang.visiondemo.util.ApplicationUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin
public class HospitalController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private HospitalRepository 	hospitalRepository;
	
	
	@Autowired
	public HospitalController(HospitalRepository hospitalRepository) {
		this.hospitalRepository = hospitalRepository;
	}


	@RequestMapping(value = "/hospitals", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseList<Hospital>> getHospitals( 
				@RequestParam("apiKey") String apiKey) {
		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<ResponseList<Hospital>>(HttpStatus.UNAUTHORIZED);
		}
		List<Hospital> hospitals = (List<Hospital>) hospitalRepository.findAll();
		ResponseList<Hospital> result = new ResponseList<Hospital>(hospitals);
			result.setTotalCounts(hospitals.size());
		
		return new ResponseEntity<ResponseList<Hospital>>(result, HttpStatus.OK);
	}
	
}
