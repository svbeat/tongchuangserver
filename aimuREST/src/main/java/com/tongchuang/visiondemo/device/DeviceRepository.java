package com.tongchuang.visiondemo.device;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tongchuang.visiondemo.device.entity.Device;
import com.tongchuang.visiondemo.doctor.entity.Doctor;

public interface DeviceRepository extends CrudRepository<Device, String>{

	 @Query("SELECT d FROM Device d ")
	 public List<Device> getDevices(Pageable pageable);
	 
   @Query("SELECT count(1) FROM Device d") 
   Integer getTotalCount();
}
