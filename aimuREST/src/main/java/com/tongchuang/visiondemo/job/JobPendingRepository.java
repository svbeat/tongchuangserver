package com.tongchuang.visiondemo.job;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tongchuang.visiondemo.device.entity.Device;
import com.tongchuang.visiondemo.job.entity.JobPending;
import com.tongchuang.visiondemo.patient.dto.PatientSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettingsCompositePK;
import com.tongchuang.visiondemo.user.dto.UserInfo;

public interface JobPendingRepository extends CrudRepository<JobPending, Integer> {
	
	 @Query("SELECT d FROM JobPending d where batchId=:batchid ")
	 public List<JobPending> getPendingJobs(@Param("batchid")String batchId);
}
