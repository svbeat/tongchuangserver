package com.tongchuang.visiondemo.device.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tongchuang.visiondemo.ApplicationConstants.EntityStatus;

@Entity
@Table(name = "device")
public class Device {
	@Id
	private String	deviceId;
	
	private String	deviceType;
	private String	deviceSettings;
	
    @Enumerated(EnumType.STRING)
    private EntityStatus	status = EntityStatus.ACTIVE;
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public String getDeviceSettings() {
		return deviceSettings;
	}
	public void setDeviceSettings(String deviceSettings) {
		this.deviceSettings = deviceSettings;
	}
	public EntityStatus getStatus() {
		return status;
	}
	public void setStatus(EntityStatus status) {
		this.status = status;
	}
	
	
}
