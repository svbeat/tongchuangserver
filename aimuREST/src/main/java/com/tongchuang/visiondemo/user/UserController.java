package com.tongchuang.visiondemo.user;

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
import com.tongchuang.visiondemo.ApplicationController;
import com.tongchuang.visiondemo.device.dto.DeviceSettings;
import com.tongchuang.visiondemo.device.entity.Device;
import com.tongchuang.visiondemo.patient.PatientExamSettingsRepository;
import com.tongchuang.visiondemo.patient.dto.PatientSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettings;
import com.tongchuang.visiondemo.user.dto.UserInfo;
import com.tongchuang.visiondemo.user.dto.UserRole;

@RestController
@CrossOrigin
public class UserController {
	private UserRoleRepository 	userRoleRepository;
	private PatientExamSettingsRepository patientExamSettingsRepository;
	
	private Gson				gson = new Gson();
	
	
	@Autowired
	public UserController(UserRoleRepository userRoleRepository, PatientExamSettingsRepository patientExamSettingsRepository) {
		this.userRoleRepository = userRoleRepository;
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
	
	@RequestMapping(value = "/userroles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserRole> getRoleByUserName(@RequestParam("username") String userName, @RequestParam("apiKey") String apiKey) {
		
		if (!ApplicationController.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<UserRole>(HttpStatus.UNAUTHORIZED);
		}
		UserRole role = userRoleRepository.getRoleByUserName(userName);
		return new ResponseEntity<UserRole>(role, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/defaults/examsettings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PatientSettings> getDefaultSettings(@RequestParam("examCode") String examCode, @RequestParam("apiKey") String apiKey) {
		
		if (!ApplicationController.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<PatientSettings>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Fetching Default patient settings");
		PatientExamSettings settings = patientExamSettingsRepository.findSetting(examCode, "0");
		if (settings==null) {
			return new ResponseEntity<PatientSettings>(HttpStatus.NOT_FOUND);
		}
		PatientSettings patientSettings = gson.fromJson(settings.getExamSettings(),  PatientSettings.class);
		return new ResponseEntity<PatientSettings>(patientSettings, HttpStatus.OK);
	}
	
}
