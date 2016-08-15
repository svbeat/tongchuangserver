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
import com.tongchuang.visiondemo.device.DeviceRepository;
import com.tongchuang.visiondemo.device.dto.DeviceSettings;
import com.tongchuang.visiondemo.device.entity.Device;

@RestController
@CrossOrigin
public class DeviceController {
	private DeviceRepository 	deviceRepository;
	private Gson				gson = new Gson();
	
	
	@Autowired
	public DeviceController(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}

	@RequestMapping(value = "/devices/{deviceId}/examsettings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DeviceSettings> getDeviceSettings(@PathVariable("deviceId") String deviceId, @RequestParam("apiKey") String apiKey) {
		
		if (!ApplicationController.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<DeviceSettings>(HttpStatus.UNAUTHORIZED);
		}
		
		System.out.println("Fetching DeviceSettings for deviceId " + deviceId);
		Device device = deviceRepository.findOne(deviceId);
		if (device==null) {
			return new ResponseEntity<DeviceSettings>(HttpStatus.NOT_FOUND);
		}
		DeviceSettings deviceSettings = gson.fromJson(device.getDeviceSettings(),  DeviceSettings.class);
		return new ResponseEntity<DeviceSettings>(deviceSettings, HttpStatus.OK);
	}
}
