package com.tongchuang.visiondemo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.tongchuang.visiondemo.device.dto.DeviceSettings;
import com.tongchuang.visiondemo.device.entity.Device;
import com.tongchuang.visiondemo.patient.PatientExamSettingsRepository;
import com.tongchuang.visiondemo.patient.dto.PatientSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettings;
import com.tongchuang.visiondemo.user.UserInfoRepository;
import com.tongchuang.visiondemo.user.UserUtil;
import com.tongchuang.visiondemo.user.dto.UserInfo;

@RestController
@CrossOrigin
public class UserController {
	private UserInfoRepository 	userInfoRepository;
	private PatientExamSettingsRepository patientExamSettingsRepository;
	
	private Gson				gson = new Gson();
	
	
	@Autowired
	public UserController(PatientExamSettingsRepository patientExamSettingsRepository) {
		//this.userInfoRepository = userInfoRepository;
		this.patientExamSettingsRepository = patientExamSettingsRepository;
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserInfo> getUserInfoByBarcode(@RequestParam("barcode") String barcode, @RequestParam("apiKey") String apiKey) {
		
		if (!ApplicationController.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<UserInfo>(HttpStatus.UNAUTHORIZED);
		}
		// ToDo: to be revised
		UserInfo userInfo = UserUtil.getUserInfoByBarcode(barcode);
		return new ResponseEntity<UserInfo>(userInfo, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/defaults/examsettings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PatientSettings> getDefaultSettings(@RequestParam("examCode") String examCode, @RequestParam("apiKey") String apiKey) {
		
		if (!ApplicationController.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<PatientSettings>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Fetching Default patient settings");
		PatientExamSettings settings = patientExamSettingsRepository.findSetting(examCode, 0);
		if (settings==null) {
			return new ResponseEntity<PatientSettings>(HttpStatus.NOT_FOUND);
		}
		PatientSettings patientSettings = gson.fromJson(settings.getExamSettings(),  PatientSettings.class);
		return new ResponseEntity<PatientSettings>(patientSettings, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/patients/{patientid}/examsettings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PatientSettings> getPatientSettings(@PathVariable("patientid") String patientId,
			@RequestParam("examCode") String examCode, @RequestParam("apiKey") String apiKey) {
		
		if (!ApplicationController.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<PatientSettings>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Fetching patient settings for patient:"+patientId);
		PatientExamSettings settings = patientExamSettingsRepository.findSetting(examCode, Integer.valueOf(patientId));
		if (settings==null) {
			settings = patientExamSettingsRepository.findSetting(examCode, 0);
			if (settings == null)
				return new ResponseEntity<PatientSettings>(HttpStatus.NOT_FOUND);
		}
		PatientSettings patientSettings = gson.fromJson(settings.getExamSettings(),  PatientSettings.class);
		return new ResponseEntity<PatientSettings>(patientSettings, HttpStatus.OK);
	}
}
