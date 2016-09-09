package com.tongchuang.visiondemo.device;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

import com.google.gson.Gson;
import com.tongchuang.visiondemo.ApplicationConstants;
import com.tongchuang.visiondemo.ApplicationController;
import com.tongchuang.visiondemo.ApplicationConstants.EntityDeleted;
import com.tongchuang.visiondemo.ApplicationConstants.EntityStatus;
import com.tongchuang.visiondemo.common.ResponseList;
import com.tongchuang.visiondemo.device.dto.DeviceSettings;
import com.tongchuang.visiondemo.device.entity.Device;
import com.tongchuang.visiondemo.doctor.entity.Doctor;

@RestController
@CrossOrigin
public class DeviceController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
	

	@RequestMapping(value = "/devices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseList<Device>> getDevices( 
				@RequestParam("apiKey") String apiKey, 
				@RequestParam(value = "filter", required=false) String filter,
				@RequestParam(value = "returnTotal", required=false, defaultValue = "false") boolean returnTotal,
				@RequestParam(value = "orderBy", required=false) String orderBy,
				@RequestParam(value = "pageno", required=false, defaultValue = "0") int pageno, 
				@RequestParam(value = "pagesize", required=false, defaultValue = "10") int pagesize) {
		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<ResponseList<Device>>(HttpStatus.UNAUTHORIZED);
		}
		logger.info("getDevices: filter="+filter+"; pageno="+pageno+"; pagesize="+pagesize);
		List<Device> devices = deviceRepository.getDevices(new PageRequest(pageno, pagesize));
		ResponseList<Device> result = new ResponseList<Device>(devices);
		Integer total = null;
		if (returnTotal) {
			total = deviceRepository.getTotalCount();
			result.setTotalCounts(total);
		}
		return new ResponseEntity<ResponseList<Device>>(result, HttpStatus.OK);
	}
	

	@RequestMapping(value = "/devices", method = RequestMethod.POST)
	public ResponseEntity<Device> createDevice(@RequestParam("apiKey") String apiKey, @RequestBody Device device) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Device>(HttpStatus.UNAUTHORIZED);
		}
		device = deviceRepository.save(device);
		return new ResponseEntity<Device>(device, HttpStatus.CREATED);
	}
		
	@RequestMapping(value = "/devices/{deviceId}", method = RequestMethod.POST)
	public ResponseEntity<Device> updateDevice(@PathVariable("deviceId")String deviceId,
							@RequestParam("apiKey") String apiKey, 
							@RequestBody Device device) {
		
		logger.info("updateDevice: deviceId="+deviceId);

		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Device>(HttpStatus.UNAUTHORIZED);
		}
		
		Device origDevice = deviceRepository.findOne(deviceId);
		if (origDevice == null) {
			return new ResponseEntity<Device>(HttpStatus.NOT_FOUND); 
		}
		device.setDeviceId(deviceId);
		deviceRepository.save(device);
		device = deviceRepository.findOne(deviceId);
		return new ResponseEntity<Device>(device, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/devices/{deviceId}", method = RequestMethod.GET)
	public ResponseEntity<Device> getDevice(@PathVariable("deviceId")String deviceId, @RequestParam("apiKey") String apiKey) {
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Device>(HttpStatus.UNAUTHORIZED);
		}
		Device device = deviceRepository.findOne(deviceId);
		if (device == null) {
			return new ResponseEntity<Device>(HttpStatus.NOT_FOUND); 
		}
		return new ResponseEntity<Device>(device, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/devices/{deviceId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteDevice(@PathVariable("deviceId") String deviceId, @RequestParam("apiKey") String apiKey) {
		
		logger.info("deleteDevice: deviceId="+deviceId);

		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}

		Device device = deviceRepository.findOne(deviceId);
		if (device == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); 
		}
		
		device.setStatus(EntityStatus.DELETED);
		deviceRepository.save(device);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	

}
