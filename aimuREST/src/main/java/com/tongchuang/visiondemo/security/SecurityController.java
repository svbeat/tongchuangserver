package com.tongchuang.visiondemo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.tongchuang.visiondemo.ApplicationConstants;
import com.tongchuang.visiondemo.patient.PatientExamSettingsRepository;
import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.user.UserRepository;
import com.tongchuang.visiondemo.user.UserRoleRepository;
import com.tongchuang.visiondemo.user.dto.User;

@RestController
@CrossOrigin
public class SecurityController {

	private static int			TIMEOUT_IN_SEC = 10*60*1000; //10min
	
	private UserRepository 	userRepository;
	private JwtUtil				jwtUtil = new JwtUtil();
	
	@Autowired
	public SecurityController(UserRepository 	userRepository) {
		this.userRepository = userRepository;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
		
		User user = userRepository.getUser(authRequest.getUserName(), authRequest.getPassword());
		if (user == null) {
			return new ResponseEntity<AuthResponse>(HttpStatus.OK); 
		}
		
		AuthResponse response = new AuthResponse();
		response.setToken(jwtUtil.generateToken(user));
		response.setExpiresTime(System.currentTimeMillis()+TIMEOUT_IN_SEC);
		return new ResponseEntity<AuthResponse>(response, HttpStatus.OK);
	}
}
