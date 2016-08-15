package com.tongchuang.visiondemo.device;

import org.springframework.data.repository.CrudRepository;

import com.tongchuang.visiondemo.device.entity.Device;

public interface DeviceRepository extends CrudRepository<Device, String>{

}
